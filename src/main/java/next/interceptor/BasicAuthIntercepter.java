package next.interceptor;

import java.nio.charset.Charset;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import next.controller.UserSessionUtils;
import next.dao.UserDao;
import next.model.User;

public class BasicAuthIntercepter extends HandlerInterceptorAdapter {
	private static final Logger log = LoggerFactory
			.getLogger(BasicAuthIntercepter.class);

	@Autowired
	private UserDao userDao;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String authorization = request.getHeader("Authorization");

		if (authorization == null) {
			return true;
		}

		if (authorization.startsWith("Basic")) {
			String base64Credentials = authorization.substring("Basic".length())
					.trim();
			String credentials = new String(
					Base64.getDecoder().decode(base64Credentials),
					Charset.forName("UTF-8"));
			log.debug(credentials);

			String[] splits = credentials.split(":");
			User u = login(splits[0], splits[1]);
			if (u != null) {
				request.getSession()
						.setAttribute(UserSessionUtils.USER_SESSION_KEY, u);
			}
		}

		return true;
	}

	public User login(String userId, String password) {
		User user = userDao.findByUserId(userId);
		if (user == null) {
			return null;
		}

		if (user.matchPassword(password)) {
			return user;
		} else {
			return null;
		}
	}

}
