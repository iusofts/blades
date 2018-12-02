package com.iusofts.blades.common.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * ZooKeeper Java Api<br>
 * ZK Api
 *
 * @author
 */
public class JavaApiSample implements Watcher {

    private static Logger logger = LoggerFactory.getLogger(JavaApiSample.class);

    private static JavaApiSample javaApiSample = null;
    private static final int SESSION_TIMEOUT = 10000;
    private static final String CONNECTION_STRING = "127.0.0.1:2181";
    private static final String ZK_PATH = "/blades-dev";
    private ZooKeeper zk = null;

    private CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static JavaApiSample getInstance() {
        if (javaApiSample == null) {
            javaApiSample = new JavaApiSample();
            javaApiSample.createConnection(CONNECTION_STRING, SESSION_TIMEOUT);
        }
        return javaApiSample;
    }

    /**
     * 创建ZK连接
     *
     * @param connectString  ZK服务器地址列表
     * @param sessionTimeout Session超时时间
     */
    public void createConnection(String connectString, int sessionTimeout) {
        this.releaseConnection();
        try {
            zk = new ZooKeeper(connectString, sessionTimeout, this);
            connectedSemaphore.await();
        } catch (InterruptedException e) {
            logger.error("连接创建失败", e);
        } catch (IOException e) {
            logger.error("连接创建失败", e);
        }
    }

    /**
     * 关闭ZK连接
     */
    public void releaseConnection() {
        if (this.zk != null) {
            try {
                this.zk.close();
            } catch (InterruptedException e) {
                // ignore
                e.printStackTrace();
            }
        }
    }

    /**
     * 检查节点是否存在
     *
     * @param path
     * @return
     */
    public boolean exist(String path) {
        try {
            Stat stat = this.zk.exists(path, true);
            return stat != null;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建节点
     *
     * @param path 节点path
     * @param data 初始数据内容
     * @return
     */
    public boolean createPath(String path, String data) {
        try {
            logger.info("节点创建成功, Path: "
                    + this.zk.create(path, //
                    data.getBytes(), //
                    Ids.OPEN_ACL_UNSAFE, //
                    CreateMode.PERSISTENT)
                    + ", content: " + data);
        } catch (KeeperException e) {
            logger.error("节点创建失败", e);
        } catch (InterruptedException e) {
            logger.error("节点创建失败", e);
        }
        return true;
    }

    /**
     * 读取指定节点数据内容
     *
     * @param path 节点path
     * @return
     */
    public String readData(String path) {
        try {
            logger.debug("获取数据成功，path：" + path);
            return new String(this.zk.getData(path, false, null));
        } catch (KeeperException e) {
            logger.error("读取数据失败，path: " + path, e);
            return "";
        } catch (InterruptedException e) {
            logger.error("读取数据失败，path: " + path, e);
            return "";
        }
    }

    /**
     * 更新指定节点数据内容
     *
     * @param path 节点path
     * @param data 数据内容
     * @return
     */
    public boolean writeData(String path, String data) {
        try {
            logger.info("更新数据成功，path：" + path + ", stat: " +
                    this.zk.setData(path, data.getBytes(), -1));
            return true;
        } catch (KeeperException e) {
            logger.error("更新数据失败，path: " + path, e);
        } catch (InterruptedException e) {
            logger.error("更新数据失败，path: " + path, e);
        }
        return false;
    }

    /**
     * 删除指定节点
     *
     * @param path 节点path
     */
    public void deleteNode(String path) {
        try {
            this.zk.delete(path, -1);
            logger.info("删除节点成功，path：" + path);
        } catch (KeeperException e) {
            logger.error("删除节点失败，path: " + path, e);
        } catch (InterruptedException e) {
            logger.error("删除节点失败，path: " + path, e);
        }
    }

    public List<String> redChildList(String path) {
        List<String> list = new ArrayList<String>();
        try {
            list = this.zk.getChildren(path, true);
        } catch (KeeperException e) {
            logger.error("获取子节点失败，path: " + path, e);
        } catch (InterruptedException e) {
            logger.error("获取子节点失败，path: " + path, e);
        }
        return list;
    }

    public static void main(String[] args) {

        JavaApiSample sample = new JavaApiSample();
        sample.createConnection(CONNECTION_STRING, SESSION_TIMEOUT);
       /* if ( sample.createPath( ZK_PATH, "我是节点初始内容" ) ) {
            System.out.println();
            System.out.println( "数据内容: " + sample.readData( ZK_PATH ) + "\n" );
            sample.writeData( ZK_PATH, "更新后的数据" );
            System.out.println( "数据内容: " + sample.readData( ZK_PATH ) + "\n" );
            sample.deleteNode( ZK_PATH );
        } */
        System.out.println("数据内容: " + sample.redChildList(ZK_PATH) + "\n");
        sample.releaseConnection();
    }

    /**
     * 收到来自Server的Watcher通知后的处理。
     */
    public void process(WatchedEvent event) {
        logger.info("收到事件通知：" + event.getState() + "\n");
        if (KeeperState.SyncConnected == event.getState()) {
            connectedSemaphore.countDown();
        }

    }

}