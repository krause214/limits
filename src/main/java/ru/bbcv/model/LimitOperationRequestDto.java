package ru.bbcv.model;

import java.math.BigDecimal;

public record LimitOperationRequestDto(Long processId, LimitChangeStage stage, String username, BigDecimal requestedAmount) {
}
