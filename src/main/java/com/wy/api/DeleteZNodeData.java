package com.wy.api;

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
public class DeleteZNodeData implements Watcher {

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
        zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new DeleteZNodeData());
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
                delNodeSync();
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // todo  在此回调过程中进行是可以行的，因为此时已经真正连接
        }


    }

    private void delNodeSync() throws KeeperException, InterruptedException {
        // 如果节点下面还有子节点的话，这种情况下是不允许删除的，需要删除所有子节点，此节点才可以删除，不然会报错
        byte[] data = zooKeeper.getData("/wy-persistent2", false, null);
        System.out.println("删除前的节点数据---"+new String(data));
        zooKeeper.delete("/wy-persistent2",-1);
        //在此处会报错，因为此节点在上一步骤已经删除掉了
        byte[] data1 = zooKeeper.getData("/wy-persistent2", false, null);
        System.out.println("删除后的节点数据---"+new String(data1));
    }


}
