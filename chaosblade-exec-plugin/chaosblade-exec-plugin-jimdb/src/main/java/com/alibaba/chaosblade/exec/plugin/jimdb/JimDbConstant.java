/*
 * Copyright 1999-2019 Alibaba Group Holding Ltd.
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

package com.alibaba.chaosblade.exec.plugin.jimdb;

import java.util.HashSet;
import java.util.Set;

/**
 * @author guoping.yao <a href="mailto:bryan880901@qq.com">
 *
 */
public interface JimDbConstant {

    String TARGET_NAME = "jimdb";

    String HOST_MATCHER_NAME = "host";
    String DATABASE_INDEX_MATCHER_NAME = "database";
    String PORT_MATCHER_NAME = "port";
    String KEY_MATCHER_NAME = "key";
    String COMMAND_TYPE_MATCHER_NAME = "cmd";
}
