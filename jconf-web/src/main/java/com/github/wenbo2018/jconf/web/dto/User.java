package com.github.wenbo2018.jconf.web.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Created by shenwenbo on 2017/4/16.
 */
@Data
public class User {
    private int id;
    private int userId;
    private String userName;
    private String passWord;
    private String userEmail;
    private String token;
    private Date addTime;
}
