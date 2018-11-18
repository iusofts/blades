package com.iusofts.blades.registry;

import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.InstanceSerializer;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.io.ByteArrayOutputStream;

public class CuratorServiceInstanceSerializer<T> implements InstanceSerializer<T> {

    private final ObjectMapper mapper;
    private final Class<T> payloadClass;
    private final JavaType type;

    public CuratorServiceInstanceSerializer(Class<T> payloadClass) {
        this.payloadClass = payloadClass;
        this.mapper = new ObjectMapper();
        this.mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.type = this.mapper.getTypeFactory().constructType(ServiceInstance.class);
    }

    public ServiceInstance<T> deserialize(byte[] bytes) throws Exception {
        ServiceInstance rawServiceInstance = this.mapper.readValue(bytes, this.type);
        this.payloadClass.cast(rawServiceInstance.getPayload());
        return rawServiceInstance;
    }

    public byte[] serialize(ServiceInstance<T> instance) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.mapper.writeValue(out, instance);
        return out.toByteArray();
    }
}
