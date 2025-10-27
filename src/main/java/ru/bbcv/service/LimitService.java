package ru.bbcv.service;

import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.bbcv.entity.Limit;
import ru.bbcv.entity.User;
import ru.bbcv.repository.LimitRepository;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class LimitService {

    private final LimitRepository limitRepository;

    @Value("${application.properties.default-limit}")
    private BigDecimal defaultLimitAmount;

    public LimitService(LimitRepository limitRepository) {
        this.limitRepository = limitRepository;
    }

    public Limit createLimit(User user) {
        Limit limit = new Limit();
        limit.setAmount(defaultLimitAmount);
        limit.setUser(user);
        return limitRepository.save(limit);
    }

    @Transactional
    public void decreaseLimit(Long limitId, BigDecimal decreaseAmount) {
        Limit limit = Optional.ofNullable(getLimit(limitId))
                .orElseThrow(NoSuchElementException::new);
        limit.setAmount(limit.getAmount().subtract(decreaseAmount));
        limitRepository.save(limit);
    }

    @Transactional
    public void increaseLimit(Long limitId, BigDecimal increaseAmount) {
        Limit limit = Optional.ofNullable(getLimit(limitId))
                .orElseThrow(NoSuchElementException::new);
        limit.setAmount(limit.getAmount().add(increaseAmount));
        limitRepository.save(limit);
    }

    public void refreshAll() {
        List<Limit> limitList = limitRepository.findAll();
        for (Limit limit : limitList) {
            limit.setAmount(defaultLimitAmount);
            limitRepository.save(limit);
        }
    }

    @Nullable
    public Limit getLimit(Long id) {
        return limitRepository.findById(id)
                .orElse(null);
    }
}
