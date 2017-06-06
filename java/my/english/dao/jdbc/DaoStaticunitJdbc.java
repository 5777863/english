package my.english.dao.jdbc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import my.english.dto.DtoStaticunit;
import my.english.dto.DtoUnit;
import my.english.dto.DtoUrword;
import my.english.dto.DtoWord;

public class DaoStaticunitJdbc {
	private Connection connect;
	private PreparedStatement creat;
	private PreparedStatement read;
	private PreparedStatement proverka;

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
			creat = getConnect().prepareStatement("insert into staticunit(idword, idstatcat) values (?, ?)");
		}
		return creat;
	}

	private PreparedStatement getRead() throws SQLException {
		if (read == null) {
			read = getConnect().prepareStatement(
					"select w.id, w.word, w.translate, w.form2, w.form3 from staticunit su INNER JOIN word w ON su.idword = w.id "
							+ "where su.idstatcat = ?");
		}
		return read;
	}

	// получаем кол-во по staticunit
	private PreparedStatement getProverka() throws SQLException {
		if (proverka == null) {
			proverka = getConnect()
					.prepareStatement("select exists (select id FROM staticunit WHERE idword=? and idstatcat = ?)");
		}
		return proverka;
	}

	// используется
	public void CreatResult(int idword, int idStatcat) throws SQLException {
		getCreat().setInt(1, idword);
		getCreat().setInt(2, idStatcat);
		getCreat().executeUpdate();
	}

	// используется.
	public ArrayList<DtoStaticunit> resulListByArrayIdstatcat(String[] unit) throws SQLException {
		int[] intArray = stringArrayToIntArray(unit);
		ArrayList<DtoStaticunit> listWord = new ArrayList<DtoStaticunit>();
		for (int i = 0; i < intArray.length; i++) {
			getRead().setInt(1, intArray[i]);
			ResultSet res = getRead().executeQuery();
			while (res.next()) {
				DtoWord word = new DtoWord(res.getInt("id"), res.getString("word"), res.getString("translate"),
						res.getString("form2"), res.getString("form3"));
				DtoStaticunit staticunit = new DtoStaticunit(word, intArray[i]);
				listWord.add(staticunit);
			}
			res.close();
		}
		return listWord;
	}

	// используется.проверяет наличие unit в БД
	public int proverkaUnit(int idword, int idstaticcat) throws SQLException {
		// int kol = 0;
		getProverka().setInt(1, idword);
		getProverka().setInt(2, idstaticcat);
		ResultSet res = getProverka().executeQuery();
		res.next();
		int kol = res.getInt(1);
		/*
		 * while (res.next()) { kol = res.getInt("COUNT(*)"); }
		 */
		res.close();
		return kol;
	}

	public void close() {
		try {
			if (creat != null) {
				creat.close();
			}
			if (read != null) {
				read.close();
			}
			if (proverka != null) {
				proverka.close();
			}
			if (connect != null) {
				connect.close();
			}
		} catch (SQLException e) {
			// System.out.println("Ошибка в daostaticunitjdbc/close");
			LOG.error(e.getStackTrace());
			e.printStackTrace();
		}
	}

	// кнвертер стрингмассива в инт массив
	public int[] stringArrayToIntArray(String[] array) {
		int numArr[] = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			numArr[i] = Integer.parseInt(array[i]);

		}
		return numArr;
	}
}