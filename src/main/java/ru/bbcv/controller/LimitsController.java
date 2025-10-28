package ru.bbcv.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bbcv.model.LimitChangeStage;
import ru.bbcv.model.LimitOperationExecutionResponseDto;
import ru.bbcv.model.LimitOperationRequestDto;
import ru.bbcv.service.LimitsApplicationService;

@RestController
@RequestMapping("/v1/api/limits")
public class LimitsController {

    private final LimitsApplicationService limitsApplicationService;

    public LimitsController(LimitsApplicationService limitsApplicationService) {
        this.limitsApplicationService = limitsApplicationService;
    }

    @PostMapping("/change-limit/reservation")
    public LimitOperationExecutionResponseDto executeReservation(@RequestBody LimitOperationRequestDto requestDto) {
        return limitsApplicationService.executeRequest(requestDto, LimitChangeStage.RESERVE);
    }

    @PostMapping("/change-limit/accept")
    public LimitOperationExecutionResponseDto executeAccept(@RequestBody LimitOperationRequestDto requestDto) {
        return limitsApplicationService.executeRequest(requestDto, LimitChangeStage.ACCEPT);
    }

    @PostMapping("/change-limit/decline")
    public LimitOperationExecutionResponseDto executeDecline(@RequestBody LimitOperationRequestDto requestDto) {
        return limitsApplicationService.executeRequest(requestDto, LimitChangeStage.DECLINE);
    }

}
