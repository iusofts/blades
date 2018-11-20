package com.iusofts.blades.core.util;

import com.iusofts.blades.common.util.JsonUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class RestTemplateUtils {

    protected static final Logger log = LoggerFactory.getLogger(RestTemplateUtils.class);

    /**
     * HTTP POST
     *
     * @param restTemplate restTemplate
     * @param path         请求路径
     * @param request      请求消息体
     * @param responseType 响应类型
     * @param <T>          响应类型
     * @param extraHeaders 额外的请求头
     * @return 响应结果
     * @throws Exception
     */
    public static <T> T post(RestTemplate restTemplate, String path, Object request, Class<T> responseType, Map<String, String> extraHeaders) throws Exception {

        log.debug("begin to do http post. path:{}, request :{}", path, request);

        MultiValueMap<String, String> headers = convert(extraHeaders);
        HttpEntity<Object> requestEntity = new HttpEntity<>(request, headers);

        T t = restTemplate.postForObject(path, requestEntity, responseType);

        log.debug("end to do http get. path:{}, request params:{}. response body: {}", path, request, t);

        return t;
    }


    /**
     * HTTP POST
     *
     * @param restTemplate restTemplate
     * @param path         请求路径
     * @param request      请求消息体
     * @param responseType 响应类型
     * @param <T>          响应类型
     * @return 响应结果
     * @throws Exception
     */
    public static <T> T post(RestTemplate restTemplate, String path, Object request, Class<T> responseType) throws Exception {
        return post(restTemplate, path, request, responseType, null);
    }


    /**
     * HTTP GET
     *
     * @param restTemplate restTemplate
     * @param path         请求路径
     * @param request      请求体，object，如果是个map，会自动转化为url参数
     * @param responseType 响应类型
     * @param <T>          响应类型
     * @param extraHeaders 额外的请求头
     * @return 结果
     * @throws Exception
     */
    public static <T> T get(RestTemplate restTemplate, String path, Object request, Class<T> responseType, Map<String, String> extraHeaders) throws Exception {

        log.debug("begin to do http get. path:{}, request params:{}", path, request);

        //add accept json
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        //添加额外的请求头
        if (MapUtils.isNotEmpty(extraHeaders)) {
            for (Map.Entry<String, String> entry : extraHeaders.entrySet()) {
                headers.set(entry.getKey(), entry.getValue());
            }
        }

        HttpEntity<?> entity = new HttpEntity<>(headers);

        //url
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(path);

        //添加参数
        Map<String, ?> params = new HashMap<>();
        if (request instanceof Map) {
            params = (Map<String, ?>) request;
        } else {
            params = JsonUtils.obj2map(request);
        }
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, ?> urlParam : params.entrySet()) {
                builder.queryParam(urlParam.getKey(), urlParam.getValue());
            }
        }
        final URI getURI = builder.build().encode().toUri();

        HttpEntity<T> response = restTemplate.exchange(getURI, HttpMethod.GET, entity, responseType);

        log.debug("end to do http get. path:{}, request params:{}. response body: {}", path, request, response.getBody());

        return response.getBody();
    }


    /**
     * HTTP GET
     *
     * @param restTemplate restTemplate
     * @param path         请求路径
     * @param request      请求体，object，如果是个map，会自动转化为url参数
     * @param responseType 响应类型
     * @param <T>          响应类型
     * @return 结果
     * @throws Exception
     */
    public static <T> T get(RestTemplate restTemplate, String path, Object request, Class<T> responseType) throws Exception {
        return get(restTemplate, path, request, responseType, null);
    }


    private final static MultiValueMap<String, String> convert(Map<String, String> extraHeaders) {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap();

        if (MapUtils.isNotEmpty(extraHeaders)) {
            for (Map.Entry<String, String> entry : extraHeaders.entrySet()) {
                headers.add(entry.getKey(), entry.getValue());
            }
        }

        return headers;
    }
}
