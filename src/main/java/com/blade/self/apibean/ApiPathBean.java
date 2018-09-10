package com.blade.self.apibean;

import lombok.Data;

import java.util.List;

@Data
public class ApiPathBean {
    String value;
    String suffix;
    boolean restful;
    String description;
    List<ApiMethodBean> apiMethodBeans;
}
