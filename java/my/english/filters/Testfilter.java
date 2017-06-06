package my.english.filters;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import my.english.dto.DtoUser;

public class Testfilter implements Filter {
	HttpServletRequest req;
	HttpServletResponse resp;
	HttpSession session;
	private static final String ENVIRONMENT = "/english";

	public void init(FilterConfig fConfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		System.out.println("вход filterChain");

		req = (HttpServletRequest) request;
		resp = (HttpServletResponse) response;
		resp.setContentType("text/html;charset=utf-8");
		session = req.getSession(true);
		System.out.println("refere " + req.getHeader("referer"));

		// login.html
		if (session.getAttribute("user") == null) {
			resp.sendRedirect(ENVIRONMENT + "/login.html");
			System.out.println("сработал filterChain");
		} else {
			filterChain.doFilter(request, response);
		}

	}

	public void destroy() {

	}
}
