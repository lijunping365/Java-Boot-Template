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
package com.openbytecode.boot.server.config;

import com.openbytecode.boot.common.exception.BaseCheckedException;
import com.openbytecode.boot.common.exception.BaseException;
import com.openbytecode.boot.common.exception.ServiceException;
import com.openbytecode.boot.common.vo.Result;
import com.openbytecode.boot.common.vo.ResultEnum;
import com.saucesubfresh.starter.security.exception.AccessDeniedException;
import com.saucesubfresh.starter.security.exception.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器，将 Exception 翻译成 CommonResult + 对应的异常编号
 *
 * @author lijunping
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 处理业务异常 ServiceException
   * <p>
   * 例如说，商品库存不足，用户手机号已存在。
   */
  @ExceptionHandler({ServiceException.class})
  public Result<Object> serviceException(ServiceException ex) {
    log.warn("[serviceExceptionHandler]", ex);
    return Result.failed(ex.getCode(), ex.getMessage());
  }

  /**
   * 处理基础异常 BaseException
   */
  @ExceptionHandler({BaseException.class})
  public Result<Object> baseException(BaseException ex) {
    log.warn("[baseExceptionHandler]", ex);
    return Result.failed(ex.getCode(), ex.getMessage());
  }

  /**
   * 处理基础异常 BaseCheckedException
   */
  @ExceptionHandler({BaseCheckedException.class})
  public Result<Object> baseCheckedException(BaseCheckedException ex) {
    log.warn("[baseCheckedExceptionHandler]", ex);
    return Result.failed(ex.getCode(), ex.getMessage());
  }

  @ExceptionHandler({SecurityException.class})
  public Result<Object> securityException(SecurityException ex) {
    log.warn("[securityException]", ex);
    if (ex instanceof AccessDeniedException){
      return Result.failed(ResultEnum.FORBIDDEN.getMsg());
    }
    return Result.failed(ResultEnum.UNAUTHORIZED.getCode(), ResultEnum.UNAUTHORIZED.getMsg());
  }

  @ExceptionHandler({RuntimeException.class})
  public Result<Object> runtime(RuntimeException ex) {
    log.warn("[runtimeExceptionHandler]", ex);
    return Result.failed(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
  }

  @ExceptionHandler({Exception.class})
  public Result<Object> exception(HttpServletRequest request, Exception ex) {
    log.warn("[exceptionHandler]", ex);
    return Result.failed(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
  }

  @ExceptionHandler({Throwable.class})
  public Result<Object> error(HttpServletRequest request, Throwable ex) {
    log.warn("[throwableHandler]", ex);
    return Result.failed(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
  }

}
