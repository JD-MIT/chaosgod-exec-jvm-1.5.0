package com.alibaba.chaosblade.exec.plugin.jimdb;

import com.alibaba.chaosblade.exec.common.aop.matcher.MethodInfo;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;

/**
 * @author renguangyin@jd.com
 */
public class Jimdb2CommandParamMatcher implements MethodMatcher {
    @Override
    public boolean isMatched(String methodName, MethodInfo methodInfo) {
        String[] parameterTypes = methodInfo.getParameterTypes();
        int length = parameterTypes.length;

        if (length == 3) {
            return false;
        }

        return (parameterTypes[0].equalsIgnoreCase("com.jd.jim.cli.protocol.CommandType") &&
                parameterTypes[1].equalsIgnoreCase("com.jd.jim.cli.protocol.CommandOutput") &&
                parameterTypes[2].equalsIgnoreCase("com.jd.jim.cli.protocol.CommandArgs"));
    }
}
