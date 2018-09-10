package com.blade.self.resourceUtils;

import com.blade.mvc.annotation.Path;
import com.blade.self.apiannotation.Api;
import com.blade.self.apibean.ApiBean;
import com.blade.self.apibean.ApiMethodBean;
import com.blade.self.apibean.ApiPathBean;
import com.blade.self.documentation.Documentation;

import java.util.List;

public class ApiControllerResolve {

    Documentation documentation = new Documentation();

    public ApiControllerResolve(Object bean) {
        process(bean);
    }

    private void process(Object bean) {
        Class<?> beanClass = bean.getClass();
        Path path = beanClass.getAnnotation(Path.class);
        if(path != null){
            ApiPathBean apiPathBean  = new ApiPathBean();
            apiPathBean.setValue(path.value());
            apiPathBean.setDescription(path.description());
            apiPathBean.setRestful(path.restful());
            apiPathBean.setSuffix(path.suffix());
            ApiMethodResolve apiMethodResolve = new ApiMethodResolve(beanClass);
            List<ApiMethodBean> apiMethodBeans  = apiMethodResolve.getApiMethods();
            apiPathBean.setApiMethodBeans(apiMethodBeans);
            documentation.setApiPathBean(apiPathBean);
        }

        Api api = beanClass.getAnnotation(Api.class);
        if(api != null){
            ApiBean apiBean = new ApiBean();
            apiBean.setTags(api.tags());
            apiBean.setValue(api.value());
            apiBean.setDescription(api.description());
            documentation.setApiBean(apiBean);
        }

    }

    public Documentation getDocumentation() {
        return documentation;
    }
}
