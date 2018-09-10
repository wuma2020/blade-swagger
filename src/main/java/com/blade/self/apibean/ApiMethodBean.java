package com.blade.self.apibean;

import com.blade.mvc.http.HttpMethod;
import lombok.Data;

@Data
public class ApiMethodBean {
    ApiOperationBean apiOperation;
    ApiResponseBean apiResponse;
    String[] value;
    String description;
    HttpMethod httpMethod;
}
