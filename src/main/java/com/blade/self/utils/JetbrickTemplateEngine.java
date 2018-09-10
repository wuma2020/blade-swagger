package com.blade.self.utils;



import com.blade.exception.TemplateException;
import com.blade.mvc.WebContext;
import com.blade.mvc.http.Request;
import com.blade.mvc.http.Session;
import com.blade.mvc.ui.ModelAndView;
import com.blade.mvc.ui.template.TemplateEngine;
import jetbrick.template.*;
import jetbrick.template.resolver.GlobalResolver;
import lombok.Getter;
import lombok.Setter;

import java.io.Writer;
import java.util.Map;
import java.util.Properties;


public class JetbrickTemplateEngine implements TemplateEngine {

    @Getter
    @Setter
    private JetEngine jetEngine;
    @Getter
    private Properties config = new Properties();
    @Getter
    @Setter
    private String     suffix = ".html";

    public JetbrickTemplateEngine() {
        config.put(JetConfig.TEMPLATE_SUFFIX, suffix);

        Class<?> bootClass = WebContext.blade().bootClass();
        if (null != bootClass) {
            config.put(JetConfig.AUTOSCAN_PACKAGES, bootClass.getPackage().getName());
        }

        String classpathLoader = "jetbrick.template.loader.ClasspathResourceLoader";
        config.put(JetConfig.TEMPLATE_LOADERS, "$classpathLoader");
        config.put("$classpathLoader", classpathLoader);
        config.put("$classpathLoader.root", "/templates/");
        config.put("$classpathLoader.reloadable", "true");
    }

    public JetbrickTemplateEngine(Properties config) {
        this.config = config;
        jetEngine = JetEngine.create(config);
    }

    public JetbrickTemplateEngine(String conf) {
        jetEngine = JetEngine.create(conf);
    }

    public JetbrickTemplateEngine(JetEngine jetEngine) {
        if (null == jetEngine) {
            throw new IllegalArgumentException("jetEngine must not be null");
        }
        this.jetEngine = jetEngine;
    }

    @Override
    public void render(ModelAndView modelAndView, Writer writer) {
        if (null == jetEngine) {
            this.jetEngine = JetEngine.create(config);
        }
        Map<String, Object> modelMap = modelAndView.getModel();

        Request request = WebContext.request();
        Session session = request.session();

        modelMap.putAll(request.attributes());

        if (null != session) {
            modelMap.putAll(session.attributes());
        }

        JetContext context = new JetContext(modelMap.size());
        context.putAll(modelMap);

        String templateName = modelAndView.getView().endsWith(suffix) ? modelAndView.getView() : modelAndView.getView() + suffix;
        try {
            JetTemplate template = jetEngine.getTemplate(templateName);
            template.render(context, writer);
        } catch (Exception e) {
            throw new TemplateException(e);
        }
    }

    public JetGlobalContext getGlobalContext() {
        if (null == jetEngine) {
            this.jetEngine = JetEngine.create(config);
        }
        return jetEngine.getGlobalContext();
    }

    public GlobalResolver getGlobalResolver() {
        if (null == jetEngine) {
            this.jetEngine = JetEngine.create(config);
        }
        return jetEngine.getGlobalResolver();
    }

    public TemplateEngine addConfig(String key, String value) {
        config.put(key, value);
        return this;
    }

}