package com.blade.swagger.utils;

import com.blade.swagger.SwaggerConfig;
import com.blade.controller.DocumentationController;
import com.blade.mvc.annotation.Path;
import com.blade.swagger.ControllerDocumentation;
import com.wordnik.swagger.core.Api;
import com.wordnik.swagger.core.DocumentationEndPoint;
import lombok.Getter;

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

    public boolean isInternalResource() {
        return controllerClass == DocumentationController.class;
    }

    public DocumentationEndPoint describeAsEndpoint() {
        DocumentationEndPoint endPoint = new DocumentationEndPoint(getControllerUri(),getApiDescription());
        return endPoint;
    }

    public String getApiDescription() {
        Api apiAnnotation = controllerClass.getAnnotation(Api.class);
        if (apiAnnotation == null)
            return null;
        return apiAnnotation.description();
    }

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

    public ControllerDocumentation createEmptyApiDocumentation() {
        String resourcePath = getControllerUri();
        if (resourcePath == null)
            return null;
        return config.newDocumentation(this);
    }
}
