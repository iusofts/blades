package com.iusofts.blades.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Json处理工具
 */
public class JsonUtils {

    private static SerializeConfig mapping = new SerializeConfig();

    static {
        mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * javaBean、list、map convert to json string
     */
    public static String obj2json(Object obj) {
        return JSON.toJSONString(obj, mapping);
    }

    /**
     * javaBean、list、map convert to json string
     */
    public static Map<String, Object> obj2map(Object obj) {
        return obj == null ? null : json2map(JSON.toJSONString(obj, mapping));
    }

    /**
     * javaBean、list、map convert to json string
     */
    public static Map<String, Object> obj2map(Object obj, SerializeFilter filter) {
        return obj == null ? null : json2map(JSON.toJSONString(obj, mapping, filter));
    }

    /**
     * javaBean、list、map convert to json string
     */
    public static String obj2Json(Object obj, Boolean formatted) {
        // 在上面的代码中添加下面的这行代码，则可以将转换后的字段名称的引号去掉。
        // serializer.config(SerializerFeature.QuoteFieldNames, false);
        // SerializerFeature.UseSingleQuotes 使用单引号
        return formatted ? JSON.toJSONString(obj, mapping, SerializerFeature.PrettyFormat)
                : JSON.toJSONString(obj, mapping);// 格式化数据，方便阅读
    }

    /**
     * json string convert to javaBean、map
     */
    public static <T> T json2obj(String jsonStr, Class<T> clazz) {
        return JSON.parseObject(jsonStr, clazz);
    }

    /**
     * json array string convert to list with javaBean
     */
    public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz) {
        return JSON.parseArray(jsonArrayStr, clazz);
    }

    /**
     * json string convert to map
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<String, Object> json2map(String jsonStr) {
        return json2obj(jsonStr, Map.class);
    }

    /**
     * json string convert to map with javaBean
     */
    public static <T> Map<String, T> json2map(String jsonStr, Class<T> clazz) {
        Map<String, T> map = JSON.parseObject(jsonStr, new TypeReference<Map<String, T>>() {
        });
        for (Entry<String, T> entry : map.entrySet()) {
            JSONObject obj = (JSONObject) entry.getValue();
            map.put(entry.getKey(), JSONObject.toJavaObject(obj, clazz));
        }
        return map;
    }
}
