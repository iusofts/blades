package com.iusofts.blades.monitor.inft.enums;

/**
 * 应用调用量统计类型
 */
public enum ApplicationCallCountType {

	NOW("90s","3s", 1), THIRTY_MINUTES("30m","1m", 2), ONE_DAY("24h","1h", 3);

	private String name;
	private String groupBy;
	private int code;

	ApplicationCallCountType(String name, String groupBy, int code) {
		this.name = name;
		this.groupBy = groupBy;
		this.code = code;
	}

	public static ApplicationCallCountType get(int code) {
		for (ApplicationCallCountType c : ApplicationCallCountType.values()) {
			if (c.getCode() == code) {
				return c;
			}
		}
		return null;
	}

	public static String getName(int code) {
		for (ApplicationCallCountType c : ApplicationCallCountType.values()) {
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

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}
}
