-- 获取应用30分被调用量数据 按分钟分组统计
select count(*) from monitor_record where time > now() - 30m and providerName = 'blades-monitor'  group by time(1m)  limit 30;

-- 获取30分钟内应用调用总量
select count(*) from monitor_record where time > now() - 30m  group by providerName;

-- 获取1天内应用调用依赖情况
select count(*) from monitor_record where time > now() - 1d  group by consumerName,providerName;

-- 获取30分钟内活跃的消费者
select count(*) from monitor_record where time > now() - 1d  group by consumerIP,consumerPort;
