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

package com.openbytecode.boot.server.controller;

import com.openbytecode.boot.common.vo.Result;
import com.openbytecode.boot.server.dto.BootMobileLoginRequest;
import com.openbytecode.boot.server.dto.BootPasswordLoginRequest;
import com.openbytecode.boot.server.service.BootLoginService;
import com.saucesubfresh.starter.oauth.token.AccessToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author lijunping on 2022/3/29
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/login")
public class BootLoginController {

    @Autowired
    private BootLoginService bootLoginService;

    @PostMapping("/account")
    public Result<AccessToken> loginByUsername(@RequestBody @Valid BootPasswordLoginRequest request){
        return Result.succeed(bootLoginService.loginByUsername(request));
    }

    @PostMapping("/mobile")
    public Result<AccessToken> loginByMobile(@RequestBody @Valid BootMobileLoginRequest request){
        return Result.succeed(bootLoginService.loginByMobile(request));
    }

    @GetMapping("/refreshToken")
    public Result<AccessToken> refreshToken(@RequestParam("refreshToken") String refreshToken){
        return Result.succeed(bootLoginService.refreshToken(refreshToken));
    }
}
