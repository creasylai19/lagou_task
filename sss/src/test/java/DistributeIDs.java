import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class DistributeIDs {

    @Test
    public void getIDByUUID(){
        String ret = UUID.randomUUID().toString();
        System.out.println(ret);
    }

    @Test
    public void getIDByRedisServer() throws ExecutionException, InterruptedException {
        FutureTask<String> task = new FutureTask<>(()->"hahaha");
        new Thread(task).start();
        System.out.println(task.get());
    }

    @Test
    @Transactional
    public void getIDByMysql(){

    }

    @Test
    public void getIDBySnowflake(){
        //0            00000000000000000000000000000000000000000      00000         00000        000000000000
        //标志位(固定0) 当前时间(微秒，41位)                              数据中心(5位)  机器ID(5位)   序列(12位)
        int idc = 1;
        int mechine = 1;
        int sequence = 1;
        long id = sequence
                | mechine << (12)
                | idc << (12+5)
                | System.currentTimeMillis()<<(12+5+5);
        System.out.println(Long.toBinaryString(id));
        System.out.println(id);
    }

}
