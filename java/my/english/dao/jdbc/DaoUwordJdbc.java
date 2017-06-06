package my.english.dao.jdbc;

import my.english.dto.*;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class DaoUwordJdbc {
	private Connection connect;
	private PreparedStatement creat;
	private PreparedStatement read;
	private PreparedStatement proverka;
	private PreparedStatement getKolvo;
	private PreparedStatement delete;

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
			creat = getConnect().prepareStatement("insert into uword(idunit, idword) values (?, ?)");
		}
		return creat;
	}

	private PreparedStatement getRead() throws SQLException {
		if (read == null) {
			read = getConnect().prepareStatement(
					"select w.id, w.word, w.translate from unit u INNER JOIN uword uw ON u.id = uw.idunit "
							+ "INNER JOIN word w ON uw.idword = w.id where u.unit=? and u.iduser = ?");
		}
		return read;
	}

	// удаляем uword(все записи по unitid) из БД
	private PreparedStatement getDelete() throws SQLException {
		if (delete == null) {
			delete = getConnect().prepareStatement("DELETE from uword WHERE idunit = ?");
		}
		return delete;
	}

	private PreparedStatement getKolvo() throws SQLException {
		if (getKolvo == null) {
			getKolvo = getConnect()
					.prepareStatement("select count(*) from unit u INNER JOIN uword uw ON u.id = uw.idunit "
							+ "INNER JOIN word w ON uw.idword = w.id where u.unit=? and u.iduser = ? and uw.active=?");
		}
		return getKolvo;
	}

	// при добавлении нового слова проверка на его УЖЕ существование в БД
	private PreparedStatement getProverka() throws SQLException {
		if (proverka == null) {
			proverka = getConnect()
					.prepareStatement("SELECT count(id) FROM uword WHERE idword = ? and idunit = ? limit 1");
		}
		return proverka;
	}

	public void CreatResult(int idunit, int idword) throws SQLException {
		getCreat().setInt(1, idunit);
		getCreat().setInt(2, idword);
		getCreat().executeUpdate();
		// getCreat().close();
	}

	// удаление uword
	public void resultDelete(int idunit) throws SQLException {
		getDelete().setInt(1, idunit);
		getDelete().executeUpdate();
	}

	public ArrayList<DtoUrword> ReadNickResult(String[] unit, int iduser) throws SQLException {
		ArrayList<DtoUrword> listWord = new ArrayList<DtoUrword>();
		for (int i = 0; i < unit.length; i++) {
			DtoUnit unitN = new DaoUnitJdbc().resultId(unit[i], iduser);
			getRead().setString(1, unit[i]);
			getRead().setInt(2, iduser);
			ResultSet res = getRead().executeQuery();
			while (res.next()) {
				DtoWord word = new DtoWord(res.getInt("id"), res.getString("word"), res.getString("translate"));
				DtoUrword uword = new DtoUrword(unitN, word);
				listWord.add(uword);
			}
			res.close();
		}
		// getRead().close();
		return listWord;
	}

	public Integer kolvoResult(String[] unit, int iduser, String active) throws SQLException {
		int kolvo = 0;
		for (int i = 0; i < unit.length; i++) {
			getKolvo().setString(1, unit[i]);
			getKolvo().setInt(2, iduser);
			getKolvo().setString(3, active);
			ResultSet res = getKolvo().executeQuery();
			while (res.next()) {
				kolvo = kolvo + res.getInt("COUNT(*)");
			}
			res.close();
		}
		// getRead().close();
		return kolvo;
	}

	public int proverkaUword(int idword, int idunit) throws SQLException {
		getProverka().setInt(1, idword);
		getProverka().setInt(2, idunit);
		ResultSet res = getProverka().executeQuery();
		res.next();
		int id = res.getInt(1);
		res.close();
		return id;
	}

	public void close() {
		try {
			if (creat != null) {
				creat.close();
			}
			if (read != null) {
				read.close();
			}
			if (delete != null) {
				delete.close();
			}
			if (proverka != null) {
				proverka.close();
			}
			if (getKolvo != null) {
				getKolvo.close();
			}
			if (connect != null) {
				connect.close();
			}
		} catch (SQLException e) {
			// System.out.println("Ошибка в close в DaoUwordJdbc");
			e.printStackTrace();
			LOG.error(e.getStackTrace());
		}
	}
}