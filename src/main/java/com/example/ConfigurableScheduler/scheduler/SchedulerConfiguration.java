package com.example.ConfigurableScheduler.scheduler;

import com.example.ConfigurableScheduler.Config.scheduler.CustomSchedulerApplicationSchedulerConfigurer;
import com.example.ConfigurableScheduler.Config.scheduler.ScheduleTaskProvider;
import com.example.ConfigurableScheduler.domain.mandator.ScheduleJobRunner;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.core.DefaultLockManager;
import net.javacrumbs.shedlock.core.LockManager;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT15M")
@EnableConfigurationProperties(SchedulerConfigurationProperties.class)
@RequiredArgsConstructor
public class SchedulerConfiguration {

 private final SchedulerConfigurationProperties schedulerConfigurationProperties;

    @Bean
    public LockProvider lockProvider(DataSource dataSource){
        System.err.println("Configuring shedlock lock provider");
        return new JdbcTemplateLockProvider(JdbcTemplateLockProvider.Configuration.builder()
                .withJdbcTemplate(new JdbcTemplate((dataSource)))
                .usingDbTime()
                .build());
    }

    @Bean
    public LockManager lockManager(LockProvider lockProvider){
        return new DefaultLockManager(lockProvider,new SchedulingLockConfigurationExtractor());
    }

    @Bean
    public ScheduleTaskProvider scheduleTaskProvider(MandatorConfigProperties mandatorConfigProperties){

        ScheduleJobRunner runner = new ScheduleJobRunner();

        return new ScheduleTaskProvider(mandatorConfigProperties,runner );
    }

    @Bean
    public ThreadPoolTaskScheduler taskScheduler(){
        SchedulerConfigurationProperties.SchedulerProperties schedulerProperties = schedulerConfigurationProperties.getExecutor();

        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(schedulerProperties.getCorePoolSize());
        threadPoolTaskScheduler.setThreadNamePrefix(schedulerProperties.getThreadNamePrefix());
        threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(schedulerProperties.isWaitForTasksToCompleteOnShutdown());
        threadPoolTaskScheduler.setAwaitTerminationSeconds(schedulerProperties.getAwaitTerminationSeconds());
        return threadPoolTaskScheduler;
    }

    @Bean
    public CustomSchedulerApplicationSchedulerConfigurer customSchedulerApplicationSchedulerConfigurer(LockManager lockManager,
                                                                                                       TaskScheduler taskScheduler,
                                                                                                       List<ScheduleTaskProvider> scheduleTaskProviders){
        return new CustomSchedulerApplicationSchedulerConfigurer(lockManager,taskScheduler,scheduleTaskProviders);
    }
}
