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

package com.alibaba.chaosblade.exec.plugin.jsf.provider;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.OrClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.*;

/**
 * @author renguangyin@jd.com
 */
public class JsfProviderPointCut implements PointCut {

    @Override
    public ClassMatcher getClassMatcher() {
        return new OrClassMatcher()
                // for jsf provider thread pool
                .or(new NameClassMatcher("com.jd.jsf.gd.server.BaseServerHandler"))
                .or(new NameClassMatcher("com.jd.jsf.gd.server.ProviderProxyInvoker"));
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        AndMethodMatcher methodMatcher = new AndMethodMatcher();
        ParameterMethodMatcher parameterMethodMatcher = new ParameterMethodMatcher(
                new String[]{"com.jd.jsf.gd.msg.BaseMessage"}, 1, ParameterMethodMatcher.EQUAL);
        methodMatcher.and(new NameMethodMatcher("invoke")).and(parameterMethodMatcher);

        OrMethodMatcher orMethodMatcher = new OrMethodMatcher();
        orMethodMatcher.or(methodMatcher).or(new NameMethodMatcher("handlerRequest"));
        return orMethodMatcher;
    }
}
