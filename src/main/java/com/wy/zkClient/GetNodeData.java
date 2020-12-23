package com.wy.zkClient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * @author yunze
 * @Classname CreateSession
 * @Description zkClient的实现  开源客户端
 * @Date 2020/12/22 11:23 下午
 * @Company 光云科技有限公司
 */
public class GetNodeData {

    /**
     * 借助zkClient完成会话的创建
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        /**
         * 创建zKClient实例，就可以完成连接，会话的创建
         * serverstring： ip+port
         *
         * zkclient 通过定义zookeeper api内部封装，将原生api异步创建进行同步化了
         */
        ZkClient zkClient = new ZkClient("127.0.0.1:2181");
        System.out.println("会话创建——————");

        List<String> children = zkClient.getChildren("/wy-persistent");
        // 注册监听事件
         /**
            客户端可以对一个不存在的节点进行子节点变更的监听
            只要该节点的子节点列表发生变化，或者该节点本身被创建或者删除，都会触发监听
         */
        zkClient.subscribeChildChanges("/wy-persistent-get", new IZkChildListener() {

            /**
                s : parentPath
                list : 变化后子节点列表
             */

            public void handleChildChange(String parentPath, List<String> list) throws Exception {
                System.out.println(parentPath + "的子节点列表发生了变化,变化后的子节点列表为"+ list);

            }
        });

        //测试  创建此节点，也会触发回调的动作
        zkClient.createPersistent("/wy-persistent-get");
        Thread.sleep(1000);

        zkClient.createPersistent("/wy-persistent-get/c1");
        Thread.sleep(1000);

    }
}
