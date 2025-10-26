package ru.bbcv.service.executor.limit.change;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.bbcv.entity.LimitChangeOperation;
import ru.bbcv.entity.LimitChangeStatus;
import ru.bbcv.entity.User;
import ru.bbcv.model.LimitChangeStage;
import ru.bbcv.model.LimitOperationExecutionResponseDto;
import ru.bbcv.model.LimitOperationRequestDto;
import ru.bbcv.service.LimitChangeOperationService;
import ru.bbcv.service.LimitService;
import ru.bbcv.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class AcceptLimitChangeExecutor extends LimitChangeExecutor {

    private final LimitChangeOperationService limitChangeOperationService;
    private final UserService userService;
    private final LimitService limitService;

    public AcceptLimitChangeExecutor(LimitChangeOperationService limitChangeOperationService,
                                     UserService userService, LimitService limitService) {
        this.limitChangeOperationService = limitChangeOperationService;
        this.userService = userService;
        this.limitService = limitService;
    }


    @Override
    @Transactional
    public LimitOperationExecutionResponseDto executeStage(LimitOperationRequestDto requestDto) {

        validateRequest(requestDto);

        LimitChangeOperation operation = limitChangeOperationService.getOperation(requestDto.processId());

        try {
            String username = operation.getUsername();
            User user = userService.get(username);

            if (user.getLimit().getAmount().compareTo(operation.getReservationAmount()) < 0) {
                throw new IllegalStateException("Не удалось выполнить операцию - лимит превышен");
            }

            limitService.decreaseLimit(user.getLimit(),
                    operation.getReservationAmount());

            operation.setStatus(LimitChangeStatus.DONE);
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
        return LimitChangeStage.ACCEPT;
    }
}
