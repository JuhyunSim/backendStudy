package zerobase.stockdividend.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import zerobase.stockdividend.exception.impl.NoCompanyException;
import zerobase.stockdividend.model.Company;
import zerobase.stockdividend.model.Dividend;
import zerobase.stockdividend.model.ScrapedResult;
import zerobase.stockdividend.persist.entity.CompanyEntity;
import zerobase.stockdividend.persist.entity.DividendEntity;
import zerobase.stockdividend.persist.repository.CompanyRepository;
import zerobase.stockdividend.persist.repository.DividendRepository;

import java.util.ArrayList;
import java.util.List;

import static zerobase.stockdividend.model.constant.CacheKey.KEY_FINANCE;

@Service
@AllArgsConstructor
@Slf4j
public class FinanceService {
    private final DividendRepository dividendRepository;
    private final CompanyRepository companyRepository;

    @Cacheable(key = "#companyName", value = KEY_FINANCE)
    public ScrapedResult getDividendAndCompany(String companyName) {
        log.info("search company -> " + companyName);

        //1. company 정보 조회
        CompanyEntity company
                = companyRepository.findByName(companyName)
                .orElseThrow(NoCompanyException::new);
        //2. dividend 정보 조회
        List<DividendEntity> dividendEntityList
                = dividendRepository.findAllByCompanyId(company.getId());

        //3. 결과값 변환
        List<Dividend> dividendList = new ArrayList<>();
        for(DividendEntity dividendEntity : dividendEntityList) {
            dividendList.add(Dividend.builder()
                    .date(dividendEntity.getDate())
                    .dividend(dividendEntity.getDividend())
                    .build()
            );
        }

        return new ScrapedResult(Company.builder()
        .ticker(company.getTicker())
        .name(company.getName())
        .build(), dividendList);
    }
}
