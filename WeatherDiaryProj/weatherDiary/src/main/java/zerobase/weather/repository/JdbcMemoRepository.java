package zerobase.weather.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import zerobase.weather.domain.Memo;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcMemoRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcMemoRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Memo save(Memo memo) {
        String sql = "INSERT INTO memo VALUES(?, ?)";
        jdbcTemplate.update(sql, memo.getId(), memo.getText());
        return memo;
    }

    public List<Memo> findAll() {
        String sql = "SELECT * FROM memo";
        return jdbcTemplate.query(sql, memoRowMapper());
    }

    public Optional<Memo> findById(Long id) {
        String sql = "SELECT * FROM memo WHERE id = ?";
        return jdbcTemplate.query(sql, memoRowMapper(), id)
                .stream()
                .findFirst();
    }

    private RowMapper<Memo> memoRowMapper() {
        return (rs, rowNum)
                -> new Memo(
                        rs.getInt("id"),
                rs.getString("text")
        );
    }

}
