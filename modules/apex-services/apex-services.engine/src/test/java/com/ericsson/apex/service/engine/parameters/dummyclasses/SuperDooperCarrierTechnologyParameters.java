/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.parameters.dummyclasses;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import com.ericsson.apex.model.basicmodel.service.ParameterService;
import com.ericsson.apex.service.parameters.carriertechnology.CarrierTechnologyParameters;

/**
 * Apex parameters for SuperDooper as an event carrier technology.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class SuperDooperCarrierTechnologyParameters extends CarrierTechnologyParameters {
    // Default parameter values
    private static final String    DEFAULT_ACKS                = "all";
    private static final String    DEFAULT_BOOTSTRAP_SERVERS   = "localhost:9092";
    private static final int       DEFAULT_RETRIES             = 0;
    private static final int       DEFAULT_BATCH_SIZE          = 16384;
    private static final int       DEFAULT_LINGER_TIME         = 1;
    private static final long      DEFAULT_BUFFER_MEMORY       = 33554432;
    private static final String    DEFAULT_GROUP_ID            = "default-group-id";
    private static final boolean   DEFAULT_ENABLE_AUTO_COMMIT  = true;
    private static final int       DEFAULT_AUTO_COMMIT_TIME    = 1000;
    private static final int       DEFAULT_SESSION_TIMEOUT     = 30000;
    private static final String    DEFAULT_PRODUCER_TOPIC      = "apex-out";
    private static final int       DEFAULT_CONSUMER_POLL_TIME  = 100;
    private static final String[]  DEFAULT_CONSUMER_TOPIC_LIST = {"apex-in"};
    private static final String    DEFAULT_KEY_SERIALIZER      = "org.apache.superDooper.common.serialization.StringSerializer";
    private static final String    DEFAULT_VALUE_SERIALIZER    = "org.apache.superDooper.common.serialization.StringSerializer";
    private static final String    DEFAULT_KEY_DESERIALIZER    = "org.apache.superDooper.common.serialization.StringDeserializer";
    private static final String    DEFAULT_VALUE_DESERIALIZER  = "org.apache.superDooper.common.serialization.StringDeserializer";

    // Parameter property map tokens
    private static final String PROPERTY_BOOTSTRAP_SERVERS  = "bootstrap.servers";
    private static final String PROPERTY_ACKS               = "acks";
    private static final String PROPERTY_RETRIES            = "retries";
    private static final String PROPERTY_BATCH_SIZE         = "batch.size";
    private static final String PROPERTY_LINGER_TIME        = "linger.ms";
    private static final String PROPERTY_BUFFER_MEMORY      = "buffer.memory";
    private static final String PROPERTY_GROUP_ID           = "group.id";
    private static final String PROPERTY_ENABLE_AUTO_COMMIT = "enable.auto.commit";
    private static final String PROPERTY_AUTO_COMMIT_TIME   = "auto.commit.interval.ms";
    private static final String PROPERTY_SESSION_TIMEOUT    = "session.timeout.ms";
    private static final String PROPERTY_KEY_SERIALIZER     = "key.serializer";
    private static final String PROPERTY_VALUE_SERIALIZER   = "value.serializer";
    private static final String PROPERTY_KEY_DESERIALIZER   = "key.deserializer";
    private static final String PROPERTY_VALUE_DESERIALIZER = "value.deserializer";

    // superDooper carrier parameters
    private String   bootstrapServers  = DEFAULT_BOOTSTRAP_SERVERS;
    private String   acks              = DEFAULT_ACKS;
    private int      retries           = DEFAULT_RETRIES;
    private int      batchSize         = DEFAULT_BATCH_SIZE;
    private int      lingerTime        = DEFAULT_LINGER_TIME;
    private long     bufferMemory      = DEFAULT_BUFFER_MEMORY;
    private String   groupId           = DEFAULT_GROUP_ID;
    private boolean  enableAutoCommit  = DEFAULT_ENABLE_AUTO_COMMIT;
    private int      autoCommitTime    = DEFAULT_AUTO_COMMIT_TIME;
    private int      sessionTimeout    = DEFAULT_SESSION_TIMEOUT;
    private String   producerTopic     = DEFAULT_PRODUCER_TOPIC;
    private int      consumerPollTime  = DEFAULT_CONSUMER_POLL_TIME;
    private String[] consumerTopicList = DEFAULT_CONSUMER_TOPIC_LIST;
    private String   keySerializer     = DEFAULT_KEY_SERIALIZER;
    private String   valueSerializer   = DEFAULT_VALUE_SERIALIZER;
    private String   keyDeserializer   = DEFAULT_KEY_DESERIALIZER;
    private String   valueDeserializer = DEFAULT_VALUE_DESERIALIZER;

    /**
     * Constructor to create a file carrier technology parameters instance and register the instance with the parameter service.
     */
    public SuperDooperCarrierTechnologyParameters() {
        super(SuperDooperCarrierTechnologyParameters.class.getCanonicalName());
        ParameterService.registerParameters(SuperDooperCarrierTechnologyParameters.class, this);

        // Set the carrier technology properties for the FILE carrier technology
        this.setLabel("SUPER_DOOPER");
        this.setEventProducerPluginClass("com.ericsson.apex.service.engine.parameters.dummyclasses.SuperDooperEventProducer");
        this.setEventConsumerPluginClass("com.ericsson.apex.service.engine.parameters.dummyclasses.SuperDooperEventSubscriber");
    }

    /**
     * Gets the superDooper producer properties.
     *
     * @return the superDooper producer properties
     */
    public Properties getSuperDooperProducerProperties() {
        Properties superDooperProperties = new Properties();

        superDooperProperties.put(PROPERTY_BOOTSTRAP_SERVERS,  bootstrapServers);
        superDooperProperties.put(PROPERTY_ACKS,               acks);
        superDooperProperties.put(PROPERTY_RETRIES,            retries);
        superDooperProperties.put(PROPERTY_BATCH_SIZE,         batchSize);
        superDooperProperties.put(PROPERTY_LINGER_TIME,        lingerTime);
        superDooperProperties.put(PROPERTY_BUFFER_MEMORY,      bufferMemory);
        superDooperProperties.put(PROPERTY_KEY_SERIALIZER,     keySerializer);
        superDooperProperties.put(PROPERTY_VALUE_SERIALIZER,   valueSerializer);

        return superDooperProperties;
    }

    /**
     * Gets the superDooper consumer properties.
     *
     * @return the superDooper consumer properties
     */
    public Properties getSuperDooperConsumerProperties() {
        Properties superDooperProperties = new Properties();

        superDooperProperties.put(PROPERTY_BOOTSTRAP_SERVERS,  bootstrapServers);
        superDooperProperties.put(PROPERTY_GROUP_ID,           groupId);
        superDooperProperties.put(PROPERTY_ENABLE_AUTO_COMMIT, enableAutoCommit);
        superDooperProperties.put(PROPERTY_AUTO_COMMIT_TIME,   autoCommitTime);
        superDooperProperties.put(PROPERTY_SESSION_TIMEOUT,    sessionTimeout);
        superDooperProperties.put(PROPERTY_KEY_DESERIALIZER,   keyDeserializer);
        superDooperProperties.put(PROPERTY_VALUE_DESERIALIZER, valueDeserializer);

        return superDooperProperties;
    }

    /**
     * Gets the bootstrap servers.
     *
     * @return the bootstrap servers
     */
    public String getBootstrapServers() {
        return bootstrapServers;
    }

    /**
     * Gets the acks.
     *
     * @return the acks
     */
    public String getAcks() {
        return acks;
    }

    /**
     * Gets the retries.
     *
     * @return the retries
     */
    public int getRetries() {
        return retries;
    }

    /**
     * Gets the batch size.
     *
     * @return the batch size
     */
    public int getBatchSize() {
        return batchSize;
    }

    /**
     * Gets the linger time.
     *
     * @return the linger time
     */
    public int getLingerTime() {
        return lingerTime;
    }

    /**
     * Gets the buffer memory.
     *
     * @return the buffer memory
     */
    public long getBufferMemory() {
        return bufferMemory;
    }

    /**
     * Gets the group id.
     *
     * @return the group id
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * Checks if is enable auto commit.
     *
     * @return true, if checks if is enable auto commit
     */
    public boolean isEnableAutoCommit() {
        return enableAutoCommit;
    }

    /**
     * Gets the auto commit time.
     *
     * @return the auto commit time
     */
    public int getAutoCommitTime() {
        return autoCommitTime;
    }

    /**
     * Gets the session timeout.
     *
     * @return the session timeout
     */
    public int getSessionTimeout() {
        return sessionTimeout;
    }

    /**
     * Gets the producer topic.
     *
     * @return the producer topic
     */
    public String getProducerTopic() {
        return producerTopic;
    }

    /**
     * Gets the consumer poll time.
     *
     * @return the consumer poll time
     */
    public long getConsumerPollTime() {
        return consumerPollTime;
    }

    /**
     * Gets the consumer topic list.
     *
     * @return the consumer topic list
     */
    public Collection<String> getConsumerTopicList() {
        return Arrays.asList(consumerTopicList);
    }

    /**
     * Gets the key serializer.
     *
     * @return the key serializer
     */
    public String getKeySerializer() {
        return keySerializer;
    }

    /**
     * Gets the value serializer.
     *
     * @return the value serializer
     */
    public String getValueSerializer() {
        return valueSerializer;
    }

    /**
     * Gets the key deserializer.
     *
     * @return the key deserializer
     */
    public String getKeyDeserializer() {
        return keyDeserializer;
    }

    /**
     * Gets the value deserializer.
     *
     * @return the value deserializer
     */
    public String getValueDeserializer() {
        return valueDeserializer;
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.apps.uservice.parameters.ApexParameterValidator#validate()
     */
    @Override
    public String validate() {
        StringBuilder errorMessageBuilder = new StringBuilder();

        errorMessageBuilder.append(super.validate());

        if (bootstrapServers == null || bootstrapServers.trim().length() == 0) {
            errorMessageBuilder.append("  bootstrapServers not specified, must be specified as a string of form host:port\n");
        }

        if (acks == null || acks.trim().length() == 0) {
            errorMessageBuilder.append("  acks not specified, must be specified as a string with values [0|1|all]\n");
        }

        if (retries < 0) {
            errorMessageBuilder.append("  retries [" + retries + "] invalid, must be specified as retries >= 0\n");
        }

        if (batchSize < 0) {
            errorMessageBuilder.append("  batchSize [" + batchSize + "] invalid, must be specified as batchSize >= 0\n");
        }

        if (lingerTime < 0) {
            errorMessageBuilder.append("  lingerTime [" + lingerTime + "] invalid, must be specified as lingerTime >= 0\n");
        }

        if (bufferMemory < 0) {
            errorMessageBuilder.append("  bufferMemory [" + bufferMemory + "] invalid, must be specified as bufferMemory >= 0\n");
        }

        if (groupId == null || groupId.trim().length() == 0) {
            errorMessageBuilder.append("  groupId not specified, must be specified as a string\n");
        }

        if (autoCommitTime < 0) {
            errorMessageBuilder.append("  autoCommitTime [" + autoCommitTime + "] invalid, must be specified as autoCommitTime >= 0\n");
        }

        if (sessionTimeout < 0) {
            errorMessageBuilder.append("  sessionTimeout [" + sessionTimeout + "] invalid, must be specified as sessionTimeout >= 0\n");
        }

        if (producerTopic == null || producerTopic.trim().length() == 0) {
            errorMessageBuilder.append("  producerTopic not specified, must be specified as a string\n");
        }

        if (consumerPollTime < 0) {
            errorMessageBuilder.append("  consumerPollTime [" + consumerPollTime + "] invalid, must be specified as consumerPollTime >= 0\n");
        }

        if (consumerTopicList == null || consumerTopicList.length == 0) {
            errorMessageBuilder.append("  consumerTopicList not specified, must be specified as a list of strings\n");
        }

        for (String consumerTopic: consumerTopicList) {
            if (consumerTopic == null || consumerTopic.trim().length() == 0) {
                errorMessageBuilder.append("  invalid consumer topic \"" + consumerTopic + "\" specified on consumerTopicList, consumer topics must be specified as strings\n");
            }
        }

        if (keySerializer == null || keySerializer.trim().length() == 0) {
            errorMessageBuilder.append("  keySerializer not specified, must be specified as a string\n");
        }

        if (valueSerializer == null || valueSerializer.trim().length() == 0) {
            errorMessageBuilder.append("  valueSerializer not specified, must be specified as a string\n");
        }

        if (keyDeserializer == null || keyDeserializer.trim().length() == 0) {
            errorMessageBuilder.append("  keyDeserializer not specified, must be specified as a string\n");
        }

        if (valueDeserializer == null || valueDeserializer.trim().length() == 0) {
            errorMessageBuilder.append("  valueDeserializer not specified, must be specified as a string\n");
        }

        return errorMessageBuilder.toString();
    }
}
