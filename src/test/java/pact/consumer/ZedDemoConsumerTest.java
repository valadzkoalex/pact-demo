package pact.consumer;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;


@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "zed_api_provider", port = "8091")
public class ZedDemoConsumerTest {
  @BeforeEach
  public void setUp(MockServer mockServer) {
    assertThat(mockServer, is(notNullValue()));
  }

  @Pact(consumer = "zed_demo_consumer")
  public RequestResponsePact getEvents(PactDslWithProvider builder) {
    PactDslJsonBody pactBodyJson = new PactDslJsonBody()
            .integerType("total")
            .stringType("period")
            .eachLike("events")
            .numberType("id")
            .stringType("title")
            .closeArray()
            .asBody();

    return builder
        .uponReceiving("get events interaction")
        .path("/api/v2/events.json")
        .method("GET")
        .willRespondWith()
            .body(pactBodyJson)
        .status(HttpStatus.SC_OK)
        .toPact();
  }

  @Tag("pact_consumer_test")
  @Test
  @PactTestFor(pactMethod = "getEvents")
  public void getEventsTest(MockServer mockServer) throws IOException {
    OkHttpClient client = new OkHttpClient();
    Request request =
        new Request.Builder().url(mockServer.getUrl() + "/api/v2/events.json").get().build();
    Response pactDescribedResponse = client.newCall(request).execute();
    assertThat(pactDescribedResponse.code(), is(equalTo(HttpStatus.SC_OK)));
  }

  // gradle canIDeploy -Ppacticipant=zed_demo_consumer -Ppacticipant=zedApiProvider -Platest=true
}
