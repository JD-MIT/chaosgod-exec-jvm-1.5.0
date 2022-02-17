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

package com.alibaba.chaosblade.exec.plugin.jsf.model;

import com.alibaba.chaosblade.exec.common.aop.CustomMatcher;
import com.alibaba.chaosblade.exec.common.util.StringUtil;
import com.alibaba.chaosblade.exec.plugin.jsf.JsfConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author BoYuan Han
 * @create 2020-06-22 14:33
 */
public class JsfParamsMatcher implements CustomMatcher {

    private static final Logger logger = LoggerFactory.getLogger(JsfParamsMatcher.class);

    private static final JsfParamsMatcher instance = new JsfParamsMatcher();

    private JsfParamsMatcher() {
    }

    public static JsfParamsMatcher getInstance() {
        return instance;
    }

    @Override
    public boolean match(String queryString, Object originValue) {
        if (StringUtil.isBlank(queryString)){
            return false;
        }
        String[] paramsStr = queryString.split(JsfConstant.AND_SYMBOL);
        List<String> paramsList = new ArrayList<String>(Arrays.asList(paramsStr));
        return paramsList.contains(originValue);
    }

    @Override
    public boolean regexMatch(String commandValue, Object originValue) {
        return false;
    }
}
