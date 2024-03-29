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

import com.openbytecode.boot.common.vo.PageResult;
import com.openbytecode.boot.server.dto.*;
import com.saucesubfresh.starter.oauth.domain.UserDetails;

/**
 * @author: 李俊平
 * @Date: 2023-02-25 15:57
 */
public interface BootUserService {

    PageResult<BootUserRespDTO> selectPage(BootUserReqDTO BootUserReqDTO);

    BootUserRespDTO getById(Long id);

    void save(BootUserCreateDTO BootUserCreateDTO);

    void updateById(BootUserUpdateDTO BootUserUpdateDTO);

    void deleteById(BatchDTO batchDTO);

    BootUserRespDTO loadUserByUserId(Long userId);

    UserDetails loadUserByProviderIdAndOpenId(String providerId, String openId);
}
