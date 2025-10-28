package ru.bbcv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bbcv.entity.Limit;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface LimitRepository extends JpaRepository<Limit, Long> {
    Optional<Limit> findByUserId(String userId);

    @Modifying
    @Query("""
            UPDATE limits l SET l.amount = :default_limit_amount - (SELECT COALESCE(SUM(o.reservation_amount), 0) 
            FROM limit_change_operation o 
            WHERE o.limit_id = l.id AND o.status = 'RESERVED')
            """)
    void refreshAllLimits(@Param("default_limit_amount") BigDecimal defaultLimitAmount);
}
