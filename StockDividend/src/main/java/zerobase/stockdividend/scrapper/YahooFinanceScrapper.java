package zerobase.stockdividend.scrapper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import zerobase.stockdividend.model.Company;
import zerobase.stockdividend.model.Dividend;
import zerobase.stockdividend.model.ScrapedResult;
import zerobase.stockdividend.model.constant.Month;
import zerobase.stockdividend.persist.repository.CompanyRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class YahooFinanceScrapper implements Scrapper{
    private static final String URL
            = "https://finance.yahoo.com/quote/%s/" +
            "history?frequency=1mo&period1=%d&period2=%d";

    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?.tsrc=fin-srch";

    private static final long START_TIME = 60 * 60 * 24;
    private static CompanyRepository companyRepository;

    @Override
    public ScrapedResult scrap(Company company) {
        ScrapedResult scrapedResult = new ScrapedResult();
        scrapedResult.setCompany(company);
        try {
            long now = System.currentTimeMillis() / 1000;
            String url = String.format(URL, company.getTicker(), START_TIME, now);

            Connection connection = Jsoup.connect(url);
            Document document = connection.get();
            Elements parsedTable = document.getElementsByClass("table svelte-ewueuo");
            Element element = parsedTable.get(0);
            Element tbody = element.children().get(1);

            List<Dividend> dividends = new ArrayList<>();
            for(Element e : tbody.children()) {
                String text = e.text();
                if (!text.endsWith("Dividend")) {
                    continue;
                }

                String[] strings = text.split(" ");
                int month = Month.strToNum(strings[0]);
                int day = Integer.parseInt(strings[1].replace(",", ""));
                int year = Integer.parseInt(strings[2]);

                if (month < 0) {
                    throw new RuntimeException("Invalid month: " + strings[0]);
                }
                dividends.add(Dividend.builder()
                        .date(LocalDateTime.of(year, month, day, 0, 0))
                        .dividend(strings[3])
                        .build());
            }
            scrapedResult.setDividends(dividends);
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
        return scrapedResult;
    }

    @Override
    public Company scrapCompanyByTicker(String ticker) {
        try {
            String url = String.format(SUMMARY_URL, ticker);
            Document document = Jsoup.connect(url).get();
            Element titleEle = document.getElementsByTag("h1").get(1);
            String title = titleEle.text().trim();

            return Company.builder()
                    .ticker(ticker)
                    .name(title)
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
