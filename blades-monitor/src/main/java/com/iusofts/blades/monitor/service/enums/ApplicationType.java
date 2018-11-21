package com.iusofts.blades.monitor.service.enums;

/**
 * 应用类型
 *
 * @author Ivan Shen
 */
public enum ApplicationType {

	PROVIDER("提供者", 1), CONSUMER("消费者", 2), PROVIDER_AND_CONSUMER("提供者和消费者", 3);

	private String name;
	private int code;

	ApplicationType(String name, int code) {
		this.name = name;
		this.code = code;
	}

	public static ApplicationType get(int code) {
		for (ApplicationType c : ApplicationType.values()) {
			if (c.getCode() == code) {
				return c;
			}
		}
		return null;
	}

	public static String getName(int code) {
		for (ApplicationType c : ApplicationType.values()) {
			if (c.getCode() == code) {
				return c.name;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
