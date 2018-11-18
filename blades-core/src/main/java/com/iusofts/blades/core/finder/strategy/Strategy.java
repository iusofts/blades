package com.iusofts.blades.core.finder.strategy;

import java.util.List;

public interface Strategy {
    <T> T getServiceInstance(List<T> services);
}
