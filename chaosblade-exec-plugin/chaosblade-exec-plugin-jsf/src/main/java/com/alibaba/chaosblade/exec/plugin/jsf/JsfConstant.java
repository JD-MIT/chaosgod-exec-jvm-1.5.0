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

package com.alibaba.chaosblade.exec.plugin.jsf;

import com.alibaba.chaosblade.exec.common.plugin.MethodConstant;

/**
 * @author renguangyin@jd.com
 */
public interface JsfConstant {

    String TIMEOUT_KEY = "timeout";

    String TIMEOUT_EXCEPTION_MSG = "taishan chaos timeout exception.";

    String SERVICE_KEY = "service";

    String METHOD_KEY = MethodConstant.METHOD_MATCHER_NAME;

    String ALIAS_KEY = "alias";

    String TARGET_NAME = "jsf";

    String CONSUMER_KEY = "consumer";

    String PROVIDER_KEY = "provider";

    String AND_SYMBOL = ";";

}
