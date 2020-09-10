# Camunda External Task with Kafka
A sample application integrating Camunda External Tasks with Apache Kafka. 

## Show me the important parts!
![BPMN Process](/process.png)

## How does it work?
This application fetches and lock a task that belongs to the topic "email-sending" and then pushes this task to the Kafka topic "email-topic" ([EmailTopicKafkaProducer](src/main/java/com/camunda/consulting/externaltaskkafkademo/producer/EmailTopicKafkaProducer.java)).

This task is later consumed by another component, that finishes the external task ([EmailTopicKafkaConsumer](src/main/java/com/camunda/consulting/externaltaskkafkademo/consumer/EmailTopicKafkaConsumer.java)).

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
    "from": {
      "value": "lucas.silva@camunda.com",
      "type": "String"
    },
    "to": {
      "value": "lucas.silva@camunda.com",
      "type": "String"
    },
    "subject": {
      "value": "Subject 1",
      "type": "String"
    },
    "content": {
      "value": "Content 1",
      "type": "String"
    }
  }
}
```

The task belonging to the "email-sending" topic will be picked up by the application. You should see some logs like below:
```
2020-09-10 14:09:29.082  INFO 13458 --- [criptionManager] c.c.c.e.p.EmailTopicKafkaProducer        : Fetching task 7a30dfbd-f35e-11ea-adb2-0242ac110002
2020-09-10 14:09:29.083  INFO 13458 --- [criptionManager] c.c.c.e.p.EmailTopicKafkaProducer        : Published task 7a30dfbd-f35e-11ea-adb2-0242ac110002 into kafka
2020-09-10 14:09:29.093  INFO 13458 --- [ntainer#0-0-C-1] c.c.c.e.c.EmailTopicKafkaConsumer        : Completing task 7a30dfbd-f35e-11ea-adb2-0242ac110002
```

## Environment Restrictions
This project uses Camunda Run 7.13.0.

## Known Limitations
