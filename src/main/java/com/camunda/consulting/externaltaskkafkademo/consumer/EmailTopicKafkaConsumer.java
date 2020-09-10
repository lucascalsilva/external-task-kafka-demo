package com.camunda.consulting.externaltaskkafkademo.consumer;

import com.camunda.consulting.externaltaskkafkademo.dto.EmailTaskDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class EmailTopicKafkaConsumer {

    private final ExternalTaskService externalTaskService;

    @KafkaListener(topics = "email-topic")
    public void completeEmailTask(EmailTaskDto emailDto){
        log.info("Completing task {}", emailDto.getExternalTask().getId());

        externalTaskService.complete(emailDto.getExternalTask());
    }
}
