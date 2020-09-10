package org.camunda.bpm.client.topic.impl;

import lombok.experimental.UtilityClass;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.impl.ExternalTaskClientImpl;
import org.camunda.bpm.client.task.ExternalTaskService;

@UtilityClass
public class ExternalTaskServiceUtil {

    public ExternalTaskService getExternalTaskService(ExternalTaskClient externalTaskClient){
        ExternalTaskClientImpl externalTaskClientImpl = (ExternalTaskClientImpl) externalTaskClient;
        return externalTaskClientImpl.getTopicSubscriptionManager().externalTaskService;
    }
}
