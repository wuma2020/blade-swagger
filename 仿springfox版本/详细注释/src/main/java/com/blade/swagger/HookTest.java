package com.blade.swagger;

import com.blade.ioc.annotation.Bean;
import com.blade.mvc.RouteContext;
import com.blade.mvc.hook.WebHook;
import com.blade.mvc.route.Route;


/**
 * WebHook 实在什么时候执行的？？？？
 */
//@Bean
public class HookTest implements WebHook {
    @Override
    public boolean before(RouteContext routeContext) {
        String allPath = ((Route) routeContext.routeTarget()).getAllPath();
        System.out.println("----------------------- " + allPath);
        return true;
    }
}
