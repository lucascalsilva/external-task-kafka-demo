package com.camunda.consulting.externaltaskkafkademo.config;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.client.topic.impl.ExternalTaskServiceUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ExternalTaskClientConfig {

    private final ExternalTaskClientSettings externalTaskClientSettings;

    @Bean
    public ExternalTaskClient externalTaskClient() {
        return ExternalTaskClient.create()
                .baseUrl(externalTaskClientSettings.getBaseUrl())
                .workerId(externalTaskClientSettings.getWorkerId())
                .asyncResponseTimeout(externalTaskClientSettings.getAsyncResponseTimeout())
                .build();
    }

    @Bean
    public ExternalTaskService externalTaskService(){
        return ExternalTaskServiceUtil.getExternalTaskService(externalTaskClient());
    }
}