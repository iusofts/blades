-- 获取应用30分被调用量数据 按分钟分组统计
select count(*) from monitor_record where time > now() - 30m and providerName = 'blades-monitor'  group by time(1m)  limit 30;

-- 获取30分钟内应用调用总量
select count(*) from monitor_record where time > now() - 30m  group by providerName;

-- 获取1天内应用调用依赖情况
select count(*) from monitor_record where time > now() - 1d and providerName!=''  group by consumerName,providerName;

-- 获取服务消费者信息
select count(*) from monitor_record where time > now() - 1d  group by serviceName,consumerName,consumerIP,consumerPort;

-- 获取1天内服务调用依赖情况
select count(*) from monitor_record where time > now() - 1d  group by serviceName,consumerName,providerName;

-- 获取1天内服务成功调用量、平均耗时和最大耗时
select count(costTime),MEAN(costTime),MAX(costTime) from monitor_record where time > now() - 1d and  success = 'true'  group by serviceName;

-- 获取1天内服务调用失败数量
select count(costTime) from monitor_record where time > now() - 1d and  success = 'false'  group by serviceName;

