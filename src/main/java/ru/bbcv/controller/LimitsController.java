package ru.bbcv.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bbcv.model.LimitOperationExecutionResponseDto;
import ru.bbcv.model.LimitOperationRequestDto;
import ru.bbcv.service.LimitsApplicationService;

@RestController
@RequestMapping("/api/limits")
public class LimitsController {

    private final LimitsApplicationService limitsApplicationService;

    public LimitsController(LimitsApplicationService limitsApplicationService) {
        this.limitsApplicationService = limitsApplicationService;
    }

    @PostMapping("/change-limit/execute")
    public LimitOperationExecutionResponseDto executePayment(@RequestBody LimitOperationRequestDto requestDto) {
        return limitsApplicationService.executeRequest(requestDto);
    }

}
