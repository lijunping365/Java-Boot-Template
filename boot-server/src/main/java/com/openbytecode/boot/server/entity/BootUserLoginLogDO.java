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

package com.openbytecode.boot.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: 李俊平
 * @Date: 2023-05-14 11:50
 */
@Data
@TableName("commerce_user_login_log")
public class BootUserLoginLogDO {

    @TableId
    private Long id;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 登录 ip
     */
    private String ip;

    /**
     * 用户状态失败信息
     */
    private String error;

    /**
     * 用户状态（0：失败，1正常）
     */
    private Integer status;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 国家
     */
    private String country;

    /**
     * 区域
     */
    private String region;

    /**
     * 省
     */
    private String province;

    /**
     * 城市
     */
    private String city;

}
