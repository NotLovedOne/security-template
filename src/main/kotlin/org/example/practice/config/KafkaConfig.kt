package org.example.practice.config 

@Configuration
class KafkaConfig {
    @Value("\${spring.kafka.bootstrap-servers}")
    private lateinit var bootstrapServers: String

    @Bean
    fun ProducerFactory(): ProducerFactory<String, String>{
        val props = mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.StringSerializer",
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.StringSerializer",
            ProducerConfig.ACKS_CONFIG to "all",
            ProducerConfig.RETRIES_CONFIG to 10,
            ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG to true
        )
        return DefaultKafkaProducerFactory(props)
    }


    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, String> {
        return KafkaTemplate(ProducerFactory())
    }

    @Bean
    fun taskEventTopic(): NewTopic = TopicBuilder.name("task-event-topic")
            .partitions(1)
            .replicas(1)
            .build()

    @Bean
    fun taskEventDlt(): NewTopic = TopicBuilder.name("task-event-dlt")
            .partitions(1)
            .replicas(1)
            .build()
}