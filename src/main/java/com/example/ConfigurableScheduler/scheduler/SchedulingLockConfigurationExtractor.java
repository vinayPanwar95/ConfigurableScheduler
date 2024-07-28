package com.example.ConfigurableScheduler.scheduler;

import com.example.ConfigurableScheduler.Config.scheduler.ScheduleTask;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockConfigurationExtractor;

import java.util.Optional;

public class SchedulingLockConfigurationExtractor implements LockConfigurationExtractor {
    @Override
    public Optional<LockConfiguration> getLockConfiguration(Runnable runnable) {
       if(runnable instanceof ScheduleTask){
           return ((ScheduleTask) runnable).getLockConfiguration();
       }
        return Optional.empty();
    }
}
