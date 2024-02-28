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

package com.openbytecode.boot.server.service;

import com.openbytecode.boot.server.cos.CosSignInfo;
import com.openbytecode.boot.server.dto.CosSignRequest;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传服务
 * @author: 李俊平
 * @Date: 2023-07-23 12:40
 */
public interface BootOssService {

    /**
     * 前端 oss 直传，即前端拿到 token 之后就可以直接上传了，不需要通过服务器了。
     * @param cosSignRequest
     * @return
     */
    CosSignInfo genUploadSign(CosSignRequest cosSignRequest);
}
