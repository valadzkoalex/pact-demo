package pact.provider;

import au.com.dius.pact.provider.junit5.HttpsTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

@Provider("zed_api_provider")
@PactBroker(host = "localhost:9292", scheme = "http")
public class ZedApiProviderTest {
  @BeforeEach
  public void before(PactVerificationContext context) {
    context.setTarget(
        new HttpsTestTarget(
            "community-z.com",
            443,
            "/"));
  }

  @Tag("pact_provider")
  @TestTemplate
  @ExtendWith(PactVerificationInvocationContextProvider.class)
  public void pactVerificationTestTemplate(PactVerificationContext context) {
    context.verifyInteraction();
  }
}
