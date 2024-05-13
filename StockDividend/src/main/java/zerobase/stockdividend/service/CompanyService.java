package zerobase.stockdividend.service;


import lombok.AllArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import zerobase.stockdividend.AutoComplete;
import zerobase.stockdividend.exception.impl.NoCompanyException;
import zerobase.stockdividend.model.Company;
import zerobase.stockdividend.model.ScrapedResult;
import zerobase.stockdividend.persist.entity.CompanyEntity;
import zerobase.stockdividend.persist.entity.DividendEntity;
import zerobase.stockdividend.persist.repository.CompanyRepository;
import zerobase.stockdividend.persist.repository.DividendRepository;
import zerobase.stockdividend.scrapper.YahooFinanceScrapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final YahooFinanceScrapper yahooFinanceScrapper;
    private final Trie trie;

    public Company save(String ticker) {
        //이미 저장된 데이터 있을 시, 저장 실패
        boolean exists = this.companyRepository.existsByTicker(ticker);
        if (exists) {
            throw new RuntimeException("ticker already exists -> " + ticker);
        }
        //저장된 데이터 없을 시, 저장
        return this.storeCompanyAndDividendByTicker(ticker);
    }

    private Company storeCompanyAndDividendByTicker(String ticker) {
        Company company = this.yahooFinanceScrapper.scrapCompanyByTicker(ticker);
        //scrap 실패
        if (ObjectUtils.isEmpty(company)) {
            throw new NoCompanyException();
        }
        //scrap 성공(찾는 회사가 존재함): company -> companyEntity, dividend -> dividendEntity
        ScrapedResult scrapedResult = this.yahooFinanceScrapper.scrap(company);

        //scrap 결과 반환
        CompanyEntity companyEntity
                = companyRepository.save(new CompanyEntity(company));

        List<DividendEntity> dividendEntityList
                = scrapedResult.getDividends().stream()
                .map(e -> new DividendEntity(companyEntity.getId(), e))
                .collect(Collectors.toList());

        dividendRepository.saveAll(dividendEntityList);
        return company;
    }

    public Page<CompanyEntity> getAllCompany(Pageable pageable) {
        return this.companyRepository.findAll(pageable);
    }

    public void addAutoCompleteKeyword(String keyword) {
        this.trie.put(keyword, null);
    }

    public List<String> getAutoCompleteKeywords(String keyword) {
        return (List<String>) this.trie.prefixMap(keyword)
                .keySet()
                .stream()
                .limit(10)
                .collect(Collectors.toList());
    }

    public void deleteAutoCompleteKeywords(String ticker) {
        this.trie.remove(ticker);
    }

    public List<String> getCompanyNamesByKeyword(String keyword) {
        Pageable limit = PageRequest.of(0, 10);
        Page<CompanyEntity> companyEntities
                = companyRepository
                .findByNameStartingWithIgnoreCase(keyword, limit);

        return companyEntities
                .stream().map(e -> e.getName()).collect(Collectors.toList());

    }

    public String deleteCompanyAndDividendByTicker(String ticker) {
        CompanyEntity company = this.companyRepository.findByTicker(ticker)
                .orElseThrow(NoCompanyException::new);

        dividendRepository.deleteAllByCompanyId(company.getId());
        companyRepository.delete(company);
        this.deleteAutoCompleteKeywords(ticker);
        return company.getName();
    }
}
