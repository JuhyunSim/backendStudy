package zerobase.stockdividend.persist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import zerobase.stockdividend.persist.entity.DividendEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DividendRepository extends JpaRepository<DividendEntity, Long> {
    List<DividendEntity> findAllByCompanyId(long companyId);

    boolean existsByCompanyIdAndDate(Long companyId, LocalDateTime date);

    @Transactional
    void deleteAllByCompanyId(long companyId);
}
