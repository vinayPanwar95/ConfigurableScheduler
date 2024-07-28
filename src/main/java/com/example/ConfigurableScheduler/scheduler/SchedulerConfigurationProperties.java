package com.example.ConfigurableScheduler.scheduler;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.example.scheduling")
@Data
public class SchedulerConfigurationProperties {

    private SchedulerProperties executor;

    @Data
    public static class SchedulerProperties{
        private int corePoolSize;
        private int maxPoolSize;
        private int queueCapacity;
        private String threadNamePrefix;
        private boolean waitForTasksToCompleteOnShutdown;
        private int awaitTerminationSeconds;
    }
}
