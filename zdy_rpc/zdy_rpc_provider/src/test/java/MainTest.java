import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

import java.util.Date;

public class MainTest {

    @Test
    public void testCurator() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);
        client.start();
        client.usingNamespace("SERVERS");
        client.create().creatingParentsIfNeeded().forPath("/my/path", "111".getBytes());
        client.close();
    }

    @Test
    public void testDate(){
        Date date = new Date();
//        System.out.println(date);
//        long time = date.getTime();
//        time = time - 5*1000;
//        date.setTime(time);
//        System.out.println(date);

        Date currentDate = new Date();
        currentDate.setTime(currentDate.getTime()-5*1000);
        System.out.println(date);
        System.out.println(currentDate);
        if( date.before(currentDate) ){
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }

}
