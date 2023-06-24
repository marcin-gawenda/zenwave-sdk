package io.zenwave360.sdk.plugins;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.*;

import io.zenwave360.sdk.MainGenerator;
import io.zenwave360.sdk.Plugin;
import io.zenwave360.sdk.formatters.JavaFormatter;
import io.zenwave360.sdk.options.PersistenceType;
import io.zenwave360.sdk.options.ProgrammingStyle;
import io.zenwave360.sdk.parsers.JDLParser;
import io.zenwave360.sdk.processors.JDLProcessor;
import io.zenwave360.sdk.templating.TemplateOutput;
import io.zenwave360.sdk.testutils.MavenCompiler;
import nl.altindag.log.LogCaptor;

public class ZDLBackendApplicationMongoImperativeGeneratorTest {

    private static LogCaptor logCaptor = LogCaptor.forRoot();

    @BeforeAll
    public static void setupLogCaptor() {
        logCaptor = LogCaptor.forRoot();
    }

    @AfterEach
    public void clearLogs() {
        logCaptor.clearLogs();
    }

    @AfterAll
    public static void tearDown() {
        logCaptor.close();
    }

    private Map<String, Object> loadJDLModelFromResource(String resource) throws Exception {
        Map<String, Object> model = new JDLParser().withSpecFile(resource).parse();
        return new JDLProcessor().process(model);
    }


    @Test
    public void test_generator_hexagonal_mongodb_customer_address() throws Exception {
        String targetFolder = "target/zdl/test_generator_hexagonal_mongodb_customer_address";
        Plugin plugin = new JDLBackendApplicationDefaultPlugin()
                .withSpecFile("classpath:io/zenwave360/sdk/resources/zdl/customer-address.zdl")
                .withTargetFolder(targetFolder)
                .withOption("basePackage", "io.zenwave360.example")
                .withOption("persistence", PersistenceType.mongodb)
                .withOption("style", ProgrammingStyle.imperative)
                .withOption("forceOverwrite", true)
                .withOption("haltOnFailFormatting", false);

        new MainGenerator().generate(plugin);

        List<String> logs = logCaptor.getLogs();
        // Assertions.assertTrue(logs.contains("Writing template with targetFile: io/example/integration/test/api/provider_for_commands_reactive/DoCreateProductConsumer.java"));
        // Assertions.assertTrue(logs.contains("Writing template with targetFile: io/example/integration/test/api/provider_for_commands_reactive/DoCreateProductService.java"));

        int exitCode = MavenCompiler.compile("src/test/resources/mongodb-elasticsearch-scs3-pom.xml", targetFolder);
        Assertions.assertEquals(0, exitCode);
    }

    @Test
    public void test_generator_hexagonal_mongodb_order_faults_attachments() throws Exception {
        String targetFolder = "target/zdl/test_generator_hexagonal_mongodb_order_faults_attachments";
        Plugin plugin = new JDLBackendApplicationDefaultPlugin()
                .withSpecFile("classpath:io/zenwave360/sdk/resources/zdl/order-faults-attachments-model.zdl")
                .withTargetFolder(targetFolder)
                .withOption("basePackage", "io.zenwave360.example")
                .withOption("persistence", PersistenceType.mongodb)
                .withOption("style", ProgrammingStyle.imperative)
                .withOption("forceOverwrite", true)
                .withOption("haltOnFailFormatting", false);

        new MainGenerator().generate(plugin);

        List<String> logs = logCaptor.getLogs();
        // Assertions.assertTrue(logs.contains("Writing template with targetFile: io/example/integration/test/api/provider_for_commands_reactive/DoCreateProductConsumer.java"));
        // Assertions.assertTrue(logs.contains("Writing template with targetFile: io/example/integration/test/api/provider_for_commands_reactive/DoCreateProductService.java"));

        int exitCode = MavenCompiler.compile("src/test/resources/mongodb-elasticsearch-scs3-pom.xml", targetFolder);
        Assertions.assertEquals(0, exitCode);
    }

}
