package next.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;

import next.model.Question;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionDao {
	private static final Logger log = LoggerFactory
			.getLogger(QuestionDao.class);

	@Resource
	private JdbcTemplate jdbcTemplate;

	public Question insert(Question question) {
		String sql = "INSERT INTO QUESTIONS (writer, title, contents, createdDate) VALUES (?, ?, ?, ?)";
		PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setString(1, question.getWriter());
				pstmt.setString(2, question.getTitle());
				pstmt.setString(3, question.getContents());
				pstmt.setTimestamp(4,
						new Timestamp(question.getTimeFromCreateDate()));
				return pstmt;
			}
		};

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, keyHolder);
		return findById(keyHolder.getKey().longValue());
	}

	public List<Question> findAll() {
		String sql = "SELECT questionId, writer, title, createdDate, countOfAnswer FROM QUESTIONS WHERE deleted = 'FALSE'"
				+ "order by questionId desc";

		RowMapper<Question> rm = new RowMapper<Question>() {
			@Override
			public Question mapRow(ResultSet rs, int index)
					throws SQLException {
				return new Question(rs.getLong("questionId"),
						rs.getString("writer"), rs.getString("title"), null,
						rs.getTimestamp("createdDate"),
						rs.getInt("countOfAnswer"));
			}

		};

		return jdbcTemplate.query(sql, rm);
	}

	public Question findById(long questionId) {
		String sql = "SELECT questionId, writer, title, contents, createdDate, countOfAnswer, deleted FROM QUESTIONS "
				+ "WHERE questionId = ?";

		RowMapper<Question> rm = new RowMapper<Question>() {
			@Override
			public Question mapRow(ResultSet rs, int index)
					throws SQLException {
				return new Question(rs.getLong("questionId"),
						rs.getString("writer"), rs.getString("title"),
						rs.getString("contents"),
						rs.getTimestamp("createdDate"),
						rs.getInt("countOfAnswer"));
			}
		};

		return jdbcTemplate.queryForObject(sql, rm, questionId);
	}

	public void update(Question question) {
		String sql = "UPDATE QUESTIONS set title = ?, contents = ?, deleted = ? WHERE questionId = ?";
		jdbcTemplate.update(sql, question.getTitle(), question.getContents(),
				question.isDeleted(), question.getQuestionId());
	}

	public void delete(long questionId) {
		String sql = "UPDATE QUESTIONS set deleted = ? WHERE questionId = ?";
		jdbcTemplate.update(sql, true, questionId);
	}

	public void increaseCountOfAnswer(long questionId) {
		String sql = "UPDATE QUESTIONS set countOfAnswer = countOfAnswer + 1 WHERE questionId = ?";
		jdbcTemplate.update(sql, questionId);
		log.debug("increase count of answer called");
	}

	public void decreaseCountOfAnswer(long questionId) {
		String sql = "UPDATE QUESTIONS set countOfAnswer = countOfAnswer - 1 WHERE questionId = ?";
		jdbcTemplate.update(sql, questionId);
		log.debug("decrease count of answer called");
	}
}
