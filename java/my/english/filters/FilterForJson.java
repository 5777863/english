package my.english.filters;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

public class FilterForJson implements Filter {
	HttpServletRequest req;
	HttpServletResponse resp;
	HttpSession session;

	public void init(FilterConfig fConfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		System.out.println("вход FilteForJson");

		req = (HttpServletRequest) request;
		resp = (HttpServletResponse) response;
		resp.setContentType("text/json;charset=utf-8");
		session = req.getSession(true);
		PrintWriter print = resp.getWriter();

		/*
		 * System.out.println("refere " + req.getHeader("referer"));
		 * System.out.println(req.getPathInfo() + " getPathInfo()");
		 * System.out.println(req.getQueryString() + " getQueryString()");
		 * System.out.println(req.getRequestURI() + " getRequestURI()");
		 * System.out.println(req.getRequestURL() + " getRequestURL()");
		 * System.out.println(req.getServletPath() + " getServletPath()");
		 */
		// login.html
		if (session.getAttribute("user") == null) {
			JSONObject json = new JSONObject();
			json.put("redirect", "login.html");
			print.println(json);
			print.close();
		} else {
			filterChain.doFilter(request, response);
		}

	}

	public void destroy() {

	}
}
