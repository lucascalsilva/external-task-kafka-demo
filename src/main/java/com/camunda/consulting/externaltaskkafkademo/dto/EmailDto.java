package com.camunda.consulting.externaltaskkafkademo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {

    private String to;
    private String from;
    private String subject;
    private String content;
    private String emailId;
    private Boolean emailSent;
    private Boolean emailRetrieved;
    private String emailTimestamp;
}
