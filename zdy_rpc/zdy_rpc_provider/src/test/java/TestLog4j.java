import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLog4j {

    public static final Logger LOGGER = LoggerFactory.getLogger(TestLog4j.class);

    @Test
    public void testLog(){
        LOGGER.info("info");
        LOGGER.error("error");
        LOGGER.warn("warn");
        LOGGER.debug("debug");
        LOGGER.trace("trace");
    }

}
