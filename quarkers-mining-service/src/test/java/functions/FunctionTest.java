package functions;

import io.quarkus.funqy.knative.events.CloudEventBuilder;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@QuarkusTest
public class FunctionTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void testFunction() {
        Bitmine bitmine = new Bitmine();
        bitmine.setType("Testium");

        (new Function()).function(CloudEventBuilder.create().build(bitmine));

        Assertions.assertEquals(
                "CloudEvent{specVersion='null', id='null', type='null', source='null', subject='null', time=null, extensions={}, dataSchema=null, dataContentType='application/json', data=Bitmine [status=processed, type=Testium, weight=null]}",
                outputStreamCaptor.toString()
                        .trim());
    }

    @Test
    public void testFunctionIntegration() {
        RestAssured.given().contentType("application/json")
                .body("{\"message\": \"Hello!\"}")
                .header("ce-id", "42")
                .header("ce-specversion", "1.0")
                .post("/")
                .then().statusCode(200)
                .header("ce-id", notNullValue())
                .header("ce-specversion", equalTo("1.0"))
                .header("ce-source", equalTo("function"))
                .header("ce-type", equalTo("function.output"))
                .body("message", equalTo("Hello!"));
    }

}
