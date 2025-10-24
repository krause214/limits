package ru.bbcv.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bbcv.model.LimitOperationExecutionResponseDto;
import ru.bbcv.model.LimitOperationRequestDto;
import ru.bbcv.service.LimitsService;

@RestController
@RequestMapping("/api/limits")
public class LimitsController {

    private final LimitsService limitsService;

    public LimitsController(LimitsService limitsService) {
        this.limitsService = limitsService;
    }

    @PostMapping("/change-limit/execute")
    public LimitOperationExecutionResponseDto executePayment(@RequestBody LimitOperationRequestDto requestDto) {
        return limitsService.executeRequest(requestDto);
    }

}
