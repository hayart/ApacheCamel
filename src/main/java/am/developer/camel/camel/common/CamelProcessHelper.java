package am.developer.camel.camel.common;

import am.developer.camel.configuration.SpringContextConfig;
import am.developer.camel.utility.BatchConstants;
import com.google.common.annotations.VisibleForTesting;
import am.developer.camel.parser.SAXUnmarshaller;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.StringReader;
import java.util.Date;
import java.util.Map;

@Component
public class CamelProcessHelper {

    /**
     * Declaration of Sax EXTERNAL_GENERAL_ENTITIES URL
     */
    @VisibleForTesting
    private static final String EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities";

    /**
     * Declaration of Sax DISALLOW_DOCTYPE_DECL URL
     */
    @VisibleForTesting
    private static final String DISALLOW_DOCTYPE_DECL = "http://apache.org/xml/features/disallow-doctype-decl";

    @Autowired
    private SpringContextConfig springContext;

    public SAXUnmarshaller unmarshall(String body) throws Exception {
        final InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(body));

        // Init the marshaller with XML input file
        SAXUnmarshaller saxUnmarshaller = new SAXUnmarshaller(body, springContext);

        final XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        // Disable external entities as well to prevent the Server Side Request Forgery(SSRF) vulnerability
        xmlReader.setFeature(EXTERNAL_GENERAL_ENTITIES, false);
        // Disallow Doctype Decl as well to prevent the Server Side Request Forgery(SSRF) vulnerability
        xmlReader.setFeature(DISALLOW_DOCTYPE_DECL, true);
        // Setting the unmarshaller
        xmlReader.setContentHandler(saxUnmarshaller);
        // Starting the extraction + persistence
        xmlReader.parse(is);

        return saxUnmarshaller;
    }

    public void fillNotTreatedInfo(final Exchange exchange,
                                   final SAXUnmarshaller saxUnmarshaller,
                                   final long fileTreatmentTimeMillis) {
        if (!saxUnmarshaller.getRejectList().isEmpty()) {
            int errorsNumber = 0;
            for (final Map.Entry<String, Map<String, Object>> entry : saxUnmarshaller.getTracePerGroupEntity().entrySet()) {
                final Map<String, Object> tempo = entry.getValue();
                if (null != tempo && ((Long) tempo.get(BatchConstants.FAILED) > 0)) {
                    errorsNumber++;
                }
            }

            String originalFileName = (String) exchange.getIn().getHeader(BatchConstants.CAMEL_HEADER_FILE_NAME);
            if (StringUtils.isBlank(originalFileName)) {
                originalFileName = (String) exchange.getIn().getHeader(BatchConstants.CAMEL_HEADER_JMS_MESSAGE_ID);
            }

            exchange.getIn().setHeader(BatchConstants.CAMEL_HEADER_HAS_REJECT, true);
            exchange.getIn().setHeader(BatchConstants.CAMEL_HEADER_ORIGINAL_FILE_NAME, originalFileName);
            exchange.getIn().setHeader(BatchConstants.CAMEL_HEADER_ERRORS_NUMBER, errorsNumber);
            exchange.getIn().setBody(saxUnmarshaller.getNotTreatedBody());
        }
    }

}
