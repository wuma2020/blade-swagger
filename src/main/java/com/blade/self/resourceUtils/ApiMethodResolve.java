package com.blade.self.resourceUtils;


import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.PostRoute;
import com.blade.mvc.annotation.Route;
import com.blade.mvc.http.HttpMethod;
import com.blade.self.apiannotation.ApiOperation;
import com.blade.self.apiannotation.ApiResponse;
import com.blade.self.apibean.ApiMethodBean;
import com.blade.self.apibean.ApiOperationBean;
import com.blade.self.apibean.ApiResponseBean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析 controller 中的方法的 注解信息
 */
public class ApiMethodResolve {

    List<ApiMethodBean> apiMethodBeans = new ArrayList<>();


    public ApiMethodResolve(Class<?> beanClass) {
        process(beanClass);
    }

    private void process(Class<?> beanClass) {

        Method[] methods = beanClass.getMethods();
        for (int i=0; i<methods.length -9 ;i++ ){
            ApiMethodBean apiMethodBean = new ApiMethodBean();
            Route route = methods[i].getAnnotation(Route.class);
            GetRoute getRoute = methods[i].getAnnotation(GetRoute.class);
            PostRoute postRoute = methods[i].getAnnotation(PostRoute.class);
            if(route != null){
                apiMethodBean.setValue(route.value());
                apiMethodBean.setDescription(route.description());
                apiMethodBean.setHttpMethod(route.method());
            }
            if(getRoute != null){
                apiMethodBean.setValue(getRoute.value());
                apiMethodBean.setDescription(getRoute.description());
                apiMethodBean.setHttpMethod(HttpMethod.GET);
            }
            if(postRoute != null){
                apiMethodBean.setValue(postRoute.value());
                apiMethodBean.setDescription(postRoute.description());
                apiMethodBean.setHttpMethod(HttpMethod.POST);
            }

            Method method1 = methods[i];
            ApiOperation apiOperation = methods[i].getAnnotation(ApiOperation.class);
            if(apiOperation != null ){
                ApiOperationBean apiOperationBean = new ApiOperationBean();
                apiOperationBean.setCode(apiOperation.code());
                apiOperationBean.setHttpMethod(apiOperation.httpMethod());
                apiOperationBean.setNote(apiOperation.note());
                apiOperationBean.setValue(apiOperation.value());
                apiMethodBean.setApiOperation(apiOperationBean);
            }

            ApiResponse apiResponse = methods[i].getAnnotation(ApiResponse.class);
            if(apiResponse != null){
                ApiResponseBean apiResponseBean  = new ApiResponseBean() ;
                apiResponseBean.setCode(apiResponse.code());
                apiResponseBean.setMessage(apiResponse.message());
                apiResponseBean.setResponse(apiResponse.response());
                apiResponseBean.setResponseContainer(apiResponse.responseContainer());
                apiMethodBean.setApiResponse(apiResponseBean);
            }

            apiMethodBeans.add(apiMethodBean);
        }
    }

    public List<ApiMethodBean> getApiMethods() {
        return apiMethodBeans;
    }
}
