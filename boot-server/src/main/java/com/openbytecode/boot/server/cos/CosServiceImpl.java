/*
 * Copyright © 2024 organization OpenByteCode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.openbytecode.boot.server.cos;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.openbytecode.boot.common.json.JSON;
import com.openbytecode.boot.server.cos.dsl.policy.Policy;
import com.openbytecode.boot.server.cos.dsl.policy.PolicyBuilder;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.Headers;
import com.qcloud.cos.auth.BasicSessionCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.auth.COSSigner;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.region.Region;
import com.tencent.cloud.CosStsClient;
import com.tencent.cloud.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * CosServiceImpl.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/8/25 00:37
 */
@Slf4j
public class CosServiceImpl implements CosService {

    private static final String HOST = "https://{Bucket}.cos.{Region}.myqcloud.com/{ObjectKey}";

    private static final IdentifierGenerator objectKeyGenerator = new DefaultIdentifierGenerator();

    private final CosConfig cosConfig;

    public CosServiceImpl(CosConfig cosConfig) {
        this.cosConfig = cosConfig;
    }

    @Override
    public CosStsInfo genStsForUpload(String credentials, String fileName, long fileSize) {
        boolean validFileName = Utils.isValidFileName(fileName);
        if (!validFileName) {
            throw new IllegalArgumentException("非法的文件名");
        }

        String name = StringUtils.getFilename(fileName);
        String extension = StringUtils.getFilenameExtension(fileName);

        String objectKey = objectKeyGenerator.nextId(null) + "." + extension;

        TreeMap<String, Object> config = new TreeMap<>();

        // 替换为您的云 api 密钥 SecretId
        config.put("secretId", cosConfig.getSecretId());
        // 替换为您的云 api 密钥 SecretKey
        config.put("secretKey", cosConfig.getSecretKey());

        // 设置域名:
        // 如果您使用了腾讯云 cvm，可以设置内部域名
        // config.put("host", "sts.internal.tencentcloudapi.com");

        // 临时密钥有效时长，单位是秒，默认 1800 秒，目前主账号最长 2 小时（即 7200 秒），子账号最长 36 小时（即 129600）秒
        config.put("durationSeconds", (int) cosConfig.getDurationSeconds().getSeconds());

        // 换成您的 bucket
        config.put("bucket", cosConfig.getBucket());
        // 换成 bucket 所在地区
        config.put("region", cosConfig.getRegion());

        // 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的具体路径
        // 列举几种典型的前缀授权场景：
        // 1、允许访问所有对象："*"
        // 2、允许访问指定的对象："a/a1.txt", "b/b1.txt"
        // 3、允许访问指定前缀的对象："a*", "a/*", "b/*"
        // 如果填写了“*”，将允许用户访问所有资源；除非业务需要，否则请按照最小权限原则授予用户相应的访问权限范围。
        config.put("allowPrefixes", new String[]{objectKey});

        // 密钥的权限列表。必须在这里指定本次临时密钥所需要的权限。
        // 简单上传、表单上传和分块上传需要以下的权限，其他权限列表请参见 https://cloud.tencent.com/document/product/436/31923
        String[] allowActions = new String[]{
                // 简单上传
                "name/cos:PutObject",
                // 表单上传、小程序上传
                "name/cos:PostObject",
                // 分块上传
                "name/cos:InitiateMultipartUpload",
                "name/cos:ListMultipartUploads",
                "name/cos:ListParts",
                "name/cos:UploadPart",
                "name/cos:CompleteMultipartUpload"
        };
        config.put("allowActions", allowActions);

        //# 临时密钥生效条件，关于condition的详细设置规则和COS支持的condition类型可以参考 https://cloud.tencent.com/document/product/436/71307

        String resource = "qcs::cos:" + cosConfig.getRegion() + ":uid/1301456089:" + cosConfig.getBucket() + "/" + objectKey;

        Policy p = PolicyBuilder.policy(policy ->
                policy.version("2.0")
                        .statement(statement ->
                                statement.effect("allow")
                                        .action("name/cos:PutObject")
                                        .action("name/cos:PostObject")
                                        .action("name/cos:InitiateMultipartUpload")
                                        .action("name/cos:ListMultipartUploads")
                                        .action("name/cos:ListParts")
                                        .action("name/cos:UploadPart")
                                        .action("name/cos:CompleteMultipartUpload")
                                        .resource(resource)));

        final String raw_policy = JSON.toJSON(p);

        config.put("policy", raw_policy);

        Response response;
        try {
            response = CosStsClient.getCredential(config);
        } catch (IOException e) {
            log.error("get sts error：{}", e.getMessage(), e);
            throw new IllegalArgumentException("no valid secret !");
        }

        CosStsInfo cosStsInfo = new CosStsInfo();
        cosStsInfo.setTmpSecretId(response.credentials.tmpSecretId);
        cosStsInfo.setTmpSecretKey(response.credentials.tmpSecretKey);
        cosStsInfo.setSessionToken(response.credentials.sessionToken);
        cosStsInfo.setBucket(cosConfig.getBucket());
        cosStsInfo.setObjectKey(objectKey);
        cosStsInfo.setRegion(cosConfig.getRegion());

        return cosStsInfo;
    }

    @Override
    public CosSignInfo genUploadSign(CosStsInfo cosStsInfo) {
        String tmpSecretId = cosStsInfo.getTmpSecretId();
        String tmpSecretKey = cosStsInfo.getTmpSecretKey();
        String sessionToken = cosStsInfo.getSessionToken();
        String bucket = cosStsInfo.getBucket();
        String objectKey = cosStsInfo.getObjectKey();
        String resourcePath = "/" + objectKey;
        String region = cosStsInfo.getRegion();

        COSCredentials cred = new BasicSessionCredentials(tmpSecretId, tmpSecretKey, sessionToken);
        ClientConfig clientConfig = new ClientConfig(new Region(region));

        // 用来生成签名
        COSSigner signer = new COSSigner();
        // 设置签名过期时间(可选)，若未进行设置，则默认使用 ClientConfig 中的签名过期时间(1小时)
        // 这里设置签名在半个小时后过期
        Date expirationDate = new Date(System.currentTimeMillis() + 30L * 60L * 1000L);

        // 填写本次请求的参数
        Map<String, String> params = new HashMap<>();

        // 填写本次请求的头部
        Map<String, String> headers = new HashMap<>();
        // host 必填
        headers.put(Headers.HOST, clientConfig.getEndpointBuilder().buildGeneralApiEndpoint(bucket));

        String sign = signer.buildAuthorizationStr(HttpMethodName.PUT, resourcePath, headers, params, cred, expirationDate, true);

        CosSignInfo cosSignInfo = new CosSignInfo();
        cosSignInfo.setCosHost(buildHost(objectKey));
        cosSignInfo.setAuthorization(sign);
        cosSignInfo.setSecurityToken(sessionToken);
        cosSignInfo.setCosKey(objectKey);
        cosSignInfo.setCdnPath(cosConfig.getCdnHost() + objectKey);

        return cosSignInfo;
    }

    @Override
    public CosSignInfo genUploadSign(String credentials, String fileName, long fileSize) {
        CosStsInfo cosStsInfo = genStsForUpload(credentials, fileName, fileSize);
        return genUploadSign(cosStsInfo);
    }

    private String buildHost(String objectKey) {
        return HOST.replace("{Bucket}", cosConfig.getBucket())
                .replace("{Region}", cosConfig.getRegion())
                .replace("{ObjectKey}", objectKey);
    }

}
