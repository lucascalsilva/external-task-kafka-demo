# Camunda External Task with Kafka
A sample application integrating Camunda External Tasks with Apache Kafka. 

## Show me the important parts!
![BPMN Process](/process.png)

## How does it work?
This application fetches and lock a task that belongs to the topic "email-sending" and then pushes this task to the Kafka topic "email-topic" ([EmailTopicKafkaProducer](src/main/java/com/camunda/consulting/externaltaskkafkademo/producer/EmailTopicKafkaProducer.java)).
This task is later consumed by another component, that finishes the external task ([EmailTopicKafkaConsumer](src/main/java/com/camunda/consulting/externaltaskkafkademo/consumer/EmailTopicKafkaConsumer.java)).

A very similar behaviour can be seen for the tasks in the topic "email-retrieving", which use the producer and consumer [EmailRetrievalTopicKafkaProducer](/Users/lucas.silva/java-idea-projects/camunda-projects/external-task-kafka-demo/src/main/java/com/camunda/consulting/externaltaskkafkademo/producer/EmailRetrievalTopicKafkaProducer.java) and, [EmailRetrievalTopicKafkaConsumer](src/main/java/com/camunda/consulting/externaltaskkafkademo/consumer/EmailRetrievalTopicKafkaConsumer.java) respectively.

## How to use it?
1. Start Apache Kafka in the default manner. Please check it here: https://kafka.apache.org/quickstart
2. Start Camunda and deploy the existing [process](/process.bpmn).
3. Star this application, you can use the following command:
```
mvn spring-boot:run
```

The topic configuration is done automatically by the class [KafkaTopicConfig](src/main/java/com/camunda/consulting/externaltaskkafkademo/config/KafkaTopicConfig.java).

You can then start an instance of the "EmailProcess" with the following payload: 

```
{
  "businessKey": "{{$guid}}",
  "variables": {
    "emailDto": {
      "value": "{\r\n\t\"from\": \"lucas.silva@camunda.com\",\r\n\t\"to\": \"lucas.silva@camunda.com\",\r\n\t\"subject\": \"Subject 1\",\r\n\t\"content\": \"Content 1\"\r\n}",
      "type": "Json"
    }
  }
}
```

The task belonging to the topics "email-sending" and "email-retrieving" will be picked up by the application. You should see some logs like below:
```
2020-09-10 14:09:29.082  INFO 13458 --- [criptionManager] c.c.c.e.p.EmailTopicKafkaProducer        : Fetching task 7a30dfbd-f35e-11ea-adb2-0242ac110002
2020-09-10 14:09:29.083  INFO 13458 --- [criptionManager] c.c.c.e.p.EmailTopicKafkaProducer        : Published task 7a30dfbd-f35e-11ea-adb2-0242ac110002 into kafka
2020-09-10 14:09:29.093  INFO 13458 --- [ntainer#0-0-C-1] c.c.c.e.c.EmailTopicKafkaConsumer        : Completing task 7a30dfbd-f35e-11ea-adb2-0242ac110002
```

## Environment Restrictions
This project uses Camunda Run 7.13.0.

## Known Limitations
