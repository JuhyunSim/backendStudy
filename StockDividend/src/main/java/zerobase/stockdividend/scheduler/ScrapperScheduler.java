package zerobase.stockdividend.scheduler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zerobase.stockdividend.model.Company;
import zerobase.stockdividend.model.ScrapedResult;
import zerobase.stockdividend.persist.entity.CompanyEntity;
import zerobase.stockdividend.persist.entity.DividendEntity;
import zerobase.stockdividend.persist.repository.CompanyRepository;
import zerobase.stockdividend.persist.repository.DividendRepository;
import zerobase.stockdividend.scrapper.Scrapper;

import java.util.List;

import static zerobase.stockdividend.model.constant.CacheKey.KEY_FINANCE;

@Slf4j
@Component
@EnableCaching
public class ScrapperScheduler {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    private final Scrapper yahooFinanceScrapper;

    @Autowired
    public ScrapperScheduler(CompanyRepository companyRepository, DividendRepository dividendRepository, Scrapper yahooFinanceScrapper) {
        this.companyRepository = companyRepository;
        this.dividendRepository = dividendRepository;
        this.yahooFinanceScrapper = yahooFinanceScrapper;
    }

    @CacheEvict(value = KEY_FINANCE, allEntries = true)
    @Scheduled (cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling() {
        log.info("Starting yahoo finance scrapper");
        //회사 정보 가져오기
        List<CompanyEntity> companyEntityList = companyRepository.findAll();

        //회사별 배당금정보 스크래핑
        for(CompanyEntity companyEntity : companyEntityList) {
            log.info("Fetching yahoo finance scrapper... ->", companyEntity.getName());
            ScrapedResult scrapedResult = yahooFinanceScrapper.scrap(Company.builder()
                    .name(companyEntity.getName())
                    .ticker(companyEntity.getTicker())
                    .build()
            );

            //중복된 데이터 외 새로 업데이트된 dividend 데이터 저장
            scrapedResult.getDividends().stream()
                    .map(e -> new DividendEntity(companyEntity.getId(), e))
                    .forEach(e -> {
                        if (!dividendRepository.existsByCompanyIdAndDate(
                                e.getCompanyId(), e.getDate())
                        ) {
                            dividendRepository.save(e);
                            log.info("insert new dividend -> " + e.toString());
                        }
                    });
            //연속적으로 스크래핑하지 않기 위해
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }
    }
}
