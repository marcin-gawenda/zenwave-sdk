package io.zenwave360.generator.plugins;

import io.zenwave360.generator.Configuration;
import io.zenwave360.generator.formatters.JavaFormatter;
import io.zenwave360.generator.parsers.DefaultYamlParser;
import io.zenwave360.generator.processors.OpenApiProcessor;
import io.zenwave360.generator.writers.TemplateFileWriter;
import io.zenwave360.generator.writers.TemplateStdoutWriter;

import java.util.Map;

public class SpringWebTestClientConfiguration extends Configuration {

    public static final String CONFIG_ID = "spring-webtestclient";

    public SpringWebTestClientConfiguration() {
        super();
        withChain(DefaultYamlParser.class, OpenApiProcessor.class, SpringWebTestClientGenerator.class, JavaFormatter.class, TemplateFileWriter.class);
    }

    @Override
    public <T extends Configuration> T processOptions() {
        System.out.println("options: " + getOptions());
        if(!getOptions().containsKey("targetFolder")) {
            replaceInChain(TemplateFileWriter.class, TemplateStdoutWriter.class);
            withOption(SpringWebTestClientGenerator.class.getName() + ".groupBy", SpringWebTestClientGenerator.GroupByType.PARTIAL.toString());
        }
        return (T) this;
    }
}
