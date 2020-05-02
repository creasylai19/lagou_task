import com.lagou.utils.Constans;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class TestCurator {

    CuratorFramework client;

    @Before
    public void init(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //默认的session时间为60秒，连接超时时间为15秒
        client = CuratorFrameworkFactory.newClient(Constans.Zookeeper.INET_ADDRESS+":"+ Constans.Zookeeper.ZOOKEEPER_PORT, retryPolicy);
        client.start();

    }

    @Test
    public void testWatcher() throws Exception {
        List<String> strings = client.getChildren().usingWatcher(
                new CuratorWatcher() {
                    @Override
                    public void process(WatchedEvent event) throws Exception {
                        System.out.println("NodeChildrenChanged");
                        client.getChildren().usingWatcher(this).forPath(Constans.Zookeeper.PREFIX);
                    }
                }
        ).forPath(Constans.Zookeeper.PREFIX);
        synchronized (TestCurator.class){
            TestCurator.class.wait();
        }
//        System.out.println("waitup...");
    }

    @Test
    public void testDate(){
        Date date = new Date();
        date.setTime(1588361174463L);
        System.out.println(date);
        date.setTime(1588361174463L-60*60*1000);
        System.out.println(date);
    }

    @After
    public void destory(){
        client.close();
    }

}
