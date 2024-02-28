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

package com.openbytecode.boot.server.component;

import com.openbytecode.boot.common.constants.CommonConstant;
import com.openbytecode.boot.common.enums.CommonStatusEnum;
import com.openbytecode.boot.common.ip2region.Ip2regionSearcher;
import com.openbytecode.boot.common.ip2region.IpInfo;
import com.openbytecode.boot.server.entity.BootUserLoginLogDO;
import com.openbytecode.boot.server.mapper.BootUserLoginLogMapper;
import com.openbytecode.boot.server.utils.IpUtil;
import com.saucesubfresh.starter.oauth.authentication.Authentication;
import com.saucesubfresh.starter.oauth.component.AuthenticationSuccessHandler;
import com.saucesubfresh.starter.oauth.domain.UserDetails;
import com.saucesubfresh.starter.oauth.request.BaseLoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 *
 * <p>
 *   自定义用户登录成功处理器，实现了 {@link AuthenticationSuccessHandler}
 *   更多文档请参考
 *   @see <a href="https://www.openbytecode.com/starter/open-starter-oauth/docs/quick-start"> Open-Starter-Oauth Docs </a>
 *
 *   这里默认的处理逻辑为记录登录时间，ip 等信息
 * </p>
 *
 * @author: 李俊平
 * @Date: 2023-07-07 20:30
 */
@Slf4j
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final Ip2regionSearcher ip2regionSearcher;
    private final BootUserLoginLogMapper userLoginLogMapper;

    public CustomAuthenticationSuccessHandler(Ip2regionSearcher ip2regionSearcher,
                                              BootUserLoginLogMapper userLoginLogMapper) {
        this.ip2regionSearcher = ip2regionSearcher;
        this.userLoginLogMapper = userLoginLogMapper;
    }

    @Override
    public <T extends BaseLoginRequest> void onAuthenticationSuccess(Authentication authentication, T request) {
        UserDetails userDetails = authentication.getUserDetails();
        log.info("[登录成功]-[用户名：{}]-[用户编号：{}]", userDetails.getUsername(), userDetails.getId());
        HttpServletRequest httpServletRequest = (HttpServletRequest) request.getAdditional().get(CommonConstant.LOGIN_REQUEST);
        BootUserLoginLogDO loginLogDO = new BootUserLoginLogDO();
        loginLogDO.setUserId(authentication.getUserDetails().getId());
        loginLogDO.setStatus(CommonStatusEnum.YES.getValue());
        loginLogDO.setLoginTime(LocalDateTime.now());

        String ip = IpUtil.getIpAddress(httpServletRequest);
        loginLogDO.setIp(ip);

        if (StringUtils.hasText(ip)) {
            IpInfo ipInfo = ip2regionSearcher.memorySearch(ip);
            if (ipInfo != null) {
                loginLogDO.setCountry(ipInfo.getCountry());
                loginLogDO.setProvince(ipInfo.getProvince());
                loginLogDO.setCity(ipInfo.getCity());
                loginLogDO.setRegion(ipInfo.getRegion());
            }
        }

        userLoginLogMapper.insert(loginLogDO);
    }
}
