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

import com.alibaba.chaosblade.exec.common.exception.ExperimentException;
import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.action.ActionExecutor;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.threadpool.ThreadPoolFullActionSpec;
import com.alibaba.chaosblade.exec.common.model.handler.PreDestroyInjectionModelHandler;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author renguangyin@jd.com
 */
public class JimDbModelSpec extends FrameworkModelSpec implements PreDestroyInjectionModelHandler {

    @Override
    protected List<MatcherSpec> createNewMatcherSpecs() {
        ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>(2);
        matcherSpecs.add(new JimDbCmdTypeMatcherSpec());
        matcherSpecs.add(new JimDbKeyMatcherSpec());
        return matcherSpecs;
    }

    public static Object cacheObjT = null;
    public static Boolean ulcT = null;
    public static Boolean uhklcT = null;
    public static Boolean hasLostCacheT = false;

    @Override
    public String getTarget() {
        return JimDbConstant.TARGET_NAME;
    }

    @Override
    public String getShortDesc() {
        return "jimdb experiment";
    }

    @Override
    public String getLongDesc() {
        return "jimdb experiment contains delay and exception by command and so on.";
    }


    @Override
    public void preDestroy(String suid, Model model) throws ExperimentException {
        if ("delay".equals(model.getActionName()) && null != cacheObjT) {
            try {
                if (null != ulcT)
                    ReflectUtil.setFieldValue(cacheObjT,"useLocalCache",ulcT,false);
                if (null != uhklcT)
                    ReflectUtil.setFieldValue(cacheObjT,"useHotKeyLocalCache",uhklcT,false);
                ulcT = null;
                uhklcT = null;
                cacheObjT = null;
                hasLostCacheT = false;
            }catch (Exception e){
                throw new ExperimentException(e.getMessage());
            }
        }
    }

    public synchronized static void loseCache(Object object) throws Exception {
        if (hasLostCacheT){
            return;
        }
        cacheObjT = object;
        boolean ulct = ReflectUtil.getFieldValue(object,"useLocalCache",false);
        boolean uhklct = ReflectUtil.getFieldValue(object,"useHotKeyLocalCache",false);
        ulcT = ulct;
        uhklcT = uhklct;
        ReflectUtil.setFieldValue(object,"useLocalCache",false,false);
        ReflectUtil.setFieldValue(object,"useHotKeyLocalCache",false,false);
        hasLostCacheT = true;
    }



}
