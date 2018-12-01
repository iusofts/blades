package com.iusofts.blades.monitor.inft.dto;


import com.iusofts.blades.monitor.inft.enums.ApplicationType;

/**
 * 应用
 *
 * @author Ivan Shen
 */
public class Application {

	/**
	 * 应用名称
	 */
	private String appName;

	/**
	 * 应用类型
	 */
	private ApplicationType type;

	/**
	 * 提供服务数
	 */
	private Integer provideServiceAmount;

	/**
	 * 消费服务数
	 */
	private Integer consumeServiceAmount;

	/**
	 * 应用不活跃
	 */
	private boolean inactive;

	public Application() {
	}

	public Application(String appName) {
		this.appName = appName;
		this.consumeServiceAmount = 0;
		this.provideServiceAmount = 0;
	}

	/**
	 * 累加提供服务数
	 */
	public void incrProvideServiceAmount() {
		this.provideServiceAmount++;
	}

	/**
	 * 累加消费服务数
	 */
	public void incrConsumeServiceAmount() {
		this.consumeServiceAmount++;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public ApplicationType getType() {
		return type;
	}

	public void setType(ApplicationType type) {
		this.type = type;
	}

	public Integer getProvideServiceAmount() {
		return provideServiceAmount;
	}

	public void setProvideServiceAmount(Integer provideServiceAmount) {
		this.provideServiceAmount = provideServiceAmount;
	}

	public Integer getConsumeServiceAmount() {
		return consumeServiceAmount;
	}

	public void setConsumeServiceAmount(Integer consumeServiceAmount) {
		this.consumeServiceAmount = consumeServiceAmount;
	}

	public boolean isInactive() {
		return inactive;
	}

	public void setInactive(boolean inactive) {
		this.inactive = inactive;
	}
}
