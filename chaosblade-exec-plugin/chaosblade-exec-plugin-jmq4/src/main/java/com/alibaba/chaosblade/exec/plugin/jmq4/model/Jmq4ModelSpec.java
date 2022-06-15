package com.alibaba.chaosblade.exec.plugin.jmq4.model;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import com.alibaba.chaosblade.exec.plugin.jmq4.Jmq4Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author renguangyin@jd.com
 */
public class Jmq4ModelSpec extends FrameworkModelSpec implements Jmq4Constant {

    @Override
    protected List<MatcherSpec> createNewMatcherSpecs() {
        ArrayList<MatcherSpec> arrayList = new ArrayList<MatcherSpec>();
        arrayList.add(new ConsumerMatcherSpec());
        arrayList.add(new ProducerMatcherSpec());
        arrayList.add(new TopicMatcherSpec());
        arrayList.add(new AppMatcherSpec());
        return arrayList;
    }

    @Override
    public String getTarget() {
        return TARGET_NAME;
    }

    @Override
    public String getShortDesc() {
        return getTarget() + " experiment";
    }

    @Override
    public String getLongDesc() {
        return "jmq4 experiment for testing service delay and exception.";
    }

    @Override
    protected PredicateResult preMatcherPredicate(Model model) {
        if (model == null) {
            return PredicateResult.fail("matcher not found for jmq4");
        }
        MatcherModel matcher = model.getMatcher();
        Set<String> keySet = matcher.getMatchers().keySet();
        for (String key : keySet) {
            if (key.equals(CONSUMER_KEY) || key.equals(PRODUCER_KEY)) {
                return PredicateResult.success();
            }
        }
        return PredicateResult.fail("less necessary matcher is consumer or producer for jmq4");
    }
}
