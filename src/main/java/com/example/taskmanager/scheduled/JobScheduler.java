package com.example.taskmanager.scheduled;

import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JobScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobScheduler.class);

    private final Scheduler quartzScheduler;
    private final List<? extends QuartzScheduledJob> quartzScheduledJobs;

    public JobScheduler(final Scheduler quartzScheduler, final List<? extends QuartzScheduledJob> quartzScheduledJobs) {
        this.quartzScheduler = quartzScheduler;
        this.quartzScheduledJobs = quartzScheduledJobs;
    }

    @PostConstruct
    public void init() throws SchedulerException {
        for (final QuartzScheduledJob quartzJob : quartzScheduledJobs) {
            final String jobName = quartzJob.getJobName();
            final String triggerCron = quartzJob.triggerCron();

            LOGGER.info("Scheduling job with name '{}' using cron expression '{}'", jobName, triggerCron);

            final JobDetail jobDetail = JobBuilder.newJob(quartzJob.getClass())
                    .withIdentity(jobName)
                    .storeDurably()
                    .build();

            final Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(jobName + "Trigger")
                    .withSchedule(CronScheduleBuilder.cronSchedule(triggerCron))
                    .build();

            final Set<Trigger> triggers = Set.of(trigger);

            quartzScheduler.scheduleJob(jobDetail, triggers, true);
        }
    }
}
