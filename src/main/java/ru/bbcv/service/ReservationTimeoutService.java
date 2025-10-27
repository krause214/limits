package ru.bbcv.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.bbcv.entity.LimitChangeOperation;
import ru.bbcv.entity.LimitChangeStatus;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ReservationTimeoutService {

    private final LimitChangeOperationService limitChangeOperationService;
    private final LimitService limitService;
    private final ScheduledExecutorService scheduledExecutorService;

    @Value("${application.properties.operation-timeout-sec}")
    private Long timeoutSec;

    public ReservationTimeoutService(LimitChangeOperationService limitChangeOperationService, LimitService limitService) {
        this.limitChangeOperationService = limitChangeOperationService;
        this.limitService = limitService;
        this.scheduledExecutorService = Executors.newScheduledThreadPool(25);
    }

    public void schedule(Long operationId) {
        scheduledExecutorService.schedule(
                () -> {
                    LimitChangeOperation operation = limitChangeOperationService.getOperation(operationId);
                    if (operation != null && operation.getStatus().equals(LimitChangeStatus.RESERVED)) {
                        limitService.increaseLimit(operation.getLimitId(), operation.getReservationAmount());
                        operation.setStatus(LimitChangeStatus.TIMEOUT);
                    }
                    limitChangeOperationService.save(operation);
                }, timeoutSec, TimeUnit.SECONDS);

    }
}
