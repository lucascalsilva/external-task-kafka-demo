package com.camunda.consulting.externaltaskkafkademo.consumer;

import com.camunda.consulting.externaltaskkafkademo.dto.EmailDto;
import com.camunda.consulting.externaltaskkafkademo.dto.EmailTaskDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.client.variable.ClientValues;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Component
public class EmailTopicKafkaConsumer {

    private final ExternalTaskService externalTaskService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "email-topic")
    public void completeEmailTask(EmailTaskDto emailTaskDto) throws JsonProcessingException {
        log.info("Completing task {}", emailTaskDto.getExternalTask().getId());
        log.info("Email Data: {}", emailTaskDto.toString());

        EmailDto emailDto = emailTaskDto.getEmailDto();
        emailDto.setEmailSent(true);
        emailDto.setEmailId(UUID.randomUUID().toString());
        emailDto.setEmailTimestamp(LocalDate.now().toString());

        VariableMap variables = Variables.createVariables();
        variables.putValueTyped("emailDto", ClientValues.jsonValue(objectMapper.writeValueAsString(emailDto)));

        externalTaskService.complete(emailTaskDto.getExternalTask(), variables);
    }
}
