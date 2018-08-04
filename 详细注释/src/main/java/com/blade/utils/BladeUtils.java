package com.blade.utils;

import com.blade.App;
import com.blade.Blade;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.route.Route;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取Blade 类对象
 */
public class BladeUtils {

    @Getter
    @Setter
    public static Blade blade;

    /**
     * 从balde中获取所有的controller对象
     *  // FIXME: 2018/8/1 0001   这个方法还没写完
     *  用  instanceof 来判断
     * @param blade
     * @return
     */
    public static Map<String,Object> getAllControllers(Blade blade){
        Map<String,Object> map = new HashMap<>();
        List<Object> beans = blade.ioc().getBeans();
        for(Object bean : beans){
            //判断bean是controller   利用类实例上面是否有Path注解来判断
            Path annotation = bean.getClass().getAnnotation(Path.class);
            if(annotation != null){
                map.put(bean.getClass().getSimpleName(),bean);
            }
        }
        return map;
    }

}
