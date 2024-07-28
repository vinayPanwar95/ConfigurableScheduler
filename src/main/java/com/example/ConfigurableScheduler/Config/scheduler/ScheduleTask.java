package com.example.ConfigurableScheduler.Config.scheduler;

import com.example.ConfigurableScheduler.domain.mandator.ScheduleJobRunner;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.core.LockConfiguration;
import org.springframework.scheduling.Trigger;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
@Data
public class ScheduleTask implements Runnable {

    private final Trigger trigger;
    private final String taskSchedulerName;
    private final String mandatorCode;
    private final Duration lockAtLeastFor;
    private final Duration lockAtMostFor;

    private final ScheduleJobRunner jobRunner;

    public Optional<LockConfiguration> getLockConfiguration(){

        LockConfiguration lockConfiguration = new LockConfiguration(Instant.now(), taskSchedulerName, lockAtMostFor,lockAtLeastFor );
        return Optional.of(lockConfiguration);
    }

    @Override
    public void run() {
        System.err.println("Scheduler started running for mandatorCode :" + mandatorCode);
        jobRunner.runScheduleJobForMandator(mandatorCode);
    }
}
