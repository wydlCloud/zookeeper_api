package com.wy.zkClient;

import org.I0Itec.zkclient.ZkClient;

/**
 * @author yunze
 * @Classname CreateSession
 * @Description zkClient的实现  开源客户端
 * @Date 2020/12/22 11:23 下午
 * @Company 光云科技有限公司
 */
public class CreateNode {

    /**
     * 借助zkClient完成会话的创建
     * @param args
     */
    public static void main(String[] args) {
        /**
         * 创建zKClient实例，就可以完成连接，会话的创建
         * serverstring： ip+port
         *
         * zkclient 通过定义zookeeper api内部封装，将原生api异步创建进行同步化了
         */
        ZkClient zkClient = new ZkClient("127.0.0.1:2181");
        System.out.println("会话创建——————");

        zkClient.createPersistent("/wy-persistent/b1", true);
        System.out.println("节点递归创建完成");

    }
}
