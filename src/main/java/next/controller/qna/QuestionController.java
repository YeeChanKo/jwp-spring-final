package next.controller.qna;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import core.web.argumentresolver.LoginUser;
import next.CannotOperateException;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import next.model.User;
import next.service.QnaService;

@Controller
@RequestMapping("/questions")
public class QuestionController {
	private static final Logger log = LoggerFactory
			.getLogger(QuestionController.class);

	@Autowired
	private QuestionDao questionDao;
	@Autowired
	private QnaService qnaService;

	@RequestMapping(value = "/{questionId}", method = RequestMethod.GET)
	public String show(@PathVariable long questionId, Model model)
			throws Exception {
		Question question = qnaService.findById(questionId);
		log.debug("requested question : {}", question);
		List<Answer> answers = qnaService.findAllByQuestionId(questionId);
		model.addAttribute("question", question);
		model.addAttribute("answers", answers);
		return "/qna/show";
	}

	@RequestMapping(value = "/{questionId}/edit", method = RequestMethod.GET)
	public String editForm(@LoginUser User loginUser,
			@PathVariable long questionId, Model model) throws Exception {
		if (loginUser.isGuestUser()) {
			return "redirect:/users/loginForm";
		}
		Question question = qnaService.findById(questionId);
		model.addAttribute("question", question);
		model.addAttribute("editFlag", true);
		return "/qna/form";
	}

	@RequestMapping(value = "/{questionId}/edit", method = RequestMethod.POST)
	public String edit(@LoginUser User loginUser, Question question)
			throws Exception {
		qnaService.updateQuestion(question.getQuestionId(), question,
				loginUser);
		return "redirect:/questions/" + question.getQuestionId();
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String createForm(@LoginUser User loginUser, Model model)
			throws Exception {
		if (loginUser.isGuestUser()) {
			return "redirect:/users/loginForm";
		}
		model.addAttribute("question", new Question());
		return "/qna/form";
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public String create(@LoginUser User loginUser, Question question)
			throws Exception {
		if (loginUser.isGuestUser()) {
			return "redirect:/users/loginForm";
		}
		questionDao.insert(question.newQuestion(loginUser));
		return "redirect:/";
	}

	@RequestMapping(value = "/{questionId}", method = RequestMethod.DELETE)
	public String delete(@LoginUser User loginUser,
			@PathVariable long questionId, Model model) throws Exception {
		try {
			qnaService.deleteQuestion(questionId, loginUser);
			return "redirect:/";
		} catch (CannotOperateException e) {
			log.error("{}", e);
			model.addAttribute("question", qnaService.findById(questionId));
			model.addAttribute("errorMessage", e.getMessage());
			return "show";
		}
	}
}
