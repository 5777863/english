package my.english.dao.jdbc;

import my.english.dto.*;
import my.english.services.*;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class DaoUserJdbc {
	private Connection connect;
	private PreparedStatement creat;
	private PreparedStatement read;
	private PreparedStatement readSolt;
	private PreparedStatement readId;
	private PreparedStatement deleteByMailSpltPass;
	private PreparedStatement updActive;
	private PreparedStatement updData;
	private PreparedStatement proverka;
	private PreparedStatement psw;
	private PreparedStatement existsByMail;
	private PreparedStatement existsByMailSoltPassWithActive;
	private PreparedStatement existsByMailSoltPassWithoutActive;

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
			creat = getConnect().prepareStatement(
					"insert into user(uname, upass, umail, active, date, solt, psw) values (?, ?, ?, ?,now(),?,?)");
		}
		return creat;
	}

	private PreparedStatement getRead() throws SQLException {
		if (read == null) {
			read = getConnect().prepareStatement(
					"select id, uname, upass, umail from user where umail=? and upass=? and active='active'");
		}
		return read;
	}

	private PreparedStatement getSolt() throws SQLException {
		if (readSolt == null) {
			readSolt = getConnect().prepareStatement("select solt from user where umail=? and active='active'");
		}
		return readSolt;
	}

	private PreparedStatement getPsw() throws SQLException {
		if (psw == null) {
			psw = getConnect().prepareStatement("select psw from user where umail=? and active='active'");
		}
		return psw;
	}

	private PreparedStatement getReadId() throws SQLException {
		if (readId == null) {
			readId = getConnect().prepareStatement("select * from user where id=?");
		}
		return readId;
	}

	private PreparedStatement getUpdActive() throws SQLException {
		if (updActive == null) {
			updActive = getConnect().prepareStatement("Update user set active='active' where active=?");
		}
		return updActive;
	}

	// обновление даты входа пользователя
	private PreparedStatement getUpdData() throws SQLException {
		if (updData == null) {
			updData = getConnect().prepareStatement("Update user set date=now() where id=?");
		}
		return updData;
	}

	// удаление пользователя при неудачной регистрации
	private PreparedStatement getDellUser() throws SQLException {
		if (deleteByMailSpltPass == null) {
			deleteByMailSpltPass = getConnect().prepareStatement("DELETE FROM user WHERE umail = ? and upass=?");
		}
		return deleteByMailSpltPass;
	}

	private PreparedStatement getProverka() throws SQLException {
		if (proverka == null) {
			proverka = getConnect().prepareStatement("select exists (select id from user where active=?)");
		}
		return proverka;
	}

	private PreparedStatement getExistsByMail() throws SQLException {
		if (existsByMail == null) {
			existsByMail = getConnect()
					.prepareStatement("select exists (select id from user WHERE umail=? and active='active')");
		}
		return existsByMail;
	}

	private PreparedStatement getExistsByMailSoltPassWithActive() throws SQLException {
		if (existsByMailSoltPassWithActive == null) {
			existsByMailSoltPassWithActive = getConnect().prepareStatement(
					"select exists (select id from user WHERE umail=? and upass=? and active='active')");
		}
		return existsByMailSoltPassWithActive;
	}

	private PreparedStatement getExistsByMailSoltPassWithoutActive() throws SQLException {
		if (existsByMailSoltPassWithoutActive == null) {
			existsByMailSoltPassWithoutActive = getConnect()
					.prepareStatement("select exists (select id from user WHERE umail=? and upass=?)");
		}
		return existsByMailSoltPassWithoutActive;
	}

	public void CreatResult(String uname, String upass, String umail, String active, String solt, String psw)
			throws SQLException {
		getCreat().setString(1, uname);
		getCreat().setString(2, upass);
		getCreat().setString(3, umail);
		getCreat().setString(4, active);
		getCreat().setString(5, solt);
		getCreat().setString(6, psw);
		getCreat().executeUpdate();
	}

	// авторизация пользователя
	public DtoUser ReadNickResult(String umail, String upass) throws SQLException {
		try {
			getRead().setString(1, umail);
			getRead().setString(2, upass);
			ResultSet res = getRead().executeQuery();
			res.next();
			DtoUser q = new DtoUser(res.getInt("id"), res.getString("uname"), res.getString("upass"),
					res.getString("umail"));
			res.close();
			return q;
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
			System.out.println("пользователь не найден");
			return new DtoUser(0, "0");
		}
	}

	// пользователь по Id
	public DtoUser ReadNickId(int id) throws SQLException {
		try {
			getReadId().setInt(1, id);
			ResultSet res = getReadId().executeQuery();
			res.next();
			DtoUser q = new DtoUser(res.getInt("id"), res.getString("uname"), res.getString("upass"),
					res.getString("umail"));
			res.close();
			return q;
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
			System.out.println("пользователь не найден/блок ReadNickId ");
			return new DtoUser(0, "0");
		}
	}

	// возвращает соль
	public String resultSolt(String umail) throws SQLException {
		try {
			getSolt().setString(1, umail);
			ResultSet res = getSolt().executeQuery();
			res.next();
			String solt = res.getString("solt");
			res.close();
			return solt;
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
			System.out.println("соль не найден");
			return null;
		}
	}

	// возвращает зн.поля psw
	public String resultPsw(String umail) throws SQLException {
		getPsw().setString(1, umail);
		ResultSet res = getPsw().executeQuery();
		res.next();
		String psw = res.getString("psw");
		res.close();
		return psw;

	}

	// обновление даты входа
	public void resUpdData(int idUser) {
		try {
			getUpdData().setInt(1, idUser);
			getUpdData().executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("ошибка в DaoUserJdbc/resUpdData");
		}
	}

	// удаляем пользоватея при неудачной регистрации
	public void resDellUser(String mail, String SoltPass) throws SQLException {
		getDellUser().setString(1, mail);
		getDellUser().setString(2, SoltPass);
		getDellUser().executeUpdate();
	}

	// меняем знач.поля active после проверки кода
	public boolean resUpdActive(String cod) throws SQLException {
		getUpdActive().setString(1, cod);
		getUpdActive().executeUpdate();
		return true;
	}

	// получаем userId по коду в active
	public int resProverka(String cod) throws SQLException {
		getProverka().setString(1, cod);
		ResultSet res = getProverka().executeQuery();
		res.next();
		int id = res.getInt(1);
		res.close();
		return id;
	}

	// существует ли запись по email
	public int resExistsByMail(String umail) throws SQLException {
		getExistsByMail().setString(1, umail);
		ResultSet res = getExistsByMail().executeQuery();
		res.next();
		int kol = res.getInt(1);
		res.close();
		return kol;
	}

	// сущесвует ли запись по паролю с учето active
	public int resExistsByMailSoltPassWithActive(String mail, String upass) throws SQLException {
		getExistsByMailSoltPassWithActive().setString(1, mail);
		getExistsByMailSoltPassWithActive().setString(2, upass);
		ResultSet res = getExistsByMailSoltPassWithActive().executeQuery();
		res.next();
		int kol = res.getInt(1);
		res.close();
		return kol;
	}

	// сущесвует ли запись по паролю БЕЗ учето active
	public int resExistsByMailSoltPassWithoutActive(String mail, String upass) throws SQLException {
		getExistsByMailSoltPassWithoutActive().setString(1, mail);
		getExistsByMailSoltPassWithoutActive().setString(2, upass);
		ResultSet res = getExistsByMailSoltPassWithoutActive().executeQuery();
		res.next();
		int kol = res.getInt(1);
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
			if (readId != null) {
				readId.close();
			}
			if (readSolt != null) {
				readSolt.close();
			}
			if (updActive != null) {
				updActive.close();
			}
			if (updData != null) {
				updData.close();
			}
			if (deleteByMailSpltPass != null) {
				deleteByMailSpltPass.close();
			}
			if (proverka != null) {
				proverka.close();
			}
			if (psw != null) {
				psw.close();
			}
			if (existsByMail != null) {
				existsByMail.close();
			}
			if (existsByMailSoltPassWithActive != null) {
				existsByMailSoltPassWithActive.close();
			}
			if (existsByMailSoltPassWithoutActive != null) {
				existsByMailSoltPassWithoutActive.close();
			}
			if (connect != null) {
				connect.close();
			}
		} catch (SQLException e) {
			// System.out.println("ошибка в close в DaoUserJdbc");
			LOG.error(e.getStackTrace());
			e.printStackTrace();
		}
	}
}