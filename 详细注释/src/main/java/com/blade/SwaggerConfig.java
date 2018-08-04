package com.blade;

import com.blade.controller.DocumentationController;
import com.blade.swagger.ControllerDocumentation;
import com.blade.swagger.utils.MvcApiResource;
import com.wordnik.swagger.core.Documentation;

/**
 * Swagger 配置文件  主要是根据配置信息
 *                      创建ControllerDocumentation 和 Documentation对象
 */
public class SwaggerConfig {

    public SwaggerConfig(String apiVersion, String swaggerVersion,
                                String basePath) {
        this.apiVersion = apiVersion;
        this.swaggerVersion = swaggerVersion;
        this.basePath = basePath;
    }

    private String swaggerVersion;
    private String apiVersion;
    private String basePath;

    /**
     * 新建一个ControllerDocumentation类对象
     *
     * @param resource
     * @return
     */
    public ControllerDocumentation newDocumentation(MvcApiResource resource) {
        return new ControllerDocumentation(apiVersion, swaggerVersion, basePath, resource);
    }

    /**
     * 新建一个Documentation实例对象
     * @return
     */
    public Documentation newDocumentation()
    {
        return new Documentation(apiVersion, swaggerVersion, basePath, null);
    }


}
