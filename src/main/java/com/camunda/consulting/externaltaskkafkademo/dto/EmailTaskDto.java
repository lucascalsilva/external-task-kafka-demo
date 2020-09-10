package com.camunda.consulting.externaltaskkafkademo.dto;

import lombok.*;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.impl.ExternalTaskImpl;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailTaskDto {

    private String to;
    private String from;
    private String subject;
    private String content;
    private ExternalTaskImpl externalTask;

}
