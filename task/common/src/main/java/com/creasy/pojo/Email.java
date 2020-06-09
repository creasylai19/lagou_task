package com.creasy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email implements Serializable {

    public static final String DEFAULT_SUBJECT = "Your Register Code!";
    public static final String DEFAULT_CONTENT = "Thanks for register. Your code is :";

    private String to;
    private String from;
    private String subject;
    private String content;

}
