package com.iusofts.blades.common.excption;

public class ServiceNotAvailableException extends RuntimeException {

    private String serviceName;

    private Throwable originException;

    public ServiceNotAvailableException(String serviceName, Throwable throwable) {
        this.serviceName = serviceName;
        this.originException = throwable;
    }

    public ServiceNotAvailableException(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMessage() {
        return "service "+serviceName+" is not available ";
    }
}
