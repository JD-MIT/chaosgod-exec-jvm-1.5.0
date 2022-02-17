package com.alibaba.chaosblade.exec.plugin.jmq2.model;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import com.alibaba.chaosblade.exec.plugin.jmq2.Jmq2Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author wanyong10
 * @date 2020/10/30 - 2:36 下午
 */
public class Jmq2ModelSpec extends FrameworkModelSpec implements Jmq2Constant {

    @Override
    protected List<MatcherSpec> createNewMatcherSpecs() {
        ArrayList<MatcherSpec> arrayList = new ArrayList<MatcherSpec>();
        arrayList.add(new ConsumerMatcherSpec());
        arrayList.add(new ProducerMatcherSpec());
        arrayList.add(new TopicMatcherSpec());
        arrayList.add(new AppMatcherSpec());
        arrayList.add(new UserMatcherSpec());
        return arrayList;
    }

    @Override
    public String getTarget() {
        return TARGET_NAME;
    }

    @Override
    public String getShortDesc() {
        return "jmq2 experiment";
    }

    @Override
    public String getLongDesc() {
        return "jmq2 experiment for testing service delay and exception.";
    }


    @Override
    protected PredicateResult preMatcherPredicate(Model model) {
        if (model == null) {
            return PredicateResult.fail("matcher not found for jmq2");
        }
        MatcherModel matcher = model.getMatcher();
        Set<String> keySet = matcher.getMatchers().keySet();
        for (String key : keySet) {
            if (key.equals(CONSUMER_KEY) || key.equals(PRODUCER_KEY)) {
                return PredicateResult.success();
            }
        }
        return PredicateResult.fail("less necessary matcher is consumer or producer for jmq2");
    }
}
