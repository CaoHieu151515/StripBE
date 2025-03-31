package strip.config;

import com.braintreegateway.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BraintreeConfiguration {

    private final ApplicationProperties applicationProperties;

    public BraintreeConfiguration(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean
    public BraintreeGateway braintreeGateway() {
        return new BraintreeGateway(
            Environment.SANDBOX,
            applicationProperties.getBraintree().getMerchantId(),
            applicationProperties.getBraintree().getPublicKey(),
            applicationProperties.getBraintree().getPrivateKey()
        );
    }
}
