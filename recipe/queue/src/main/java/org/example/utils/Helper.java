package org.example.utils;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

public class Helper {

    /**
     * Create a node in ZooKeeper if it doesn't exist.
     *
     * @param zooKeeper the ZooKeeper instance to use
     * @param nodePath  the path of the node to create
     */
    public static void createNodeIfDoesNotExists(ZooKeeper zooKeeper, String nodePath) {
        try {
            if (zooKeeper.exists(nodePath, false) == null) {

                zooKeeper.create(nodePath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
        }
    }
}
