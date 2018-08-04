package com.blade.swagger;

import com.blade.Blade;
import com.blade.controller.DocumentationController;
import com.blade.event.BeanProcessor;
import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Order;
import com.blade.swagger.utils.MvcApiReader;

@Order(20)
@Bean
public class DCBeanProcess implements BeanProcessor {
    @Override
    public void processor(Blade blade) {
        DocumentationController bean = blade.getBean(DocumentationController.class);
        if (bean != null ) {

            String documentationBasePath = bean.getBasePath();
            if (!bean.getBasePath().endsWith("/"))
                documentationBasePath += "/";
            documentationBasePath += DocumentationController.CONTROLLER_ENDPOINT;

            SwaggerConfig config = new SwaggerConfig(bean.getApiVersion(), bean.getSwaggerVersion(), documentationBasePath);
            MvcApiReader apiReader = new MvcApiReader(blade, config);
            bean.setApiReader(apiReader);
         }
    }
}
