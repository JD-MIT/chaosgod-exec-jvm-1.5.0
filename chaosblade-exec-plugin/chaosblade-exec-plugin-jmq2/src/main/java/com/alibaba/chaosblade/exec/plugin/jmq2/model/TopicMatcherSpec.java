package com.alibaba.chaosblade.exec.plugin.jmq2.model;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;
import com.alibaba.chaosblade.exec.plugin.jmq2.Jmq2Constant;

/**
 * @author renguangyin@jd.com
 */
public class TopicMatcherSpec extends BasePredicateMatcherSpec implements Jmq2Constant {

    @Override
    public String getName() {
        return TOPIC_KEY;
    }

    @Override
    public String getDesc() {
        return getName();
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
