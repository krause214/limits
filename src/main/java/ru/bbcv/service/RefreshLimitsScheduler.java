package ru.bbcv.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RefreshLimitsScheduler {

    private final LimitService limitService;

    public RefreshLimitsScheduler(LimitService limitService) {
        this.limitService = limitService;
    }

    @Scheduled(cron = "${application.properties.refresh-limits-cron}")
    public void refreshLimits(){
        limitService.refreshAll();
    }

}
