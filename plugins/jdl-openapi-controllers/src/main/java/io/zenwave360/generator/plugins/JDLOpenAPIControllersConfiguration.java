package io.zenwave360.generator.plugins;

import io.zenwave360.generator.Configuration;
import io.zenwave360.generator.formatters.JavaFormatter;
import io.zenwave360.generator.parsers.DefaultYamlParser;
import io.zenwave360.generator.parsers.JDLParser;
import io.zenwave360.generator.processors.JDLProcessor;
import io.zenwave360.generator.processors.JDLWithOpenApiProcessor;
import io.zenwave360.generator.processors.OpenApiProcessor;
import io.zenwave360.generator.writers.TemplateFileWriter;
import io.zenwave360.generator.writers.TemplateStdoutWriter;
import org.apache.commons.lang3.StringUtils;

public class JDLOpenAPIControllersConfiguration extends Configuration {

    public static final String CONFIG_ID = "jdl-openapi-controllers";

    public JDLOpenAPIControllersConfiguration() {
        super();
        withChain(DefaultYamlParser.class, OpenApiProcessor.class, JDLParser.class, JDLProcessor.class, JDLWithOpenApiProcessor.class, JDLOpenAPIControllersGenerator.class, JavaFormatter.class, TemplateFileWriter.class);
    }

    @Override
    public <T extends Configuration> T processOptions() {
        if(!getOptions().containsKey("targetFolder")) {
            withChain(DefaultYamlParser.class, OpenApiProcessor.class, JDLParser.class, JDLProcessor.class, JDLWithOpenApiProcessor.class, JDLOpenAPIControllersGenerator.class, JavaFormatter.class, TemplateStdoutWriter.class);
        }
        // because we have more than one model, we need to configure how they are passed around from parser to processor and generator
        // we use class name for passing the properties, in case one class is repeated in chain we'd use the index number in the chain
        withOption("DefaultYamlParser.specFile", StringUtils.firstNonBlank(this.getSpecFile(), (String) getOptions().get("openapiFile")));
        withOption("DefaultYamlParser.targetProperty", "openapi");
        withOption("OpenApiProcessor.targetProperty", "openapi");
        withOption("JDLParser.specFile", getOptions().get("jdlFile"));
        withOption("JDLParser.targetProperty", "jdl");
        withOption("JDLProcessor.targetProperty", "jdl");
        withOption("JDLWithOpenApiProcessor.openapiProperty", "openapi");
        withOption("JDLWithOpenApiProcessor.jdlProperty", "jdl");
        withOption("JDLOpenAPIControllersGenerator.openapiProperty", "openapi");
        withOption("JDLOpenAPIControllersGenerator.jdlProperty", "jdl");
        return (T) this;
    }
}
