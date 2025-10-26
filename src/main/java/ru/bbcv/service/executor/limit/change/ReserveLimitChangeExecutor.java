package ru.bbcv.service.executor.limit.change;

import ch.qos.logback.core.util.StringUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.bbcv.entity.Limit;
import ru.bbcv.entity.LimitChangeOperation;
import ru.bbcv.entity.LimitChangeStatus;
import ru.bbcv.entity.User;
import ru.bbcv.model.LimitChangeStage;
import ru.bbcv.model.LimitOperationExecutionResponseDto;
import ru.bbcv.model.LimitOperationRequestDto;
import ru.bbcv.service.LimitChangeOperationService;
import ru.bbcv.service.LimitService;
import ru.bbcv.service.UserService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReserveLimitChangeExecutor extends LimitChangeExecutor {

    private final LimitChangeOperationService limitChangeOperationService;
    private final LimitService limitService;
    private final UserService userService;

    public ReserveLimitChangeExecutor(LimitChangeOperationService limitChangeOperationService,
                                      LimitService limitService,
                                      UserService userService) {
        this.limitChangeOperationService = limitChangeOperationService;
        this.limitService = limitService;
        this.userService = userService;
    }


    @Override
    @Transactional
    public LimitOperationExecutionResponseDto executeStage(LimitOperationRequestDto requestDto) {

        validateRequest(requestDto);

        LimitChangeOperation limitChangeOperation = new LimitChangeOperation();
        try {
            String username = requestDto.username();
            User user = userService.getOrCreateUser(username);
            limitChangeOperation = limitChangeOperationService.createProcess(user, requestDto.requestedAmount());
            Optional.ofNullable(user.getLimit())
                    .map(Limit::getAmount)
                    .orElseThrow(() -> new IllegalStateException("У данного пользователя нет зарегистрированного лимита"));
            if (user.getLimit().getAmount().compareTo(requestDto.requestedAmount()) < 0) {
                throw new IllegalStateException(String.format("Запрошенная сумма превышает допустимый лимит - %s", user.getLimit().getAmount()));
            }
            limitChangeOperation.setStatus(LimitChangeStatus.RESERVED);
            limitChangeOperationService.save(limitChangeOperation);
            return mapToDto(limitChangeOperation);
        } catch (Exception e) {
            limitChangeOperation.setStatus(LimitChangeStatus.ERROR);
            limitChangeOperationService.save(limitChangeOperation);
            throw e;
        }
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
