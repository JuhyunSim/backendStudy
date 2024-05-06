package zerobase.weather.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.Exception.InvalidDateException;
import zerobase.weather.WeatherDiaryApplication;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DiaryService {

    @Value("${openWeatherMap.key}")
    private String apiKey;

    private final DiaryRepository diaryRepository;

    private final DateWeatherRepository dateWeatherRepository;

    private static final Logger logger = LoggerFactory.getLogger(WeatherDiaryApplication.class);

    @Autowired
    public DiaryService(DiaryRepository diaryRepository, DateWeatherRepository dateWeatherRepository) {
        this.diaryRepository = diaryRepository;
        this.dateWeatherRepository = dateWeatherRepository;
    }

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void saveWeatherDate() {
        logger.info("Saving today weather data success");
        dateWeatherRepository.save(getWeatherFromApi());
    }

    /**
     *
     * readOnly = true가 적용되지 않고 있음. 원인파악 필요..
     *
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createDiary(LocalDate date, String text) {

        logger.info("started to create diary");
        //날씨 데이터 가져오기 (DB에서)
        DateWeather dataDateWeather = getDataDateWeather(date);

        //파싱된 데이터 + 일기 값을 우리 db에 넣기
        Diary nowDiary = new Diary();
        nowDiary.setDateWeather(dataDateWeather);
        nowDiary.setText(text);
        diaryRepository.save(nowDiary);
        logger.info("end to create diary");
    }

    @Transactional(readOnly = true)
    public List<Diary> readDiary(LocalDate date) {
        logger.debug("read diary");
        if (date.isAfter(LocalDate.ofYearDay(3050, 1))) {
            throw new InvalidDateException();
        }
        return diaryRepository.findAllByDate(date);
    }

    @Transactional(readOnly = true)
    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {
        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }

    public void updateDiary(LocalDate date, String text) {
        Diary nowDiary = diaryRepository.getFirstByDate(date);
        nowDiary.setText(text);
        diaryRepository.save(nowDiary);
    }

    public void deleteDiary(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }


    private  String getWeatherString() {
        String apiUrl
                = "https://api.openweathermap.org/data/2.5" +
                "/weather?q=seoul&appid=" + apiKey; // api 경로 지정, 서울 현재 데이터

        try {
            URL url = new URL(apiUrl); //String -> URL로 변환
            HttpURLConnection connection
                    = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(
                        connection.getInputStream())
                );
            } else {
                br = new BufferedReader(new InputStreamReader(
                        connection.getErrorStream())
                );
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            return response.toString();
        } catch (Exception e) {
            return "fail to get response";
        }
    }

    private Map<String, Object> parseWeather(String weatherString) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) jsonParser.parse(weatherString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> resultMap = new HashMap<>();
        JSONObject mainData = (JSONObject) jsonObject.get("main");
        resultMap.put("temp", mainData.get("temp"));
        JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
        JSONObject weatherData = (JSONObject) weatherArray.get(0);
        resultMap.put("icon", weatherData.get("icon"));
        resultMap.put("main", weatherData.get("main"));
        return resultMap;
    }

    private DateWeather getWeatherFromApi() {
        //날씨 데이터 불러오기
        String weatherData = getWeatherString();

        //받아온 날씨 json 데이터를 파싱하기(simple json 라이브러리 이용)
        Map<String, Object> parsedWeather = parseWeather(weatherData);

        DateWeather dateWeather = new DateWeather();
        dateWeather.setDate(LocalDate.now());
        dateWeather.setWeather(parsedWeather.get("main").toString());
        dateWeather.setIcon(parsedWeather.get("icon").toString());
        dateWeather.setTemperature((Double) parsedWeather.get("temp"));
        return dateWeather;
    }

    private DateWeather getDataDateWeather(LocalDate date) {
        List<DateWeather> dateWeatherList
                = dateWeatherRepository.findAllByDate(date);
        if (dateWeatherList.size() == 0) {
            // 새로 날씨정보 가져옴(api로 과거 데이터 가져오면 과금될 수 있으므로 피함)
            // -> 현재 날씨 가져옴
            return getWeatherFromApi();
        } else {
            return dateWeatherList.get(0);
        }
    }
}
