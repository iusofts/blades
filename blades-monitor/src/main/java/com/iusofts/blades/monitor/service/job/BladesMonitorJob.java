package com.iusofts.blades.monitor.service.job;

import com.iusofts.blades.monitor.inft.dto.Application;
import com.iusofts.blades.monitor.inft.enums.ApplicationType;
import com.iusofts.blades.monitor.service.util.BladesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Blades监控定时任务
 *
 * @author Ivan Shen
 */
public class BladesMonitorJob {

	//@Resource
	//private MailService mailService;

	@Value("${spring.profiles.active}")
	private String envName;

	private static Map<String, String> envNameMap=new HashMap<String,String>(){{put("default","本地环境");put("dev","开发环境");put("test","测试环境");put("production","生产环境");}};

	private static List<String> noProviders = new ArrayList<String>();
	private static List<String> providers = new ArrayList<String>();

	public static void main(String[] args) throws InterruptedException {
		BladesMonitorJob job = new BladesMonitorJob();
		while (true) {
			job.checkAll();
			Thread.sleep(3000);
		}
	}

	/**
	 * 检查全部
	 */
	public void checkAll() {
		BladesUtil.init();

		String checkNoProviders = checkNoProviders();
		String checkProviders = checkProviders();

		StringBuilder sb = new StringBuilder();

		if(!"1".equals(checkNoProviders)){
			System.out.println(checkNoProviders);
			sb.append(checkNoProviders+"<br/><br/><br/>");
		}


		if(!"1".equals(checkProviders)){
			System.out.println(checkProviders);
			sb.append(checkProviders);
		}

		if(StringUtils.isNotBlank(sb.toString())){

			String devNameStr = envNameMap.get(envName);
			if(devNameStr==null) devNameStr = envName;

			// 发送邮件
			//mailService.sendSystemMail("["+devNameStr+"] Blades服务异常通知",sb.toString());
		}
	}

	/**
	 * 检查是否存在没有提供者的服务
	 */
	private String checkNoProviders() {
		String result = "1";
		List<String> noProvidersList = new ArrayList<>();
		List<Application> applicationList = BladesUtil.getApplicationList();
		if (!CollectionUtils.isEmpty(applicationList)) {
			for (Application application : applicationList) {
				if (application.isInactive()) {
					noProvidersList.add(application.getAppName());
				}
			}
		}
		if (noProvidersList != null && noProvidersList.size() > 0) {
			List<String> diff = diff(noProvidersList, noProviders);
			if (diff.size() > 0) {
				result = "[<font color=red >严重</font>] 以下服务没有提供者：<font color=blue >" + diff + "</font>";
			}
			noProviders = noProvidersList;
		}
		return result;
	}

	/**
	 * 检查是否有提供者下线
	 */
	private String checkProviders() {
		String result = "1";
		List<String> providersList = new ArrayList<>();
		List<Application> applicationList = BladesUtil.getApplicationList();
		if (!CollectionUtils.isEmpty(applicationList)) {
			for (Application application : applicationList) {
				if (!application.isInactive() && (application.getType() == ApplicationType.PROVIDER
						|| application.getType() == ApplicationType.PROVIDER_AND_CONSUMER)) {
					providersList.add(application.getAppName());
				}
			}
		}
		if (providersList != null && providersList.size() > 0) {
			List<String> diff = diff(providers, providersList);
			if (diff.size() > 0) {
				result = "[<font color=blue >提醒</font>] 以下提供者被下线：" + diff;
			}
			providers = providersList;
		}
		return result;
	}

	/**
	 * 求ls对ls2的差集,即ls中有，但ls2中没有的
	 *
	 * @param ls
	 * @param ls2
	 * @return
	 */
	public static List diff(List ls, List ls2) {
		List list = new ArrayList(Arrays.asList(new Object[ls.size()]));
		Collections.copy(list, ls);
		list.removeAll(ls2);
		return list;
	}
}
