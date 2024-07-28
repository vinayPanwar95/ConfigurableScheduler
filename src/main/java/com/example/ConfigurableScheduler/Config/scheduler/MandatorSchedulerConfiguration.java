package com.example.ConfigurableScheduler.Config.scheduler;

import com.example.ConfigurableScheduler.scheduler.MandatorConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Configuration
@RequiredArgsConstructor
@Conditional(MandatorSchedulerConfiguration.ScheduleCondition.class)
@EnableConfigurationProperties(MandatorConfigProperties.class)
public class MandatorSchedulerConfiguration {

    static class ScheduleCondition extends SpringBootCondition{
        private static final String SCHEDULER_ENABLE_FLAG = "com.example.custom-scheduler.enabled";

        @Override
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            if(isSchedulerEnabled(context.getEnvironment())){
                return ConditionOutcome.match("Scheduler is enabled");
            }
            System.err.println("scheduler is disabled , please check property : "+ SCHEDULER_ENABLE_FLAG);
            return ConditionOutcome.noMatch("Scheduler is disabled");
        }

        private boolean isSchedulerEnabled(Environment environment) {
            return environment.getProperty(SCHEDULER_ENABLE_FLAG, Boolean.class, Boolean.FALSE);
        }
    }

}
