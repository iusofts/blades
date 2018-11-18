package com.iusofts.blades.registry;

public interface IRegister {
    void registerService(String hostName, String ip, String port, String classPath, String methodPath, String serviceName, String serviceGroup);
}
