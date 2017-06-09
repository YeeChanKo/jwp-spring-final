package next.service;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import com.google.common.collect.Lists;

import next.CannotOperateException;
import next.model.Answer;
import next.model.Question;
import next.model.User;

public class QuestionAnswerDeleteTest {

	@Test(expected = CannotOperateException.class)
	public void deleteQuestion_다른_사용자() throws Exception {
		Question question = newQuestion(1L, "javajigi");
		question.delete(newUser("sanjigi"), Lists.newArrayList());
	}

	@Test
	public void deleteQuestion_같은_사용자_답변없음() throws Exception {
		Question question = newQuestion(1L, "javajigi");
		question.delete(newUser("javajigi"), Lists.newArrayList());
		assertTrue(question.isDeleted());
	}

	@Test
	public void deleteQuestion_질문_답변_글쓴이_같음() throws Exception {
		String userId = "javajigi";
		Question question = newQuestion(1L, userId);
		question.delete(newUser(userId),
				Lists.newArrayList(newAnswer(userId), newAnswer(userId)));
		assertTrue(question.isDeleted());
	}

	@Test(expected = CannotOperateException.class)
	public void deleteQuestion_질문_답변_글쓴이_다름() throws Exception {
		String userId = "javajigi";
		Question question = newQuestion(1L, userId);
		question.delete(newUser(userId),
				Lists.newArrayList(newAnswer(userId), newAnswer("sanjigi")));
	}

	private User newUser(String userId) {
		return new User(userId, "password", "name", "test@sample.com");
	}

	private Question newQuestion(long questionId, String userId) {
		return new Question(questionId, userId, "title", "contents", new Date(),
				0);
	}

	private Answer newAnswer(String userId) {
		return new Answer(userId, "contents", 3L);
	}
}
