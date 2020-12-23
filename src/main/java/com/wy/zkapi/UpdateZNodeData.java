package com.wy.zkapi;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * @author yunze
 * @Classname CreateSession
 * @Description 创建会话节点
 * @Date 2020/12/22 9:17 下午
 * @Company 光云科技有限公司
 */
public class UpdateZNodeData implements Watcher {

    private static ZooKeeper zooKeeper;

    // 建立会话
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        /**
         * step1 客户端可以通过创建一个zk实例来连接zk服务器
         * new zookeeper（connectString,sessionTimeOut,Wather）
         * connectString 连接地址，IP : PORT
         * sessionTimeOut 会话超时时间 单位毫秒
         * Wather  监听器（当特定事件触发监听时，zk会通过watcher通知客户端）
         */
        zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new UpdateZNodeData());
        System.out.println(zooKeeper.getState() + "---连接中");
        System.out.println("zookeeper 客户端和服务端会话真正建立");
        Thread.sleep(Integer.MAX_VALUE);



    }


    /**
     * 事件通知
     * 回调方法：处理来自服务器端的wather的通知
     *
     * @param watchedEvent
     */
    public void process(WatchedEvent watchedEvent) {
        System.out.println("zookeeper---" + zooKeeper.getState());
        //其实就是一个SyncConnected事件
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            System.out.println("Watcher 异步回调，事件通知");
            // 解除主程序在的countDownLatch上的等待阻塞
            try {
                updateNodeSync();
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // todo  在此回调过程中进行是可以行的，因为此时已经真正连接
        }


    }


    /**
     * path:路径
     * data:要修改的内容 byte[]
     * version:为-1，表示对最新版本的数据进行修改
     * zooKeeper.setData(path, data,version);
     */
    private void updateNodeSync() throws KeeperException, InterruptedException {
        byte[] data = zooKeeper.getData("/wy-persistent", false, null);
        System.out.println("修改前的值：" + new String(data));
        Stat stat = zooKeeper.setData("/wy-persistent", "进行修改持久性".getBytes(), -1);
        byte[] endBytes = zooKeeper.getData("/wy-persistent", false, null);
        System.out.println("修改后的值：" + new String(endBytes));
    }
}
