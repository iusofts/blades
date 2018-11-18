package com.iusofts.blades.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ServiceLocator implements ApplicationContextAware {

	private static final ServiceLocator instance = new ServiceLocator();

	private ApplicationContext context;

	private ServiceLocator() {
	}

	public static ServiceLocator init() {
		return instance;
	}

	public Object getService(Class<?> _class) {
		if (context == null) {
			return null;
		}
		String[] name = context.getBeanNamesForType(_class);
		if (name == null || name.length != 1) {
			throw new IllegalArgumentException(_class + " haven instance or have not instance in spring context!");
		}
		return context.getBean(name[0]);
	}

	public Object getService(String beanName) {
		if (context == null) {
			return null;
		}
		return context.getBean(beanName);
	}

	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}
}
