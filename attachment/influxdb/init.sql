-- 创建数据库
CREATE DATABASE blades_monitor;
use blades_monitor
-- 创建存储策略（设置为一个月）
create retention policy "default" on "blades_monitor" duration 30d replication 1 default;