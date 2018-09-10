package com.blade.self.apicontroller;

import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.annotation.PathParam;
import com.blade.mvc.http.Request;
import com.blade.mvc.http.Response;
import com.blade.self.documentation.Documentation;
import com.blade.self.resourceUtils.ApiReader;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Map;

/**
 * controller api信息查询的控制器信息
 */
@Path("/apidoc")
public class CustomController {

    @Setter
    ApiReader apiReader ;


    @GetRoute("/")
    public String getResourceListing(Response response , Request request) {
        ArrayList cc = apiReader.getDocumentation();

        String jsoncc = com.alibaba.fastjson.JSONObject.toJSONString(cc);
        request.attribute("apidoc",jsoncc);
        return "apidoc.html";
    }

    @GetRoute(value = "/:apiName")
    public void getApiDocumentation(@PathParam String apiName, Response response) {
        Documentation cc = apiReader.getDocumentation(apiName);
        String jsoncc = com.alibaba.fastjson.JSON.toJSONString(cc);
        response.json(jsoncc);
    }


}
