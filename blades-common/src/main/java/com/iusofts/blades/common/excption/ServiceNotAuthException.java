package com.iusofts.blades.common.excption;

public class ServiceNotAuthException extends RuntimeException {
    private String serviceName;

    private String group;

    public ServiceNotAuthException(String serviceName,String group){
        this.serviceName = serviceName;
        this.group = group;
    }

    public String getMessage() {
        return "access "+ this.serviceName +" is not permitted for group : " + this.group;
    }
}
