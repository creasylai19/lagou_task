package com.creasy;

import org.junit.jupiter.api.Test;

import java.util.UUID;

public class UUIDTest {

    @Test
    public void testUUID(){
        String email = "abc@126.com";
        String password = "aq123456";//0bfa67e9-e4bc-3cdf-ba07-630a8a854550
        System.out.println(UUID.nameUUIDFromBytes((email + password).getBytes()));
        System.out.println(UUID.nameUUIDFromBytes((email+password).getBytes()));
        System.out.println(UUID.nameUUIDFromBytes((email+password).getBytes()));
    }

}
