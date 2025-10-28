package ru.bbcv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bbcv.entity.LimitChangeOperation;

import java.util.List;

@Repository
public interface LimitChangeOperationRepository extends JpaRepository<LimitChangeOperation, Long> {
    List<LimitChangeOperation> findByUserIdContaining(String username);
    List<LimitChangeOperation> findByLimitId(Long limitId);
}
