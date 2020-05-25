package com.creasy;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateTest {

    @Test
    public void testDate(){
        System.out.println(new Date());
    }

    @Test
    public void dateAfterTest(){
        Date date = new Date();
        Date date1 = new Date(date.getTime() + TimeUnit.MINUTES.toMillis(10));
        System.out.println("date" + date);
        System.out.println("date1" + date1);
        System.out.println(date.after(date1));
    }

}
