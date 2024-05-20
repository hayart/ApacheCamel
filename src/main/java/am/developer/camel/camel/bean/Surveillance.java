package am.developer.camel.camel.bean;

import am.developer.camel.utility.BatchConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Surveillance {

    /**
     * Constante de log
     */
    private static final String LOG_JMS_SURVEILLANCE = "[UWV] JMS is active.";

    public void surveillanceJMS() {
        log.info(LOG_JMS_SURVEILLANCE);
    }

    public String buildSurveillanceMsg() {
        return BatchConstants.SURVEILLANCE;
    }

}
