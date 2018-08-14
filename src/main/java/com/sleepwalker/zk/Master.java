package com.sleepwalker.zk;

import java.io.IOException;
import java.util.Random;

import org.apache.log4j.Logger;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.KeeperException.ConnectionLossException;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class Master implements Watcher {

    private static ZooKeeper    zk;

    private String              hostPort;

    static Random               random   = new Random();

    static String               serverId = Integer.toString(random.nextInt());

    private static boolean      isLeader = false;

    private static final Logger logger   = Logger.getLogger(Master.class);

    boolean checkMaster() {
        while (true) {
            try {
                Stat stat = new Stat();
                byte data[] = zk.getData("/master", false, stat);
                isLeader = new String(data).equals(serverId);
                return true;
            } catch (NoNodeException e) {
                return false;
            } catch (ConnectionLossException e) {
                // TODO: handle exception
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void createParent(String path, byte[] data) {
        zk.create(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, createParentCallback,
            data);
    }

    public void bootstrap() {
        createParent("/workers", new byte[0]);
        createParent("/assign", new byte[0]);
        createParent("/tasks", new byte[0]);
        createParent("/status", new byte[0]);
    }

    StringCallback createParentCallback = new StringCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (Code.get(rc)) {
                case CONNECTIONLOSS:
                    createParent(path, (byte[]) ctx);
                    return;
                case OK:
                    logger.info("Parent created");
                    break;
                case NODEEXISTS:
                    logger.warn("Parent already registered: " + path);
                    break;
                default: {
                    logger.error("Something went wrong: ",
                        KeeperException.create(Code.get(rc), path));
                }
            }

        }
    };

    public void runForMaster() throws InterruptedException, KeeperException {
        zk.create("/master", serverId.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
            masterCreateCallback, null);
    }

    @Override
    public void process(WatchedEvent e) {
        System.out.println(e);
    }

    StringCallback masterCreateCallback = new StringCallback() {

        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (Code.get(rc)) {
                case CONNECTIONLOSS:
                    checkMaster();
                    return;
                case OK:
                    isLeader = true;
                    break;
                default:
                    isLeader = false;
            }
            System.out.println("I'm " + (isLeader ? "" : "not ") + "the leader");
        }
    };

    public Master(String hostPort) {
        this.hostPort = hostPort;
    }

    public void startZK() throws IOException {
        zk = new ZooKeeper(hostPort, 15000, this);
    }

    public void stopZK() throws InterruptedException {
        zk.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Master master = new Master("127.0.0.1:2181");
        master.startZK();

        Thread.sleep(60000);

        master.stopZK();
    }
}
