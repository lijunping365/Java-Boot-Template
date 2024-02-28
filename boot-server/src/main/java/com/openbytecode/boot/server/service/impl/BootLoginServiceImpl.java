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
import com.openbytecode.boot.server.dto.BootMobileLoginRequest;
import com.openbytecode.boot.server.dto.BootPasswordLoginRequest;
import com.openbytecode.boot.server.service.BootLoginService;
import com.saucesubfresh.starter.captcha.exception.ValidateCodeException;
import com.saucesubfresh.starter.captcha.processor.CaptchaVerifyProcessor;
import com.saucesubfresh.starter.captcha.request.CaptchaVerifyRequest;
import com.saucesubfresh.starter.oauth.core.password.PasswordAuthenticationProcessor;
import com.saucesubfresh.starter.oauth.core.sms.SmsMobileAuthenticationProcessor;
import com.saucesubfresh.starter.oauth.exception.AuthenticationException;
import com.saucesubfresh.starter.oauth.exception.BadCredentialsException;
import com.saucesubfresh.starter.oauth.exception.UserNotFoundException;
import com.saucesubfresh.starter.oauth.request.MobileLoginRequest;
import com.saucesubfresh.starter.oauth.request.PasswordLoginRequest;
import com.saucesubfresh.starter.oauth.token.AccessToken;
import com.saucesubfresh.starter.oauth.token.TokenStore;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
public class BootLoginServiceImpl implements BootLoginService {

    private final TokenStore tokenStore;
    private final HttpServletRequest httpServletRequest;
    private final CaptchaVerifyProcessor captchaVerifyProcessor;
    private final PasswordAuthenticationProcessor passwordAuthentication;
    private final SmsMobileAuthenticationProcessor smsMobileAuthentication;

    public BootLoginServiceImpl(TokenStore tokenStore,
                                HttpServletRequest httpServletRequest,
                                CaptchaVerifyProcessor captchaVerifyProcessor,
                                PasswordAuthenticationProcessor passwordAuthentication,
                                SmsMobileAuthenticationProcessor smsMobileAuthentication) {
        this.tokenStore = tokenStore;
        this.httpServletRequest = httpServletRequest;
        this.captchaVerifyProcessor = captchaVerifyProcessor;
        this.passwordAuthentication = passwordAuthentication;
        this.smsMobileAuthentication = smsMobileAuthentication;
    }
    
    @Override
    public AccessToken loginByUsername(BootPasswordLoginRequest request) {
        CaptchaVerifyRequest captchaVerifyRequest = new CaptchaVerifyRequest()
                .setRequestId(request.getDeviceId())
                .setCode(request.getCaptcha());
        try {
            captchaVerifyProcessor.validate(captchaVerifyRequest);
        } catch (ValidateCodeException e){
            throw new ServiceException(e.getMessage());
        }

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

    @Override
    public AccessToken loginByMobile(BootMobileLoginRequest request) {
        CaptchaVerifyRequest captchaVerifyRequest = new CaptchaVerifyRequest()
                .setRequestId(request.getDeviceId())
                .setCode(request.getCaptcha());
        try {
            captchaVerifyProcessor.validate(captchaVerifyRequest);
        } catch (ValidateCodeException e){
            throw new ServiceException(e.getMessage());
        }

        MobileLoginRequest mobileLoginRequest = new MobileLoginRequest().setMobile(request.getMobile());
        Map<String,Object> map = new HashMap<>(1);
        map.put(CommonConstant.LOGIN_REQUEST, httpServletRequest);
        mobileLoginRequest.setAdditional(map);

        try {
            return smsMobileAuthentication.authentication(mobileLoginRequest);
        } catch (AuthenticationException e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public AccessToken refreshToken(String refreshToken) {
        try {
            return tokenStore.refreshToken(refreshToken);
        } catch (AuthenticationException e){
            throw new ServiceException(e.getMessage());
        }
    }
}
