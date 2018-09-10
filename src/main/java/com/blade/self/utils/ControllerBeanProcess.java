package com.blade.self.utils;

import com.blade.Blade;
import com.blade.event.BeanProcessor;
import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Order;
import com.blade.self.apicontroller.CustomController;
import com.blade.self.resourceUtils.ApiReader;

/**
 * 向api 的controller中注入 apiReader 对象
 */
@Order(100)
@Bean
public class ControllerBeanProcess  implements BeanProcessor {

    @Override
    public void processor(Blade blade) {
        ApiReader apiReader = new ApiReader(blade);
        CustomController bean = blade.ioc().getBean(CustomController.class);
        bean.setApiReader(apiReader);
    }
}
