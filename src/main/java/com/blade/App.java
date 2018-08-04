package com.blade;

import com.blade.utils.BladeUtils;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Blade blade = Blade.of();
        blade.get("/",ctx -> ctx.text("Blade--swagger")).start(App.class, args);
    }
}
