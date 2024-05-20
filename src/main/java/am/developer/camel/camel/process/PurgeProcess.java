package am.developer.camel.camel.process;

import am.developer.camel.utility.ProcessUtility;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Slf4j
@Component
public class PurgeProcess implements Processor {


    private Integer purgeMonthNumber=5;


    @Override
    public void process(Exchange exchange) throws Exception {
        final long start = System.currentTimeMillis();

        log.info("------------------------------------");
        log.info("Starting Purge process...");
        log.info("Number of months property to keep : {} Months.", purgeMonthNumber);

        Calendar now = Calendar.getInstance();
        Calendar purgeDate = Calendar.getInstance();

        now.add(Calendar.MONTH, -purgeMonthNumber);

        // set purge date
        purgeDate.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), 0, 0, 0);


        log.info("Purging process ended, Time : {}.", ProcessUtility.durationTime(start));
    }


}
