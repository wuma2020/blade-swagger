package com.blade.self.apibean;


import com.blade.mvc.http.HttpMethod;
import lombok.Data;

@Data
public class ApiRouteBean {
    String[] value;
    HttpMethod method;
    String description;
}
