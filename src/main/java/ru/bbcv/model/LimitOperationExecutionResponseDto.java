package ru.bbcv.model;

import ru.bbcv.entity.LimitChangeStatus;

public record LimitOperationExecutionResponseDto(String operationId, LimitChangeStatus status) {

}
