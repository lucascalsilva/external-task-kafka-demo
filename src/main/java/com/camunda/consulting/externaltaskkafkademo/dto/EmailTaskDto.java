package com.camunda.consulting.externaltaskkafkademo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.impl.ExternalTaskImpl;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailTaskDto {

    private EmailDto emailDto;
    private ExternalTaskImpl externalTask;

}
