package io.zenwave360.generator.plugins;

import io.zenwave360.generator.parsers.DefaultYamlParser;
import io.zenwave360.generator.processors.AsyncApiProcessor;
import io.zenwave360.generator.processors.OpenApiProcessor;
import io.zenwave360.generator.templating.TemplateOutput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

public class SpringWebTestClientGeneratorTest {

    SpringWebTestClientGenerator generator = new SpringWebTestClientGenerator();

    private Map<String, ?> loadApiModelFromResource(String resource) throws Exception {
        File file = new File(getClass().getClassLoader().getResource(resource).toURI());
        Map<String, ?> model = new DefaultYamlParser().withSpecFile(file.getAbsolutePath()).parse();
        return new OpenApiProcessor().process(model);
    }

    @Test
    public void test_output_partial_one_operation() throws Exception {
        Map<String, ?> model = loadApiModelFromResource("io/zenwave360/generator/plugins/SpringWebTestClientGenerator/openapi-petstore.yml");

        generator.groupBy = SpringWebTestClientGenerator.GroupByType.PARTIAL;
        generator.apiPackage = "io.example.api";
        generator.modelPackage = "io.example.api.model";
        generator.operationIds = List.of("addPet");
        List<TemplateOutput> outputTemplates = generator.generate(model);
        Assertions.assertEquals(1, outputTemplates.size());
        Assertions.assertEquals("io/example/api/Operation.java", outputTemplates.get(0).getTargetFile());
        System.out.println(outputTemplates.get(0).getContent());
    }

    @Test
    public void test_output_by_one_service() throws Exception {
        Map<String, ?> model = loadApiModelFromResource("io/zenwave360/generator/plugins/SpringWebTestClientGenerator/openapi-petstore.yml");

        generator.groupBy = SpringWebTestClientGenerator.GroupByType.SERVICE;
        generator.apiPackage = "io.example.api";
        generator.modelPackage = "io.example.api.model";
        generator.operationIds = List.of("addPet");
        List<TemplateOutput> outputTemplates = generator.generate(model);
        Assertions.assertEquals(2, outputTemplates.size());
        Assertions.assertEquals("io/example/api/ControllersTestSet.java", outputTemplates.get(0).getTargetFile());
        Assertions.assertEquals("io/example/api/PetServiceIT.java", outputTemplates.get(1).getTargetFile());
        System.out.println(outputTemplates.get(1).getContent());
    }

    @Test
    public void test_output_by_all_services() throws Exception {
        Map<String, ?> model = loadApiModelFromResource("io/zenwave360/generator/plugins/SpringWebTestClientGenerator/openapi-petstore.yml");

        generator.groupBy = SpringWebTestClientGenerator.GroupByType.SERVICE;
        generator.apiPackage = "io.example.api";
        generator.modelPackage = "io.example.api.model";
        List<TemplateOutput> outputTemplates = generator.generate(model);
        Assertions.assertEquals(4, outputTemplates.size());
        Assertions.assertEquals("io/example/api/ControllersTestSet.java", outputTemplates.get(0).getTargetFile());
        Assertions.assertEquals("io/example/api/UserServiceIT.java", outputTemplates.get(1).getTargetFile());
        Assertions.assertEquals("io/example/api/StoreServiceIT.java", outputTemplates.get(2).getTargetFile());
        Assertions.assertEquals("io/example/api/PetServiceIT.java", outputTemplates.get(3).getTargetFile());
        System.out.println(outputTemplates.get(3).getContent());
    }
}
