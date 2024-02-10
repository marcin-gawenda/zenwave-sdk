package io.zenwave360.sdk.plugins;

import io.zenwave360.sdk.Plugin;
import io.zenwave360.sdk.doc.DocumentedPlugin;
import io.zenwave360.sdk.formatters.JavaFormatter;
import io.zenwave360.sdk.parsers.ZDLParser;
import io.zenwave360.sdk.processors.ZDLProcessor;
import io.zenwave360.sdk.writers.TemplateFileWriter;

/**
 * This is the long description
 */
@DocumentedPlugin(value = "Generates a full backend application using a flexible hexagonal architecture", shortCode = "mag-backend-application", description = "")
public class MagBackendApplicationPlugin extends Plugin {

    public MagBackendApplicationPlugin() {
        super();
        withChain(ZDLParser.class, ZDLProcessor.class, BackendDefaultApplicationGenerator.class, JavaFormatter.class, TemplateFileWriter.class);
    }

    @Override
    public <T extends Plugin> T processOptions() {
        if (getOptions().containsKey("multiModule") && "true".equals(getOptions().get("multiModule").toString())) {
            replaceInChain(BackendDefaultApplicationGenerator.class, BackendMultiModuleApplicationGenerator.class);
        }
        return (T) this;
    }

}
