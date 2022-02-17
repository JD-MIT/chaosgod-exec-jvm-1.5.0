package com.alibaba.chaosblade.exec.plugin.jmq2.model;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;
import com.alibaba.chaosblade.exec.plugin.jmq2.Jmq2Constant;

/**
 * @author wanyong10
 * @date 2020/10/30 - 2:50 下午
 */
public class AppMatcherSpec extends BasePredicateMatcherSpec implements Jmq2Constant {
    @Override
    public String getName() {
        return APP_KEY;
    }

    @Override
    public String getDesc() {
        return "";
    }

    @Override
    public boolean noArgs() {
        return false;
    }

    @Override
    public boolean required() {
        return false;
    }
}
