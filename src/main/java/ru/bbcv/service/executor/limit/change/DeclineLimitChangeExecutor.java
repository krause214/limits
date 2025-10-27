package ru.bbcv.service.executor.limit.change;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.bbcv.entity.LimitChangeOperation;
import ru.bbcv.entity.LimitChangeStatus;
import ru.bbcv.model.LimitChangeStage;
import ru.bbcv.model.LimitOperationExecutionResponseDto;
import ru.bbcv.model.LimitOperationRequestDto;
import ru.bbcv.service.LimitChangeOperationService;
import ru.bbcv.service.LimitService;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeclineLimitChangeExecutor extends LimitChangeExecutor {

    private final LimitChangeOperationService limitChangeOperationService;
    private final LimitService limitService;

    public DeclineLimitChangeExecutor(LimitChangeOperationService limitChangeOperationService, LimitService limitService) {
        this.limitChangeOperationService = limitChangeOperationService;
        this.limitService = limitService;
    }


    @Override
    @Transactional
    public LimitOperationExecutionResponseDto executeStage(LimitOperationRequestDto requestDto) {

        validateRequest(requestDto);

        LimitChangeOperation operation = limitChangeOperationService.getOperation(requestDto.processId());

        try {
            if (!operation.getStatus().equals(LimitChangeStatus.RESERVED)) {
                throw new IllegalStateException(String.format("Невозможно продолжить заявку в финальном статусе %s", operation.getStatus()));
            }
            limitService.increaseLimit(operation.getLimitId(), operation.getReservationAmount());
            operation.setStatus(LimitChangeStatus.DECLINED);
            limitChangeOperationService.save(operation);
            return mapToDto(operation);
        } catch (Exception e) {
            operation.setStatus(LimitChangeStatus.ERROR);
            limitChangeOperationService.save(operation);
            throw e;
        }
    }

    private LimitOperationExecutionResponseDto mapToDto(LimitChangeOperation operation) {
        return new LimitOperationExecutionResponseDto(operation.getId().toString(), operation.getStatus());
    }

    private void validateRequest(LimitOperationRequestDto requestDto) {
        List<String> errorList = new ArrayList<>();

        if (requestDto.processId() == null) {
            errorList.add("Передано пустое значение request.processId");
        }

        if (!errorList.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", errorList));
        }
    }

    @Override
    public LimitChangeStage getLimitOperationStage() {
        return LimitChangeStage.DECLINE;
    }
}
