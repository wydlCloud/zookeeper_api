package com.wy.zkapi;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author yunze
 * @Classname CreateSession
 * @Description 创建会话节点
 * @Date 2020/12/22 9:17 下午
 * @Company 光云科技有限公司
 */
public class GetZNodeData implements Watcher {

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
        zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new GetZNodeData());
        System.out.println(zooKeeper.getState() + "---连接中");

        System.out.println("zookeeper 客户端和服务端会话真正建立");
        System.out.println("zookeeper---" + zooKeeper.getState());
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 获取某个节点的内容
     */
    private static void getNodeData() throws KeeperException, InterruptedException {

        /**
         * path    : 获取数据的路径
         * watch    : 是否开启监听
         * stat    : 节点状态信息
         * null: 表示获取最新版本的数据
         *  zk.getData(path, watch, stat);
         */
        byte[] data = zooKeeper.getData("/wy-persistent", false, null);
        System.out.println(new String(data));
    }

    /**
     * 获取某个节点的子节点列表方法
     */
    public static void getChildrens() throws KeeperException, InterruptedException {

        /**
         path:路径
         watch:是否要启动监听，当子节点列表发生变化，会触发监听
         zooKeeper.getChildren(path, watch);
         */
        List<String> children = zooKeeper.getChildren("/wy-persistent", true);
        System.out.println(children);
    }

    /**
     * 事件通知
     * 回调方法：处理来自服务器端的wather的通知
     *
     * @param watchedEvent
     */
    public void process(WatchedEvent watchedEvent) {
         /**
            子节点列表发生改变时，服务器端会发生noteChildrenChanged事件通知
            要重新获取子节点列表，同时注意：通知是一次性的，需要反复注册监听
         */
        if (watchedEvent.getType()==Event.EventType.NodeChildrenChanged){
            List<String> children = null;
            try {
                children = zooKeeper.getChildren("/wy-persistent", true);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(children);
        }

        //其实就是一个SyncConnected事件
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            System.out.println("Watcher 异步回调，事件通知");
            // 解除主程序在的countDownLatch上的等待阻塞
            try {
                getNodeData();
                getChildrens();
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // todo  在此回调过程中进行是可以行的，因为此时已经真正连接
        }

    }
}
