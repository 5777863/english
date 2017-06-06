package my.english.main;

import my.english.dto.*;
import my.englsih.exception.MyException;
import my.english.services.*;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.http.Part;

import com.ibm.useful.http.FileData;
import com.ibm.useful.http.PostData;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

//@WebServlet(urlPatterns = { "/engl", "/e" })
public class English extends HttpServlet {
	private static final String ENVIRONMENT = "/english";
	private static final String ERRPAGENOTFOUND = "Такая страница не найдена...";
	private static final String INCORRECTDATA = "Некорректные данные запроса";
	private static final String UNERROR = "Произошла ошибка, попробуйте позже";
	private static final String ERRORPAGE = "error.html";
	private static final Logger LOG = LogManager.getLogger("allExcept");

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		session.setMaxInactiveInterval(60 * 60);
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=utf-8");
		// PrintWriter print;
		// Добавляем слова в Unit
		if ((req.getServletPath().equals("/unitedit.html")) && (req.getParameter("word") != null)
				&& (req.getParameter("translate") != null) && (req.getParameter("unitName") != null)) {
			try {
				resp.sendRedirect(
						ENVIRONMENT + "/unitedit.html?unit="
								+ Service.wordsAddHands(req.getParameterValues("word"),
										req.getParameterValues("translate"), req.getParameter("unitName"),
										(DtoUser) session.getAttribute("user")));
			} catch (MyException e) {
				e.printStackTrace();
				LOG.error(" in unitedit.html(POST запрос).Добавляем слова в Unit. userId=  "
						+ ((DtoUser) session.getAttribute("user")).getId() + ". " + e.getStackTrace());
				session.setAttribute("errormsg", e.getMessage());
				resp.sendRedirect("/errorpg");
			}
			return;
		}

		// загрузка файла
		if (req.getServletPath().equals("/upload")) {
			DtoUser user = (DtoUser) session.getAttribute("user");
			if (isMultipartFormat(req)) {
				/*
				 * Part parts = req.getPart("file"); InputStream
				 * content=parts.getInputStream(); BufferedReader bufRead = new
				 * BufferedReader( new InputStreamReader(content));
				 */
				fileLoad(session, req, resp, user.getId());
			}
			return;
		}

	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		session.setMaxInactiveInterval(60 * 60);
		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter print;
		// PrintWriter print = resp.getWriter();

		// index.html
		if ((req.getServletPath().equals("/index.html")) && (req.getQueryString() == null)) {
			System.out.println("запрос index");
			// добавляем факты в карусель
			req.setAttribute("facts", Service.factForIndex());
			// добавляем 6тестов
			req.setAttribute("groupname", Service.testForIndex());
			req.getRequestDispatcher("/jindex.jsp").forward(req, resp);
			return;
		}

		// feedback.html (fancybox окна)
		if (req.getServletPath().equals("/feedback.html")) {
			print = resp.getWriter();
			JSONObject json = new JSONObject();
			try {
				if (req.getParameter("text") != null) {
					System.out.println("запрос feedback");
					resp.setContentType("text/json;charset=utf-8");
					String message = req.getParameter("text");
					if (session.getAttribute("user") != null) {
						System.out.println("сессия !пуста");
						DtoUser user = (DtoUser) session.getAttribute("user");
						json.put("result", Service.sendMess(message, user.getUmail()));
					} else {
						json.put("result", Service.sendMess(message));
					}
					System.out.println(json);
				} else {
					session.setAttribute("errormsg", ERRPAGENOTFOUND);
					json.put("errorpg", ERRORPAGE);
					LOG.warn("ERRPAGENOTFOUND in feedback");
				}
			} catch (MyException e) {
				e.printStackTrace();
				json.put("result", e.getMessage());
				LOG.error(" in feedback " + e.getStackTrace());
			} finally {
				print.println(json);
				print.close();
			}
			return;
		}

		// error.html
		if (req.getServletPath().equals("/error.html")) {
			print = resp.getWriter();
			if (session.getAttribute("errormsg") != null) {
				req.getRequestDispatcher("/jerror.jsp").forward(req, resp);
			} else {
				session.setAttribute("errormsg", ERRPAGENOTFOUND);
				req.getRequestDispatcher("/jerror.jsp").forward(req, resp);
			}
			return;
		}

		// registration.html
		if (req.getServletPath().equals("/registration.html")) {
			System.out.println("запрос registration");
			if (req.getQueryString() == null) {
				if (session.getAttribute("user") != null) {
					System.out.println("сессия !пуста");
					resp.sendRedirect(ENVIRONMENT + "/dictionary.html");
				} else {
					System.out.println("сессия пуста");
					req.getRequestDispatcher("/jregistration.jsp").forward(req, resp);
				}
			} else {
				// обработка формы регистрации
				if ((req.getParameter("name") != null) && (req.getParameter("email") != null)
						&& (req.getParameter("pass1") != null) && (req.getParameter("pass2") != null)) {
					System.out.println("получен параметр формы регистрации");
					resp.setContentType("text/json;charset=utf-8");
					print = resp.getWriter();
					String name = req.getParameter("name");
					String email = req.getParameter("email");
					String pass1 = req.getParameter("pass1");
					String pass2 = req.getParameter("pass2");
					print.println(Service.registrationForm(name, email, pass1, pass2));
					print.close();
				}
			}
			return;
		}

		// password.html
		// переход на страницу password.html+++
		if (req.getServletPath().equals("/password.html")) {
			System.out.println("запрос password");
			if (req.getQueryString() == null) {
				if (session.getAttribute("user") != null) {
					resp.sendRedirect(ENVIRONMENT + "/dictionary.html");
				} else {
					req.getRequestDispatcher("/jpassword.jsp").forward(req, resp);
				}
				return;
			}

			// запрос на отправку пароля на email
			if (req.getParameter("email") != null) {
				resp.setContentType("text/json;charset=utf-8");
				print = resp.getWriter();
				String mail = req.getParameter("email");
				print.println(Service.sendPswForm(mail));
				print.close();
			}
			return;
		}

		// login.html
		// переход на страницу login.html+++
		if (req.getServletPath().equals("/login.html")) {
			System.out.println("запрос login");
			if (req.getQueryString() == null) {
				if (session.getAttribute("user") != null) {
					System.out.println("сессия !пуста");
					resp.sendRedirect("/dictionary.html");
				} else {
					System.out.println("сессия пуста");
					req.getRequestDispatcher("/jlogin.jsp").forward(req, resp);
				}
				return;
			}

			// обработка формы входа пользователя
			if (req.getQueryString() != null) {
				resp.setContentType("text/json;charset=utf-8");
				print = resp.getWriter();
				String mail = req.getParameter("email");
				String pass = req.getParameter("pass");
				JSONObject json = new JSONObject();
				try {
					if (req.getParameter("email") != null && req.getParameter("pass") != null) {
						DtoUser user = Service.loginForm(mail, pass);
						session.setAttribute("user", user);
					} else {
						session.setAttribute("errormsg", ERRPAGENOTFOUND);
						json.put("errorpg", ERRORPAGE);
					}
				} catch (MyException e) {
					e.printStackTrace();
					json.put("msg", e.getMessage());
					LOG.error(" in login.html. обработка формы входа пользователя " + e.getStackTrace());
				} finally {
					print.println(json);
					print.close();
				}
			}
			return;
		}

		// переход по ссылке подтверждения, проверка, смена статуса на active
		if (req.getServletPath().equals("/activate.html")) {
			if (req.getParameter("active") != null) {
				print = resp.getWriter();
				String cod = req.getParameter("active");
				System.out.println(cod);
				if (cod.length() == 25) {
					session.setAttribute("errormsg", Service.finalRegistration(cod));
					req.getRequestDispatcher("/jerror.jsp").forward(req, resp);
				} else {
					LOG.warn("activate.html.Некорректная ссылка активации");
					session.setAttribute("errormsg", "Некорректная ссылка активации");
					req.getRequestDispatcher("/jerror.jsp").forward(req, resp);
				}
			}
			return;
		}

		// мой словарь, список унитов
		if (req.getServletPath().equals("/dictionary.html")) {
			if (req.getQueryString() == null) {
				try {
					if (session.getAttribute("user") != null) {
						// выводим спис.усерских тестов
						req.setAttribute("resultListUnit",
								Service.listUsersTests((DtoUser) session.getAttribute("user")));
						// выводим спис.встроенных тестов
						req.setAttribute("resultListGroupname", Service.setDefaultTests());
						req.getRequestDispatcher("/jdictionary.jsp").forward(req, resp);
					} else {
						// выводим спис.встроенных тестов
						req.setAttribute("resultListGroupname", Service.setDefaultTests());
						req.getRequestDispatcher("/jdictionarystatic.jsp").forward(req, resp);
					}
				} catch (MyException e) {
					e.printStackTrace();
					LOG.error(" in dictionary.html. мой словарь, список унитов " + e.getStackTrace());
					session.setAttribute("errormsg", e.getMessage());
					req.getRequestDispatcher("/jerror.jsp").forward(req, resp);
				}
			}
			return;
		}

		// удаляем юнит
		if (req.getRequestURI().equals(ENVIRONMENT + "/json/dictionary.html")) {
			if (req.getParameter("unitdel") != null) {
				resp.setContentType("text/json;charset=utf-8");
				print = resp.getWriter();
				String unitName = req.getParameter("unitdel");
				JSONObject json = new JSONObject();
				try {
					Service.unitDel(unitName, (DtoUser) session.getAttribute("user"));
					json.put("redirect", "dictionary.html");
					print.print(json);
					System.out.println(json);
				} catch (MyException e) {
					e.printStackTrace();
					LOG.error(" in dictionary.html.  удаляем юнит " + e.getStackTrace());
					json.put("msg", e.getMessage());
					print.print(json);
				} finally {
					print.close();
				}
				return;
			}

			// сбрасывает значение оценки в unit пользователя
			if (req.getParameter("ratingreset") != null) {
				print = resp.getWriter();
				resp.setContentType("text/json;charset=utf-8");
				JSONObject jobject = new JSONObject();
				try {
					jobject = Service.resetRating(Integer.parseInt(req.getParameter("ratingreset")));
				} catch (MyException e) {
					e.printStackTrace();
					LOG.error(e.getMessage() + " in dictionary.html. сбрасывает значение оценки в unit пользователя");
					session.setAttribute("errormsg", e.getStackTrace());
					jobject.put("redirect", ERRORPAGE);
				} finally {
					print.println(jobject);
					print.close();
				}
			}
			return;
		}

		// переход к jteststatic
		if (req.getServletPath().equals("/teststatic.html")) {
			if (req.getQueryString() == null) {
				req.getRequestDispatcher("/jteststatic.jsp").forward(req, resp);
			}
			return;
		}

		// сбрасывает значение оценки в staticunit
		if (req.getRequestURI().equals(ENVIRONMENT + "/json/staticunit.html")) {
			if (req.getParameter("ratingreset") != null) {
				print = resp.getWriter();
				resp.setContentType("text/json;charset=utf-8");
				JSONObject jobject = new JSONObject();
				try {
					jobject = Service.resetRatingInStatic(Integer.parseInt(req.getParameter("ratingreset")));
				} catch (MyException e) {
					e.printStackTrace();
					LOG.error("in staticunit.html. брасывает значение оценки в staticunit " + e.getStackTrace());
					session.setAttribute("errormsg", e.getMessage());
					jobject.put("redirect", ERRORPAGE);
				} finally {
					print.println(jobject);
					print.close();
				}
				return;
			}
		}

		// список с конкретным статик тестом
		if (req.getServletPath().equals("/staticunit.html")) {
			if (req.getParameter("groupname") != null) {
				try {
					// если усер не залогинен то список выбирает из
					// статиккатеори без оценки
					if (session.getAttribute("user") == null) {
						req.setAttribute("resultListStaticCat",
								Service.listStaticcategory(req.getParameter("groupname")));
					} else {
						req.setAttribute("resultListStaticRating", Service.listStaticrating(
								(DtoUser) session.getAttribute("user"), req.getParameter("groupname")));
					}
					req.setAttribute("groupname", req.getParameter("groupname"));
					req.getRequestDispatcher("/jstaticunit.jsp").forward(req, resp);
				} catch (MyException e) {
					e.printStackTrace();
					LOG.error("in staticunit.html. список с конкретным статик тестом " + e.getStackTrace());
					session.setAttribute("errormsg", e.getMessage());
					req.getRequestDispatcher("/jerror.jsp").forward(req, resp);
				}
				return;
			}

			// возвращаем список слов статикюнита для разворота при клике на
			// юнит(jsonarray)
			if (req.getParameter("staticcategoryId") != null) {
				resp.setContentType("text/json;charset=utf-8");
				print = resp.getWriter();
				try {
					print.print(Service.listWordsStaticunit(Integer.parseInt(req.getParameter("staticcategoryId"))));
				} catch (Exception e) {
					e.printStackTrace();
					LOG.error(
							"in staticunit.html. возвращаем список слов статикюнита для разворота при клике на юнит(jsonarray) "
									+ e.getStackTrace());
					session.setAttribute("errormsg", UNERROR);
					print.print(new JSONObject().put("errorpg", ERRORPAGE));
				} finally {
					print.close();
				}
				return;
			}
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// выбор уроков для тестирования статикюниты
			if ((req.getParameter("test").equals("vvod") ^ (req.getParameter("test").equals("variant")))
					&& (req.getParameter("lang") != null)) {
				print = resp.getWriter();
				resp.setContentType("text/json;charset=utf-8");
				JSONObject json = new JSONObject();
				try {
					json = Service.chooseStaticunitTest(req.getParameterValues("idstatcat"), req.getParameter("test"));
					// сохраняем в сессии коллекцию слов для теста
					session.setAttribute("statunitArray", json.remove("statunitArray"));
					// сохраняем в сессию массив созначениями резальтатов
					// для клиента
					session.setAttribute("result", json.remove("resultToSession"));
					print.println(json);
				} catch (MyException e) {
					e.printStackTrace();
					LOG.error("in staticunit.html. выбор уроков для тестирования статикюниты userId= "
							+ ((DtoUser) session.getAttribute("user")).getId() + ". " + e.getStackTrace());
					session.setAttribute("errormsg", e.getMessage());
					json.put("redirect", ERRORPAGE);
					print.println(json);
				} finally {
					print.close();
				}
				return;
			}

			// проверка слов
			if ((req.getParameter("test").equals("vvod") ^ (req.getParameter("test").equals("variant")))
					&& (req.getParameter("answer") != null) && (req.getParameter("question") != null)
					&& (req.getParameter("unitid") != null)) {
				print = resp.getWriter();
				resp.setContentType("text/json;charset=utf-8");
				System.out.println("получен параметр answer");
				// json массив в котором будут слова переданные клиенту
				JSONObject json = new JSONObject();
				// если сессия пуста и список слов нуль
				if (session.getAttribute("statunitArray") == null) {
					json.put("redirect", "dictionary.html");
					print.println(json);
					print.close();
					return;
				}
				String answer = req.getParameter("answer");
				String question = req.getParameter("question");
				ArrayList<DtoStaticunit> statunitArray = (ArrayList<DtoStaticunit>) session
						.getAttribute("statunitArray");
				int[] result;
				int kolvo = statunitArray.size();
				DtoStaticunit dtoStatunit;
				result = (int[]) session.getAttribute("result");
				try {
					System.out.println(answer + "** " + question);
					boolean answerResult = answer.equals(question);
					if (answerResult) {
						// кол-во прав.ответов
						result[2] = result[2] + 1;
					}
					// если пользователь залогинен то меняем оценку в
					// Staticrating
					if (session.getAttribute("user") != null) {
						Service.updateRatingInStatictest(req.getParameter("unitid"),
								(DtoUser) session.getAttribute("user"), answerResult);
					}
					// колво оставшихся слов
					result[1] = statunitArray.size();
					// если ответ получен не на последнее слово
					if (kolvo > 0) {
						json.put("arraywords",
								Service.nextWordInStatictest(statunitArray, kolvo, req.getParameter("test")));
						json.put("result", jsonKolvo(result));
						// сохраняем в сессии коллекцию слов для теста
						session.setAttribute("statunitArray", statunitArray);
						// сохраняем в сессию массив созначениями
						// резальтатов
						// для клиента
						session.setAttribute("result", result);
					} else {
						json.put("end", jsonKolvo(result));
						session.removeAttribute("statunitArray");
						session.removeAttribute("result");
					}
					print.println(json);
				} catch (MyException e) {
					e.printStackTrace();
					LOG.error("in staticunit.html. проверка слов. userId= "
							+ ((DtoUser) session.getAttribute("user")).getId() + ". " + e.getStackTrace());
					session.setAttribute("errormsg", UNERROR);
					json.put("errorpg", ERRORPAGE);
					print.println(json);
				} finally {
					print.close();
				}
			} else {
				session.setAttribute("errormsg", ERRPAGENOTFOUND);
				req.getRequestDispatcher("/jerror.jsp").forward(req, resp);
			}
			return;
		}
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// возвращает список слов через JSONArray при нажатии на UnitUsera
		if (req.getServletPath().equals("/wordarray.html")) {
			if (req.getParameter("unitId") != null) {
				resp.setContentType("text/json;charset=utf-8");
				print = resp.getWriter();
				try {
					print.print(Service.listWordsUserUnit(Integer.parseInt(req.getParameter("unitId"))));
				} catch (Exception e) {
					e.printStackTrace();
					LOG.error(
							"in wordarray.html. возвращает список слов через JSONArray при нажатии на UnitUsera userId= "
									+ ((DtoUser) session.getAttribute("user")).getId() + ". " + e.getStackTrace());
					session.setAttribute("errormsg", UNERROR);
					print.print(new JSONObject().put("redirect", ERRORPAGE));
				} finally {
					print.close();
				}
			}
			return;
		}

		// добавление unit через поле ввода
		if ((req.getServletPath().equals("/unitadd.html")) && (req.getParameter("newUnit") != null)) {
			resp.setContentType("text/json;charset=utf-8");
			print = resp.getWriter();
			try {
				print.print(Service.newUnit(req.getParameter("newUnit"), (DtoUser) session.getAttribute("user")));
			} catch (Exception e) {
				e.printStackTrace();
				LOG.error("in unitadd.html. добавление unit через поле ввода. userId= "
						+ ((DtoUser) session.getAttribute("user")).getId() + ". " + e.getStackTrace());
				print.print(new JSONObject().put("msg", UNERROR));
			} finally {
				print.close();
			}
			return;
		}

		// Переименование Unita
		if ((req.getServletPath().equals("/unitrename.html")) && (req.getParameter("oldUnit") != null)
				&& (req.getParameter("newUnit") != null)) {
			resp.setContentType("text/json;charset=utf-8");
			print = resp.getWriter();
			try {
				print.println(Service.renameUnit(req.getParameter("oldUnit"), req.getParameter("newUnit"),
						(DtoUser) session.getAttribute("user")));
			} catch (Exception e) {
				e.printStackTrace();
				LOG.error("in unitrename.html. Переименование Unita. userId= "
						+ ((DtoUser) session.getAttribute("user")).getId() + ". " + e.getStackTrace());
				print.println(new JSONObject().put("msg", UNERROR));
			} finally {
				print.close();
			}
			return;
		}

		// страница редактировать содержимого unit
		if (req.getServletPath().equals("/unitedit.html")) {// unitedit.html?unit=82
			print = resp.getWriter();
			if (req.getParameter("unit") != null) {
				try {
					JSONObject json = Service.unitEdit(req.getParameter("unit"),
							(DtoUser) session.getAttribute("user"));
					req.setAttribute("resultListWord", json.get("resultListWord"));
					req.setAttribute("unitName", json.get("unitName"));
					req.getRequestDispatcher("/jword.jsp").forward(req, resp);
				} catch (MyException e) {
					e.printStackTrace();
					LOG.error("in /unitedit.html. страница редактировать содержимого unit. userId=  "
							+ ((DtoUser) session.getAttribute("user")).getId() + ". " + e.getStackTrace());
					session.setAttribute("errormsg", e.getMessage());
					req.getRequestDispatcher("/jerror.jsp").forward(req, resp);
				} finally {
					print.close();
				}
				return;
			}
		}
		if (req.getRequestURI().equals(ENVIRONMENT + "/json/unitedit.html")) {
			print = resp.getWriter();
			// меняем слово
			if ((req.getParameter("oldWord") != null) && (req.getParameter("oldTranslate") != null)
					&& (req.getParameter("newWord") != null) && (req.getParameter("newTranslate") != null)
					&& (req.getParameter("unitname") != null)) {
				resp.setContentType("text/json;charset=utf-8");
				try {
					print.println(Service.wordEdit(req.getParameter("oldWord"), req.getParameter("oldTranslate"),
							req.getParameter("newWord"), req.getParameter("newTranslate"), req.getParameter("unitname"),
							(DtoUser) session.getAttribute("user")));
				} catch (Exception e) {
					e.printStackTrace();
					LOG.error("in /unitedit.html. меняем слово " + ((DtoUser) session.getAttribute("user")).getId()
							+ ". " + e.getStackTrace());
					print.println(new JSONObject().put("msg", UNERROR));
				} finally {
					print.close();
				}
				return;
			}

			// обработка кнопки копировать, возвращает json список unitов для
			// выбора копирования
			if (req.getParameter("copy") != null) {
				resp.setContentType("text/json;charset=utf-8");
				try {
					print.println(Service.wordCopyListUnits((DtoUser) session.getAttribute("user")));
				} catch (Exception e) {
					e.printStackTrace();
					LOG.error(
							"in /unitedit.html. обработка кнопки копировать, возвращает json список unitов для выбора копирования "
									+ ((DtoUser) session.getAttribute("user")).getId() + ". " + e.getStackTrace());
					session.setAttribute("errormsg", UNERROR);
					print.println(new JSONObject().put("redirect", "errorpg.html"));
				} finally {
					print.close();
				}
				return;
			}
			// обработка кнопки удалить слова
			if ((req.getParameter("delword") != null) && (req.getParameter("word") != null)
					&& (req.getParameter("unitname") != null)) {
				resp.setContentType("text/json;charset=utf-8");
				try {
					print.println(Service.wordDell(req.getParameter("unitname"), req.getParameterValues("word"),
							(DtoUser) session.getAttribute("user")));
				} catch (Exception e) {
					e.printStackTrace();
					LOG.error("in /unitedit.html. обработка кнопки удалить слова "
							+ ((DtoUser) session.getAttribute("user")).getId() + ". " + e.getStackTrace());
					session.setAttribute("errormsg", UNERROR);
					print.println(new JSONObject().put("redirect", ERRORPAGE));
				} finally {
					print.close();
				}
				return;
			}
			// обработка кнопки продолжить копировать
			if ((req.getParameter("unitforcopy") != null) && (req.getParameter("word") != null)) {
				resp.setContentType("text/json;charset=utf-8");
				JSONObject json = new JSONObject();
				try {
					Service.wordCopy(req.getParameter("unitforcopy"), req.getParameterValues("word"));
				} catch (Exception e) {
					e.printStackTrace();
					LOG.error("in /unitedit.html. обработка кнопки продолжить копировать " + e.getStackTrace());
					json.put("msg", UNERROR);
				} finally {
					print.println(json);
					print.close();
				}
			}
			return;
		}

		// список слов по запросу Search
		if (req.getServletPath().equals("/search.html")) {
			resp.setContentType("text/json;charset=utf-8");
			print = resp.getWriter();
			JSONArray jsnArray = new JSONArray();
			try {
				print.println(Service.search(req.getParameter("search"), req.getParameter("lang"),
						req.getParameter("words"), (DtoUser) session.getAttribute("user")));
			} catch (MyException e) {
				e.printStackTrace();
				LOG.error("in /search.html. список слов по запросу Search "
						+ ((DtoUser) session.getAttribute("user")).getId() + ". " + e.getStackTrace());
				jsnArray.add("поиск временно не работает, попробуйте позже");
				print.println(jsnArray);
			} finally {
				print.close();
			}
			return;
		}

		// переход на стр.теста
		if (req.getRequestURI().equals(ENVIRONMENT + "/test.html")) {
			System.out.println(req.getServletPath());
			if (req.getQueryString() == null) {
				System.out.println("запрос test");
				req.getRequestDispatcher("/jtest.jsp").forward(req, resp);
				return;
			}
		}

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// выбор уроков для тестирования
		if (req.getRequestURI().equals(ENVIRONMENT + "/json/test.html")) {
			print = resp.getWriter();
			if ((req.getParameter("test").equals("vvod") ^ (req.getParameter("test").equals("variant")))
					&& (req.getParameter("lang") != null)) {
				resp.setContentType("text/json;charset=utf-8");
				JSONObject json = new JSONObject();
				try {
					json = Service.chooseUserUnitTest(req.getParameterValues("unit"), req.getParameter("test"),
							(DtoUser) session.getAttribute("user"));
					// сохраняем в сессии коллекцию слов для теста
					session.setAttribute("urwordArray", json.remove("urwordArray"));
					// сохраняем в сессию массив созначениями резальтатов
					// для клиента
					session.setAttribute("result", json.remove("resultToSession"));
					print.println(json);
				} catch (MyException e) {
					e.printStackTrace();
					LOG.error("in /test.html/json. выбор уроков для тестирования. userId= "
							+ ((DtoUser) session.getAttribute("user")).getId() + ". " + e.getStackTrace());
					session.setAttribute("errormsg", e.getMessage());
					json.put("redirect", ERRORPAGE);
					print.println(json);
				} finally {
					print.close();
				}
			}
			// проверка слов
			if ((req.getParameter("test").equals("vvod") ^ (req.getParameter("test").equals("variant")))
					&& (req.getParameter("answer") != null) && (req.getParameter("question") != null)
					&& (req.getParameter("unitid") != null)) {
				resp.setContentType("text/json;charset=utf-8");
				System.out.println("получен параметр answer");
				// json массив в котором будут слова переданные клиенту
				JSONObject json = new JSONObject();
				if (session.getAttribute("urwordArray") == null) {
					json.put("redirect", "dictionary.html");
					print.println(json);
					print.close();
					return;
				}
				String answer = req.getParameter("answer");
				String question = req.getParameter("question");
				ArrayList<DtoUrword> urwordArray = (ArrayList<DtoUrword>) session.getAttribute("urwordArray");
				int kolvo = urwordArray.size();
				int[] result = (int[]) session.getAttribute("result");
				try {
					System.out.println(answer + "** " + question);
					boolean answerResult = answer.equals(question);
					if (answerResult) {
						// кол-во прав.ответов
						result[2] = result[2] + 1;
					}
					// обновляем оценку
					Service.updateRatingInUserTest(req.getParameter("unitid"), answerResult);
					// колво оставшихся слов
					result[1] = urwordArray.size();
					// если ответ получен не на последнее слово
					if (kolvo > 0) {
						json.put("arraywords",
								Service.nextWordInUserTest(urwordArray, kolvo, req.getParameter("test")));
						json.put("result", jsonKolvo(result));
						// сохраняем в сессии коллекцию слов для теста
						session.setAttribute("urwordArray", urwordArray);
						// сохраняем в сессию массив созначениями
						// резальтатов
						// для клиента
						session.setAttribute("result", result);
					} else {
						json.put("end", jsonKolvo(result));
						session.removeAttribute("urwordArray");
						session.removeAttribute("result");
					}
					print.println(json);
				} catch (Exception e) {
					e.printStackTrace();
					LOG.error("in /test.html/json. проверка слов. userId= "
							+ ((DtoUser) session.getAttribute("user")).getId() + ". " + e.getStackTrace());
					session.setAttribute("errormsg", UNERROR);
					json.put("redirect", ERRORPAGE);
					print.println(json);
				} finally {
					print.close();
				}
			}
			return;
		}
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Выход
		if (req.getServletPath().equals("/logout.html")) {
			if (req.getQueryString() == null) {
				System.out.println("out");
				session.invalidate();
				System.out.println("press out");
				resp.sendRedirect(ENVIRONMENT + "/index.html");
			}
			return;
		}
	}

	// доп.методы*********************************************************************************************************************

	// добавляет к json объекту поля с инф. текущем результате прохождения теста
	private JSONObject jsonKolvo(int[] result) {
		JSONObject json = new JSONObject();
		json.put("all", result[0]);
		json.put("ost", result[1]);
		json.put("res", result[2]);
		return json;
	}

	// добавляет к json объекту поля с инф. текущем результате прохождения теста
	JSONObject jsonAll(DtoUrword dtoUrword, int[] result) {
		JSONObject json = dtoUrword.toJSON();
		json.put("all", result[0]);
		json.put("ost", result[1]);
		json.put("res", result[2]);
		return json;
	}

	void fileLoad(HttpSession session, HttpServletRequest req, HttpServletResponse resp, int idUser)
			throws ServletException, IOException {
		resp.setContentType("aplication/json;charset=utf-8");
		PrintWriter print = resp.getWriter();
		JSONObject result = new JSONObject();
		BufferedReader bufRead = null;
		try {
			// проверка пришел ли запрос в multipart формате
			if (isMultipartFormat(req)) {
				// разбор формата multipart и помещение информации из
				// запроса в поля объекта
				// класса PostData
				PostData multidata = new PostData(req);
				// извлечение посланной информации
				// извлечение из доп.текстовой строки ввода
				// String fileDescription =
				// multidata.getParameter("description");
				FileData tempFile = multidata.getFileData("fileName");
				System.out.println(tempFile);
				if (tempFile.getByteData().length > 5 * 1024 * 1024) {
					session.setAttribute("errormsg", "Превышен лимит размера загружаемого файла");
					result.put("redirect", "error.html");
					print.println(result);
					print.close();
					return;
				}
				ByteArrayInputStream str = new ByteArrayInputStream(tempFile.getByteData());
				// определяем кодировку имени файла
				String codiName = System.getProperty("file.encoding");
				// определяем кодировку текста файла
				String codiFile = CharacterDetector.getInstance()
						.detect(new ByteArrayInputStream(tempFile.getByteData()));
				// получаем поток bufferReader из содержимого файла и
				// указываем исходную кодировку(codiFile)
				bufRead = new BufferedReader(
						new InputStreamReader(new ByteArrayInputStream(tempFile.getByteData()), codiFile));
				Import.fromFile(bufRead, idUser);
				// Загрузка статических слов
				// Import.fromFileStatic(bufRead);
				// print.println("файл загружен");
				// перекодируем получаемое имя загружаемого файла указав
				// исходную кодировку (codiName)
				String fileName;
				if (codiName != "UTF-8") {
					fileName = new String(tempFile.getFileName().getBytes(codiName), "UTF-8");
				} else {
					fileName = tempFile.getFileName();
				}
				result.put("redirect", "dictionary.html");
			} else {
				session.setAttribute("errormsg", "unError");
				result.put("redirect", "error.html");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("in метод fileLoad. userId= " + ((DtoUser) session.getAttribute("user")).getId() + ". "
					+ e.getStackTrace());
			session.setAttribute("errormsg", "unError");
			result.put("redirect", "error.html");
		} finally {
			print.println(result);
			print.close();
			bufRead.close();
		}
	}

	private boolean isMultipartFormat(HttpServletRequest req)
			throws javax.servlet.ServletException, java.io.IOException {
		String temptype = req.getContentType();
		if (temptype.indexOf("multipart/form-data") != -1)
			return true;
		else
			return false;
	}
}