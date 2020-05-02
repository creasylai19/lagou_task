package com.creasy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MySQLConfig {

    private String url;
    private String username;
    private String password;
    private Integer maxActive;

}
