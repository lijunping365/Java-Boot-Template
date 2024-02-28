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

import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.InputStream;

/**
 * Ip2regionSearcherImpl.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/6/10 23:09
 */
@Service
public class Ip2regionSearcherImpl implements Ip2regionSearcher, InitializingBean, DisposableBean {

   private static final Resource IP2REGION = new ClassPathResource("data/ip2region.xdb");

    Searcher searcher;

    @Override
    public IpInfo memorySearch(String ip) {
        try {
            return IpInfoUtils.toIpInfo(searcher.search(ip));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try (InputStream inputStream = IP2REGION.getInputStream()) {
            this.searcher = Searcher.newWithBuffer(StreamUtils.copyToByteArray(inputStream));
        }
    }

    @Override
    public void destroy() throws Exception {
        if (searcher != null) {
            this.searcher.close();
        }
    }
}
