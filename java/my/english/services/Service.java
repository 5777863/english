package my.english.services;

import my.english.dto.*;
import my.english.services.*;
import my.englsih.exception.MyException;
import my.english.dao.*;
import my.english.dao.jdbc.DaoFactsJdbc;
import my.english.dao.jdbc.DaoStaticcategoryJdbc;
import my.english.dao.jdbc.DaoStaticratingJdbc;
import my.english.dao.jdbc.DaoStaticunitJdbc;
import my.english.dao.jdbc.DaoUnitJdbc;
import my.english.dao.jdbc.DaoUserJdbc;
import my.english.dao.jdbc.DaoUwordJdbc;
import my.english.dao.jdbc.DaoWordJdbc;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public class Service {
	private static final String URLIMG = "http://testingwords.ru";
	private static final String URLACTIVATE = "http://testingwords.ru/activate.html?active=";
	private static final String TXTREG = "Подтверждение регистрации ...";
	private static final String TXTPSW = "Востановление пароля ...";
	private static final String DFLT_ERROR = "Произошла ошибка, попробуйте позже";
	private static final String INCORRECT_DATA = "Некорректные данные запроса";
	private static final Logger LOG_ALL = LogManager.getLogger("allExcept");
	private static final Logger LOG_MAIL = LogManager.getLogger("mail");

	// POST запрос.unitedit.html Добавляем слова в Unit
	public static int wordsAddHands(String[] words, String[] translates, String unitName, DtoUser dtoUser)
			throws MyException {
		int idUser = dtoUser.getId();
		DaoWordJdbc daoWord = new DaoWordJdbc();
		DaoUwordJdbc daoUword = new DaoUwordJdbc();
		DaoUnitJdbc daoUnit = new DaoUnitJdbc();
		int unitId = 0;
		try {
			// получаем Id unit
			unitId = daoUnit.resultId(unitName, idUser).getId();
			for (int a = 0; a < words.length; a++) {
				// проверяем нет ли такого слова в юните усера
				if (daoWord.proverkaWordCustom(idUser, unitName, words[a], translates[a]) == 0) {
					// проверяем нет ли слова в word(Db)
					if (daoWord.proverkaWord(words[a], translates[a]) > 0) {
						int wordId = daoWord.getIdWord(words[a], translates[a]);
						daoUword.CreatResult(unitId, wordId);
					} else {// если слова в БД нет
						daoWord.CreatResult(words[a], translates[a], null, null);
						int wordId = daoWord.getIdWord(words[a], translates[a]);
						daoUword.CreatResult(unitId, wordId);
					}
				}
			}
		} catch (SQLException e) {
			throw new MyException(DFLT_ERROR, e);
		} finally {
			daoWord.close();
			daoUword.close();
			daoUnit.close();
		}
		return unitId;
	}

	// факты для index.html
	public static ArrayList<DtoFacts> factForIndex() {
		DaoFactsJdbc daoFacts = new DaoFactsJdbc();
		ArrayList<DtoFacts> dtoFacts = daoFacts.getEightRndFacts();
		return dtoFacts;
	}

	// 6 тестов для index.html
	public static TreeSet<String> testForIndex() {
		DaoStaticcategoryJdbc daoStaticcategory = new DaoStaticcategoryJdbc();
		TreeSet<String> sixGroupname = daoStaticcategory.getSixRndGroupname();
		return sixGroupname;
	}

	// feedback.html отправка сообщения если усер залогинен
	// @overloading
	public static String sendMess(String mess, String userEmail) throws MyException {
		try {
			new mailSend().send("5777863@mail.ru", mess + " from: " + userEmail, "feedback");
		} catch (MessagingException e) {
			LOG_MAIL.error("Error in feedback.html отправка сообщения если усер залогинен " + e.getStackTrace());
			throw new MyException("DFLT_ERROR");
		}
		return "Ваше сообщение отправлено";
	}

	// feedback.html отправка сообщения если усер НЕ залогинен
	// @overloading
	public static String sendMess(String mess) throws MyException {
		try {
			new mailSend().send("5777863@mail.ru", mess, "feedback");
		} catch (MessagingException e) {
			// System.out.println(e.getStackTrace());
			throw new MyException("DFLT_ERROR", e);
		}
		return "Ваше сообщение отправлено";
	}

	// registration.html обработка формы регистрации
	public static JSONObject registrationForm(String name, String email, String pass1, String pass2) {
		// проверка на совпадение введенного пользователем пароля
		JSONObject result = new JSONObject();
		if (pass1.equals(pass2)) {
			DaoUserJdbc daoUser = new DaoUserJdbc();
			try {
				// проверка нет ли уже такой записи по email
				if (daoUser.resExistsByMail(email) == 0) {
					// if (!true) {
					// генерируем значение для поля active
					String active = Coder.generateString(25);
					// генерируем соль
					String solt = Coder.generateString(10);
					// хешируем соль+пароль пользователя
					String pass = null;
					try {
						pass = Coder.getHash(solt + pass1);
					} catch (NoSuchAlgorithmException | IOException e) {
						throw new SQLException();
					}
					daoUser.CreatResult(name, pass, email, active, solt, pass1);
					String message = "<!DOCTYPE html><html><head><meta charset='utf-8'></head><body><div><a href='"
							+ URLIMG + "'data-title='" + URLIMG + "'> <img src='img/logo1.jpg'alt='" + URLIMG
							+ "'></a><hr size='1'><br><p>Здравствуйте!</p><br>"
							+ "	<p>Для подтверждения регистрации на www.testingwords.ru Вам"
							+ "	необходимо пройти по ссылке:</p><br><p><a href='" + URLACTIVATE + active
							+ "'>подтвердить регистрацию</a></p><p>С уважением,</p>"
							+ "	<p>testingwords.ru</p></div></body></html> ";
					try {
						new mailSend().send(email, message, TXTREG);
						result.put("result", "на ваш адрес отправлена ссылка подтверждения");
						System.out.println(result);
					} catch (AddressException e) {
						e.printStackTrace();
						LOG_MAIL.error("Error in AddressException " + "e.getMessage()=" + e.getMessage()
								+ " e.toString()=" + e.toString());
						if (daoUser.resExistsByMailSoltPassWithoutActive(email, pass) > 0) {
							daoUser.resDellUser(email, pass);
							System.out.println("учетка удалена");
						}
						System.out.println("ошибка в блоке english.html отправка пароля");
						result.put("error", "ошибка отправки сообщения, попробуйте позже");
					} catch (MessagingException e) {
						e.printStackTrace();
						LOG_MAIL.error("Error in MessagingException " + "e.getMessage()=" + e.getMessage()
								+ " e.toString()=" + e.toString());
						if (daoUser.resExistsByMailSoltPassWithoutActive(email, pass) > 0) {
							daoUser.resDellUser(email, pass);
							System.out.println("учетка удалена");
						}
						System.out.println("ошибка в блоке english.html отправка пароля");
						result.put("error", "некорректный адрес электронной почты");
					}
				} else {
					result.put("error", "Пользователь с таким email уже зарегистрирован");
					System.out.println(result);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				LOG_ALL.error("password.html обработка запроса на отправку пароля на почту." + e.getStackTrace());
				System.out.println("ошибка в блоке registration.html");
				result.put("error", "DFLT_ERROR");
				// throw new SQLException();
			} finally {
				daoUser.close();
			}
			return result;
		} else {
			result.put("error", "пароли не совпадают");
			return result;
		}
	}

	// password.html обработка запроса на отправку пароля на почту
	public static JSONObject sendPswForm(String email) {
		JSONObject json = new JSONObject();
		DaoUserJdbc daoUser = new DaoUserJdbc();
		try {
			// проверяем существует ли
			if (daoUser.resExistsByMail(email) != 0) {
				String psw = daoUser.resultPsw(email);
				try {
					String message = "<!DOCTYPE html><html><head><meta charset='utf-8'>"
							+ "	<meta name=viewport content='width=device-width, initial-scale=1.0 '></head>"
							+ "	<body><div><a href='" + URLIMG + "'data-title='" + URLIMG
							+ "'> <img src='img/logo1.jpg'" + "	alt='" + URLIMG
							+ "'></a><hr size='1'><br><p>Здравствуйте!</p><br>"
							+ "	<p>Ваш пароль на www.testingwords.ru: " + psw
							+ "</p><br><p>С уважением,</p><p>testingwords.ru</p></div></body></html>";
					new mailSend().send(email, message, TXTPSW);
					json.put("msg", "Пароль отправлен на Ваш email");
				} catch (AddressException e) {
					e.printStackTrace();
					LOG_MAIL.error("Error in AddressException " + "e.getMessage()=" + e.getMessage() + " e.toString()="
							+ e.toString());
					System.out.println("ошибка в блоке english.html отправка пароля1");
					json.put("error", "ошибка отправки сообщения, попробуйте позже");
				} catch (MessagingException e) {
					e.printStackTrace();
					LOG_MAIL.error("Error in MessagingException " + "e.getMessage()=" + e.getMessage()
							+ " e.toString()=" + e.toString());
					System.out.println("ошибка в блоке english.html отправка пароля2");
					json.put("error", "некорректный адрес электронной почты");
				}
			} else {
				json.put("error", "Пользователь с таким email не зарегистрирован");
				System.out.println(json);
			}
		} catch (SQLException e) {
			LOG_ALL.error("password.html обработка запроса на отправку пароля на почту." + e.getStackTrace());
			e.printStackTrace();
			json.put("error", "Сервис занят, повторите попытку позже");
		} finally {
			daoUser.close();
		}
		return json;
	}

	// login.html обработка формы входа пользователя
	public static DtoUser loginForm(String mail, String pass) throws MyException {
		JSONObject json = new JSONObject();
		DaoUserJdbc daoUser = new DaoUserJdbc();
		DtoUser user = null;
		try {
			String pasSolt = null;
			// проверяем существует ли вводимый паролль
			if (daoUser.resExistsByMail(mail) == 0) {
				throw new MyException("Вы ввели неправильные имя или пароль");
			} else {
				// возвращает соль из БД
				String solt = daoUser.resultSolt(mail);
				try {
					pasSolt = Coder.getHash(solt + pass);
				} catch (NoSuchAlgorithmException | IOException e) {
					throw new MyException("DFLT_ERROR", e);
				}
				// проверяем существует ли пользователь с таким
				// паролем(пасс+солт) и актив
				if (daoUser.resExistsByMailSoltPassWithActive(mail, pasSolt) > 0) {
					user = daoUser.ReadNickResult(mail, pasSolt);
					// обновление даты последнего логина
					daoUser.resUpdData(user.getId());
				} else {
					throw new MyException("Вы ввели неправильные имя или пароль");
				}
			}
		} catch (SQLException e) {
			throw new MyException("DFLT_ERROR", e);
		} finally {
			daoUser.close();
		}
		return user;
	}

	// activate.html переход по ссылке подтверждения регистрации
	public static String finalRegistration(String cod) {
		DaoUserJdbc daoUser = new DaoUserJdbc();
		String result = null;
		try {
			int id = daoUser.resProverka(cod);
			if (id == 0) {
				result = "Учетной записи, ожидающей активации по Вашей ссылке нет";
			} else {
				daoUser.resUpdActive(cod);
				result = "Активация прошла успешно";
			}
		} catch (SQLException e) {
			result = "В процессе активации произошла ошибка, попробуйте позже";
			e.printStackTrace();
			LOG_ALL.error("Error in activate.html переход по ссылке подтверждения регистрации " + e.getStackTrace());
			System.out.println("ошибка в блоке активации учетки");
		} finally {
			daoUser.close();
		}
		return result;
	}

	// dictionary.html(1) вовзращает список тестов усера
	public static ArrayList<DtoUnit> listUsersTests(DtoUser dtoUser) throws MyException {
		DaoUnitJdbc daoUnit = new DaoUnitJdbc();
		ArrayList<DtoUnit> listUnit = null;
		try {
			if (daoUnit.rExistsTestsByUserId(dtoUser) != 0) {
				listUnit = daoUnit.resultAllButStatic(dtoUser);
			}
		} catch (SQLException e) {
			throw new MyException(DFLT_ERROR, e);
		} finally {
			daoUnit.close();
		}
		return listUnit;
	}

	// dictionary.html(2) вовзращает список встроенных тестов
	public static TreeSet<String> setDefaultTests() throws MyException {
		DaoStaticcategoryJdbc daoStaticcategory = new DaoStaticcategoryJdbc();
		TreeSet<String> setUnit = null;
		try {
			setUnit = daoStaticcategory.resultReadDistinctGroupname();
		} catch (SQLException e) {
			throw new MyException(DFLT_ERROR, e);
		} finally {
			daoStaticcategory.close();
		}
		return setUnit;
	}

	// dictionary.html unitdel удаление юнита
	public static void unitDel(String unitName, DtoUser dtoUser) throws MyException {
		int idUser = dtoUser.getId();
		DaoUnitJdbc daoUnit = new DaoUnitJdbc();
		DaoUwordJdbc daoUword = new DaoUwordJdbc();
		DaoWordJdbc daoWord = new DaoWordJdbc();
		try {

			// проверяем есть ли такой юнит у пользователя
			if (daoUnit.rProverkaUnit(unitName, idUser) != 0) {
				int idUnit = daoUnit.resultId(unitName, idUser).getId();
				Iterator<DtoWord> iter = daoWord.resultListFromUnit(idUnit).iterator();
				while (iter.hasNext()) {
					int idWord = iter.next().getId();
					// проверка есть ли изменяемое слово в таблице
					// статикворд
					int existStatic = daoWord.proverkaStaticById(idWord);
					// естьли меняемое слово в других юнитах всех усероф
					int existOtherUnit = daoWord.proverkaOtherUn(idWord, unitName, idUser);
					// если изменяемого слова нет в других юнтак и
					// статике
					if ((existStatic == 0) && (existOtherUnit == 0)) {
						// удаляем изменяемое слово из unit усера
						daoWord.resultDeleteFromUnit(idWord, unitName, idUser);
						// удаляем изменяемое слово из word(Db)
						daoWord.resultDeleteFromDb(idWord);
					} else {
						// удаляем изменяемое слово из unit усера
						daoWord.resultDeleteFromUnit(idWord, unitName, idUser);
					}
				}
				daoUword.resultDelete(idUnit);
				daoUnit.resultDelete(unitName, idUser);
			}
		} catch (SQLException e) {
			throw new MyException(DFLT_ERROR, e);
		} finally {
			daoUnit.close();
			daoUword.close();
			daoWord.close();
		}
	}

	// dictionary.html ratingreset сбрасывает значение оценки в unit
	// пользователя
	public static JSONObject resetRating(int rating) throws MyException {
		JSONObject jobject = new JSONObject();
		DaoUnitJdbc daoUnit = new DaoUnitJdbc();
		try {
			daoUnit.rResetOcenka(rating);
			jobject.put("result", "0");
		} catch (NumberFormatException | SQLException e) {
			throw new MyException(DFLT_ERROR, e);
		} finally {
			daoUnit.close();
		}
		return jobject;
	}

	// staticunit.html(1)если усер не залогинен то список выбирает из
	// статиккатеори без оценки
	public static ArrayList<DtoStaticcategory> listStaticcategory(String groupname) throws MyException {
		ArrayList<DtoStaticcategory> listStaticcategory = null;
		DaoStaticcategoryJdbc daoStatcat = new DaoStaticcategoryJdbc();
		try {
			listStaticcategory = daoStatcat.getUnitnameByGroupname(groupname);
		} catch (SQLException e) {
			throw new MyException(DFLT_ERROR, e);
		} finally {
			daoStatcat.close();
		}
		return listStaticcategory;
	}

	// staticunit.html(2)если усер залогинен
	public static ArrayList<DtoStaticrating> listStaticrating(DtoUser user, String groupname) throws MyException {
		ArrayList<DtoStaticrating> dtoStatratList = new ArrayList<DtoStaticrating>();
		DaoStaticcategoryJdbc daoStatcat = new DaoStaticcategoryJdbc();
		DaoStaticratingJdbc daoStatrating = new DaoStaticratingJdbc();
		// получаем список staticcategory по полю groupname
		try {
			ArrayList<DtoStaticcategory> dtoStatcatArr = daoStatcat.getUnitnameByGroupname(groupname);
			// проходим по списку и проверяем
			for (DtoStaticcategory statcat : dtoStatcatArr) {
				// если записи в staticrating нет, то добавляем
				// после читаем
				if (daoStatrating.resExists(statcat.getId(), user.getId()) == 0) {
					daoStatrating.CreatResult(statcat.getId(), user.getId());
					dtoStatratList.add(daoStatrating.getByUserStatcat(statcat, user.getId()));
				} else { // если запись есть, то читаем ее
					dtoStatratList.add(daoStatrating.getByUserStatcat(statcat, user.getId()));
				}
			}
			/*
			 * dtoStatratList.sort(new Comparator<DtoStaticrating>() { public
			 * int compare(DtoStaticrating o1, DtoStaticrating o2) { return
			 * o1.getStaticcategory().getUnitname()
			 * .compareTo(o2.getStaticcategory().getUnitname()); } });
			 */
		} catch (SQLException e) {
			throw new MyException(DFLT_ERROR, e);
		} finally {
			daoStatcat.close();
			daoStatrating.close();
		}
		return dtoStatratList;
	}

	// staticunit.html ratingreset сбрасывает оценку в статикунитах
	public static JSONObject resetRatingInStatic(int rating) throws MyException {
		JSONObject jobject = null;
		DaoStaticratingJdbc daoStatrat = new DaoStaticratingJdbc();
		try {
			daoStatrat.rResetOcenka(rating);
			jobject.put("result", "0");
		} catch (NumberFormatException | SQLException e) {
			throw new MyException(DFLT_ERROR, e);
		} finally {
			daoStatrat.close();
		}
		return jobject;
	}

	// staticunit.html staticcategoryId
	public static JSONArray listWordsStaticunit(int statcatId) throws MyException {
		DaoStaticcategoryJdbc daoStatcat = new DaoStaticcategoryJdbc();
		DaoWordJdbc daoWord = new DaoWordJdbc();
		ArrayList<DtoWord> listWord = null;
		JSONArray jsnArray = new JSONArray();
		try {
			// проверяем существование в БД юнита
			if (daoStatcat.proverkaById(statcatId) > 0) {
				System.out.println(statcatId);
				listWord = daoWord.resultListFromStaticunit(statcatId);
				int listSize = listWord.size();
				// Если в unit содержатся слова
				if (listSize > 0) {
					jsnArray = daoWord.getWordInJson(listWord);
					// Если в unit нет слов
				} else {
					jsnArray.add("В этом Unit нет слов");
				}
			} else {
				jsnArray.add(DFLT_ERROR);
			}
		} catch (NumberFormatException | SQLException e) {
			throw new MyException(e);
		} finally {
			daoWord.close();
			daoStatcat.close();
		}
		return jsnArray;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// staticunit.html выбор уроков для тестирования статикюниты(1)
	public static ArrayList<DtoStaticunit> listStaticunit(String[] idstatcat) throws MyException {
		ArrayList<DtoStaticunit> listDtoStaticunit = null;
		DaoStaticunitJdbc daoStatunit = new DaoStaticunitJdbc();
		try {
			listDtoStaticunit = daoStatunit.resulListByArrayIdstatcat(idstatcat);
		} catch (SQLException e) {
			throw new MyException(DFLT_ERROR, e);
		} finally {
			daoStatunit.close();
		}
		return listDtoStaticunit;
	}

	// staticunit.html ВЫБОР УРОКОВ ДЛЯ ТЕСТИРОВАНИЯ СТАТИКЮНИТ (2)
	public static JSONObject chooseStaticunitTest(String[] idstatcat, String testVariant) throws MyException {
		DtoStaticunit dtoStaticunit;
		// json массив в котором будут слова переданные клиенту
		JSONArray jsnarrayWords = new JSONArray();
		JSONObject json = new JSONObject();
		try {
			// получаем список выбранных статикюнитов
			ArrayList<DtoStaticunit> statunitArray = listStaticunit(idstatcat);
			int kolvo = statunitArray.size();
			// кол-во слов,оставшихся слов,правильных ответов
			int[] result = { kolvo, kolvo, 0 };
			// если в выбранных тестах 0 слов
			if (kolvo == 0) {
				json.put("msg", "К сожалению в выбранных словарях пока нет слов, попроуйте позже");
			} else {
				// если тест "вариант"
				if (testVariant.equals("variant")) {
					// если в выбранных тестах больше 1ого слова
					if (kolvo > 1) {
						ArrayList<Integer> generated = new ArrayList<Integer>();
						Random rndm = new Random();
						// если в тестах не меньше 4слов
						if (kolvo >= 4) {
							while (generated.size() < 4) {
								int num = rndm.nextInt(kolvo);
								if (!generated.contains(num)) {
									generated.add(num);
								}
							}
							// если в тестах меньше 4рех слов
						} else {
							while (generated.size() < kolvo) {
								int num = rndm.nextInt(kolvo);
								if (!generated.contains(num)) {
									generated.add(num);
								}
							}
						}
						for (int i = 1; i < generated.size(); i++) {
							jsnarrayWords.add(statunitArray.get(generated.get(i)).toJSON());
						}
						int ferstWord = generated.get(0);
						dtoStaticunit = statunitArray.remove(ferstWord);
						jsnarrayWords.add(dtoStaticunit.toJSON());
					} else {
						dtoStaticunit = statunitArray.remove(0);
						jsnarrayWords.add(dtoStaticunit.toJSON());
					}
				}
				// если тест "ввод"
				if (testVariant.equals("vvod")) {
					dtoStaticunit = statunitArray.remove(new Random().nextInt(kolvo));
					jsnarrayWords.add(dtoStaticunit.toJSON());
				}
				json.put("arraywords", jsnarrayWords);
				json.put("resultToPage", jsonKolvo(result));
				// для сохраняем в сессии коллекцию слов для теста
				json.put("statunitArray", statunitArray);
				// для сохраняем в сессию массив созначениями резальтатов
				// для клиента
				json.put("resultToSession", result);
			}
		} catch (MyException e) {
			throw new MyException(DFLT_ERROR, e);
		}
		return json;
	}

	// staticunit.html проверка слов(1). Изменяем оценку, если пользователь
	// залогинен и сессия жива.
	public static void updateRatingInStatictest(String unitid, DtoUser dtoUser, boolean answerResult)
			throws MyException {
		DaoStaticratingJdbc daoStatrat = new DaoStaticratingJdbc();
		try {
			int unitId = Integer.parseInt(unitid);
			// получаем dtoStaticrating где нужно изменить
			// оценку
			DtoStaticrating dtoStatrat = daoStatrat.getByIduserIdstatcat(unitId, dtoUser.getId());
			if (answerResult) {
				// обновляем оценку в поле truue
				daoStatrat.rUpdateOcenka("truue", dtoStatrat.getId());
			} else {
				// обновляем оценку в поле faalse
				daoStatrat.rUpdateOcenka("faalse", dtoStatrat.getId());
			}
		} catch (SQLException | NullPointerException | NumberFormatException e) {
			throw new MyException(DFLT_ERROR, e);
		} finally {
			daoStatrat.close();
		}
	}

	// staticunit.html проверка слов(2). Следующее слово из теста
	public static JSONArray nextWordInStatictest(ArrayList<DtoStaticunit> statunitArray, int kolvo, String testVariant)
			throws MyException {
		JSONArray jsnarrayWords = new JSONArray();
		DtoStaticunit dtoStatunit;
		try {
			// если тест "вариант"
			if (testVariant.equals("variant")) {
				// если в выбранных тестах больше 1ого слова
				if (kolvo > 1) {
					ArrayList<Integer> generated = new ArrayList<Integer>();
					Random rndm = new Random();
					// если в тестах не меньше 4слов
					if (kolvo >= 4) {
						while (generated.size() < 4) {
							int num = rndm.nextInt(kolvo);
							if (!generated.contains(num)) {
								generated.add(num);
							}
						}
						// если в тестах меньше 4рех слов
					} else {
						while (generated.size() < kolvo) {
							int num = rndm.nextInt(kolvo);
							if (!generated.contains(num)) {
								generated.add(num);
							}
						}
					}
					for (int i = 1; i < generated.size(); i++) {
						jsnarrayWords.add(statunitArray.get(generated.get(i)).toJSON());
					}
					int ferstWord = generated.get(0);
					dtoStatunit = statunitArray.remove(ferstWord);
					jsnarrayWords.add(dtoStatunit.toJSON());
				} else {
					dtoStatunit = statunitArray.remove(0);
					jsnarrayWords.add(dtoStatunit.toJSON());
				}
			}
			// если тест "ввод"
			if (testVariant.equals("vvod")) {
				dtoStatunit = statunitArray.remove(new Random().nextInt(kolvo));
				jsnarrayWords.add(dtoStatunit.toJSON());
			}
		} catch (NullPointerException e) {
			throw new MyException(DFLT_ERROR, e);
		}
		return jsnarrayWords;
	}

	// test.html/json выбор уроков для тестирования в пользовательских юнитах(1)
	public static ArrayList<DtoUrword> listUrword(String[] units, DtoUser dtoUser) throws MyException {
		ArrayList<DtoUrword> listDtoUrword = null;
		DaoUwordJdbc uwordDao = new DaoUwordJdbc();
		try {
			listDtoUrword = uwordDao.ReadNickResult(units, dtoUser.getId());
		} catch (SQLException e) {
			throw new MyException(DFLT_ERROR, e);
		} finally {
			uwordDao.close();
		}
		return listDtoUrword;
	}

	// test.html/json выбор уроков для тестирования в пользовательских
	// юнитах(2)
	public static JSONObject chooseUserUnitTest(String[] unit, String testVariant, DtoUser dtoUser) throws MyException {
		DtoUrword uwordDto;
		// json массив в котором будут слова переданные клиенту
		JSONArray jsnarrayWords = new JSONArray();
		JSONObject json = new JSONObject();
		try {
			ArrayList<DtoUrword> urwordArray = Service.listUrword(unit, dtoUser);
			int kolvo = urwordArray.size();
			// кол-во слов,оставшихся слов,правильных ответов
			int[] result = { kolvo, kolvo, 0 };
			// если в выбранных тестах 0 слов
			if (kolvo == 0) {
				json.put("msg",
						"К сожалению в выбранных словарях пока нет слов, наполните словами и повторите попытку");
			} else {
				// если тест "вариант"
				if (testVariant.equals("variant")) {
					// если в выбранных тестах больше 1ого слова
					if (kolvo > 1) {
						ArrayList<Integer> generated = new ArrayList<Integer>();
						Random rndm = new Random();
						// если в тестах не меньше 4слов
						if (kolvo >= 4) {
							while (generated.size() < 4) {
								int num = rndm.nextInt(kolvo);
								if (!generated.contains(num)) {
									generated.add(num);
									System.out.println(num);
								}
							}
							// если в тестах меньше 4рех слов
						} else {
							while (generated.size() < kolvo) {
								int num = rndm.nextInt(kolvo);
								if (!generated.contains(num)) {
									generated.add(num);
								}
							}
						}
						for (int i = 1; i < generated.size(); i++) {
							jsnarrayWords.add(urwordArray.get(generated.get(i)).toJSON());
						}
						int ferstWord = generated.get(0);
						uwordDto = urwordArray.remove(ferstWord);
						jsnarrayWords.add(uwordDto.toJSON());
					} else {
						uwordDto = urwordArray.remove(0);
						jsnarrayWords.add(uwordDto.toJSON());
					}
				}
				// если тест "ввод"
				if (testVariant.equals("vvod")) {
					uwordDto = urwordArray.remove(new Random().nextInt(kolvo));
					jsnarrayWords.add(uwordDto.toJSON());
				}
				json.put("arraywords", jsnarrayWords);
				json.put("resultToPage", jsonKolvo(result));
				// сохраняем в сессии коллекцию слов для теста
				json.put("urwordArray", urwordArray);
				// сохраняем в сессию массив созначениями резальтатов
				// для клиента
				json.put("resultToSession", result);
			}
		} catch (MyException e) {
			throw new MyException(DFLT_ERROR, e);
		}
		return json;
	}

	// test.html/json проверка слов в пользовательских юнитах(1).Обновляем
	// оценку
	public static void updateRatingInUserTest(String unitid, boolean answerResult) throws MyException {
		DaoUnitJdbc forOcenka = new DaoUnitJdbc();
		try {
			int unitId = Integer.parseInt(unitid);
			if (answerResult) {
				// обновляем оценку в поле truue
				forOcenka.rUpdateOcenka("truue", unitId);
			} else {
				// обновляем оценку в поле faalse
				forOcenka.rUpdateOcenka("faalse", unitId);
			}
		} catch (NumberFormatException | NullPointerException | SQLException e) {
			throw new MyException(DFLT_ERROR, e);
		} finally {
			forOcenka.close();
		}
	}

	// test.html/json проверка слов в пользовательских юнитах(2).готовим
	// следующее слово
	public static JSONArray nextWordInUserTest(ArrayList<DtoUrword> urwordArray, int kolvo, String testVariant)
			throws MyException {
		DtoUrword uwordDto;
		JSONArray jsnarrayWords = new JSONArray();
		try {
			// если тест "вариант"
			if (testVariant.equals("variant")) {
				// если в выбранных тестах больше 1ого слова
				if (kolvo > 1) {
					ArrayList<Integer> generated = new ArrayList<Integer>();
					Random rndm = new Random();
					// если в тестах не меньше 4слов
					if (kolvo >= 4) {
						while (generated.size() < 4) {
							int num = rndm.nextInt(kolvo);
							if (!generated.contains(num)) {
								generated.add(num);
								System.out.println(num);
							}
						}
						// если в тестах меньше 4рех слов
					} else {
						while (generated.size() < kolvo) {
							int num = rndm.nextInt(kolvo);
							if (!generated.contains(num)) {
								generated.add(num);
							}
						}
					}
					for (int i = 1; i < generated.size(); i++) {
						jsnarrayWords.add(urwordArray.get(generated.get(i)).toJSON());
					}
					int ferstWord = generated.get(0);
					uwordDto = urwordArray.remove(ferstWord);
					jsnarrayWords.add(uwordDto.toJSON());
				} else {
					uwordDto = urwordArray.remove(0);
					jsnarrayWords.add(uwordDto.toJSON());
				}
			}
			// если тест "ввод"
			if (testVariant.equals("vvod")) {
				uwordDto = urwordArray.remove(new Random().nextInt(kolvo));
				jsnarrayWords.add(uwordDto.toJSON());
			}
		} catch (NullPointerException e) {
			throw new MyException(DFLT_ERROR, e);
		}
		return jsnarrayWords;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// wordarray.html возвращает список слов JSONArray при нажатии на UnitUsera
	public static JSONArray listWordsUserUnit(int unitId) throws MyException {
		DaoWordJdbc daoWord = new DaoWordJdbc();
		DaoUnitJdbc daoUnit = new DaoUnitJdbc();
		ArrayList<DtoWord> listWord = null;
		JSONArray jsnArray = new JSONArray();
		try {
			// проверяем существование в БД юнита
			if (daoUnit.rProverkaId(unitId) > 0) {
				listWord = daoWord.resultListFromUnit(unitId);
				int listSize = listWord.size();
				// Если в unit содержатся слова
				if (listSize > 0) {
					jsnArray = daoWord.getWordInJson(listWord);
					// Если в unit нет слов
				} else {
					jsnArray.add("В этом Unit нет слов");
				}
			} else {
				jsnArray.add(DFLT_ERROR);
			}
		} catch (NumberFormatException | SQLException e) {
			throw new MyException(e);
		} finally {
			daoUnit.close();
			daoWord.close();
		}
		return jsnArray;
	}

	// unitadd.html?newUnit
	public static JSONObject newUnit(String unitName, DtoUser dtoUser) throws MyException {
		JSONObject json = new JSONObject();
		DaoUnitJdbc daoUnit = new DaoUnitJdbc();
		int idUser = dtoUser.getId();
		try {
			// проверяем нет ли в бд юнита с именем, которое хотят
			// использовать
			if (daoUnit.rProverkaUnit(unitName, idUser) > 0) {
				json.put("msg", "В БД уже существует Unit с именем " + unitName);
			} else {
				daoUnit.CreatResult(unitName, idUser);
				json.put("redirect", "dictionary.html");
			}
		} catch (SQLException e) {
			throw new MyException(e);
		} finally {
			daoUnit.close();
		}
		return json;
	}

	// unitrename.html Переименование Unita
	public static JSONObject renameUnit(String oldUnit, String newUnit, DtoUser dtoUser) throws MyException {
		JSONObject json = new JSONObject();
		DaoUnitJdbc daoUnit = new DaoUnitJdbc();
		int idUser = dtoUser.getId();
		System.out.println(newUnit + " " + oldUnit + " " + idUser);
		try {
			// проверяем существование в БД юнита, который хотят
			// переименовать
			if (daoUnit.rProverkaUnit(oldUnit, idUser) == 0) {
				json.put("msg", "В БД не обнаружен такой Unit");
			} else {
				// проверяем нет ли в бд юнита с именем, которое хотят
				// использовать
				if (daoUnit.rProverkaUnit(newUnit, idUser) > 0) {
					json.put("msg", "В БД уже существует Unit с именем " + newUnit);
				} else {
					System.out.println(newUnit + " " + oldUnit + " " + idUser);
					daoUnit.rUpdateName(newUnit, oldUnit, idUser);
					json.put("redirect", "dictionary.html");
				}
			}
		} catch (SQLException e) {
			throw new MyException(e);
		} finally {
			daoUnit.close();
		}
		return json;
	}

	// unitdell.html удаление Unita

	// unitedit.html страница редактировать содержимого unit
	public static JSONObject unitEdit(String unit, DtoUser dtoUser) throws MyException {
		DaoWordJdbc daoWord = new DaoWordJdbc();
		DaoUnitJdbc daoUnit = new DaoUnitJdbc();
		int idUser = dtoUser.getId();
		JSONObject json = new JSONObject();
		try {
			int unitId = Integer.parseInt(unit);
			// проверяем существование в БД юнита
			if (daoUnit.rProverkaId(unitId) == 0) {
				throw new MyException(INCORRECT_DATA);
			} else {
				ArrayList<DtoWord> arrayWord = daoWord.resultListFromUnit(unitId);
				arrayWord.sort(new Comparator<DtoWord>() {
					public int compare(DtoWord o1, DtoWord o2) {
						return o1.getWord().compareTo(o2.getWord());
					}
				});
				json.put("resultListWord", arrayWord);
				json.put("unitName", daoUnit.resultUnitId(unitId, idUser).getUnit());
			}
		} catch (NumberFormatException | SQLException e) {
			throw new MyException(DFLT_ERROR, e);
		} finally {
			daoUnit.close();
			daoWord.close();
		}
		return json;
	}

	// unitedit.html меняем слово
	public static JSONObject wordEdit(String oldWord, String oldTranslate, String newWord, String newTranslate,
			String unitName, DtoUser dtoUser) throws MyException {
		DaoWordJdbc daoWord = new DaoWordJdbc();
		DaoUnitJdbc daoUnit = new DaoUnitJdbc();
		DaoUwordJdbc daoUword = new DaoUwordJdbc();
		int idUser = dtoUser.getId();
		JSONObject json = new JSONObject();
		try {
			// проверяем существование слова, которе хотим изменить в
			// unit пользователя
			int oldExistUn = daoWord.proverkaWordCustom(idUser, unitName, oldWord, oldTranslate);
			// Получаем id unita
			int idUnit = daoUnit.resultId(unitName, idUser).getId();
			if (oldExistUn == 0) {
				json.put("msg", "В Вашем словаре не обнаружено " + oldWord + "-" + oldTranslate);
			} else {
				// проверяем существование слова, НА которе хотим
				// изменить в unit пользователя
				int newExistUn = daoWord.proverkaWordCustom(idUser, unitName, newWord, newTranslate);
				if (newExistUn == 1) {
					json.put("msg", "В Вашем словаре уже существует " + newWord + "-" + newTranslate);
				} else {
					// получаем id oldWord
					int idOldWord = daoWord.getIdWord(oldWord, oldTranslate);
					// проверка есть ли изменяемое слово в таблице
					// статикворд
					int oldExistSu = daoWord.proverkaStaticById(idOldWord);
					// естьли меняемое слово в других юнитах всех усероф
					int oldExistOther = daoWord.proverkaOtherUn(idOldWord, unitName, idUser);
					// нет ли УЖЕ слова на которое меняем в БД
					int newExistDb = daoWord.proverkaWord(newWord, newTranslate);
					// если изменяемого слова нет в других юнтак и
					// статике
					if ((oldExistSu == 0) && (oldExistOther == 0)) {
						System.out.println("если изменяемого слова нет в других юнтак и статике");
						// если нового слова нет в БД
						if (newExistDb == 0) {
							daoWord.resultUpdate(newWord, newTranslate, idOldWord);
						} else {// если newWord есть в word(DB)
								// получаем id newWord
							int idNewWord = daoWord.getIdWord(newWord, newTranslate);
							// удаляем изменяемое слово из unit усера
							daoWord.resultDeleteFromUnit(idOldWord, unitName, idUser);
							// удаляем изменяемое слово из word(Db)
							daoWord.resultDeleteFromDb(idOldWord);
							// добавляем новое слово в unit усера
							daoUword.CreatResult(idUnit, idNewWord);
						}
					} else {// если изменяемое слово есть в ругих уроках
						System.out.println("если изменяемое слово есть в ругих уроках");
						// удаляем изменяемое слово из unit усера
						daoWord.resultDeleteFromUnit(idOldWord, unitName, idUser);
						if (newExistDb == 0) {// Если нового слова нет в
							// word(Db)
							// добавляем новое слово в БД
							daoWord.CreatResult(newWord, newTranslate, null, null);
							// получаем id нового слова
							int idNewWord = daoWord.getIdWord(newWord, newTranslate);
							// добавляем новое слово в unit усера
							daoUword.CreatResult(idUnit, idNewWord);
						} else {// если новое слово есть в
								// word(DataBase)
								// получаем id newWord
							int idNewWord = daoWord.getIdWord(newWord, newTranslate);
							// добавляем новое слово в unit усера
							daoUword.CreatResult(idUnit, idNewWord);
						}
					}
				}
			}
			json.put("redirect", "unitedit.html?unit=" + idUnit);
		} catch (SQLException e) {
			throw new MyException(e);
		} finally {
			daoUnit.close();
			daoWord.close();
			daoUword.close();
		}
		return json;
	}

	// unitedit.html?copy обработка кнопки копировать, возвращает json список
	// unitов для выбора куда копировать
	public static JSONArray wordCopyListUnits(DtoUser dtoUser) throws MyException {
		JSONArray jsnArray = new JSONArray();
		DaoUnitJdbc daoUnit = new DaoUnitJdbc();
		try {
			Iterator<DtoUnit> iter = daoUnit.resultAllButStatic(dtoUser).iterator();
			while (iter.hasNext()) {
				jsnArray.add(iter.next().toJSON());
			}
		} catch (SQLException e) {
			throw new MyException(e);
		} finally {
			daoUnit.close();
		}
		return jsnArray;
	}

	// unitedit.html обработка кнопки удалить слова
	public static JSONObject wordDell(String unitName, String[] wordsId, DtoUser dtoUser) throws MyException {
		int idUser = dtoUser.getId();
		int masSize = wordsId.length;
		DaoWordJdbc daoWord = new DaoWordJdbc();
		DaoUwordJdbc daoUword = new DaoUwordJdbc();
		DaoUnitJdbc daoUnit = new DaoUnitJdbc();
		JSONObject jsnObj = new JSONObject();
		try {
			// Получаем id unita
			int idUnit = daoUnit.resultId(unitName, idUser).getId();
			for (int a = 0; a < masSize; a++) {
				// проверка на наличие слова в юните откуда хотим
				// удалить
				if (daoUword.proverkaUword(Integer.parseInt(wordsId[a]), idUnit) > 0) {
					// проверка наличие удаляемого слова в аблице
					// статикворд
					int existStatic = daoWord.proverkaStaticById(Integer.parseInt(wordsId[a]));
					// проверка наличие удаляемого слова в других юнитах
					// всех усероф
					int existOther = daoWord.proverkaOtherUn(Integer.parseInt(wordsId[a]), unitName, idUser);
					// если изменяемого слова нет в других юнтак и
					// статике
					if ((existStatic == 0) && (existOther == 0)) {
						System.out.println("existStatic =" + existStatic);
						System.out.println("existStatic =" + existStatic);
						System.out.println(wordsId[a]);
						// удаляем слово из юнита усера
						daoWord.resultDeleteFromUnit(Integer.parseInt(wordsId[a]), unitName, idUser);
						// удаляем слово из word(Db)
						daoWord.resultDeleteFromDb(Integer.parseInt(wordsId[a]));
					} else {
						// удаляем слово из юнита усера
						daoWord.resultDeleteFromUnit(Integer.parseInt(wordsId[a]), unitName, idUser);
					}
				}
			}
			jsnObj.put("redirect", "unitedit.html?unit=" + idUnit);

		} catch (NullPointerException | NumberFormatException | SQLException e) {
			throw new MyException(e);
		} finally {
			daoUnit.close();
			daoWord.close();
			daoUword.close();

		}
		return jsnObj;
	}

	// unitedit.html обработка кнопки продолжить копировать
	public static void wordCopy(String unitForCopy, String[] wordsId) throws MyException {
		int unitId = Integer.parseInt(unitForCopy);
		int masSize = wordsId.length;
		DaoUwordJdbc daoUword = new DaoUwordJdbc();
		try {
			for (int a = 0; a < masSize; a++) {
				// проверяем если слова нет в юните куда хотим
				// скопировать его
				if (daoUword.proverkaUword(Integer.parseInt(wordsId[a]), unitId) == 0) {
					daoUword.CreatResult(unitId, Integer.parseInt(wordsId[a]));
				}
			}
		} catch (NullPointerException | NumberFormatException | SQLException e) {
			throw new MyException(e);
		} finally {
			daoUword.close();
		}
	}

	// search.hrml
	public static JSONArray search(String srch, String lang, String srchWords, DtoUser dtoUser) throws MyException {
		DaoWordJdbc daoWord = new DaoWordJdbc();
		JSONArray jsnArray = new JSONArray();
		try {
			// возвращает подсказки в посик
			if ((srch != null) && (lang != null)) {
				jsnArray = daoWord.getSearchJson(srch, dtoUser.getId(), lang);
			} else {
				// Запускается после выбора слова из подсказки поиска
				if (srchWords != null) {
					jsnArray = daoWord.rSearchWord(srchWords, dtoUser.getId());
				}
			}
		} catch (NullPointerException | SQLException e) {
			throw new MyException(e);
		} finally {
			daoWord.close();
		}
		return jsnArray;
	}

	/////////////////////////////////////////////////////////////////////////////////////
	// ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
	// добавляет к json объекту поля с инф. текущем результате прохождения теста
	private static JSONObject jsonKolvo(int[] result) {
		JSONObject json = new JSONObject();
		json.put("all", result[0]);
		json.put("ost", result[1]);
		json.put("res", result[2]);
		return json;
	}
}
