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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openbytecode.boot.common.vo.PageResult;
import com.openbytecode.boot.server.convert.BootUserConvert;
import com.openbytecode.boot.server.dto.*;
import com.openbytecode.boot.server.entity.BootUserDO;
import com.openbytecode.boot.server.mapper.BootUserMapper;
import com.openbytecode.boot.server.service.BootUserService;
import com.saucesubfresh.starter.oauth.domain.UserDetails;
import com.saucesubfresh.starter.oauth.service.UserDetailService;
import com.saucesubfresh.starter.security.context.UserSecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author: 李俊平
 * @Date: 2023-02-25 15:58
 */
@Service
public class BootUserServiceImpl extends ServiceImpl<BootUserMapper, BootUserDO> implements BootUserService, UserDetailService {

    private final BootUserMapper BootUserMapper;

    public BootUserServiceImpl(BootUserMapper BootUserMapper) {
        this.BootUserMapper = BootUserMapper;
    }

    @Override
    public PageResult<BootUserRespDTO> selectPage(BootUserReqDTO BootUserReqDTO) {
        Page<BootUserDO> page = BootUserMapper.queryPage(BootUserReqDTO);
        IPage<BootUserRespDTO> convert = page.convert(BootUserConvert.INSTANCE::convert);
        return PageResult.build(convert);
    }

    @Override
    public BootUserRespDTO getById(Long id) {
        BootUserDO crawlerUserDO = BootUserMapper.selectById(id);
        return BootUserConvert.INSTANCE.convert(crawlerUserDO);
    }

    @Override
    public void save(BootUserCreateDTO BootUserCreateDTO) {
        BootUserMapper.insert(BootUserConvert.INSTANCE.convert(BootUserCreateDTO));
    }

    @Override
    public void updateById(BootUserUpdateDTO BootUserUpdateDTO) {
        BootUserDO BootUserDO = new BootUserDO();
        BootUserDO.setId(UserSecurityContextHolder.getUserId());
        BootUserDO.setUsername(BootUserUpdateDTO.getUsername());
        BootUserDO.setPhone(BootUserUpdateDTO.getPhone());
        BootUserDO.setEmail(BootUserUpdateDTO.getEmail());
        BootUserDO.setAvatar(BootUserUpdateDTO.getAvatar());
        BootUserMapper.updateById(BootUserDO);
    }

    @Override
    public void deleteById(BatchDTO batchDTO) {
        BootUserMapper.deleteBatchIds(batchDTO.getIds());
    }

    @Override
    public BootUserRespDTO loadUserByUserId(Long userId) {
        BootUserDO crawlerUserDO = BootUserMapper.selectById(userId);
        return BootUserConvert.INSTANCE.convert(crawlerUserDO);
    }

    @Override
    public UserDetails loadUserByProviderIdAndOpenId(String providerId, String openId) {
        BootUserDO BootUserDO = BootUserMapper.loadUserByOpenId(openId);
        if (BootUserDO == null) {
            BootUserDO = new BootUserDO();
            BootUserDO.setOpenId(openId);
            BootUserDO.setUsername("Boot-ux");
            BootUserDO.setStatus(1);
            BootUserDO.setCreateTime(LocalDateTime.now());
            save(BootUserDO);
        }
        return convert(BootUserDO);
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        BootUserDO crawlerUserDO = BootUserMapper.loadUserByUsername(username);
        return convert(crawlerUserDO);
    }

    @Override
    public UserDetails loadUserByMobile(String mobile) {
        BootUserDO crawlerUserDO = BootUserMapper.loadUserByMobile(mobile);
        return convert(crawlerUserDO);
    }

    private UserDetails convert(BootUserDO userDO) {
        if (Objects.isNull(userDO)) {
            return null;
        }
        UserDetails userDetails = new UserDetails();
        userDetails.setId(userDO.getId());
        userDetails.setUsername(userDO.getUsername());
        userDetails.setPassword(userDO.getPassword());
        userDetails.setMobile(userDO.getPhone());
        userDetails.setAccountLocked(userDO.getStatus() != 1);
        return userDetails;
    }
}
