package next.service;

import java.util.List;

import javax.inject.Inject;

import next.CannotOperateException;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import next.model.User;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class QnaService {
	private QuestionDao questionDao;
	private AnswerDao answerDao;

	@Inject
	public QnaService(QuestionDao questionDao, AnswerDao answerDao) {
		this.questionDao = questionDao;
		this.answerDao = answerDao;
	}

	public Question findById(long questionId) {
		return questionDao.findById(questionId);
	}

	public List<Answer> findAllByQuestionId(long questionId) {
		return answerDao.findAllByQuestionId(questionId);
	}

	public void deleteAnswer(long answerId, User user)
			throws CannotOperateException {
		Answer answer = answerDao.findById(answerId);
		if (answer == null) {
			throw new EmptyResultDataAccessException("존재하지 않는 질문입니다.", 1);
		}

		if (!answer.isSameUser(user)) {
			throw new CannotOperateException("다른 사용자가 쓴 글을 수정할 수 없습니다.");
		}

		answer.setDeleted(true);
		answerDao.update(answer);
		questionDao.decreaseCountOfAnswer(answer.getQuestionId());
	}

	public void updateQuestion(long questionId, Question newQuestion, User user)
			throws CannotOperateException {
		Question question = questionDao.findById(questionId);
		if (question == null) {
			throw new EmptyResultDataAccessException("존재하지 않는 질문입니다.", 1);
		}

		if (!question.isSameUser(user)) {
			throw new CannotOperateException("다른 사용자가 쓴 글을 수정할 수 없습니다.");
		}

		question.update(newQuestion);
		questionDao.update(question);
	}

	public void deleteQuestion(long questionId, User loginUser)
			throws CannotOperateException {
		// TODO Auto-generated method stub

	}
}
