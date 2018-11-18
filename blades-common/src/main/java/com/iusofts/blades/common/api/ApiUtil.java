package com.iusofts.blades.common.api;

import com.iusofts.blades.common.util.HttpUtil;
import com.iusofts.blades.common.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Api调用工具类 <br/>
 * 注：请根据应用场景调用适合的API
 *
 */
public class ApiUtil {

	private static Logger LOG = LoggerFactory.getLogger(ApiUtil.class);

	private static ObjectMapper objectMapper;
	static {
		objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public static void init() {
	}

	/**
	 * Api执行简易版 <br/>
	 * 注：性能最好，用于纯转发无任何数据二次处理的时候
	 *
	 * @param url
	 * @param param
	 * @return
	 */
	public static String executeToString(String url, Object param) {
		long startTime = System.currentTimeMillis();
		String params = JsonUtils.obj2json(param);
		String result = sendPost(url, params, "utf-8");
		long endTime = System.currentTimeMillis();
		LOG.info("url:{},param:{},time:{}", url, params, endTime - startTime);
		return result;
	}

	/**
	 * Api执行简易版 <br/>
	 * 注：性能最好，用于纯转发无任何数据二次处理的时候
	 *
	 * @param url
	 * @param param
	 * @return
	 */
	public static String executeToString(String url, String param) {
		long startTime = System.currentTimeMillis();
		String result = sendPost(url, param, "utf-8");
		long endTime = System.currentTimeMillis();
		LOG.info("url:{},param:{},time:{}", url, param, endTime - startTime);
		return result;
	}

	/**
	 * Api执行通用版 <br/>
	 * 注：性能第二,用于获取返回结果中少量属性时使用（如：只获取执行状态）
	 *
	 * @param url
	 * @param param
	 * @return
	 */
	public static Map<String, Object> execute(String url, Object param) {
		long startTime = System.currentTimeMillis();
		String params = JsonUtils.obj2json(param);
		String result = sendPost(url, params, "utf-8");
		Map<String, Object> resultMap;
		if (StringUtils.isNotBlank(result)) {
			resultMap = JsonUtils.json2map(result);
		} else {
			resultMap = new HashMap<String, Object>();
			resultMap.put("code", 501);
			resultMap.put("msg", url + ":求请失败！");
			resultMap.put("success", false);
		}
		long endTime = System.currentTimeMillis();
		LOG.info("url:{},param:{},time:{}", url, param, endTime - startTime);
		return resultMap;
	}

	/**
	 * Api执行普通版 <br/>
	 * 注：性能第三,适用于代理调用
	 *
	 * @param url
	 * @param param
	 * @return
	 */
	public static ResponseVo<Object> execute(String url, String param) {
		long startTime = System.currentTimeMillis();
		String result = sendPost(url, param, "utf-8");
		boolean isOK = true;
		if (StringUtils.isBlank(result)) {
			isOK = false;
			result = "{}";
		}
		ResponseVo<Object> vo = null;
		try {
			vo = objectMapper.readValue(result, new TypeReference<ResponseVo<Object>>() {
			});
			if (isOK == false) {
				vo.setCode(501);
				vo.setMsg(url + ":求请失败！");
				vo.setSuccess(false);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		LOG.info("url:{},param:{},time:{}", url, param, endTime - startTime);
		return vo;
	}

	/**
	 * Api执行增强版-单线程 <br/>
	 * 注：性能偏弱,支持复杂嵌套泛型,适用于业务处理时调用
	 *
	 * @param url
	 * @param param
	 * @param typeReference
	 *            引用类型 复杂泛型使用例子： new
	 *            TypeReference&lt;ResponseVo&lt;Page&lt;AccountBookRecordVo
	 *            >>>(){}
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static <T> ResponseVo<T> execute(String url, Object param, TypeReference typeReference) {
		long startTime = System.currentTimeMillis();
		String params = JsonUtils.obj2json(param);
		String result = sendPost(url, params, "utf-8");
		boolean isOK = true;
		if (StringUtils.isBlank(result)) {
			isOK = false;
			result = "{}";
		}
		ResponseVo<T> vo = null;
		try {
			if (typeReference == null) {
				typeReference = new TypeReference<Object>() {
				};
			}
			vo = objectMapper.readValue(result, typeReference);
			if (isOK == false) {
				vo.setCode(501);
				vo.setMsg(url + ":求请失败！");
				vo.setSuccess(false);
			}
		} catch (IOException e) {
			LOG.error("deserialization error", e);
		}
		long endTime = System.currentTimeMillis();
		LOG.info("url:{},param:{},time:{}", url, param, endTime - startTime);
		return vo;
	}


	@SuppressWarnings("rawtypes")
	public static Object mapper(Object param, TypeReference typeReference) {
		try {
			return objectMapper.readValue(JsonUtils.obj2json(param), typeReference);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * POST请求，字符串形式数据
	 *
	 * @param url
	 *            请求地址
	 * @param param
	 *            请求数据
	 * @param charset
	 *            编码方式
	 */
	private static String sendPost(String url, String param, String charset) {

	    OutputStreamWriter  out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("charset", charset);
			 conn.setRequestProperty("contentType", charset);
			conn.setRequestProperty("Content-Type", "application/json");

			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new OutputStreamWriter(conn.getOutputStream(), charset);
			// 发送请求参数
			out.write(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			LOG.error("url:" + url + ",param:" + param + ", post failed", e);
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 获取泛型的Collection Type
	 *
	 * @param collectionClass
	 *            泛型的Collection
	 * @param elementClasses
	 *            元素类
	 * @return JavaType Java类型
	 * @since 1.0
	 */
	public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	/**
	 * 通用POST请求
	 * @param url
	 * @param param
	 * @param clazz
	 * @return
	 */
	public static <T> T post(String url, Object param, Class<T> clazz) {
		long startTime = System.currentTimeMillis();
		String params = JsonUtils.obj2json(param);
		String result = sendPost(url, params, "utf-8");
		if (StringUtils.isNotBlank(result)) {
			try {
				return objectMapper.readValue(result, clazz);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		long endTime = System.currentTimeMillis();
		LOG.info("url:{},param:{},time:{}", url, params, endTime - startTime);
		return null;
	}

	/**
	 * 通用get请求
	 * @param url
	 * @param clazz
	 * @return
	 */
	public static <T> T get(String url, Class<T> clazz) {
		long startTime = System.currentTimeMillis();
		String result = HttpUtil.sendGet(url, "utf-8");
		if (StringUtils.isNotBlank(result)) {
			try {
				return objectMapper.readValue(result, clazz);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		long endTime = System.currentTimeMillis();
		LOG.info("url:{},time:{}", url, endTime - startTime);
		return null;
	}

}