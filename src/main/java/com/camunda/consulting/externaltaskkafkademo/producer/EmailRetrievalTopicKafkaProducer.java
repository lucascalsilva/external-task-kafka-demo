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

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailRetrievalTopicKafkaProducer implements CommandLineRunner {

    private final KafkaTemplate<String, EmailTaskDto> kafkaTemplate;
    private final ExternalTaskClient externalTaskClient;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        externalTaskClient.subscribe("email-retrieving").lockDuration(30000L).handler(this::handleTask).open();
    }

    private void handleTask(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        try {
            log.info("Fetching task {}", externalTask.getId());

            JsonValue emailDtoJson = externalTask.getVariableTyped("emailDto");
            EmailDto emailDto = objectMapper.readValue(emailDtoJson.getValue(), EmailDto.class);
            EmailTaskDto emailTaskDto = EmailTaskDto.builder().emailDto(emailDto).externalTask((ExternalTaskImpl) externalTask).build();

            kafkaTemplate.send("email-retrieving-topic", emailTaskDto);

            log.info("Published task {} into kafka", externalTask.getId());
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }
}
