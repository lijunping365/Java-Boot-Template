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
import com.openbytecode.boot.server.dto.BootRegisterFormRequest;
import com.openbytecode.boot.server.service.BootUserRegisterService;
import com.saucesubfresh.starter.oauth.token.AccessToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author lijunping on 2022/3/29
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/register")
public class BootUserRegisterController {

    private final BootUserRegisterService bootUserRegisterService;

    public BootUserRegisterController(BootUserRegisterService bootUserRegisterService) {
        this.bootUserRegisterService = bootUserRegisterService;
    }

    @PostMapping("/form")
    public Result<AccessToken> registerForm(@RequestBody @Valid BootRegisterFormRequest request){
        return Result.succeed(bootUserRegisterService.registerForm(request));
    }
}
