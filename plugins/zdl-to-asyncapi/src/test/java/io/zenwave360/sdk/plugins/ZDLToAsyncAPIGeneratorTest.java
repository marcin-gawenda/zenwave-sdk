package io.zenwave360.sdk.plugins;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.zenwave360.sdk.parsers.ZDLParser;
import io.zenwave360.sdk.processors.ZDLProcessor;
import io.zenwave360.sdk.templating.TemplateOutput;
import io.zenwave360.sdk.utils.JSONPath;

public class ZDLToAsyncAPIGeneratorTest {

    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    private Map<String, Object> loadZDLModelFromResource(String resource) throws Exception {
        Map<String, Object> model = new ZDLParser().withSpecFile(resource).parse();
        return new ZDLProcessor().process(model);
    }

    @Test
    public void test_zdl_to_asyncapi() throws Exception {
        Map<String, Object> model = loadZDLModelFromResource("classpath:io/zenwave360/sdk/resources/zdl/customer-address.zdl");
        ZDLToAsyncAPIGenerator generator = new ZDLToAsyncAPIGenerator();
        generator.includeCommands = true;
        generator.idType = "integer";
        generator.idTypeFormat = "int64";

        List<TemplateOutput> outputTemplates = generator.generate(model);
        Assertions.assertEquals(1, outputTemplates.size());

        System.out.println(outputTemplates.get(0).getContent());

        Map<String, Object> oasSchema = mapper.readValue(outputTemplates.get(0).getContent(), Map.class);
//        Assertions.assertEquals(3, ((List) JSONPath.get(oasSchema, "$.channels.customer-orders.publish.message.oneOf")).size());

//        Assertions.assertTrue(((List) JSONPath.get(oasSchema, "$.components.schemas.AddressType.enum")).contains("HOME"));
        Assertions.assertTrue(((List) JSONPath.get(oasSchema, "$.components.schemas.Customer.required")).contains("username"));
//        Assertions.assertEquals("3", JSONPath.get(oasSchema, "$.components.schemas.Customer.properties.firstName.minLength").toString());

//        Assertions.assertEquals("#/components/schemas/CustomerOrder", JSONPath.get(oasSchema, "$.components.messages.CustomerOrderEventMessage.payload.$ref"));
    }

}
