package com.wy.api;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author yunze
 * @Classname CreateSession
 * @Description 创建会话节点
 * @Date 2020/12/22 9:17 下午
 * @Company 光云科技有限公司
 */
public class CreateZNode implements Watcher {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);
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
        zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new CreateZNode());
        System.out.println(zooKeeper.getState() + "---连接中");
        // 计数工具类 CountDownLatch ：不让main方法结束，让线程处于阻塞状态，等待阻塞
        countDownLatch.await();

        System.out.println("zookeeper 客户端和服务端会话真正建立");
        System.out.println("zookeeper---" + zooKeeper.getState());

        /**
         * step2 客户端连接到服务端后，通过zk实例来进行创建节点
         * 创建节点有两种方式，
         * 1.同步方式
         * 2.异步方式
         */
        createNodeSync();
    }

    private static void createNodeSync() throws KeeperException, InterruptedException {
        /**
         * 1.path :节点创建路径
         * 2.data[] : 节点创建要保存的数据，是个byte类型
         * 3.acl ： 节点创建的权限信息(4种类型)
         *
         * ANYONE_ID_UNSAFE :表示任何人
         * AUTH_IDS : 此ID仅可用于设置ACL。它将被客户端验证的ID替换
         * OPEN_ACL_UNSAFE: 这是一个完全开放的ACL（常用--world:anyone）
         * CREATOR_ALL_ACL: 此ACL授予创建者身份验证ID的所有权限
         *
         * 4.createNode：创建节点的4种类型
         *
         *  PERSISTENT:持久性节点
         *  PERSISTENT_SEQUENTIAL:持久顺序节点
         *  EPHEMERAL:临时节点
         *  EPHEMERAL_SEQUENTIAL：临时顺序性节点
         */
        // 持久性节点，返回参数为路径
        String node_persist_id = zooKeeper.create("/wy-persistent2", "持久节点".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        String node_ephemeral_id = zooKeeper.create("/wy-ephemeral2", "临时节点".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        String persistent_sequential_id = zooKeeper.create("/wy-persistent_sequential2", "持久顺序节点".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        System.out.println("持久节点---"+node_persist_id);
        System.out.println("临时节点---"+node_ephemeral_id);
        System.out.println("持久顺序节点---"+persistent_sequential_id);
    }

    /**
     * 事件通知
     * 回调方法：处理来自服务器端的wather的通知
     *
     * @param watchedEvent
     */
    public void process(WatchedEvent watchedEvent) {
        //其实就是一个SyncConnected事件
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            System.out.println("Watcher 异步回调，事件通知");
            // 解除主程序在的countDownLatch上的等待阻塞
            countDownLatch.countDown();
            // todo  在此回调过程中进行是可以行的，因为此时已经真正连接
        }

    }
}
