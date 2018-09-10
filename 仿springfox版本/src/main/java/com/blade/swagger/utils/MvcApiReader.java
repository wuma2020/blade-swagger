package com.blade.swagger.utils;

import com.blade.Blade;
import com.blade.swagger.SwaggerConfig;
import com.blade.swagger.ControllerDocumentation;
import com.blade.utils.BladeUtils;
import com.wordnik.swagger.core.Documentation;
import com.wordnik.swagger.core.DocumentationEndPoint;
import com.wordnik.swagger.core.DocumentationOperation;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MvcApiReader {

    private final Blade blade;
    private final SwaggerConfig config;
    private Map<String, Object> routeBeansMap;
    @Getter
    private Documentation documentation;
    @Getter
    private final Map<Class<?>,DocumentationEndPoint> resourceListCache = new HashMap<>();
    @Getter
    private final Map<Class<?>,ControllerDocumentation> apiCache = new HashMap<>();

    public MvcApiReader( Blade blade ,SwaggerConfig config){
        this.blade = blade;
        this.config = config;
        routeBeansMap = BladeUtils.getAllControllers(blade);
        buildRouteDocuments();
    }

    private void buildRouteDocuments() {
        documentation = config.newDocumentation();
        for (Object controller : routeBeansMap.values())
        {
            processRoute(controller);
        }
    }

    private void processRoute(Object controller) {
        Class routeClass = controller.getClass();
        MvcApiResource apiResource = new MvcApiResource(controller,config);
        if(apiResource.isInternalResource()){
            return;
        }
        addApiListingIfMissing(apiResource);
        ControllerDocumentation apiDocumentation = getApiDocumentation(apiResource);
        String requestUri = apiResource.getControllerUri();
        DocumentationEndPoint endPoint = apiDocumentation.getEndPoint(requestUri);
        appendOperationsToEndpoint(controller,endPoint);
    }

    private void appendOperationsToEndpoint(Object controller, DocumentationEndPoint endPoint) {
        Method[] methods = controller.getClass().getMethods();
        for(int i=0;i< methods.length -9 ;i++){
            MvcApiMethodReader methodDoc = new MvcApiMethodReader(methods[i]);
            DocumentationOperation operation = methodDoc.getOperation(methods[i]);
            endPoint.addOperation(operation);
        }
    }

    private ControllerDocumentation getApiDocumentation(MvcApiResource apiResource) {
        //判断apiCache map 中是否有该 controller的class对象
        if (!apiCache.containsKey(apiResource.getControllerClass()))
        { //如果没有，向apiCache中put该controller class 以及对应的 ControllerDocumentation 对象

            ControllerDocumentation emptyApiDocumentation = apiResource.createEmptyApiDocumentation();
            if (emptyApiDocumentation != null)
                apiCache.put(apiResource.getControllerClass(),emptyApiDocumentation);
        }
        //然后返回该 apiCache中的该controller 对应的 ControllerDocumentation 对象
        return apiCache.get(apiResource.getControllerClass());
    }

    private void addApiListingIfMissing(MvcApiResource apiResource) {
        //查看缓存map中是否有该controller的class对象
        if (resourceListCache.containsKey(apiResource.getControllerClass()))
            return;
        //如果缓存map没有该controller， 新建一个DocumentationEndPoint 对象
        DocumentationEndPoint endpoint = apiResource.describeAsEndpoint();
        if (endpoint != null)
        {   //向缓存map中添加 相应的 controller class对象 和 DocumentationEndPoint对象
            resourceListCache.put(apiResource.getControllerClass(),endpoint);
            //向 resourceListing （Documentation）中添加DocumentationEndPoint对象
            documentation.addApi(endpoint);
        }
    }

    public ControllerDocumentation getDocumentation(String apiName) {
        for (Class<?> className : apiCache.keySet())
        {
            if (className.getSimpleName().equals(apiName))
                return apiCache.get(className);
        }
        return null;
    }
}
