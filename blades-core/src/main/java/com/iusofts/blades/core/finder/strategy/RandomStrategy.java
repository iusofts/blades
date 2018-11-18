package com.iusofts.blades.core.finder.strategy;


import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Random;

public class RandomStrategy implements Strategy {

    private final Random random = new Random();

    @Override
    public <T> T getServiceInstance(List<T> services) {
        if (CollectionUtils.isEmpty(services)) {
            return null;
        }
        int index =random.nextInt(services.size());
        return services.get(index);
    }
}
