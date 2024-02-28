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

package com.openbytecode.boot.common.ip2region;

import org.springframework.lang.Nullable;

import java.util.function.Function;

/**
 * Ip2regionSearcher.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/6/10 23:01
 */
public interface Ip2regionSearcher {

    /**
     * ip 位置 搜索
     *
     * @param ip ip
     * @return 位置
     */
    @Nullable
    IpInfo memorySearch(String ip);

    /**
     * 读取 ipInfo 中的信息
     *
     * @param ip       ip
     * @param function Function
     * @return 地址
     */
    @Nullable
    default String getInfo(String ip, Function<IpInfo, String> function) {
        return IpInfoUtils.readInfo(memorySearch(ip), function);
    }

    /**
     * 获取地址信息
     *
     * @param ip ip
     * @return 地址
     */
    @Nullable
    default String getAddress(String ip) {
        return getInfo(ip, IpInfo::getAddress);
    }

    /**
     * 获取地址信息包含 isp
     *
     * @param ip ip
     * @return 地址
     */
    @Nullable
    default String getAddressAndIsp(String ip) {
        return getInfo(ip, IpInfo::getAddressAndIsp);
    }

}
