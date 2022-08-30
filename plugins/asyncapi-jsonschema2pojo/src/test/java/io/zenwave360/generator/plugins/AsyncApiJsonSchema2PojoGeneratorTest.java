package io.zenwave360.generator.plugins;

import io.zenwave360.generator.Configuration;
import io.zenwave360.generator.MainGenerator;
import nl.altindag.log.LogCaptor;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class AsyncApiJsonSchema2PojoGeneratorTest {

    @BeforeEach
    public void setup() throws IOException {
        FileUtils.deleteDirectory(new File("target/zenwave630/out"));
    }

    @Test
    public void test_generator_for_asyncapi_schemas() throws Exception {
        Configuration configuration = new AsyncApiJsonSchema2PojoConfiguration()
                .withSpecFile("classpath:io/zenwave360/generator/resources/asyncapi/asyncapi-events.yml")
                .withTargetFolder("target/zenwave630/out")
                .withOption("modelPackage", "io.example.integration.test.with_schemas.model")
                ;

        new MainGenerator().generate(configuration);

        Assertions.assertTrue(new File("target/zenwave630/out/io/example/integration/test/with_schemas/model/ColorRelatedMsg.java").exists());
    }


    @Test
    public void test_generator_for_json_schemas() throws Exception {
        Configuration configuration = new AsyncApiJsonSchema2PojoConfiguration()
                .withSpecFile("classpath:io/zenwave360/generator/resources/asyncapi/json-schemas/asyncapi.yml")
                .withTargetFolder("target/zenwave630/out")
                ;

        new MainGenerator().generate(configuration);

        Assertions.assertTrue(new File("target/zenwave630/out/io/example/transport/schema/TransportNotificationEvent.java").exists());
    }
}
