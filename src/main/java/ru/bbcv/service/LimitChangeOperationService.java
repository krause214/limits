package ru.bbcv.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.bbcv.entity.Limit;
import ru.bbcv.entity.LimitChangeOperation;
import ru.bbcv.repository.LimitChangeOperationRepository;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
public class LimitChangeOperationService {

    public LimitChangeOperationService(LimitChangeOperationRepository limitChangeOperationRepository) {
        this.limitChangeOperationRepository = limitChangeOperationRepository;
    }

    private final LimitChangeOperationRepository limitChangeOperationRepository;

    @Transactional
    public LimitChangeOperation createProcess(Limit limit, BigDecimal requestedAmount) {
        LimitChangeOperation limitChangeOperation = new LimitChangeOperation();
        limitChangeOperation.setLimitId(limit.getId());
        limitChangeOperation.setUsername(limit.getUsername());
        limitChangeOperation.setReservationAmount(requestedAmount);
        limitChangeOperationRepository.save(limitChangeOperation);
        return limitChangeOperation;
    }

    @Transactional
    public void save(LimitChangeOperation limitChangeOperation) {
        limitChangeOperationRepository.save(limitChangeOperation);
    }

    public LimitChangeOperation getOperation(Long id) {
        return limitChangeOperationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("Не найдено зарезервированной операции с идентификатором %s", id)));
    }
}
