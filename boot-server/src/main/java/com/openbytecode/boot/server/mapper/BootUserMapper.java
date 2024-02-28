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

package com.openbytecode.boot.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openbytecode.boot.server.dto.BootUserReqDTO;
import com.openbytecode.boot.server.entity.BootUserDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Objects;

/**
 * 用户登录mapper接口
 *
 * @author louzai
 * @date 2022-07-18
 */
@Mapper
public interface BootUserMapper extends BaseMapper<BootUserDO> {

    default BootUserDO loadUserByUsername(String username) {
        return selectOne(Wrappers.<BootUserDO>lambdaQuery()
                .eq(BootUserDO::getUsername, username)
                .last("limit 1")
        );
    }

    default BootUserDO loadUserByMobile(String mobile) {
        return selectOne(Wrappers.<BootUserDO>lambdaQuery()
                .eq(BootUserDO::getPhone, mobile)
                .last("limit 1")
        );
    }

    default Page<BootUserDO> queryPage(BootUserReqDTO BootUserReqDTO) {
        return selectPage(BootUserReqDTO.page(), Wrappers.<BootUserDO>lambdaQuery()
                .like(StringUtils.isNotBlank(BootUserReqDTO.getUsername()), BootUserDO::getUsername, BootUserReqDTO.getUsername())
                .eq(StringUtils.isNotBlank(BootUserReqDTO.getPhone()), BootUserDO::getPhone, BootUserReqDTO.getPhone())
                .eq(Objects.nonNull(BootUserReqDTO.getStatus()), BootUserDO::getStatus, BootUserReqDTO.getStatus())
        );
    }

    default BootUserDO loadUserByOpenId(String openId) {
        return selectOne(Wrappers.<BootUserDO>lambdaQuery()
                .eq(BootUserDO::getOpenId, openId));
    }

    default List<BootUserDO> selectByEmail(String email){
        return selectList(Wrappers.<BootUserDO>lambdaQuery()
                .eq(BootUserDO::getEmail, email)
        );
    }
}
