package com.camunda.consulting.externaltaskkafkademo.producer;

import com.camunda.consulting.externaltaskkafkademo.dto.EmailTaskDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.client.task.impl.ExternalTaskImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class EmailTopicKafkaProducer implements CommandLineRunner {

    private final KafkaTemplate<String, EmailTaskDto> kafkaTemplate;
    private final ExternalTaskClient externalTaskClient;

    @Override
    public void run(String... args){
        externalTaskClient.subscribe("email-sending").handler(this::handleTask).open();
    }

    private void handleTask(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        log.info("Fetching task {}", externalTask.getId());

        String to = externalTask.getVariable("to");
        String from = externalTask.getVariable("from");
        String subject = externalTask.getVariable("subject");
        String content = externalTask.getVariable("content");

        EmailTaskDto emailDto = EmailTaskDto.builder().to(to).from(from).subject(subject).content(content)
                .externalTask((ExternalTaskImpl) externalTask).build();

        kafkaTemplate.send("email-topic", emailDto);

        log.info("Published task {} into kafka", externalTask.getId());
    }

}
