package zerobase.weather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import zerobase.weather.domain.Diary;
import zerobase.weather.repository.DiaryRepository;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DiaryController.class)
class DiaryControllerTest {

    @MockBean
    private DiaryService diaryService;

    @MockBean
    private DiaryRepository diaryRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void successCreateDiary() throws Exception {
        // given
        LocalDate date = LocalDate.of(2024, 1, 1);
        String text = "오늘의 일기";
        System.out.println(date.toString());

        //when
        // then
        mockMvc.perform(post("/create/diary")
                        .param("date", date.toString())  // 날짜를 파라미터로 전달
                        .content(text)  // 텍스트를 요청 바디에 포함
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
    }

    @Test
    void successReadDiary() throws Exception {
        LocalDate date = LocalDate.of(2024,1,1);
        List<Diary> diaryList = new ArrayList<>();
        diaryList.add(new Diary(
                1, "weather", "icon", 36.5,
                date, "text"));
        diaryList.add(new Diary(
                2, "weather2", "icon2", 36.6,
                date, "text2"));

        //given
        given(diaryService.readDiary(date)).willReturn(diaryList);

        //when
        //then
        mockMvc.perform(get("/read/diary")
                .param("date", date.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[1].id")
                        .value(2))
                .andExpect(jsonPath("$[1].weather")
                        .value("weather2"))
                .andExpect(jsonPath("$[1].icon")
                        .value("icon2"))
                .andExpect(jsonPath("$[1].temperature")
                        .value(36.6))
                .andExpect(jsonPath("$[1].date")
                        .value("2024-01-01"))
                .andExpect(jsonPath("$[1].text")
                        .value("text2"));
    }

    @Test
    void successReadDiaries() throws Exception {
        LocalDate date1 = LocalDate.of(2024,1,1);
        LocalDate date2 = LocalDate.of(2024,1,4);

        List<Diary> diaryList = new ArrayList<>();
        diaryList.add(new Diary(
                1, "weather", "icon", 36.5,
                date1, "text"));
        diaryList.add(new Diary(
                2, "weather2", "icon2", 36.6,
                date2, "text2"));

        //given
        LocalDate startDate = LocalDate.of(2024,1,1);
        LocalDate endDate = LocalDate.of(2024,1,5);
        given(diaryService.readDiaries(startDate, endDate))
                .willReturn(diaryList);

        //when
        //then
        mockMvc.perform(get("/read/diaries")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[0].id")
                        .value(1))
                .andExpect(jsonPath("$[0].weather")
                        .value("weather"))
                .andExpect(jsonPath("$[0].icon")
                        .value("icon"))
                .andExpect(jsonPath("$[0].temperature")
                        .value(36.5))
                .andExpect(jsonPath("$[0].date")
                        .value("2024-01-01"))
                .andExpect(jsonPath("$[0].text")
                        .value("text"));
    }

    @Test
    void successUpdateDiary() throws Exception {
        // given
        LocalDate date = LocalDate.of(2024, 1, 1);
        String updatedText = "오늘의 일기";
        System.out.println(date.toString());

        //when
        // then
        mockMvc.perform(put("/update/diary")
                        .param("date", date.toString())  // 날짜를 파라미터로 전달
                        .content(updatedText)  // 텍스트를 요청 바디에 포함
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(diaryService).updateDiary(date, updatedText);
    }

    @Test
    void successDeleteDiary() throws Exception {
        // given
        LocalDate date = LocalDate.of(2024, 1, 1);

        //when
        // then
        mockMvc.perform(delete("/delete/diary")
                        .param("date", date.toString())  // 날짜를 파라미터로 전달
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(diaryService).deleteDiary(date);
    }
}