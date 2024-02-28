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

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * CosConfig.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/8/25 20:19
 */
@Data
@ConfigurationProperties(prefix = "com.saucesubfresh.cos")
public class CosConfig {

    /**
     * api秘钥 SecretId, 推荐子账号
     */
    private String secretId;

    /**
     * api秘钥 SecretKey, 推荐子账号
     */
    private String secretKey;

    /**
     * 临时密钥有效时长，单位是秒，默认 1800 秒，目前主账号最长 2 小时（即 7200 秒），子账号最长 36 小时（即 129600）秒
     */
    private Duration durationSeconds = Duration.ofHours(2L);

    /**
     * bucket
     */
    private String bucket;

    /**
     * bucket 所在地区
     */
    private String region;

    /**
     * cdnHost
     */
    private String cdnHost;
}
