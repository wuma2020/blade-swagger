package com.blade.swagger.utils;

import com.blade.Blade;
import com.blade.SwaggerConfig;
import com.blade.mvc.route.Route;
import com.blade.swagger.ControllerDocumentation;
import com.blade.utils.BladeUtils;
import com.wordnik.swagger.core.Documentation;
import com.wordnik.swagger.core.DocumentationEndPoint;
import com.wordnik.swagger.core.DocumentationOperation;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 缓存api信息的类
 */
public class MvcApiReader {

    //上下文
    private final Blade blade;
    //swagger 配置对象
    private final SwaggerConfig config;
    //存放所有controller的集合,这里的Object 就是controller 的实例对象
    private Map<String, Object> routeBeansMap;
    //swagger 中存放api信息的类 , 这个主要是要多次使用，在方法中new的话需要多次传递，比较麻烦
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

    /**
     * 把所有的controller对象，解析成swagger 中的 Document 中，并相应的放到成员属性resourceListCache，apiCache中进行缓存
     */
    private void buildRouteDocuments() {
        documentation = config.newDocumentation();
        for (Object controller : routeBeansMap.values())
        {
            //加工每一个controller
            processRoute(controller);
        }
    }

    /**
     * 加工一个controller的实现方法
     * @param controller
     */
    private void processRoute(Object controller) {
        /**
         * 对类进行解析
         */
        Class routeClass = controller.getClass();
        //在这里解析类的所有注解信息，并设置一些get 方法用于获取
        MvcApiResource apiResource = new MvcApiResource(controller,config);

        //如果该controller是自定义的DocumentationController 则，不解析,该方法到此处执行结束
        if(apiResource.isInternalResource()){
            return;
        }

        addApiListingIfMissing(apiResource);

        //向apiCache 中添加相应的controller class 以及 ControllerDocumentation对象
        ControllerDocumentation apiDocumentation = getApiDocumentation(apiResource);

        String requestUri = apiResource.getControllerUri();
        //向apiDocumentation 的 成员 map中添加 Map<String, com.wordnik.swagger.core.DocumentationEndPoint>信息
        DocumentationEndPoint endPoint = apiDocumentation.getEndPoint(requestUri);
        //解析 方法上的注解信息
        appendOperationsToEndpoint(controller,endPoint);

    }

    private void appendOperationsToEndpoint(Object controller, DocumentationEndPoint endPoint) {
        //遍历该route的所有方法，进行揭西   八个object 方法不解析
        Method[] methods = controller.getClass().getMethods();
        for(int i=0;i< methods.length -9 ;i++){
            MvcApiMethodReader methodDoc = new MvcApiMethodReader(methods[i]);
            //根据 RequestMethod 来获取一个 DocumentationOperation 对象
            DocumentationOperation operation = methodDoc.getOperation(methods[i]);
            //向DocumentationEndPoint 中添加 DocumentationOperation
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

    /**
     * 生成 resourceListCache   Map<Class<?>, DocumentationEndPoint> 中添加DocumentationEndPoint信息
     *    并向 documentation 中添加 DocumentationEndPoint信息
     * @param apiResource
     */
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
