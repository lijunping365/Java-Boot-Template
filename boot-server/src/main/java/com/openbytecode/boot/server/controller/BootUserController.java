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

import com.openbytecode.boot.common.vo.PageResult;
import com.openbytecode.boot.common.vo.Result;
import com.openbytecode.boot.server.dto.*;
import com.openbytecode.boot.server.service.BootUserService;
import com.saucesubfresh.starter.security.context.UserSecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Validated
@RestController
@RequestMapping("/user")
public class BootUserController {

  @Autowired
  private BootUserService bootUserService;

  @GetMapping("/currentUser")
  public Result<BootUserRespDTO> getCurrentUser() {
    Long userId = UserSecurityContextHolder.getUserId();
    return Result.succeed(bootUserService.loadUserByUserId(userId));
  }

  @GetMapping("/page")
  public Result<PageResult<BootUserRespDTO>> page(BootUserReqDTO bootUserReqDTO) {
    return Result.succeed(bootUserService.selectPage(bootUserReqDTO));
  }

  @GetMapping("/info/{id}")
  public Result<BootUserRespDTO> info(@PathVariable("id") Long id) {
    return Result.succeed(bootUserService.getById(id));
  }

  @PostMapping("/save")
  public Result save(@RequestBody @Valid BootUserCreateDTO bootUserCreateDTO) {
    bootUserService.save(bootUserCreateDTO);
    return Result.succeed();
  }

  @PutMapping("/update")
  public Result update(@RequestBody @Valid BootUserUpdateDTO bootUserUpdateDTO) {
    bootUserService.updateById(bootUserUpdateDTO);
    return Result.succeed();
  }

  @DeleteMapping("/delete")
  public Result delete(@RequestBody @Valid BatchDTO batchDTO) {
    bootUserService.deleteById(batchDTO);
    return Result.succeed();
  }
}
