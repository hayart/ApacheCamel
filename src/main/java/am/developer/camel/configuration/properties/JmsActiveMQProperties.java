package am.developer.camel.configuration.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class JmsActiveMQProperties {

    @Value("${broker.url}")
    private String brokerUrl;

    @Value("${arebo.broker.url}")
    private String areboBrokerUrl;

}
