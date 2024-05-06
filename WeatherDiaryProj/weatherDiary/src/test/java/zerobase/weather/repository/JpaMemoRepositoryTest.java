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
class JpaMemoRepositoryTest {

    @Autowired
    JpaMemoRepository jpaMemoRepository;

    @Test
    void insertMemoTest() {
        //given
        Memo newMemo = new Memo(10, "This is new memo");
        //when
        jpaMemoRepository.save(newMemo);
        List<Memo> memoList = jpaMemoRepository.findAll();

        //then
        assertTrue(!memoList.isEmpty());
    }

    @Test
    void findByIdTest() {
        //given
        Memo newMemo = new Memo(10, "jpa");
        //when
        Memo memo = jpaMemoRepository.save(newMemo);
        //then
        Optional<Memo> result = jpaMemoRepository.findById(memo.getId());
        assertEquals("jpa", result.get().getText());
    }
}