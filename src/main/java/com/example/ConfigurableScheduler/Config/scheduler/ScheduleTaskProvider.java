package com.example.ConfigurableScheduler.Config.scheduler;

import com.example.ConfigurableScheduler.scheduler.MandatorConfigProperties;
import com.example.ConfigurableScheduler.domain.mandator.ScheduleJobRunner;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.support.CronTrigger;

import java.time.Duration;
import java.util.*;

import static org.springframework.scheduling.annotation.Scheduled.CRON_DISABLED;

@RequiredArgsConstructor
public class ScheduleTaskProvider {

    private final MandatorConfigProperties mandatorConfigProperties;
    private static final String SCHEDULER_NAME = "housekeepingScheduler%s";
    private final ScheduleJobRunner scheduleJobRunner;
    public List<ScheduleTask> getScheduledTask(){
        List<ScheduleTask> scheduleTasks = new ArrayList<>();

        for (Map.Entry<String, MandatorConfigProperties.JobScheduler> entry : mandatorConfigProperties.getMandatorConfiguration().entrySet()) {
            String mandatorCode = entry.getKey();
            MandatorConfigProperties.JobScheduler jobScheduler = entry.getValue();

            if (CRON_DISABLED.equals(jobScheduler.getCron())) {
                System.err.println("Schedular is disabled for mondator : " + mandatorCode);
                continue;
            }
            scheduleTasks.add(getScheduleRunnable(jobScheduler, mandatorCode));

        }
        return  scheduleTasks;
    }

    private ScheduleTask getScheduleRunnable(MandatorConfigProperties.JobScheduler jobScheduler, String mandatorCode) {

        CronTrigger cronTrigger = getJonScheduledCron(jobScheduler);
        return new ScheduleTask(
                cronTrigger,
                String.format(SCHEDULER_NAME,mandatorCode),
                mandatorCode,
                Duration.parse(jobScheduler.getLockAtLeastFor()),
                Duration.parse(jobScheduler.getLockAtMostFor()),
                scheduleJobRunner
        );
    }

    private CronTrigger getJonScheduledCron(MandatorConfigProperties.JobScheduler jobScheduler) {
        return new CronTrigger(jobScheduler.getCron(), TimeZone.getTimeZone(jobScheduler.getTimeZone()));
    }
}
