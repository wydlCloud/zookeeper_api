package com.wy.zkapi;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author yunze
 * @Classname CreateSession
 * @Description 创建会话节点
 * @Date 2020/12/22 9:17 下午
 * @Company 光云科技有限公司
 */
public class CreateSession implements Watcher {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    // 建立会话
    public static void main(String[] args) throws IOException, InterruptedException {
        /**
         * step1 客户端可以通过创建一个zk实例来连接zk服务器
         * new zookeeper（connectString,sessionTimeOut,Wather）
         * connectString 连接地址，IP : PORT
         * sessionTimeOut 会话超时时间 单位毫秒
         * Wather  监听器（当特定事件触发监听时，zk会通过watcher通知客户端）
         */
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new CreateSession());
        System.out.println(zooKeeper.getState()+"---连接中");
        // 计数工具类 CountDownLatch ：不让main方法结束，让线程处于阻塞状态，等待阻塞
        countDownLatch.await();

        System.out.println("zookeeper 客户端和服务端会话真正建立");
        System.out.println("zookeeper---"+zooKeeper.getState());

        /**
         * step2 客户端连接到服务端后，通过zk实例来进行创建节点
         */


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
        }

    }
}
