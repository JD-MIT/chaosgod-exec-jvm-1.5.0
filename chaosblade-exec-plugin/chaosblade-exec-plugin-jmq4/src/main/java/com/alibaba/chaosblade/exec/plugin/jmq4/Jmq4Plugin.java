package com.alibaba.chaosblade.exec.plugin.jmq4;

import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;
import com.alibaba.chaosblade.exec.plugin.jmq4.model.Jmq4ModelSpec;

/**
 * @author renguangyin@jd.com
 */
public abstract class Jmq4Plugin implements Plugin, Jmq4Constant {

    @Override
    public ModelSpec getModelSpec() {
        return new Jmq4ModelSpec();
    }
}
