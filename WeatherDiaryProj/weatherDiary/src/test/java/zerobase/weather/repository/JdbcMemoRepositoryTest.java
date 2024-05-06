package zerobase.weather.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Memo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JdbcMemoRepositoryTest {

    @Autowired
    JdbcMemoRepository jdbcMemoRepository;

    @Test
    void insertMemoTest() {
        //given
        Memo newMemo = new Memo(1, "this is new Memo");

        //when
        jdbcMemoRepository.save(newMemo);

        //then
        Optional<Memo> result = jdbcMemoRepository.findById(1L);
        assertEquals("this is new Memo", result.get().getText());
    }


    @Test
    void findAllTest() {
        //given
        jdbcMemoRepository.save(Memo.builder()
                .id(1)
                .text("this is new Memo")
                .build());
        List<Memo> memos = jdbcMemoRepository.findAll();
        System.out.println(memos);
        //when

        //then
        assertNotNull(memos);

    }




}