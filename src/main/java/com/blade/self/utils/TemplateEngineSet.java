package com.blade.self.utils;

import com.blade.Blade;
import com.blade.ioc.annotation.Bean;
import com.blade.loader.BladeLoader;

@Bean
public class TemplateEngineSet implements BladeLoader {

    @Override
    public void load(Blade blade) {
        blade.templateEngine(new JetbrickTemplateEngine());
    }
}
