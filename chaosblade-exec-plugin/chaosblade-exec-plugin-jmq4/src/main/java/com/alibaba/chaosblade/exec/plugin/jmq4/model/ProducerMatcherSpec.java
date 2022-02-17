package com.alibaba.chaosblade.exec.plugin.jmq4.model;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;
import com.alibaba.chaosblade.exec.plugin.jmq4.Jmq4Constant;

/**
 * @author wanyong10
 * @date 2020/10/30 - 2:45 下午
 */
public class ProducerMatcherSpec extends BasePredicateMatcherSpec implements Jmq4Constant {

    @Override
    public String getName() {
        return PRODUCER_KEY;
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