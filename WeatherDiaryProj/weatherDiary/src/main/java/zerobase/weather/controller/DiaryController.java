package zerobase.weather.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.domain.Diary;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

@RestController
public class DiaryController {

    private final DiaryService diaryService;

    @Autowired
    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @PostMapping("/create/diary")
    @Operation(summary = "일기 텍스트와 날씨를 이용해서 DB에 저장")
    @ApiResponses(
            value = {
                @ApiResponse (
                        responseCode = "200",
                        description = "OK"
                ),
                @ApiResponse (
                    responseCode = "404",
                    description = "존재하지 않는 리소스 접근",
                    content = @Content(schema =
                        @Schema(implementation = ErrorResponse.class)
                    )
                )
                , @ApiResponse (
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema =
                        @Schema(implementation = ErrorResponse.class)
                ))})
    void createDiary(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "일기를 작성할 날짜", example = "2024-01-01")
            LocalDate date,
            @RequestBody String text) {
        diaryService.createDiary(date, text);
    }

    @GetMapping("/read/diary")
    @Operation(summary = "선택한 날짜의 모든 일기 데이터를 가져옵니다.")
    @ApiResponses(
            value = {
                    @ApiResponse (
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse (
                            responseCode = "404",
                            description = "존재하지 않는 리소스 접근",
                            content = @Content(schema =
                            @Schema(implementation = ErrorResponse.class)
                            )
                    )
                    , @ApiResponse (
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema =
                    @Schema(implementation = ErrorResponse.class)
                    ))})
    List<Diary> readDiary(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "조회할 데이터의 날짜", example = "2024-01-01")
            LocalDate date
    ) {
        return diaryService.readDiary(date);
    }

    @GetMapping("/read/diaries")
    @Operation(summary = "선택한 기간 중의 모든 일기 데이터를 가져옵니다. ")
    @ApiResponses(
            value = {
                    @ApiResponse (
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse (
                            responseCode = "404",
                            description = "존재하지 않는 리소스 접근",
                            content = @Content(schema =
                            @Schema(implementation = ErrorResponse.class)
                            )
                    )
                    , @ApiResponse (
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema =
                    @Schema(implementation = ErrorResponse.class)
                    ))})
    List<Diary> readDiaries(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "조회할 기간의 첫 번째 날짜", example = "2020-01-01")
            LocalDate startDate,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "조회할 기간의 마지막 날짜", example = "2020-01-08")
            LocalDate endDate
    ) {
        return diaryService.readDiaries(startDate, endDate);
    }

    @PutMapping("/update/diary")
    @Operation(summary = "선택한 날짜의 일기 데이터 중 첫 번째 데이터를 수정합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse (
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse (
                            responseCode = "404",
                            description = "존재하지 않는 리소스 접근",
                            content = @Content(schema =
                            @Schema(implementation = ErrorResponse.class)
                            )
                    )
                    , @ApiResponse (
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema =
                    @Schema(implementation = ErrorResponse.class)
                    ))})
    void updateDiary(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "수정할 데이터의 날짜", example = "2024-01-01")
            LocalDate date,
            @RequestBody String text
    ) {
        diaryService.updateDiary(date, text);
    }

    @DeleteMapping("/delete/diary")
    @Operation(summary = "선택한 날짜의 모든 일기 데이터를 삭제합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse (
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse (
                            responseCode = "404",
                            description = "존재하지 않는 리소스 접근",
                            content = @Content(schema =
                            @Schema(implementation = ErrorResponse.class)
                            )
                    )
                    , @ApiResponse (
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema =
                    @Schema(implementation = ErrorResponse.class)
                    ))})
    void deleteDiary(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "삭제할 데이터의 날짜", example = "2024-01-01")
            LocalDate date
    ) {
        diaryService.deleteDiary(date);
    }
}
