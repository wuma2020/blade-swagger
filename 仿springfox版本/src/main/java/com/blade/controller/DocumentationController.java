package com.blade.controller;

import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.annotation.PathParam;
import com.blade.mvc.http.Response;
import com.blade.swagger.ControllerDocumentation;
import com.blade.swagger.utils.MvcApiReader;
import com.wordnik.swagger.core.Documentation;
import lombok.Getter;
import lombok.Setter;

@Path(value = "/apidoc")
public class DocumentationController {

    public static final String CONTROLLER_ENDPOINT = "apidoc";

    @Getter
    @Setter
    private String apiVersion = "1.0";
    @Getter
    @Setter
    private String swaggerVersion = "1.0";

    @Getter
    @Setter
    private String basePath = "/";
    @Getter
    @Setter
    private MvcApiReader apiReader;

    @GetRoute("/")
    public void getResourceListing(Response response) {
        Documentation cc = apiReader.getDocumentation();
        String jsoncc = com.alibaba.fastjson.JSON.toJSONString(cc);
        response.json(jsoncc);
    }

    @GetRoute(value = "/:apiName")
    public void getApiDocumentation(@PathParam String apiName, Response response) {
        ControllerDocumentation cc = apiReader.getDocumentation(apiName);
        String jsoncc = com.alibaba.fastjson.JSON.toJSONString(cc);
        response.json(jsoncc);
    }

}
