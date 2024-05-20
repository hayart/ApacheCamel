package am.developer.camel.camel.route;

import am.developer.camel.utility.BatchConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PurgeRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:PurgeErr")
                .routeId("PROCESS_MESSAGE")
                .log(LoggingLevel.ERROR, BatchConstants.BASE_LOGGER_NAME, "[VATMS_ERR_10][Error in processing message]")
                .log(LoggingLevel.ERROR, BatchConstants.BASE_LOGGER_NAME, "Error Message: ${exception.message}")
                .log(LoggingLevel.ERROR, BatchConstants.BASE_LOGGER_NAME, "Stack Trace: ${exception.stacktrace}")
                .to("file://tmpFolder");
    }

}
