package io.zenwave360.generator.plugins;

import io.zenwave360.generator.doc.DocumentedOption;
import io.zenwave360.generator.GeneratorPlugin;
import io.zenwave360.generator.templating.TemplateOutput;

import java.util.List;
import java.util.Map;

public class NoOpPluginGenerator implements GeneratorPlugin {

    @DocumentedOption(description = "This is the plugin option description")
    public String pluginOption;

    public static Map<String, Object> context;

    @Override
    public List<TemplateOutput> generate(Map<String, Object> contextModel) {
        context = contextModel;
        return List.of(new TemplateOutput("nop.txt", "nop"));
    }
}
