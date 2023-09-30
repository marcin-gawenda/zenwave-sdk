package io.zenwave360.sdk.processors;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.zenwave360.sdk.parsers.DefaultYamlParser;
import io.zenwave360.sdk.parsers.ZDLParser;
import io.zenwave360.sdk.utils.JSONPath;

public class JDLWithOpenApiProcessorTest {

    private Map<String, Object> loadOpenApi(File file) throws Exception {
        return loadOpenApi(file.getAbsoluteFile().toURI().toString());
    }
    private Map<String, Object> loadZDL(File file) throws Exception {
        return loadZDL(file.getAbsolutePath());
    }
    private Map<String, Object> loadOpenApi(String resource) throws Exception {
        Map<String, Object> model = new DefaultYamlParser().withSpecFile(URI.create(resource)).parse();
        return new OpenApiProcessor().process(model);
    }

    private Map<String, Object> loadZDL(String resource) throws IOException {
        Map<String, Object> model = new ZDLParser().withSpecFile(resource).withTargetProperty("zdl").parse();
        return new JDLProcessor().process(model);
    }

    @Test
    public void testProcessJDLWithOpenAPI() throws Exception {
        var openapiModel = loadOpenApi("classpath:io/zenwave360/sdk/resources/openapi/openapi-petstore.yml");
        var zdlModel = loadZDL("classpath:io/zenwave360/sdk/resources/jdl/petstore.jdl");
        var model = new HashMap<String, Object>();
        model.putAll(openapiModel);
        model.putAll(zdlModel);

        var processed = new EnrichOpenAPIWithJDLProcessor().process(model);
        List requestEntities = JSONPath.get(processed, "$..[?(@.x--request-entity)]");
        Assertions.assertFalse(requestEntities.isEmpty());
        List responseEntities = JSONPath.get(processed, "$..[?(@.x--response-entity)]");
        Assertions.assertFalse(responseEntities.isEmpty());
    }

    @Test
    // @Disabled
    public void testProcessJDLWithOpenAPI_registry() throws Exception {
        var openapiModel = loadOpenApi("classpath:io/zenwave360/sdk/resources/openapi/openapi-orders.yml");
        var zdlModel = loadZDL("classpath:io/zenwave360/sdk/resources/jdl/orders-model.jdl");
        var model = new HashMap<String, Object>();
        model.putAll(openapiModel);
        model.putAll(zdlModel);

        var processed = new EnrichOpenAPIWithJDLProcessor().process(model);
        List requestEntities = JSONPath.get(processed, "$..[?(@.x--request-entity)]");
        Assertions.assertFalse(requestEntities.isEmpty());
        List responseEntities = JSONPath.get(processed, "$..[?(@.x--response-entity)]");
        Assertions.assertFalse(responseEntities.isEmpty());
    }
}
