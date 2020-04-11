import org.junit.Test;

import java.util.UUID;

public class MainTestClass {

    @Test
    public void testFinally(){
        try{
            System.out.printf("try");
            System.exit(1);
        } finally {
            System.out.printf("finally");
        }
    }

    @Test
    public void testUUID(){
        System.out.println(UUID.randomUUID());
    }

    @Test
    public void testAlg(){
        System.out.println(2*2*2*2*2);
        System.out.println(Long.toBinaryString(System.currentTimeMillis()));
        System.out.println(Long.toBinaryString(System.currentTimeMillis()).length());
        System.out.println(1<<12);
        System.out.println(((long) (1 << 12)) *(1<<10));
        System.out.println(Long.MAX_VALUE);
        System.out.println("-1 is :" + Long.toBinaryString(-1));
        System.out.println(~(-1L << 5));
        System.out.println((1<<5) - 1);
        System.out.println(Long.toBinaryString(-1));
        System.out.println(Long.toBinaryString((-1L << 5)));

    }

}
