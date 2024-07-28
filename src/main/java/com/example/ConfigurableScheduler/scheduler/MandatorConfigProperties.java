package com.example.ConfigurableScheduler.scheduler;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties("com.example.custom-scheduler")
@Data
public class MandatorConfigProperties {
    boolean enabled;
    Map<String , JobScheduler> mandatorConfiguration;

    @Data
    public static class JobScheduler{
        String cron;
        String timeZone;
        String lockAtLeastFor;
        String lockAtMostFor;
    }
}
