package ru.bbcv.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.bbcv.entity.Limit;
import ru.bbcv.entity.User;
import ru.bbcv.repository.LimitRepository;

import java.math.BigDecimal;
import java.util.List;

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

    public void decreaseLimit(Limit limit, BigDecimal decreaseAmount) {
        limit.setAmount(limit.getAmount().subtract(decreaseAmount));
        limitRepository.save(limit);
    }

    public void refreshAll() {
        List<Limit> limitList = limitRepository.findAll();
        for (Limit limit : limitList) {
            limit.setAmount(defaultLimitAmount);
            limitRepository.save(limit);
        }
    }
}
