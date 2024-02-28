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

package com.openbytecode.boot.server.cos.dsl.policy;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Statement.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/8/25 20:19
 */
@Getter
@Setter
public class Statement {

    private List<String> action = new ArrayList<>();

    private List<String> resource = new ArrayList<>();

    private String effect;

    public void addAction(String act) {
        action.add(act);
    }

    public void addResource(String rec) {
        resource.add(rec);
    }
}
