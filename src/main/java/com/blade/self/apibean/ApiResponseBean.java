package com.blade.self.apibean;


import lombok.Data;

@Data
public class ApiResponseBean {
    String code;
    String message;
    String response;
    String responseContainer;
}
