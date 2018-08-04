package com.blade.swagger.utils;

import com.blade.SwaggerConfig;
import com.blade.controller.DocumentationController;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.route.Route;
import com.blade.swagger.ControllerDocumentation;
import com.wordnik.swagger.core.Api;
import com.wordnik.swagger.core.DocumentationEndPoint;
import lombok.Getter;

/**
 * 解析 controller 对象 类上注解 和 方法上的注解 的 类
 */
public class MvcApiResource {

    @Getter
    private final Object route;
    @Getter
    private final Class<?> controllerClass;
    private final SwaggerConfig config;

    public MvcApiResource(Object route, SwaggerConfig config) {
        this.route = route;
        this.config = config;
        controllerClass = route.getClass();
    }

    /**
     * 判断该 controller 是否是自定义的 DocumentationController
     * @return
     */
    public boolean isInternalResource() {
        return controllerClass == DocumentationController.class;
    }

    /**
     * 根据该类的成员信息，创建相应的 DocumentationEndPoint 对象
     * @return
     */
    public DocumentationEndPoint describeAsEndpoint() {
        //传进去 注解@RequestMapping 的 uri（String） 和 注解@api 的 description (String )
        DocumentationEndPoint endPoint = new DocumentationEndPoint(getControllerUri(),getApiDescription());
        return endPoint;
    }

    /**
     *
     * @return
     */
    public String getApiDescription() {
        Api apiAnnotation = controllerClass.getAnnotation(Api.class);
        if (apiAnnotation == null)
            return null;
        return apiAnnotation.description();
    }

    /**
     * 获取 类 上面的 @Path注解的信息
     * @return
     */
    public String getControllerUri() {
        Path pathAnnotation = controllerClass.getAnnotation(Path.class);
        if(pathAnnotation == null){
            return null;
        }
        String requestMapping = pathAnnotation.value();
        if(requestMapping == null && requestMapping.length() < 1){
            return null;
        }
        return requestMapping;
    }

    /**
     * 根绝该类锁拥有的controller 和 config 对象，创建相应的 ControllerDocumentation 对象
     * @return
     */
    public ControllerDocumentation createEmptyApiDocumentation() {
        String resourcePath = getControllerUri();
        if (resourcePath == null)
            return null;
        return config.newDocumentation(this);
    }
}
