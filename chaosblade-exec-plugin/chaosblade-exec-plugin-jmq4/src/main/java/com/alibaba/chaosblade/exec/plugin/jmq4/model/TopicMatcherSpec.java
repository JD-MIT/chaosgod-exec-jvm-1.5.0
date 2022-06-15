package com.alibaba.chaosblade.exec.plugin.jmq4.model;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;
import com.alibaba.chaosblade.exec.plugin.jmq4.Jmq4Constant;

/**
 * @author renguangyin@jd.com
 */
public class TopicMatcherSpec extends BasePredicateMatcherSpec implements Jmq4Constant {

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
