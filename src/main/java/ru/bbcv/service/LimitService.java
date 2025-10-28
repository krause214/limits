package ru.bbcv.service;

import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.bbcv.entity.Limit;
import ru.bbcv.entity.LimitChangeOperation;
import ru.bbcv.entity.LimitChangeStatus;
import ru.bbcv.repository.LimitChangeOperationRepository;
import ru.bbcv.repository.LimitRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class LimitService {

    private final LimitRepository limitRepository;
    private final LimitChangeOperationRepository operationRepository;

    @Value("${application.properties.default-limit}")
    private BigDecimal defaultLimitAmount;

    public LimitService(LimitRepository limitRepository, LimitChangeOperationRepository operationRepository) {
        this.limitRepository = limitRepository;
        this.operationRepository = operationRepository;
    }

    @Transactional
    public void decreaseLimit(Long limitId, BigDecimal decreaseAmount) {
        Limit limit = getLimit(limitId);
        limit.setAmount(limit.getAmount().subtract(decreaseAmount));
        limitRepository.save(limit);
    }

    @Transactional
    public void increaseLimit(Long limitId, BigDecimal increaseAmount) {
        Limit limit = getLimit(limitId);
        limit.setAmount(limit.getAmount().add(increaseAmount));
        if (limit.getAmount().compareTo(defaultLimitAmount) > 0) {
            limit.setAmount(defaultLimitAmount);
        }
        limitRepository.save(limit);
    }

    @Transactional
    public void refreshAll() {
        List<Limit> limitList = limitRepository.findAll();
        List<Limit> limitListToUpdate = new ArrayList<>();
        for (Limit limit : limitList) {
            BigDecimal reservedSum = operationRepository.findByLimitId(limit.getId())
                            .stream().filter(o -> LimitChangeStatus.RESERVED.equals(o.getStatus()))
                            .map(LimitChangeOperation::getReservationAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
            limit.setAmount(defaultLimitAmount.subtract(reservedSum));
            limitListToUpdate.add(limit);
        }
        limitRepository.saveAll(limitListToUpdate);
    }

    @NonNull
    public Limit getLimit(Long id) {
        return limitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Не найден лимит"));
    }

    @NonNull
    @Transactional
    public Limit getOrCreateLimit(String username) {
        Optional<Limit> limit = limitRepository.findByUsername(username);
        return limit.orElseGet(() -> {
                    Limit limitToCreate = new Limit();
                    limitToCreate.setUsername(username);
                    limitToCreate.setAmount(defaultLimitAmount);
                    return limitRepository.save(limitToCreate);
                }
        );
    }
}
