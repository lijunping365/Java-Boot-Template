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

/**
 * StatementBuilder.
 *
 * @author u0039724
 * @since 2023/6/25 11:04
 */
public final class StatementBuilder {

    Statement statement = new Statement();

    StatementBuilder() {
        // no instance
    }

    public StatementBuilder effect(String effect) {
        statement.setEffect(effect);
        return this;
    }

    public StatementBuilder action(String action) {
        statement.addAction(action);
        return this;
    }

    public StatementBuilder resource(String resource) {
        statement.addResource(resource);
        return this;
    }

}
