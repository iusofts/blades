package com.iusofts.blades.core.finder.strategy;


import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询
 */
public class RoundRobinStrategy implements Strategy {
    private final AtomicInteger index = new AtomicInteger(0);
    @Override
    public <T> T getServiceInstance(List<T> services) {
        if (CollectionUtils.isEmpty(services)) {
            return null;
        }
        int thisIndex = Math.abs(index.getAndIncrement());
        return services.get(thisIndex % services.size());
    }
}
