import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestSchedule {

    @Test
    public void testSc() throws IOException {
        Executors.newSingleThreadScheduledExecutor()
                .scheduleWithFixedDelay(() -> System.out.println("----"), 1, 5, TimeUnit.SECONDS);
        System.in.read();
    }

}
