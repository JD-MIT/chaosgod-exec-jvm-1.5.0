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

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.OrClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.*;

/**
 * @author guoping.yao <a href="mailto:bryan880901@qq.com">
 */
public class JimDbPointCut implements PointCut {

//    private static final String JEDIS_CONNECTION = "redis.clients.jedis.Protocol";
    private static final String JEDIS_CONNECTION_1 = "com.jd.jim.cli.redis.jedis.Protocol";
    private static final String JEDIS_CONNECTION_2 = "com.jd.jim.cli.protocol.JimCommandBuilder";

    private static final String cacheClass = "com.jd.jim.cli.cache.MultiLevelLocalCache";

    private static final String INTERCEPTOR_PRE_METHOD_1 = "sendCommand";
    private static final String INTERCEPTOR_PRE_METHOD_2_1 = "createReadCommand";
    private static final String INTERCEPTOR_PRE_METHOD_2_2 = "createCommand";

    private static final String cacheMethodName = "isUseLocalCache";

    @Override
    public ClassMatcher getClassMatcher() {
        OrClassMatcher cm = new OrClassMatcher();
        cm.or(new NameClassMatcher(JEDIS_CONNECTION_1)).or(new NameClassMatcher(JEDIS_CONNECTION_2)).or(new NameClassMatcher(cacheClass));
        return cm;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        AndMethodMatcher andMethodMatcher = new AndMethodMatcher();
        andMethodMatcher.and(new OrMethodMatcher()
                .or(new NameMethodMatcher(INTERCEPTOR_PRE_METHOD_2_1))
                .or(new NameMethodMatcher(INTERCEPTOR_PRE_METHOD_2_2))).and(new Jimdb2CommandParamMatcher());
        OrMethodMatcher omm = new OrMethodMatcher();
        omm.or(new NameMethodMatcher(INTERCEPTOR_PRE_METHOD_1)).or(andMethodMatcher);
        omm.or(new NameMethodMatcher(cacheMethodName));
        return omm;
    }
}
