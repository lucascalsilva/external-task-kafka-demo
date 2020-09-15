package com.camunda.consulting.externaltaskkafkademo.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.UUID;

@ConfigurationProperties(prefix = "camunda.client")
@Getter
@Setter
@Component
public class ExternalTaskClientSettings {

    private Long asyncResponseTimeout = 60000L;
    private String baseUrl = "http://localhost:8080/engine-rest";
    private String workerId = UUID.randomUUID().toString();

}
