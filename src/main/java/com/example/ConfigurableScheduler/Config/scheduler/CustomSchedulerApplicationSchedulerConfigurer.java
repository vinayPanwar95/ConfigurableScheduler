package com.example.ConfigurableScheduler.Config.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockManager;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CustomSchedulerApplicationSchedulerConfigurer  implements SchedulingConfigurer {

  private final LockManager lockManager;
  private final TaskScheduler taskScheduler;
  private final List<ScheduleTaskProvider> scheduleTaskProviders;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(taskScheduler);

        scheduleTaskProviders.forEach(scheduleTaskProvider -> {
            scheduleTaskProvider.getScheduledTask().forEach(scheduleTask -> {
                taskRegistrar.addTriggerTask(()->lockManager.executeWithLock(scheduleTask),scheduleTask.getTrigger());
            });
        });
    }
}
