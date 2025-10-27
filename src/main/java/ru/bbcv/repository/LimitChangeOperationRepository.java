package ru.bbcv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bbcv.entity.LimitChangeOperation;

@Repository
public interface LimitChangeOperationRepository extends JpaRepository<LimitChangeOperation, Long> {
}
