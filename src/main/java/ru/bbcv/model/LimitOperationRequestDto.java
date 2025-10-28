package ru.bbcv.model;

import java.math.BigDecimal;

public record LimitOperationRequestDto(Long processId, String username, BigDecimal requestedAmount) {
}
