package com.alibaba.chaosblade.exec.plugin.jimdb;

import com.alibaba.chaosblade.exec.common.aop.matcher.MethodInfo;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Jimdb2CommandParamMatcher implements MethodMatcher {
    @Override
    public boolean isMatched(String methodName, MethodInfo methodInfo) {
        String[] parameterTypes = methodInfo.getParameterTypes();
        int length = parameterTypes.length;
        if (length != 3) {
            return false;
        }

        if (!parameterTypes[0].equals("com.jd.jim.cli.protocol.CommandType")){
            return false;
        }
        if (!parameterTypes[1].equals("com.jd.jim.cli.protocol.CommandOutput")){
            return false;
        }
        if (!parameterTypes[2].equals("com.jd.jim.cli.protocol.CommandArgs")){
            return false;
        }
        return true;
    }
}
