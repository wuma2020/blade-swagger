package com.blade.utils;

import com.blade.App;
import com.blade.Blade;
import com.blade.mvc.annotation.Path;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BladeUtils {

    @Getter
    @Setter
    public static Blade blade;

    public static Map<String,Object> getAllControllers(Blade blade){
        Map<String,Object> map = new HashMap<>();
        List<Object> beans = blade.ioc().getBeans();
        for(Object bean : beans){
            Path annotation = bean.getClass().getAnnotation(Path.class);
            if(annotation != null){
                map.put(bean.getClass().getSimpleName(),bean);
            }
        }
        return map;
    }

}
