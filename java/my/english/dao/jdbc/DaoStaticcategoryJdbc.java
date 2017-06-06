package my.english.dao.jdbc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;

import my.english.dto.DtoFacts;
import my.english.dto.DtoStaticcategory;

public class DaoStaticcategoryJdbc {
 private Connection connect;
	private PreparedStatement creat;
	private PreparedStatement readId;
	// получаем объект staticcategory по id
	private PreparedStatement readById;
	// выбирает по полю groupname
	private PreparedStatement readByGroupname;
	private PreparedStatement proverka;
	// проверяем существование записи по id
	private PreparedStatement proverkaById;
	// Выбираем гроупнейм без павторений без яровой
	private PreparedStatement readDistinctGroupnameWithoutYarovaya;
	// Выбираем гроупнейм без павторений
	private PreparedStatement readDistinctGroupname;

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
			creat = getConnect().prepareStatement("insert into staticcategory(unitname, groupname) values (?, ?)");
		}
		return creat;
	}

	private PreparedStatement getReadId() throws SQLException {
		if (readId == null) {
			readId = getConnect().prepareStatement("SELECT * from staticcategory where unitname = ? and groupname=?");
		}
		return readId;
	}

	private PreparedStatement getReadById() throws SQLException {
		if (readById == null) {
			readById = getConnect().prepareStatement("SELECT * from staticcategory where id=?");
		}
		return readById;
	}

	// читает по полю groupname
	private PreparedStatement getReadByGroupname() throws SQLException {
		if (readByGroupname == null) {
			readByGroupname = getConnect().prepareStatement("SELECT * from staticcategory where groupname=?");
		}
		return readByGroupname;
	}

	// проверяем наличи записи по юнитнейм и гроупнейм
	private PreparedStatement getProverka() throws SQLException {
		if (proverka == null) {
			proverka = getConnect().prepareStatement(
					"select exists (select id FROM staticcategory WHERE unitname=? and groupname = ?)");
		}
		return proverka;
	}

	// проверяем наличи записи по id
	private PreparedStatement getProverkaById() throws SQLException {
		if (proverkaById == null) {
			proverkaById = getConnect().prepareStatement("select exists (select id FROM staticcategory WHERE id = ?)");
		}
		return proverkaById;
	}

	// Выбираем гроупнейм без павторений без яровой
	private PreparedStatement getReadDistinctGroupnameWithoutYarovaya() throws SQLException {
		if (readDistinctGroupnameWithoutYarovaya == null) {
			readDistinctGroupnameWithoutYarovaya = getConnect().prepareStatement(
					"select distinct groupname from staticcategory where groupname not in('Учебник Ольги Яровой')");
		}
		return readDistinctGroupnameWithoutYarovaya;
	}

	// Выбираем гроупнейм без павторений
	private PreparedStatement getReadDistinctGroupname() throws SQLException {
		if (readDistinctGroupname == null) {
			readDistinctGroupname = getConnect().prepareStatement("select distinct groupname from staticcategory");
		}
		return readDistinctGroupname;
	}

	// используется
	public void CreatResult(String unitname, String groupname) throws SQLException {
		getCreat().setString(1, unitname);
		getCreat().setString(2, groupname);
		getCreat().executeUpdate();
	}

	// получаем объект по unitname&groupname
	public DtoStaticcategory getStaticcategory(String unitname, String groupname) throws SQLException {
		getReadId().setString(1, unitname);
		getReadId().setString(2, groupname);
		ResultSet res = getReadId().executeQuery();
		res.next();
		DtoStaticcategory dtoStaticcategory = new DtoStaticcategory(res.getInt("id"), res.getString("unitname"),
				res.getString("groupname"));
		res.close();
		return dtoStaticcategory;
	}

	// получаем объект по id
	public DtoStaticcategory getStaticcategoryById(int id) throws SQLException {
		getReadById().setInt(1, id);
		ResultSet res = getReadById().executeQuery();
		res.next();
		DtoStaticcategory dtoStaticcategory = new DtoStaticcategory(res.getInt("id"), res.getString("unitname"),
				res.getString("groupname"));
		res.close();
		return dtoStaticcategory;
	}

	// возвращает списоком по полю гроупнэйм
	public ArrayList<DtoStaticcategory> getUnitnameByGroupname(String groupname) throws SQLException {
		ArrayList<DtoStaticcategory> spisokUnitname = new ArrayList<DtoStaticcategory>();
		getReadByGroupname().setString(1, groupname);
		ResultSet res = getReadByGroupname().executeQuery();
		while (res.next()) {
			spisokUnitname.add(
					new DtoStaticcategory(res.getInt("id"), res.getString("unitname"), res.getString("groupname")));
		}
		res.close();
		// сортируем список по unitname
		/*
		 * spisokUnitname.sort(new Comparator<DtoStaticcategory>() { public int
		 * compare(DtoStaticcategory o1, DtoStaticcategory o2) { return
		 * o1.getUnitname().compareTo(o2.getUnitname()); } });
		 */
		return spisokUnitname;
	}

	// Выбираем гроупнейм без павторений без яровой
	public ArrayList<String> resultReadDistinctGroupnameWithoutYarovaya() throws SQLException {
		ArrayList<String> groupname = new ArrayList<String>();
		ResultSet res = getReadDistinctGroupnameWithoutYarovaya().executeQuery();
		while (res.next()) {
			groupname.add(res.getString("groupname"));
		}
		res.close();
		return groupname;
	}

	// Выбираем гроупнейм без павторений
	public TreeSet<String> resultReadDistinctGroupname() throws SQLException {
		TreeSet<String> groupname = new TreeSet<String>();
		ResultSet res = getReadDistinctGroupname().executeQuery();
		while (res.next()) {
			groupname.add(res.getString("groupname"));
		}
		res.close();
		return groupname;
	}

	// используется
	// проверяет наличие unit в БД
	public int proverkaStaticcategory(String unitname, String groupname) throws SQLException {
		// int kol = 0;
		getProverka().setString(1, unitname);
		getProverka().setString(2, groupname);
		ResultSet res = getProverka().executeQuery();
		res.next();
		int kol = res.getInt(1);
		/*
		 * while (res.next()) { kol = res.getInt("COUNT(*)"); }
		 */
		res.close();
		return kol;
	}

	// используется
	// проверяет наличие unit по id
	public int proverkaById(int idStatcat) throws SQLException {
		getProverkaById().setInt(1, idStatcat);
		ResultSet res = getProverkaById().executeQuery();
		res.next();
		int kol = res.getInt(1);
		res.close();
		return kol;
	}

	// Возвращает 6 groupname(1яровой+5 случайных)
	public TreeSet<String> getSixRndGroupname() {
		Random rndm = new Random();
		TreeSet<String> sixGroupname = new TreeSet<String>();
		sixGroupname.add("Учебник Ольги Яровой");
		try {
			ArrayList<String> allGroupname = resultReadDistinctGroupnameWithoutYarovaya();
			int groupnameSize = allGroupname.size();
			while (sixGroupname.size() < 6) {
				int num = rndm.nextInt(groupnameSize);
				sixGroupname.add(allGroupname.get(num));
				System.out.println(num);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("ошибка в daoStaticcategory,6 случайных groupname");
		} finally {
			close();
		}
		return sixGroupname;
	}

	public void close() {
		try {
			if (creat != null) {
				creat.close();
			}
			if (readId != null) {
				readId.close();
			}
			if (readById != null) {
				readById.close();
			}
			if (readByGroupname != null) {
				readByGroupname.close();
			}
			if (proverka != null) {
				proverka.close();
			}
			if (proverkaById != null) {
				proverkaById.close();
			}
			if (readDistinctGroupnameWithoutYarovaya != null) {
				readDistinctGroupnameWithoutYarovaya.close();
			}
			if (readDistinctGroupname != null) {
				readDistinctGroupname.close();
			}
			if (connect != null) {
				connect.close();
			}
		} catch (SQLException e) {
			LOG.error(e.getStackTrace());
			e.printStackTrace();
		}
	}

}
