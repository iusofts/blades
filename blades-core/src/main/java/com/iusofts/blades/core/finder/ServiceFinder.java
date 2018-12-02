package com.iusofts.blades.core.finder;


import com.iusofts.blades.common.domain.ServiceInstanceDetail;
import com.iusofts.blades.common.excption.ServiceNotFoundException;

public interface ServiceFinder {

    ServiceInstanceDetail getService(String serviceName) throws ServiceNotFoundException;

}
