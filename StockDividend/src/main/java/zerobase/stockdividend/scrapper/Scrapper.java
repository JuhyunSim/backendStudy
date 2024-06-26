package zerobase.stockdividend.scrapper;

import zerobase.stockdividend.model.Company;
import zerobase.stockdividend.model.ScrapedResult;

public interface Scrapper {
    ScrapedResult scrap(Company company);
    Company scrapCompanyByTicker(String ticker);
}
