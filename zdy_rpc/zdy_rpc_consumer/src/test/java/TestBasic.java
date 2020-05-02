import org.junit.Test;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TestBasic {

    @Test
    public void testJUL(){
        Logger logger = Logger.getLogger(TestBasic.class.getName());
        logger.log(Level.INFO, "Log info:{0},{1}", new Object[]{"信息", 123});
        logger.log(Level.WARNING, "Log info:{0},{1}", new Object[]{"信息", 123});
    }

    @Test
    public void zdyHandler(){
        Logger logger = Logger.getLogger(TestBasic.class.getName());
        logger.setUseParentHandlers(false);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        SimpleFormatter simpleFormatter = new SimpleFormatter();
        consoleHandler.setFormatter(simpleFormatter);
        logger.addHandler(consoleHandler);

        consoleHandler.setLevel(Level.ALL);
        logger.setLevel(Level.ALL);

        logger.log(Level.SEVERE, "Level.SEVERE");
        logger.log(Level.WARNING, "Level.WARNING");
        logger.log(Level.INFO, "Level.INFO");
        logger.log(Level.CONFIG, "Level.CONFIG");
        logger.log(Level.FINE, "Level.FINE");
        logger.log(Level.FINER, "Level.FINER");
        logger.log(Level.FINEST, "Level.FINEST");

    }

}
