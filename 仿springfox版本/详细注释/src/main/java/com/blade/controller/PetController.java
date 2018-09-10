package com.blade.controller;

import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.http.Response;
import com.wordnik.swagger.core.Api;
import com.wordnik.swagger.core.ApiOperation;
import com.wordnik.swagger.core.ApiParam;

@Path("/Pet")
@Api(value = "percontoller" ,description = "小明 pet")
public class PetController {

    @GetRoute("/getPet")
    @ApiOperation(value = "apiOperation")
    private void getPet(Response response){

        response.text("woshi  PetController 的 getPet");
    }
}
