package ru.bbcv.service.executor.limit.change;

import ru.bbcv.model.LimitChangeStage;
import ru.bbcv.model.LimitOperationExecutionResponseDto;
import ru.bbcv.model.LimitOperationRequestDto;

public class ReserveLimitChangeExecutor extends LimitChangeExecutor {
    @Override
    public LimitOperationExecutionResponseDto executeStage(LimitOperationRequestDto requestDto) {
        return null;
    }

    @Override
    public LimitChangeStage getLimitOperationStage() {
        return null;
    }
}
