package ru.bbcv.service;

import org.springframework.stereotype.Service;
import ru.bbcv.model.LimitChangeStage;
import ru.bbcv.model.LimitOperationExecutionResponseDto;
import ru.bbcv.model.LimitOperationRequestDto;
import ru.bbcv.service.executor.limit.change.LimitChangeExecutor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LimitsApplicationService {

    private final Map<LimitChangeStage, LimitChangeExecutor> paymentExecutors;

    public LimitsApplicationService(List<LimitChangeExecutor> paymentExecutors) {
        this.paymentExecutors = paymentExecutors.stream()
                .collect(Collectors.toMap(LimitChangeExecutor::getLimitOperationStage,
                        executor -> executor));
    }

    public LimitOperationExecutionResponseDto executeRequest(LimitOperationRequestDto requestDto, LimitChangeStage stage) {
        return paymentExecutors.get(stage).executeStage(requestDto);
    }
}
