package com.sleepwalker.zk;

import java.io.IOException;
import java.util.Random;

import org.apache.log4j.Logger;
import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class Worker implements Watcher {

    private static final Logger logger   = Logger.getLogger(Worker.class);

    ZooKeeper                   zk;

    String                      hostPort;

    Random                      random   = new Random();

    String                      serverId = Integer.toHexString(random.nextInt());

    String                      status;

    String                      name;

    public Worker(String hostPort) {
        this.hostPort = hostPort;
    }

    void startZK() throws IOException {
        zk = new ZooKeeper(hostPort, 15000, this);
    }

    @Override
    public void process(WatchedEvent e) {
        logger.info(e.toString() + ", " + hostPort);
    }

    StatCallback statusUpdateCallback = new StatCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            switch (Code.get(rc)) {
                case CONNECTIONLOSS:
                    updateStatus((String) ctx);
                    return;
            }
        }
    };

    synchronized private void updateStatus(String status) {
        if (status == this.status) {
            zk.setData("/workers/" + name, status.getBytes(), -1, statusUpdateCallback, status);
        }
    }

    public void setStatus(String status) {
        this.status = status;
        updateStatus(status);
    }

    void register() {
        zk.create("/workers/worker-" + serverId, "Idle".getBytes(), Ids.OPEN_ACL_UNSAFE,
            CreateMode.EPHEMERAL, createWorkerCallback, null);
    }

    StringCallback createWorkerCallback = new StringCallback() {

        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (Code.get(rc)) {
                case CONNECTIONLOSS:
                    register();
                    break;
                case OK:
                    logger.info("Registered successfully: " + serverId);
                    break;
                case NODEEXISTS:
                    logger.warn("Already registered: " + serverId);
                    break;
                default:
                    logger.error(
                        "Something went wrong: " + KeeperException.create(Code.get(rc), path));
            }
        }
    };

    public void main(String[] args) throws IOException, InterruptedException {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            System.out.println(random.nextInt(3));
        }
    }
}
