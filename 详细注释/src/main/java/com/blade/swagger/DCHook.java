package com.blade.swagger;

import com.blade.Blade;
import com.blade.SwaggerConfig;
import com.blade.controller.DocumentationController;
import com.blade.ioc.annotation.Bean;
import com.blade.mvc.RouteContext;
import com.blade.mvc.hook.WebHook;
import com.blade.mvc.route.Route;
import com.blade.swagger.utils.MvcApiReader;
import com.blade.utils.BladeUtils;

/**
 * DCHook 为 DocumentationController 类的成员变量 blade 和 apiReader 进行赋值
 */
public class DCHook  implements WebHook {
    @Override
    public boolean before(RouteContext routeContext) {
        return false;
    }

    @Override
    public boolean after(RouteContext context) {
        System.out.println(  ((Route) context.routeTarget()).getAllPath()   );
        Class<?> controllerClass = ((Route) context.routeTarget()).getTargetType();
        if (controllerClass != null && controllerClass == DocumentationController.class) {

            DocumentationController dc = null;
            try {
                dc = (DocumentationController) controllerClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            String documentationBasePath = dc.getBasePath();
            if (!dc.getBasePath().endsWith("/"))
                documentationBasePath += "/";
            documentationBasePath += DocumentationController.CONTROLLER_ENDPOINT;

            SwaggerConfig config = new SwaggerConfig(dc.getApiVersion(), dc.getSwaggerVersion(), documentationBasePath);
            Blade blade = BladeUtils.getBlade();
            MvcApiReader apiReader = new MvcApiReader(blade, config);
//            dc.setApiReader(apiReader);
        }
        return true;
    }
}
