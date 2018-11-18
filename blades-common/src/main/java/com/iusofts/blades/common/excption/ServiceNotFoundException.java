package com.iusofts.blades.common.excption;

public class ServiceNotFoundException extends RuntimeException {

    private Throwable originException;
    private String message;

    private String serviceName;

    public ServiceNotFoundException(String serviceName) {
        super();
        this.serviceName = serviceName;
    }

    public ServiceNotFoundException(String serviceName, Throwable throwable) {
        super();
        this.serviceName = serviceName;
        this.originException = throwable;
    }

    public String getMessage() {
        return "Could find service : " + serviceName;
    }

}
