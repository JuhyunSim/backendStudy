package zerobase.stockdividend.web;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import zerobase.stockdividend.model.Company;
import zerobase.stockdividend.persist.entity.CompanyEntity;
import zerobase.stockdividend.service.CompanyService;

import java.util.List;

import static zerobase.stockdividend.model.constant.CacheKey.KEY_FINANCE;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final RedisCacheManager redisCacheManager;

    @GetMapping
    @PreAuthorize("hasRole('READ')")
    //spring boot에서 제공하는 paging 기술 활용(노출되는 데이터를 간략하게)
    public ResponseEntity<?> searchCompany(final Pageable pageable) {
        Page<CompanyEntity> companyEntityList
                = this.companyService.getAllCompany(pageable);
        return ResponseEntity.ok(companyEntityList);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autoComplete(@RequestParam String keyword) {
        List<String> autoCompleteKeywords = companyService.getAutoCompleteKeywords(keyword);
        return ResponseEntity.ok(autoCompleteKeywords);

//        List<String> companyNames
//                = companyService.getCompanyNamesByKeyword(keyword);
//        return ResponseEntity.ok(companyNames);
    }

    @PostMapping
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> addCompany(@RequestBody Company request) {
        String ticker = request.getTicker().trim();
        if (ObjectUtils.isEmpty(ticker)) {
            throw new RuntimeException("ticker is empty");
        }
        Company company = this.companyService.save(ticker);
        this.companyService.addAutoCompleteKeyword(company.getTicker());
        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/{ticker}")
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> deleteCompany(@PathVariable String ticker) {
        String companyName = companyService.deleteCompanyAndDividendByTicker(ticker);
        this.clearFinanceCache(companyName);
        return ResponseEntity.ok(companyName);
    }

    public void clearFinanceCache(String companyName) {
        this.redisCacheManager.getCache(KEY_FINANCE).evict(companyName);
    }
}
