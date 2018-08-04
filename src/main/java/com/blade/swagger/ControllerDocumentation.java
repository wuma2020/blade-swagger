package com.blade.swagger;

import com.blade.swagger.utils.MvcApiResource;
import com.wordnik.swagger.core.Api;
import com.wordnik.swagger.core.Documentation;
import com.wordnik.swagger.core.DocumentationEndPoint;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.Map;

public class ControllerDocumentation extends Documentation {

    private final MvcApiResource resource;
    private final Map<String, DocumentationEndPoint> endpointMap = new HashMap<>();

    public ControllerDocumentation(String apiVersion, String swaggerVersion, String basePath, MvcApiResource resource) {
        super(apiVersion,swaggerVersion,basePath,resource.getControllerUri());
        this.resource = resource;
    }

    public DocumentationEndPoint getEndPoint(String requestUri) {
        if (!endpointMap.containsKey(requestUri)){
            DocumentationEndPoint endPoint = new DocumentationEndPoint(requestUri, getApiDescription());
            endpointMap.put(requestUri, endPoint);
            addApi(endPoint);
        }
        return endpointMap.get(requestUri);
    }

    private String getApiDescription() {
        Api apiAnnotation = getControllerClass().getAnnotation(Api.class);
        return (apiAnnotation != null) ? apiAnnotation.description() : "";
    }

    private AnnotatedElement getControllerClass() {
        return resource.getControllerClass();
    }

    public boolean matchesName(String apiName) {
        String nameWithForwardSlash = (apiName.startsWith("/")) ? apiName : "/" + apiName;
        String nameWithoutForwardSlash = (apiName.startsWith("/")) ? apiName.substring(1) : apiName;

        return getResourcePath().equals(nameWithoutForwardSlash) ||
                getResourcePath().equals(nameWithForwardSlash);
    }
}
