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

import com.openbytecode.boot.server.dto.BootMobileLoginRequest;
import com.openbytecode.boot.server.dto.BootPasswordLoginRequest;
import com.saucesubfresh.starter.oauth.token.AccessToken;

public interface BootLoginService {

    /**
     * 账号登录
     * @param request
     * @return
     */
    AccessToken loginByUsername(BootPasswordLoginRequest request);

    /**
     * 手机号登录
     * @param request
     * @return
     */
    AccessToken loginByMobile(BootMobileLoginRequest request);

    /**
     * 刷新 token
     * @param refreshToken
     * @return
     */
    AccessToken refreshToken(String refreshToken);

}
