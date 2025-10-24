package ru.bbcv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bbcv.entity.Limit;

@Repository
public interface LimitRepository extends JpaRepository<Limit, Long> {
}
