package com.alibaba.chaosblade.exec.plugin.jmq2;

import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;
import com.alibaba.chaosblade.exec.plugin.jmq2.model.Jmq2ModelSpec;

/**
 * @author renguangyin@jd.com
 */
public abstract class Jmq2Plugin implements Plugin, Jmq2Constant {

    @Override
    public ModelSpec getModelSpec() {
        return new Jmq2ModelSpec();
    }
}
