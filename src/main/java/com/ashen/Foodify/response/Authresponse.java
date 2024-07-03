package com.ashen.Foodify.response;

import com.ashen.Foodify.model.USER_ROLE;
import lombok.Data;

@Data
public class Authresponse {
    private String jwt;
    private String message;
    private USER_ROLE role;
}
