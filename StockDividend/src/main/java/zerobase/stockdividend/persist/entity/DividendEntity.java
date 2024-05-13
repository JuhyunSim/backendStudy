package zerobase.stockdividend.persist.entity;


import jakarta.persistence.*;
import lombok.*;
import zerobase.stockdividend.model.Company;
import zerobase.stockdividend.model.Dividend;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "Dividend")
@Table( uniqueConstraints = {@UniqueConstraint(columnNames = {"companyId", "date"})})
public class DividendEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long companyId;

    private LocalDateTime date;

    private String dividend;

    public DividendEntity(Long companyId, Dividend dividend) {
        this.companyId = companyId;
        this.date = dividend.getDate();
        this.dividend = dividend.getDividend();
    }
}
