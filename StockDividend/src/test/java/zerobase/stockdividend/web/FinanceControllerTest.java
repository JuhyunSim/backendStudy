package zerobase.stockdividend.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import zerobase.stockdividend.model.Company;
import zerobase.stockdividend.model.Dividend;
import zerobase.stockdividend.model.ScrapedResult;
import zerobase.stockdividend.service.FinanceService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FinanceController.class)
@ExtendWith(MockitoExtension.class)
class FinanceControllerTest {
    @MockBean
    private FinanceService financeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private FinanceController financeController;

    @Test
    public void successGetDividend() throws Exception {
        // Given
        Company company = Company.builder()
                .name("3m")
                .ticker("mmm")
                .build();
        Dividend dividend1 = Dividend.builder()
                .date(LocalDateTime.of(1, 1, 1, 0, 0))
                .dividend("10.0")
                .build();

        Dividend dividend2 = Dividend.builder()
                .date(LocalDateTime.of(2, 2, 2, 0, 0))
                .dividend("20.0")
                .build();
        List<Dividend> dividends = List.of(dividend1, dividend2);

        given(financeService.getDividendAndCompany("3m"))
                .willReturn(ScrapedResult.builder()
                        .company(Company.builder()
                                .ticker("mmm")
                                .name("3m")
                                .build())
                        .dividends(List.of(dividend1, dividend2))
                        .build());
        // When
        // Then
        verify(financeService, times(1)).getDividendAndCompany("3m");
        mockMvc.perform(get("/finance/dividend")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ScrapedResult(company, dividends))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dividends[0].dividend").value("10.0"))
                .andExpect(jsonPath("$.company.name").value("3m"))
                .andDo(print());

    }
}