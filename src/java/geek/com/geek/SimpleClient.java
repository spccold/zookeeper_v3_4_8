package com.geek;

import java.nio.charset.Charset;
import java.util.concurrent.locks.LockSupport;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * just for learn
 * 
 * @author jileng
 * @version $Id: SimpleClient.java, v 0.1 2016年7月16日 下午1:37:17 jileng Exp $
 */
public class SimpleClient {
    public static void main(String[] args) throws Exception {
        String path = "/metabase";
        String value = "config";
        ZooKeeper zk = new ZooKeeper("127.0.0.1:2181", 2000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println(event);
            }
        });
        while (!zk.getState().isConnected()) {
            LockSupport.parkNanos(1000 * 1000 * 10);
        }
        Stat stat = zk.exists(path, false);
        if (null == stat) {
            zk.create(path, value.getBytes(Charset.forName("UTF-8")), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }else{
            Stat s = new Stat();
            System.out.println("data from " + path + ": " + zk.getData(path, false, s));
            System.out.println("data version: "+ s.getVersion());
        }
    }
}
