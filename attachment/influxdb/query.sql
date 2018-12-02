-- 获取应用30分被调用量数据 按分钟分组统计
select count(consumerIP) from monitor_record where time > now() - 30m and providerName = 'blades-monitor'  group by time(1m)  limit 30;

select count(consumerIP) from monitor_record where time > now() - 30m  group by providerName;