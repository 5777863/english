package my.english.dao.jdbc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import my.english.dto.DtoUnit;
import my.english.dto.DtoUrword;
import my.english.dto.DtoUser;
import my.english.dto.DtoWord;

public class DaoWordJdbc {
	private Connection connect;
	private PreparedStatement creat;
	private PreparedStatement read;
	private PreparedStatement update;
	private PreparedStatement deleteFromDb;
	private PreparedStatement deleteFromUnit;
	private PreparedStatement readW;
	private PreparedStatement readId;
	private PreparedStatement readIdstatic;
	private PreparedStatement proverka;
	private PreparedStatement proverkaVerbs;
	private PreparedStatement proverkaStaticById;
	private PreparedStatement proverkaCustom;
	private PreparedStatement proverkaOtherUn;
	private PreparedStatement kolvoWordExist;
	private PreparedStatement readListFromUnit;
	private PreparedStatement readListFromStaticunit;
	private PreparedStatement readSearch;
	private PreparedStatement resultSearch;

	private static final Logger LOG = LogManager.getLogger("allExcept");

	private Connection getConnect() throws SQLException {
		if (connect == null) {
			Properties prop = new Properties();
			try {
				InputStream input = getClass().getClassLoader().getResourceAsStream("prop.ini");
				// InputStream input =
				// getClass().getResourceAsStream("prop.ini");
				prop.load(input);
				input.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				LOG.error(e.getStackTrace());
			} catch (IOException e) {
				e.printStackTrace();
				LOG.error(e.getStackTrace());
			}
			String driver = prop.getProperty("driver");
			String url = prop.getProperty("url");
			String user = prop.getProperty("user");
			String pass = prop.getProperty("pass");

			try {
				Class.forName(driver);
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
			connect = DriverManager.getConnection(url, user, pass);
		}
		return connect;
	}

	private PreparedStatement getCreat() throws SQLException {
		if (creat == null) {
			creat = getConnect()
					.prepareStatement("insert into word(word, translate, form2, form3) values (?, ?, ?, ?)");
		}
		return creat;
	}

	private PreparedStatement getReadListFromUnit() throws SQLException {
		if (readListFromUnit == null) {
			readListFromUnit = getConnect().prepareStatement(
					"select w.id, w.word, w.translate from unit u INNER JOIN uword uw ON u.id = uw.idunit "
							+ "INNER JOIN word w ON uw.idword = w.id where u.id=?");
		}
		return readListFromUnit;
	}

	// возвращаем список вордов по idstatcat из staticunit
	private PreparedStatement getReadListFromStaticunit() throws SQLException {
		if (readListFromStaticunit == null) {
			readListFromStaticunit = getConnect()
					.prepareStatement("select w.id, w.word, w.translate, w.form2, w.form3 from word w "
							+ "INNER JOIN staticunit su ON w.id = su.idword where su.idstatcat=?");
		}
		return readListFromStaticunit;
	}

	private PreparedStatement getRead() throws SQLException {
		if (read == null) {
			read = getConnect().prepareStatement("SELECT word, translate from word where id = ?");
		}
		return read;
	}

	private PreparedStatement getReadId() throws SQLException {
		if (readId == null) {
			readId = getConnect().prepareStatement("SELECT id from word where word = ?  and translate = ?");
		}
		return readId;
	}

	private PreparedStatement getReadIdstatic() throws SQLException {
		if (readIdstatic == null) {
			readIdstatic = getConnect()
					.prepareStatement("SELECT id from word where word = ?  and translate = ? and form2=? and form3=?");
		}
		return readIdstatic;
	}

	// update слова по его id
	private PreparedStatement getUpdate() throws SQLException {
		if (update == null) {
			update = getConnect().prepareStatement("Update word set word=?, translate=? where id=?");
		}
		return update;
	}

	// удаляем слово из БД
	private PreparedStatement getDeleteFromDb() throws SQLException {
		if (deleteFromDb == null) {
			deleteFromDb = getConnect().prepareStatement("DELETE from word WHERE id = ?");
		}
		return deleteFromDb;
	}

	// удаляем слово из Unit
	private PreparedStatement getDeleteFromUnit() throws SQLException {
		if (deleteFromUnit == null) {
			deleteFromUnit = getConnect().prepareStatement(
					"DELETE  uword from uword INNER JOIN  unit ON unit.id = uword.idunit WHERE uword.idword = ? and unit.unit=? and unit.iduser=?");
		}
		return deleteFromUnit;
	}

	// проверяет содержится слово в таблице статикюнит
	private PreparedStatement getProverkaVerbs() throws SQLException {
		if (proverkaVerbs == null) {
			proverkaVerbs = getConnect().prepareStatement(
					"select exists (select id FROM word WHERE word = ? and translate = ? and form2=? and  form3=?)");
		}
		return proverkaVerbs;
	}

	// проверяет содержится слово в таблице статикюнит по idword
	private PreparedStatement getProverkaStaticById() throws SQLException {
		if (proverkaStaticById == null) {
			proverkaStaticById = getConnect()
					.prepareStatement("select exists (select id FROM staticunit WHERE idword = ?)");
		}
		return proverkaStaticById;
	}

	// поверка содержится ли слово в таблице ворд(БД)
	private PreparedStatement getProverka() throws SQLException {
		if (proverka == null) {
			proverka = getConnect()
					.prepareStatement("select exists (select id FROM word WHERE word = ? and translate = ?)");
		}
		return proverka;
	}

	// проверяем существует ли слово и перевод у усера в уните
	private PreparedStatement getProverkaCustom() throws SQLException {
		if (proverkaCustom == null) {
			proverkaCustom = getConnect().prepareStatement(
					"select exists (select * from word w INNER JOIN uword uw ON w.id=uw.idword INNER JOIN "
							+ "unit u ON uw.idunit=u.id where u.iduser=? and u.unit=? and w.word=? and w.translate=?)");
		}
		return proverkaCustom;
	}

	// проверяем существует ли слово в юнитах кроме указанного
	private PreparedStatement getProverkaOtherUn() throws SQLException {
		if (proverkaOtherUn == null) {
			proverkaOtherUn = getConnect()
					.prepareStatement("select exists (select id from uword where idword=? and idunit "
							+ "not in(select id from unit where unit=? and iduser=? ))");
		}
		return proverkaOtherUn;
	}

	// поиск совпадений словах
	private PreparedStatement getSearch() throws SQLException {
		if (readSearch == null) {
			readSearch = getConnect().prepareStatement(
					"select w.word, w.translate, u.unit from unit u INNER JOIN uword uw ON u.id = uw.idunit INNER JOIN word w ON uw.idword = w.id where w.translate like ? or w.word like ? and u.iduser = ?");
		}
		return readSearch;
	}

	// поиск всех слов по слову и усеру
	private PreparedStatement getResultSearch() throws SQLException {
		if (resultSearch == null) {
			resultSearch = getConnect().prepareStatement(
					"select w.word, w.translate, u.unit from unit u INNER JOIN uword uw ON u.id = uw.idunit INNER JOIN word w ON uw.idword = w.id where w.word = ? or w.translate = ? and u.iduser = ?");
		}
		return resultSearch;
	}

	public void CreatResult(String word, String translate, Object form2, Object form3) throws SQLException {
		getCreat().setString(1, word);
		getCreat().setString(2, translate);
		getCreat().setObject(3, form2);
		getCreat().setObject(4, form3);
		getCreat().executeUpdate();
	}

	public DtoWord getWord(int id) throws SQLException {
		getRead().setInt(1, id);
		ResultSet res = getRead().executeQuery();
		res.next();
		DtoWord word = new DtoWord(res.getString("word"), res.getString("translate"), res.getString("form2"),
				res.getString("form3"));
		res.close();
		// getRead().close();
		return word;
	}

	// возвращает ID из word по полю word + translate
	public int getIdWord(String word, String translate) throws SQLException {
		getReadId().setString(1, word);
		getReadId().setString(2, translate);
		ResultSet res = getReadId().executeQuery();
		res.next();
		int idWord = res.getInt("id");
		res.close();
		return idWord;
	}

	// возвращает IDstaticword из word по полю word+translate+form2+form3
	public int getIdWordstatic(String word, String translate, Object form2, Object form3) throws SQLException {
		getReadIdstatic().setString(1, word);
		getReadIdstatic().setString(2, translate);
		/*
		 * if (form2 == null && form3 == null) { getReadIdstatic().setNull(3,
		 * java.sql.Types.INTEGER); getReadIdstatic().setNull(4,
		 * java.sql.Types.INTEGER); }
		 */
		getReadIdstatic().setObject(3, form2);
		getReadIdstatic().setObject(4, form3);
		ResultSet res = getReadIdstatic().executeQuery();
		res.next();
		int idWord = res.getInt("id");
		res.close();
		return idWord;
	}

	// ИЗМЕНЕНИЕ слова по Id
	public void resultUpdate(String word, String translate, int id) throws SQLException {
		getUpdate().setString(1, word);
		getUpdate().setString(2, translate);
		getUpdate().setInt(3, id);
		getUpdate().executeUpdate();
	}

	// удаление слова из БД по Id
	public void resultDeleteFromDb(int idword) throws SQLException {
		getDeleteFromDb().setInt(1, idword);
		getDeleteFromDb().executeUpdate();
	}

	// удаление слова из Unit по его id, Iduser и unitName
	public void resultDeleteFromUnit(int idword, String unitName, int iduser) throws SQLException {
		getDeleteFromUnit().setInt(1, idword);
		getDeleteFromUnit().setString(2, unitName);
		getDeleteFromUnit().setInt(3, iduser);
		getDeleteFromUnit().executeUpdate();
	}

	// проверка наличия слова в БД
	public int proverkaWord(String word, String translate) throws SQLException {
		getProverka().setString(1, word);
		getProverka().setString(2, translate);
		ResultSet res = getProverka().executeQuery();
		res.next();
		int id = res.getInt(1);
		res.close();
		return id;
	}

	// проверка наличия слова в юните у усера
	public int proverkaWordCustom(int iduser, String unitName, String word, String translate) throws SQLException {
		getProverkaCustom().setInt(1, iduser);
		getProverkaCustom().setString(2, unitName);
		getProverkaCustom().setString(3, word);
		getProverkaCustom().setString(4, translate);
		ResultSet res = getProverkaCustom().executeQuery();
		res.next();
		int id = res.getInt(1);
		res.close();
		return id;
	}

	// проверка наличия слова в юнитах кроме указанного
	public int proverkaOtherUn(int idword, String unitName, int iduser) throws SQLException {
		getProverkaOtherUn().setInt(1, idword);
		getProverkaOtherUn().setString(2, unitName);
		getProverkaOtherUn().setInt(3, iduser);
		ResultSet res = getProverkaOtherUn().executeQuery();
		res.next();
		int id = res.getInt(1);
		res.close();
		return id;
	}

	// проверка наличия слова в юнитах кроме указанного
	public int proverkaOtherUn2(int idword, String unitName, int iduser) throws SQLException {
		getProverkaOtherUn().setInt(1, idword);
		getProverkaOtherUn().setString(2, unitName);
		getProverkaOtherUn().setInt(3, iduser);
		ResultSet res = getProverkaOtherUn().executeQuery();
		res.next();
		int id = res.getInt(1);
		res.close();
		return id;
	}

	// проверка наличия стаических слов в БД
	public int proverkaVerbs(String word, String translate, String form2, String form3) throws SQLException {
		getProverkaVerbs().setString(1, word);
		getProverkaVerbs().setString(2, translate);
		getProverkaVerbs().setString(3, form2);
		getProverkaVerbs().setString(4, form3);

		ResultSet res = getProverkaVerbs().executeQuery();
		res.next();
		int id = res.getInt(1);
		res.close();
		return id;
	}

	// проверяет содержится слово в таблице статикюнит по idword
	public int proverkaStaticById(int idword) throws SQLException {
		getProverkaStaticById().setInt(1, idword);
		ResultSet res = getProverkaStaticById().executeQuery();
		res.next();
		int id = res.getInt(1);
		res.close();
		return id;
	}

	// Возвращает слова по id пользователя и юнту(не неправильные глаголы)
	public ArrayList<DtoWord> resultListFromUnit(int unitId) throws SQLException {
		ArrayList<DtoWord> listWord = new ArrayList<DtoWord>();
		getReadListFromUnit().setInt(1, unitId);
		ResultSet res = getReadListFromUnit().executeQuery();
		while (res.next()) {
			DtoWord word = new DtoWord(res.getInt("id"), res.getString("word"), res.getString("translate"), null, null);
			listWord.add(word);
		}
		res.close();
		return listWord;
	}

	// Возвращает слова по idstacat из staticunit
	public ArrayList<DtoWord> resultListFromStaticunit(int idStatcat) throws SQLException {
		ArrayList<DtoWord> listWord = new ArrayList<DtoWord>();
		getReadListFromStaticunit().setInt(1, idStatcat);
		ResultSet res = getReadListFromStaticunit().executeQuery();
		while (res.next()) {
			DtoWord word = new DtoWord(res.getInt("id"), res.getString("word"), res.getString("translate"),
					res.getString("form2"), res.getString("form3"));
			listWord.add(word);
		}
		res.close();
		return listWord;
	}

	// Список слов в виде массива строк, json объектом
	public JSONArray getWordInJson(ArrayList<DtoWord> listWord) {
		JSONArray jsonArrayWord = new JSONArray();
		String word;
		DtoWord dtoWord;
		for (int i = 0; i < listWord.size(); i++) {
			dtoWord = listWord.get(i);
			if ((dtoWord.getForm2() != null) && (dtoWord.getForm3() != null)) {
				word = i + 1 + " " + dtoWord.getWord() + " - " + dtoWord.getForm2() + " - " + dtoWord.getForm3() + " - "
						+ dtoWord.getTranslate();
			} else {
				word = i + 1 + " " + dtoWord.getWord() + " - " + dtoWord.getTranslate();
			}
			jsonArrayWord.add(word);
		}
		return jsonArrayWord;
	}

	// Список слов jsonМассива первичный поиск из search
	public JSONArray getSearchJson(String word, int iduser, String lang) throws SQLException {
		JSONArray jsonArrayWord = new JSONArray();
		TreeSet<String> setCol = new TreeSet<String>();
		getSearch().setString(1, "%" + word + "%");
		getSearch().setString(2, "%" + word + "%");
		getSearch().setInt(3, iduser);
		ResultSet res = getSearch().executeQuery();
		while (res.next()) {
			if (lang.equals("ru")) {
				setCol.add(res.getString("translate"));
			} else {
				setCol.add(res.getString("word"));
			}
		}
		res.close();
		if (setCol.size() == 0) {
			setCol.add("В Вашем словаре соответствий не найдено");
		}
		jsonArrayWord.addAll(setCol);
		return jsonArrayWord;
	}

	// Список слов+транс+юнит в виде массива строк, jsonМассив конечный поиск
	// Запускается посел выбора слова из подсказки поиска
	public JSONArray rSearchWord(String word, int iduser) throws SQLException {
		JSONArray jsonArrayWord = new JSONArray();
		TreeSet<String> setCol = new TreeSet<String>();
		getResultSearch().setString(1, word);
		getResultSearch().setString(2, word);
		getResultSearch().setInt(3, iduser);
		ResultSet res = getResultSearch().executeQuery();
		while (res.next()) {
			setCol.add(res.getString("word") + " - " + res.getString("translate") + ". " + res.getString("unit"));
		}
		res.close();
		if (setCol.size() == 0) {
			setCol.add("В Вашем словаре соответствий не найдено");
		}
		jsonArrayWord.addAll(setCol);
		return jsonArrayWord;
	}

	public void close() {
		try {
			if (creat != null) {
				creat.close();
			}
			if (read != null) {
				read.close();
			}
			if (readW != null) {
				readW.close();
			}
			if (readSearch != null) {
				readSearch.close();
			}
			if (resultSearch != null) {
				resultSearch.close();
			}
			if (readId != null) {
				readId.close();
			}
			if (update != null) {
				update.close();
			}
			if (readIdstatic != null) {
				readIdstatic.close();
			}
			if (proverka != null) {
				proverka.close();
			}
			if (proverkaCustom != null) {
				proverkaCustom.close();
			}
			if (proverkaOtherUn != null) {
				proverkaOtherUn.close();
			}
			if (readListFromUnit != null) {
				readListFromUnit.close();
			}
			if (readListFromStaticunit != null) {
				readListFromStaticunit.close();
			}
			if (deleteFromDb != null) {
				deleteFromDb.close();
			}
			if (deleteFromUnit != null) {
				deleteFromUnit.close();
			}
			if (proverkaVerbs != null) {
				proverkaVerbs.close();
			}
			if (proverkaStaticById != null) {
				proverkaStaticById.close();
			}
			if (kolvoWordExist != null) {
				kolvoWordExist.close();
			}
			if (connect != null) {
				connect.close();
			}
		} catch (SQLException e) {
			// System.out.println("Ошибка в close в DaoWordJdbc");
			LOG.error(e.getStackTrace());
			e.printStackTrace();
		}
	}
}