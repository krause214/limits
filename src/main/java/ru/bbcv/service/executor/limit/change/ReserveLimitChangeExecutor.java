package ru.bbcv.service.executor.limit.change;

import ch.qos.logback.core.util.StringUtil;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.bbcv.entity.Limit;
import ru.bbcv.entity.LimitChangeOperation;
import ru.bbcv.entity.LimitChangeStatus;
import ru.bbcv.model.LimitChangeStage;
import ru.bbcv.model.LimitOperationExecutionResponseDto;
import ru.bbcv.model.LimitOperationRequestDto;
import ru.bbcv.service.LimitChangeOperationService;
import ru.bbcv.service.LimitService;
import ru.bbcv.service.ReservationTimeoutService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReserveLimitChangeExecutor extends LimitChangeExecutor {

    private final LimitChangeOperationService limitChangeOperationService;
    private final LimitService limitService;
    private final ApplicationContext applicationContext;


    public ReserveLimitChangeExecutor(LimitChangeOperationService limitChangeOperationService,
                                      LimitService limitService,
                                      ApplicationContext applicationContext) {
        this.limitChangeOperationService = limitChangeOperationService;
        this.limitService = limitService;
        this.applicationContext = applicationContext;
    }


    @Override
    @Transactional
    public LimitOperationExecutionResponseDto executeStage(LimitOperationRequestDto requestDto) {

        validateRequest(requestDto);

        LimitChangeOperation limitChangeOperation;
        String username = requestDto.username();
        Limit limit = limitService.getOrCreateLimit(username);
        limitChangeOperation = limitChangeOperationService.createProcess(limit, requestDto.requestedAmount());
        try {
            if (limit.getAmount().compareTo(requestDto.requestedAmount()) < 0) {
                throw new IllegalStateException(String.format("Запрошенная сумма превышает допустимый лимит - %s", limit.getAmount()));
            }
            limitService.decreaseLimit(limit.getId(), limitChangeOperation.getReservationAmount());
            scheduleOperation(limitChangeOperation.getId());
            limitChangeOperation.setStatus(LimitChangeStatus.RESERVED);
            limitChangeOperationService.save(limitChangeOperation);
            return mapToDto(limitChangeOperation);
        } catch (Exception e) {
            limitChangeOperation.setStatus(LimitChangeStatus.ERROR);
            limitChangeOperationService.save(limitChangeOperation);
            throw e;
        }
    }

    private void scheduleOperation(Long id) {
        ReservationTimeoutService timeoutService = applicationContext.getBean(ReservationTimeoutService.class);
        timeoutService.schedule(id);
    }

    private LimitOperationExecutionResponseDto mapToDto(LimitChangeOperation limitChangeOperation) {
        return new LimitOperationExecutionResponseDto(limitChangeOperation.getId().toString(), limitChangeOperation.getStatus());
    }

    private void validateRequest(LimitOperationRequestDto requestDto) {
        List<String> errorList = new ArrayList<>();

        if (StringUtil.isNullOrEmpty(requestDto.username())) {
            errorList.add("Передано пустое значение request.username");
        }
        if (requestDto.requestedAmount() == null
                || requestDto.requestedAmount().compareTo(BigDecimal.ZERO) <= 0) {
            errorList.add(String.format("Передано неверное значение request.requestedAmount = %s", requestDto.requestedAmount()));
        }

        if (!errorList.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", errorList));
        }
    }

    @Override
    public LimitChangeStage getLimitOperationStage() {
        return LimitChangeStage.RESERVE;
    }

}
