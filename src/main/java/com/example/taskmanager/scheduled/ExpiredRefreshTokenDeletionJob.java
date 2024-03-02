package com.example.taskmanager.scheduled;

import java.util.List;
import java.util.stream.Collectors;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.taskmanager.model.persistance.RefreshToken;
import com.example.taskmanager.service.RefreshTokenService;

@Component
@DisallowConcurrentExecution
public class ExpiredRefreshTokenDeletionJob extends QuartzScheduledJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpiredRefreshTokenDeletionJob.class);

    @Value("${jobs.expired-refresh-token-deletion.name}")
    private String jobName;

    @Value("${jobs.expired-refresh-token-deletion.cron-expression}")
    private String cronExpression;

    private final RefreshTokenService refreshTokenService;

    public ExpiredRefreshTokenDeletionJob(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void executeInternal(JobExecutionContext jec) {
        List<Long> expiredTokenIds = refreshTokenService.getExpiredRegistrationTokens().stream()
                .map(RefreshToken::getId)
                .collect(Collectors.toList());
        LOGGER.info("Deleting a total of {} expired refresh tokens", expiredTokenIds.size());
        expiredTokenIds.forEach(refreshTokenService::deleteById);
    }

    public String getJobName() {
        return jobName;
    }

    public String triggerCron() {
        return cronExpression;
    }
}
