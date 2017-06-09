package next.controller.qna;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;

import core.web.argumentresolver.LoginUser;
import next.CannotOperateException;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import next.model.Result;
import next.model.User;
import next.service.QnaService;

@RestController
@RequestMapping("/api/questions")
public class ApiQuestionController {
	private Logger log = LoggerFactory.getLogger(ApiQuestionController.class);

	@Autowired
	private QuestionDao questionDao;
	@Autowired
	private AnswerDao answerDao;
	@Autowired
	private QnaService qnaService;

	@RequestMapping(value = "/{questionId}", method = RequestMethod.DELETE)
	public Result deleteQuestion(@LoginUser User loginUser,
			@PathVariable long questionId) throws Exception {
		try {
			qnaService.deleteQuestion(questionId, loginUser);
			return Result.ok();
		} catch (CannotOperateException e) {
			return Result.fail(e.getMessage());
		}
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<Question> list() throws Exception {
		return questionDao.findAll();
	}

	@RequestMapping(value = "/{questionId}/answers", method = RequestMethod.POST)
	public Map<String, Object> addAnswer(@LoginUser User loginUser,
			@PathVariable long questionId, String contents) throws Exception {
		log.debug("questionId : {}, contents : {}", questionId, contents);
		Map<String, Object> values = Maps.newHashMap();
		Answer answer = new Answer(loginUser.getUserId(), contents, questionId);
		Answer savedAnswer = answerDao.insert(answer);
		questionDao.increaseCountOfAnswer(savedAnswer.getQuestionId());

		values.put("answer", savedAnswer);
		values.put("result", Result.ok());
		return values;
	}

	/// api/questions/8/answers/7
	@RequestMapping(value = "/{questionId}/answers/{answerId}", method = RequestMethod.DELETE)
	public Result deleteAnswer(@LoginUser User loginUser,
			@PathVariable("answerId") long answerId) throws Exception {
		try {
			qnaService.deleteAnswer(answerId, loginUser);
			return Result.ok();
		} catch (CannotOperateException e) {
			return Result.fail(e.getMessage());
		}
	}
}
