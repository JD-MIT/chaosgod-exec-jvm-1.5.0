package com.alibaba.chaosblade.exec.plugin.jmq4.model;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;
import com.alibaba.chaosblade.exec.plugin.jmq4.Jmq4Constant;

/**
 * @author wanyong10
 * @date 2020/10/30 - 2:43 下午
 */
public class ConsumerMatcherSpec extends BasePredicateMatcherSpec implements Jmq4Constant {

    @Override
    public String getName() {
        return CONSUMER_KEY;
    }

    @Override
    public String getDesc() {
        return "";
    }

    @Override
    public boolean noArgs() {
        return true;
    }

    @Override
    public boolean required() {
        return false;
    }
}
