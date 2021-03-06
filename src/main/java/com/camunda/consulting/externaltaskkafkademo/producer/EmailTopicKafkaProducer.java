package com.camunda.consulting.externaltaskkafkademo.producer;

import com.camunda.consulting.externaltaskkafkademo.dto.EmailDto;
import com.camunda.consulting.externaltaskkafkademo.dto.EmailTaskDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.client.task.impl.ExternalTaskImpl;
import org.camunda.bpm.client.variable.value.JsonValue;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class EmailTopicKafkaProducer implements CommandLineRunner {

    private final KafkaTemplate<String, EmailTaskDto> kafkaTemplate;
    private final ExternalTaskClient externalTaskClient;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args){
        externalTaskClient.subscribe("email-sending").lockDuration(30000L).handler(this::handleTask).open();
    }

    private void handleTask(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        try {
            log.info("Fetching task {}", externalTask.getId());

            JsonValue emailDtoJson = externalTask.getVariableTyped("emailDto");
            EmailDto emailDto = objectMapper.readValue(emailDtoJson.getValue(), EmailDto.class);
            EmailTaskDto emailTaskDto = EmailTaskDto.builder().emailDto(emailDto).externalTask((ExternalTaskImpl) externalTask).build();

            kafkaTemplate.send("email-topic", emailTaskDto);

            log.info("Published task {} into kafka", externalTask.getId());

        }
        catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }

}
