package com.blade.controller;


import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.JSON;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.http.Response;
import com.wordnik.swagger.core.ApiOperation;

@Path
public class TestController {

    @GetRoute("/signin")
    @JSON
    @ApiOperation(value = "value sing" ,notes = "我是猪")
    public void signin(Response response){
        System.out.println("singin");
        User user = new User();
        user.setAge("22");
        user.setName("受到收到");
        response.json(user);
    }

    @GetRoute("/signinT")
    @JSON
    @ApiOperation(value = "111111" ,notes = "1111111111")
    public void signinT(Response response){
        System.out.println("singin");
        User user = new User();
        user.setAge("22");
        user.setName("受到收到");
        response.json(user);
    }


}
