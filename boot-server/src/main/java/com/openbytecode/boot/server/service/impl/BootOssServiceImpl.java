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

package com.openbytecode.boot.server.service.impl;

import com.openbytecode.boot.server.cos.CosService;
import com.openbytecode.boot.server.cos.CosSignInfo;
import com.openbytecode.boot.server.dto.CosSignRequest;
import com.openbytecode.boot.server.service.BootOssService;
import com.saucesubfresh.starter.security.context.UserSecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author: 李俊平
 * @Date: 2023-07-23 12:41
 */
@Slf4j
@Service
public class BootOssServiceImpl implements BootOssService {

    private final CosService cosService;

    public BootOssServiceImpl(CosService cosService) {
        this.cosService = cosService;
    }


    @Override
    public CosSignInfo genUploadSign(CosSignRequest cosSignRequest) {
        Long userId = UserSecurityContextHolder.getUserId();
        return cosService.genUploadSign(String.valueOf(userId), cosSignRequest.getFileName(), cosSignRequest.getFileSize());
    }
}
