/*
 * Copyright (c) Worldline 2022 - All Rights Reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 **/
package am.developer.camel.parser;

import am.developer.camel.configuration.SpringContextConfig;
import am.developer.camel.configuration.properties.UnmarshallerProperties;
import am.developer.camel.utility.BatchConstants;
import am.developer.camel.utility.ClassUtility;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;

/**
 * This class reads an XML file to put its elements in the Batch class before persisting
 *
 * @author a509159
 * @version 2.0.38
 */
@Slf4j
public class SAXUnmarshaller extends DefaultHandler {

    /**
     * Stack of XML elements used to fill in the batch class attributes
     */
    private final Stack<Object> stack;

    /**
     * Refers to the number of elements currently in the stack
     */
    private int numberOfStackElement;

    /**
     * Boolean that tells if a value can be read to be put into the batch class Else, it means that a start/end tag has been read in the document
     */
    private boolean isStackReadyForData;

    /**
     * The number of threat transaction in file
     */
    private Integer transactionCount = 0;

    /**
     * Map contains real internal number and technical terminal ID
     */
    private final Map<String, Long> internalNumberTerminalId;

    /**
     * List contains all entity that can't be persist
     */
    private final List<Object> rejectList;

    /**
     * boolean to show if there is rejected transactions in file
     */
    private boolean isRejectObject = false;

    /**
     * this variable is a kind of collection which will contains ,for each key , the number of elements treated relative to the key, the duration of
     * treatment for the set of elements HashMap<key, HashMap<String, Object> []> traceParGroupeEntite; key : name of the entity eg. History
     */
    @Getter
    private Map<String, Map<String, Object>> tracePerGroupEntity;

    /**
     * The value of instance History field type
     */
    private String historyFieldTypeValue;

    /**
     * Map servant au retry lorsqu'il y a un probleme de connexion ou sur des tables de la base de donnees,
     * elle contient le nombre d'essais a refaire et le temps les separant
     */
    private final SortedMap<Integer, Integer> delayMap;

    /**
     * The not treated body of the unmarshalled XML file
     */
    @Getter
    private String notTreatedBody;

    /**
     * The index of <.Row> tag element</>
     */
    private int rowTagIndex;


    /**
     * Methode SAXUnmarshaller
     *
     * @param body XML file's body
     * @throws InterruptedException an InterruptedException
     */
    public SAXUnmarshaller(String body, SpringContextConfig springContext) throws Exception {
        UnmarshallerProperties unmarshallerProperties = springContext.getBean(UnmarshallerProperties.class);

        this.notTreatedBody = body;
        this.rowTagIndex = 0;

        // Init stack
        this.stack = new Stack<>();
        this.numberOfStackElement = 0;
        this.isStackReadyForData = false;

        // contains terminal Id and primary key
        this.internalNumberTerminalId = new HashMap<>();
        this.rejectList = new ArrayList<>();
        this.tracePerGroupEntity = new HashMap<>();
        this.delayMap = initDelayMap(unmarshallerProperties.getDelayPattern(), unmarshallerProperties.getMaxRedeliveries());

    }

    /**
     * Launched in the XML for each start tag element, and manages the stack in function of the element type
     */
    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attribs) throws RuntimeException {
        try {
            log.debug(this.getCurrentTagProperties(uri, localName, qName));
            this.isStackReadyForData = false;

            // Case of first start tag element <batch>
            if (BatchConstants.XML_START_TAG_ELEMENT_NAMES.contains(localName)) {
                this.stack.push(new StringBuffer());
                this.numberOfStackElement++;
            } else if (localName.endsWith("Row")) { // Case of <...Row> tag element
                final StringBuilder className = new StringBuilder();
                this.transactionCount++;

                // Creation of the class name from the <...Row> tag element package
                className.append("net.awl.bfi.websuite.atmosbatch.models.");

                // Set first letter in upper case
                className.append(localName.substring(0, 1).toUpperCase(Locale.FRENCH));

                // Append other letters except "Row"
                className.append(localName, 1, localName.length() - BatchConstants.VALUE_3);

                // Retrieve class
                Class<?> myClass = ClassUtility.retrieveClass(className.toString());

                // Instantiation of the class and push it on the top of the stack
                if (null != myClass) {
                    try {
                        this.stack.push(myClass.getDeclaredConstructor().newInstance());
                        this.numberOfStackElement++;
                        /*
                         * BBE
                         */
                        final String key = myClass.getName();
                        Map<String, Object> tempo = this.tracePerGroupEntity.get(key);
                        if (null == tempo) {
                            tempo = new HashMap<>();
                            tempo.put(BatchConstants.NUMBER, 1L);// number = successfully persisted entities
                            tempo.put(BatchConstants.CURRENT_ELEMENT_START_TIME, System.currentTimeMillis());
                            tempo.put(BatchConstants.DURATION, 0L);
                            tempo.put(BatchConstants.FAILED, 0L);// failed elements
                            tempo.put(BatchConstants.NAME, myClass.getSimpleName());
                        } else {
                            final Long number = (Long) tempo.get(BatchConstants.NUMBER) + 1;
                            tempo.put(BatchConstants.CURRENT_ELEMENT_START_TIME, System.currentTimeMillis());
                            tempo.put(BatchConstants.NUMBER, number);
                        }
                        this.tracePerGroupEntity.put(key, tempo);

                        updateOpenRowTagIndex(localName);
                        /*
                         * end BBE
                         */
                    } catch (final InstantiationException e) {
                        log.error("[An exception occurred while instantiating the class '{}'.][UWV-105]", className);
                        log.error(this.getCurrentTagProperties(uri, localName, qName));
                    } catch (final IllegalAccessException e) {
                        log.error("[An illegal access exception has occurred while invoking the class '{}'.][UWV-106]", className);
                        log.error("{}", this.getCurrentTagProperties(uri, localName, qName));
                    }
                } else {
                    log.error("{}", this.getCurrentTagProperties(uri, localName, qName));
                }
            } else if (!localName.equals("command")) { // Case of Simple tag element <...>
                // Push a new Object, and make the stack ready to accept character text
                this.stack.push(new StringBuffer());
                this.numberOfStackElement++;
                this.isStackReadyForData = true;

                // check attributes characters
                attributeCharacters(BatchConstants.PILOTAGE_RESPONSE_FIELD_CHAMP_NAME,
                        localName,
                        BatchConstants.PILOTAGE_RESPONSE_FIELD_CHAMP_ATTRIBUTE_NUMBERO_NAME,
                        BatchConstants.PILOTAGE_RESPONSE_FIELD_CHAMP_ATTRIBUTE_NUMBERO_VALUE,
                        BatchConstants.PILOTAGE_RESPONSE_FIELD_CHAMP_ATTRIBUTE_VALUER_NAME,
                        attribs);
            }
        } catch (final RuntimeException e) {
            log.error("An exception has occurred", e);
            throw e;
        } catch (final Exception e) {
            log.error("An unexpected exception has occurred in StartElement at transaction", e);
        }
    }

    /**
     * Launched for each end tag element - for a simple element, it sets a batch class attribute - for the transaction case, elements of the
     * transaction are stored into the TransactionCriteria element above the transaction table - for any other elements, it sets the object
     * (TransactionCriteria table...) into the batch class
     */
    @Override
    public void endElement(final String uri, final String localName, final String qName) {
        log.debug(this.getCurrentTagProperties(uri, localName, qName));
        Long terminalId;
        StringBuilder methodName;
        boolean isInserted = false;
        boolean isSet = false;
        Object temp;
        Object element = null;

        try {
            // recognized text is always content of an element
            // when the element closes, no more text should be expected
            this.isStackReadyForData = false;

            // Pop the top element of the stack and add it to the 'parent' element,
            // which is the next element of the stack
            temp = this.stack.pop();

            // Popping means decreasing the number of left elements in the stack
            this.numberOfStackElement--;

            // Case stack empty when arrived to </batch> end tag
            if (this.stack.isEmpty()) {
                this.stack.push(temp);
                this.numberOfStackElement++;
            } else if (this.numberOfStackElement == 1) {
                if (localName.endsWith("Row")) {


                    if (!isInserted) {
                        this.rejectList.add(temp);
                        updateCloseRowTagIndex(localName);
                    } else {


                        eliminateTreatedRow(localName);
                    }
                    this.isRejectObject = false;
                } else {
                    this.stack.push(temp);
                    this.numberOfStackElement++;
                }
            } else {
                // Pop the top element of the stack:
                // If It is a RemoteParametersTableDetails object and the previous
                // popped object (temp) is a RemoteParametersTableDetailsId
                // Else it can be a normal Entity object
                element = this.stack.pop();

                methodName = new StringBuilder();

                // Creation of the set method name from the simple tag element
                methodName.append("set").append(localName.substring(0, 1).toUpperCase(Locale.FRENCH)).append(localName.substring(1));


                // if can't set the attribute of an object entity
                if (!isSet) {
                    this.isRejectObject = true;
                }
                // Push the element back onto the top of the stack
                this.stack.push(element);
            }
        } catch (final Exception e) {
            this.isRejectObject = true;
            // Push the element back onto the top of the stack
            this.stack.push(element);
            log.error("An unexpected exception has occurred in EndElement at transaction {}", this.transactionCount, e);
        }
    }



    /**
     * When reading a simple element, the isStackReadyForData is true and the element is put into the StringBuffer which is pushed onto the stack
     */
    @Override
    public void characters(final char[] data, final int start, final int length) {
        // if stack is not ready, data is not part of recognized element
        if (this.isStackReadyForData) {
            ((StringBuffer) this.stack.peek()).append(data, start, length);
        }
    }

    /**
     * When reading a simple element with attributes,
     * the isStackReadyForData is true and the element is put into the StringBuffer
     * which is pushed onto the stack
     * Check if attributeNameToCheck value equals to attributeValueToCheck,
     * if true append attributeNameToSave value onto the stack
     *
     * @param tagNameToCheck        a tag name that have attribute which need to check
     * @param localName             current read tag name
     * @param attributeNameToCheck  attribute name which value must be equals to attributeValueToCheck
     * @param attributeValueToCheck attribute value that need to check
     * @param attributeNameToSave   attribute name that need to save if all conditions is true
     * @param attribs               list of attributes
     */
    public void attributeCharacters(final String tagNameToCheck,
                                    final String localName,
                                    final String attributeNameToCheck,
                                    final String attributeValueToCheck,
                                    final String attributeNameToSave,
                                    final Attributes attribs) {
        // if stack is not ready, data is not part of recognized element
        if (this.isStackReadyForData && tagNameToCheck.equals(localName) && attribs.getValue(attributeNameToCheck).equals(attributeValueToCheck)) {
            ((StringBuffer) this.stack.peek()).append(attribs.getValue(attributeNameToSave));
        }
    }


    /**
     * @param uri       uri
     * @param localName localName
     * @param qName     qName
     * @return a string with log message
     */
    private String getCurrentTagProperties(final String uri, final String localName, final String qName) {
        return "Error occurred at transaction " + this.transactionCount + " for uri = " + uri + ", localName = " + localName + ", qName = "
                + qName;

    }

    /**
     * Methode to convert string value in true or false boolean value
     *
     * @param value string
     * @return boolean value true or false
     */
    private boolean convertToBoolean(final String value) {
        return ("1".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value));
    }


    /**
     * @return List<Object>
     */
    public List<Object> getRejectList() {
        return new ArrayList<>(this.rejectList);
    }

    /**
     * Updates current element's process duration
     *
     * @param tempo current treated element
     */
    private void updateCurrentElementDuration(final Map<String, Object> tempo) {
        final Long currentElementStartTime = (Long) tempo.get(BatchConstants.CURRENT_ELEMENT_START_TIME);
        Long duration = (Long) tempo.get(BatchConstants.DURATION);
        /*
         * increment the duration by System.currentTimeMillis minus currentElementStartTime
         */
        final long currentElementDuration = (System.currentTimeMillis() - currentElementStartTime);
        duration = duration + currentElementDuration;
        tempo.put(BatchConstants.DURATION, duration);
    }

    /**
     * Updates treated elements failed count
     *
     * @param tempo current treated element
     */
    private void updateTraceFailedCase(final Map<String, Object> tempo) {
        // case failed
        Long number = (Long) tempo.get(BatchConstants.NUMBER);
        Long failed = (Long) tempo.get(BatchConstants.FAILED);
        number = number - 1;
        failed = failed + 1;
        /*
         * decrement the duration by currentElementDuration
         */
        tempo.put(BatchConstants.NUMBER, number);
        tempo.put(BatchConstants.FAILED, failed);
    }

    private void updateOpenRowTagIndex(String localName) {
        this.rowTagIndex = this.notTreatedBody.indexOf(getOpenTagName(localName), this.rowTagIndex);
    }

    private void updateCloseRowTagIndex(String localName) {
        this.rowTagIndex = getCloseRowTagIndex(localName);
    }

    private void eliminateTreatedRow(String localName) {
        this.notTreatedBody = this.notTreatedBody.substring(0, this.rowTagIndex).trim() + this.notTreatedBody.substring(getCloseRowTagIndex(localName));
    }

    private int getCloseRowTagIndex(String localName) {
        String closeTagName = getCloseTagName(localName);
        return this.notTreatedBody.indexOf(closeTagName, this.rowTagIndex) + closeTagName.length();
    }

    private String getOpenTagName(String localName) {
        return "<" + localName + ">";
    }

    private String getCloseTagName(String localName) {
        return "</" + localName + ">";
    }

    /**
     * Method to get a delay Map representing strategie for redelivering policy
     *
     * @param delayPattern    delay pattern
     * @param maxRedeliveries maximum redeliveries
     * @return a treemap with number of iteration and time to wait between each retry
     */
    public static SortedMap<Integer, Integer> initDelayMap(String delayPattern, int maxRedeliveries) {
        String[] delayArray = delayPattern.split(";");

        TreeMap<Integer, Integer> delayMap = new TreeMap<>();
        for (String delay : delayArray) {
            String[] array = delay.split(":");
            delayMap.put(Integer.parseInt(array[0]), Integer.parseInt(array[1]));
        }

        log.debug("delayMap {}", delayMap);
        TreeMap<Integer, Integer> fullDelayMap = new TreeMap<>();
        for (Map.Entry<Integer, Integer> entry : delayMap.entrySet()) {
            int nextDelay;
            if (delayMap.higherKey(entry.getKey()) != null) {
                nextDelay = delayMap.higherKey(entry.getKey());
            } else {
                nextDelay = maxRedeliveries;
            }
            for (int i = entry.getKey(); i < nextDelay; i++) {
                fullDelayMap.put(i, entry.getValue());
            }
        }
        log.debug("fullDelayMap {}", fullDelayMap);

        return fullDelayMap;
    }

}
