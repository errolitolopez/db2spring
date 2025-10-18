package com.errol.db2spring.writer;

import com.errol.db2spring.exception.Db2springException;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.StringWriter;
import java.util.Map;

public class FreeMarkerWriter {

    private static final String TEMPLATE_DIR = "/templates";

    private final Configuration freemarkerConfig;

    public FreeMarkerWriter() {
        this.freemarkerConfig = new Configuration(Configuration.VERSION_2_3_32);

        // Use classloader to load templates from classpath (works across modules)
        this.freemarkerConfig.setClassLoaderForTemplateLoading(
                Thread.currentThread().getContextClassLoader(), TEMPLATE_DIR
        );

        this.freemarkerConfig.setDefaultEncoding("UTF-8");
        this.freemarkerConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        this.freemarkerConfig.setLogTemplateExceptions(false);
        this.freemarkerConfig.setWrapUncheckedExceptions(true);

        DefaultObjectWrapperBuilder owb = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_32);
        owb.setExposeFields(true);
        this.freemarkerConfig.setObjectWrapper(owb.build());
    }

    public String writeContent(Map<String, Object> data, String templateName) {
        try {

            Template template = freemarkerConfig.getTemplate(templateName + ".ftl");
            StringWriter writer = new StringWriter();
            template.process(data, writer);
            return writer.toString();

        } catch (Exception e) {
            throw new Db2springException("Failed to process template '" + templateName + "': " + e.getMessage());
        }
    }
}
