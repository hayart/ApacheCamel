/**
 * Copyright (c) Worldline 2017 - All Rights Reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 **/
package am.developer.camel.utility;

import java.util.List;
import java.util.Map;

/**
 * Definition Class for constant values
 *
 * @author a509159
 * @version 2.0.38
 */
public final class BatchConstants {

    /**
     * Variable to store base logger name for camel
     */
    public static final String BASE_LOGGER_NAME = "net.awl.bfi.websuite.atmosbatch";

    /**
     * Constant for file surveillance
     */
    public static final String SURVEILLANCE = "surveillance";

    /**
     * Variable to store the value of 2
     */
    public static final int VALUE_2 = 2;

    /**
     * Variable to store the value of one day as hours
     */
    public static final int DAY_AS_HOURS = 24;

    /**
     * Variable to store the value of one hour as minutes
     */
    public static final int HOUR_AS_MINUTES = 60;

    /**
     * Variable to store the value of one minute as seconds
     */
    public static final int MINUTE_AS_SECONDS = 60;

    /**
     * Variable to store the value of one minute as millis
     */
    public static final int MINUTE_AS_MILLIS = 1000;

    /**
     * Constant failed
     */
    public static final String FAILED = "failed";

    /**
     * constant number
     */
    public static final String NUMBER = "number";

    /**
     * constant duration
     */
    public static final String DURATION = "duration";

    /**
     * constant currentElementStartTime
     */
    public static final String CURRENT_ELEMENT_START_TIME = "currentElementStartTime";

    /**
     * Variable to store the value of a ActivityTracking sendingBy VIB_AUTO
     */
    public static final String RESPONSE_SENT_BY_VIB_AUTO = "VIB_AUTO";

    /**
     * Constant which is used for ModuleRow logging
     */
    public static final String ATMOS = "ATMOS";

    /**
     * Constant which is used for ModuleRow logging
     */
    public static final String TERMSTATEGL = "TERMSTATEGL";

    /**
     * Constant which is used for ModuleRow logging
     */
    public static final String EG = "EG";

    /**
     * Constant for attribute historyCode
     */
    public static final String HISTORY_TABLE_ATTRIBUTE_HISTORY_CODE = "historyCode";

    /**
     * Regex constant to have only positive integers
     */
    public static final String REGEX_FOR_POSITIVE_INTEGER = "\\d+";

    /**
     * Constant for Response feedback field
     */
    public static final String RESPONSE_FIELD_FEEDBACK_NAME = "feedback";

    /**
     * Constant for attribute champ
     */
    public static final String PILOTAGE_RESPONSE_FIELD_CHAMP_NAME = "champ";

    /**
     * Constant for attribute Type
     */
    public static final String BATCH_FILE_HISTORY_FIELD_TYPE_NAME = "type";

    /**
     * Constant for attribute numbero
     */
    public static final String PILOTAGE_RESPONSE_FIELD_CHAMP_ATTRIBUTE_NUMBERO_NAME = "numero";

    /**
     * Constant for attribute value numbero
     */
    public static final String PILOTAGE_RESPONSE_FIELD_CHAMP_ATTRIBUTE_NUMBERO_VALUE = "296";

    /**
     * Constant for attribute valeur
     */
    public static final String PILOTAGE_RESPONSE_FIELD_CHAMP_ATTRIBUTE_VALUER_NAME = "valeur";

    /**
     * Constant name
     */
    public static final String NAME = "name";

    /**
     * Variable to store the value of 3
     */
    public static final int VALUE_3 = 3;

    /**
     * Constant for Response action field, when value is CMD_IS_IN_YELLOW_LIST
     */
    public static final String CMD_IS_IN_YELLOW_LIST_ACTION = "CMD_IS_IN_YELLOW_LIST";

    /**
     * Feedback coders that needs to convert to corresponding value
     */
    public static final Map<String, String> FEEDBACK_CONVERSION_CODES = Map.of(
            "0", "NOT_IN_YELLOW_LIST",
            "1", "YELLOW_LIST_DUE_TO_REFERENTIAL_ISSUE",
            "2", "YELLOW_LIST_DUE_TO_CRYPTO_ISSUE");

    /**
     * Constant for XML start tag element names
     */
    public static final List<String> XML_START_TAG_ELEMENT_NAMES = List.of("batch", "SURVEILLANCE", "REFERENTIEL", "FLUX_ALIMVATMS");

    /**
     * Variable to store the camel header value of HAS_REJECT
     */
    public static final String CAMEL_HEADER_HAS_REJECT = "HAS_REJECT";

    /**
     * Variable to store the camel header value of ORIGINAL_FILE_NAME
     */
    public static final String CAMEL_HEADER_ORIGINAL_FILE_NAME = "ORIGINAL_FILE_NAME";

    /**
     * Variable to store the camel header value of FILE_TREATMENT_TIME
     */
    public static final String CAMEL_HEADER_FILE_TREATMENT_TIME = "FILE_TREATMENT_TIME";

    /**
     * Variable to store the camel header value of ERRORS_NUMBER
     */
    public static final String CAMEL_HEADER_ERRORS_NUMBER = "ERRORS_NUMBER";

    /**
     * Variable to store the camel header value of CamelFileName
     */
    public static final String CAMEL_HEADER_FILE_NAME = "CamelFileName";

    /**
     * Variable to store the camel header value of JMSMessageID
     */
    public static final String CAMEL_HEADER_JMS_MESSAGE_ID = "JMSMessageID";

    /**
     * Variable to store the camel header value of CamelFileNameOnly
     */
    public static final String CAMEL_HEADER_FILE_NAME_ONLY = "CamelFileNameOnly";

    /**
     * Constructor
     */
    private BatchConstants() {
    }

}
