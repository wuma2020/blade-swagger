package com.blade.self.resourceUtils;

import com.blade.Blade;
import com.blade.mvc.annotation.Path;
import com.blade.self.apicontroller.CustomController;
import com.blade.self.documentation.Documentation;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析所有的 controller 的api信息，并缓存在apiCache中
 */
public class ApiReader {

    Documentation documentationTemp;
    @Getter
    Map<String,Documentation> apiCache = new HashMap<>();
    public ApiReader(Blade blade) {
        process(blade);
    }

    private void process(Blade blade) {
        List<Object> beans = blade.ioc().getBeans();
        for(Object bean : beans){
            Path annotation = bean.getClass().getAnnotation(Path.class);
            if(annotation != null){
                if( bean.getClass() == CustomController.class ){
                    continue;
                }
                ApiControllerResolve apiControllerResolve  = new ApiControllerResolve(bean);
                documentationTemp =  apiControllerResolve.getDocumentation();
                documentationTemp.setControllerName(bean.getClass().getSimpleName());
                apiCache.put(bean.getClass().getSimpleName(),documentationTemp);

            }
        }
    }

    public ArrayList getDocumentation() {
        //转换成合适的数据结构便于前端解析
        ArrayList list = new ArrayList(apiCache.values());
        return list;
    }

    public Documentation getDocumentation(String apiName) {
        return apiCache.get(apiName);
    }
}
