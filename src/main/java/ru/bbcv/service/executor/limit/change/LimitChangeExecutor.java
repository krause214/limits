package ru.bbcv.service.executor.limit.change;

import ru.bbcv.entity.LimitChangeOperation;
import ru.bbcv.entity.LimitChangeStatus;
import ru.bbcv.model.LimitChangeStage;
import ru.bbcv.model.LimitOperationExecutionResponseDto;
import ru.bbcv.model.LimitOperationRequestDto;

import java.util.List;

public abstract class LimitChangeExecutor {

    public void checkLimitOperationStatus(LimitChangeOperation paymentExecution) {
        if (List.of(LimitChangeStatus.DONE, LimitChangeStatus.ERROR).contains(paymentExecution.getStatus())) {
            throw new IllegalStateException(String.format("Заявка уже в финальном статусе - %s", paymentExecution.getStatus()));
        }
    }

    public abstract LimitOperationExecutionResponseDto executeStage(LimitOperationRequestDto requestDto);

    public abstract LimitChangeStage getLimitOperationStage();
}
