package ru.bbcv.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.bbcv.repository.LimitRepository;

import java.math.BigDecimal;

@Component
public class RefreshLimitsScheduler {

    private final LimitRepository limitRepository;

    @Value("${application.properties.default-limit}")
    private BigDecimal defaultLimitAmount;

    public RefreshLimitsScheduler(LimitRepository limitRepository) {
        this.limitRepository = limitRepository;
    }

    @Transactional
    @Scheduled(cron = "${application.properties.refresh-limits-cron}")
    public void refreshLimits() {
        limitRepository.refreshAllLimits(defaultLimitAmount);
    }

}
