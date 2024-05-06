package zerobase.weather.service;

import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiaryServiceTest {
    @Mock
    private DiaryRepository diaryRepository;

    @Mock
    private DateWeatherRepository dateWeatherRepository;

    @InjectMocks
    private DiaryService diaryService;

    @Test
    void createDiary() {
        //given
        LocalDate date = LocalDate.of(2024, 1, 1);
        String text = "This is a test";
        DateWeather dateWeather
                = new DateWeather(
                        date, "weather",
                "icon",  36.5
        );

        when(dateWeatherRepository.findAllByDate(date))
                .thenReturn(Collections.singletonList(dateWeather));

        //when
        diaryService.createDiary(date, text);

        ArgumentCaptor<Diary> captor = ArgumentCaptor.forClass(Diary.class);
        ArgumentCaptor<DateWeather> captor2 = ArgumentCaptor.forClass(DateWeather.class);

        //then
        verify(diaryRepository, times(1))
                .save(captor.capture());
        assertEquals("This is a test", captor.getValue().getText());
    }

    @Test
    void readDiary() {
        //given
        LocalDate date = LocalDate.of(2024, 1, 1);
        String text = "This is a test";
        DateWeather dateWeather
                = new DateWeather(
                date, "weather",
                "icon",  36.5
        );

        given(diaryRepository.findAllByDate(date))
                .willReturn(Collections.singletonList(Diary.builder()
                        .date(dateWeather.getDate())
                        .icon(dateWeather.getIcon())
                        .temperature(dateWeather.getTemperature())
                        .text(text)
                        .weather(dateWeather.getWeather())
                        .build()));

        //when
        List<Diary> diaryList = diaryService.readDiary(date);

        //then
        verify(diaryRepository, times(1))
                .findAllByDate(date);
        assertEquals("This is a test", diaryList.get(0).getText());
    }

    @Test
    void readDiaries() {
        //given
        LocalDate date1 = LocalDate.of(2024, 1, 2);
        LocalDate date2 = LocalDate.of(2024, 1, 5);
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 6);

        Diary diary1
                = Diary.builder()
                .date(date1)
                .icon("icon1")
                .temperature(11.1)
                .text("diary1")
                .weather("weather1")
                .build();
        Diary diary2
                = Diary.builder()
                .date(date2)
                .icon("icon2")
                .temperature(22.2)
                .text("diary2")
                .weather("weather2")
                .build();

        List<Diary> expectedDiaries = Arrays.asList(diary1, diary2);

        given(diaryRepository.findAllByDateBetween(startDate, endDate))
                .willReturn(expectedDiaries);


        //when
        List<Diary> result = diaryService.readDiaries(startDate, endDate);

        //then
        verify(diaryRepository, times(1))
                .findAllByDateBetween(startDate, endDate);
        assertEquals("diary1", result.get(0).getText());
        assertEquals("diary2", result.get(1).getText());
        assertEquals(expectedDiaries, result);
    }

    @Test
    void updateDiary() {
        //given
        LocalDate date = LocalDate.of(2024, 1, 1);
        String text = "This is a test";
        String updatedText = "This is a new test";

        given(diaryRepository.getFirstByDate(date))
                .willReturn(Diary.builder()
                        .date(date)
                        .icon("icon")
                        .temperature(11.1)
                        .text(text)
                        .weather("weather")
                        .build());

        //when
        diaryService.updateDiary(date, updatedText);

        ArgumentCaptor<Diary> captor = ArgumentCaptor.forClass(Diary.class);

        //then
        verify(diaryRepository, times(1))
                .save(captor.capture());
        assertEquals(updatedText, captor.getValue().getText());
        assertEquals("weather", captor.getValue().getWeather());
    }

    @Test
    void deleteDiary() {
        //given
        LocalDate date = LocalDate.of(2024, 1, 1);

        //when
        diaryService.deleteDiary(date);

        //then

        verify(diaryRepository,
                times(1)).deleteAllByDate(date);
    }

}