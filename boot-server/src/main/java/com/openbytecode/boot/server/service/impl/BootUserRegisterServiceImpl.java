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

import com.openbytecode.boot.common.constants.CommonConstant;
import com.openbytecode.boot.common.exception.ServiceException;
import com.openbytecode.boot.common.vo.ResultEnum;
import com.openbytecode.boot.server.dto.BootRegisterFormRequest;
import com.openbytecode.boot.server.entity.BootUserDO;
import com.openbytecode.boot.server.mapper.BootUserMapper;
import com.openbytecode.boot.server.service.BootUserRegisterService;
import com.saucesubfresh.starter.oauth.core.password.PasswordAuthenticationProcessor;
import com.saucesubfresh.starter.oauth.exception.AuthenticationException;
import com.saucesubfresh.starter.oauth.exception.BadCredentialsException;
import com.saucesubfresh.starter.oauth.exception.UserNotFoundException;
import com.saucesubfresh.starter.oauth.request.PasswordLoginRequest;
import com.saucesubfresh.starter.oauth.token.AccessToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BootUserRegisterServiceImpl implements BootUserRegisterService {

    private final PasswordEncoder passwordEncoder;
    private final BootUserMapper BootUserMapper;
    private final HttpServletRequest httpServletRequest;
    private final PasswordAuthenticationProcessor passwordAuthentication;

    public BootUserRegisterServiceImpl(PasswordEncoder passwordEncoder,
                                       BootUserMapper BootUserMapper,
                                       HttpServletRequest httpServletRequest,
                                       PasswordAuthenticationProcessor passwordAuthentication) {
        this.passwordEncoder = passwordEncoder;
        this.BootUserMapper = BootUserMapper;
        this.httpServletRequest = httpServletRequest;
        this.passwordAuthentication = passwordAuthentication;
    }

    @Override
    public AccessToken registerForm(BootRegisterFormRequest request) {

        List<BootUserDO> BootUserDOS = BootUserMapper.selectByEmail(request.getUsername());
        if (!CollectionUtils.isEmpty(BootUserDOS)){
            throw new ServiceException("该邮箱已注册");
        }

        BootUserDO BootUserDO = new BootUserDO();
        BootUserDO.setUsername(request.getUsername());
        BootUserDO.setPassword(passwordEncoder.encode(request.getPassword()));
        BootUserDO.setEmail(request.getUsername());
        BootUserDO.setCreateTime(LocalDateTime.now());
        BootUserMapper.insert(BootUserDO);

        PasswordLoginRequest passwordLoginRequest = new PasswordLoginRequest()
                .setUsername(request.getUsername())
                .setPassword(request.getPassword());

        Map<String,Object> map = new HashMap<>(1);
        map.put(CommonConstant.LOGIN_REQUEST, httpServletRequest);
        passwordLoginRequest.setAdditional(map);

        try {
            return passwordAuthentication.authentication(passwordLoginRequest);
        } catch (AuthenticationException e){
            String error = "";
            if((e instanceof UserNotFoundException) || (e instanceof BadCredentialsException)){
                error = ResultEnum.USERNAME_OR_PASSWORD_ERROR.getMsg();
            }else {
                error = e.getMessage();
            }
            throw new ServiceException(error);
        }
    }
}
