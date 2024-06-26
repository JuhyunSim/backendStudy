package zerobase.stockdividend.persist.entity;


import jakarta.persistence.*;
import lombok.*;
import zerobase.stockdividend.model.Company;

@Entity(name = "Company")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String ticker;
    private String name;

    public CompanyEntity(Company company) {
        this.ticker = company.getTicker();
        this.name = company.getName();
    }
}
