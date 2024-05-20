package am.developer.camel.configuration.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class UnmarshallerProperties {

    private String delayPattern;

    private Integer maxRedeliveries=5;

    private Boolean showStackTrace;

}
