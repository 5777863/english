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
import my.english.dto.DtoStaticrating;

public class DaoStaticratingJdbc {
	private Connection connect;
	// создаем запись
	private PreparedStatement creat;
	// выбираем по idUser и idstatcat
	private PreparedStatement readByUserStatcat;
	// проверяем существует ли запись по idstatcat&iduser
	private PreparedStatement exists;
	// обнуляем значение оценки(поля труе фолс)
	private PreparedStatement resetOcenka;
	// считываем значение поя трууе или фоолс
	private PreparedStatement readOcenka;
	// меняем значение поля трууе или фолс на +1
	private PreparedStatement updateOcenka;

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
			creat = getConnect().prepareStatement("insert into staticrating(idstatcat, iduser) values (?, ?)");
		}
		return creat;
	}

	private PreparedStatement getReadByUserStatcat() throws SQLException {
		if (readByUserStatcat == null) {
			readByUserStatcat = getConnect()
					.prepareStatement("SELECT * from staticrating where idstatcat=? and iduser=?");
		}
		return readByUserStatcat;
	}

	// поверка содержится запись по idstatcat&iduser
	private PreparedStatement getExists() throws SQLException {
		if (exists == null) {
			exists = getConnect()
					.prepareStatement("select exists (select id FROM staticrating WHERE idstatcat = ? and iduser = ?)");
		}
		return exists;
	}

	// сбрасываем оценку по unitId
	private PreparedStatement getResetOcenka() throws SQLException {
		if (resetOcenka == null) {
			resetOcenka = getConnect().prepareStatement("Update staticrating set truue=0, faalse=0 where id=?");
		}
		return resetOcenka;
	}

	// ПОЛУЧЕНИ оценки из конкретного столбца(truue или faalse)
	private PreparedStatement getOcenka(String stolbec) throws SQLException {
		if (readOcenka == null) {
			readOcenka = getConnect().prepareStatement("select " + stolbec + " from staticrating where id = ?");
		}
		return readOcenka;
	}

	// ИЗМЕНЕНИЕ оценки в поле труе или фолс
	private PreparedStatement getUpdateOcenka(String stolbec) throws SQLException {
		if (updateOcenka == null) {
			updateOcenka = getConnect().prepareStatement("Update staticrating set " + stolbec + "=? where id=?");
		}
		return updateOcenka;
	}

	public void CreatResult(int idStatcat, int idUser) throws SQLException {
		getCreat().setInt(1, idStatcat);
		getCreat().setInt(2, idUser);
		getCreat().executeUpdate();
	}

	// использается.получаем staticrating по DtoStaticcategory и iduser
	public DtoStaticrating getByUserStatcat(DtoStaticcategory dtoStatcat, int idUser) throws SQLException {
		DaoStaticcategoryJdbc daoStatCat = new DaoStaticcategoryJdbc();
		getReadByUserStatcat().setInt(1, dtoStatcat.getId());
		getReadByUserStatcat().setInt(2, idUser);
		ResultSet res = getReadByUserStatcat().executeQuery();
		res.next();
		DtoStaticrating dtoStatrat = new DtoStaticrating(res.getInt("id"),
				daoStatCat.getStaticcategoryById(res.getInt("idstatcat")), res.getInt("iduser"), res.getInt("truue"),
				res.getInt("faalse"));
		res.close();
		daoStatCat.close();
		return dtoStatrat;
	}

	// использается.получаем staticrating по idstacat и iduser
	public DtoStaticrating getByIduserIdstatcat(int idstatcat, int idUser) throws SQLException {
		DaoStaticcategoryJdbc daoStatCat = new DaoStaticcategoryJdbc();
		getReadByUserStatcat().setInt(1, idstatcat);
		getReadByUserStatcat().setInt(2, idUser);
		ResultSet res = getReadByUserStatcat().executeQuery();
		res.next();
		DtoStaticrating dtoStatrat = new DtoStaticrating(res.getInt("id"),
				daoStatCat.getStaticcategoryById(res.getInt("idstatcat")), res.getInt("iduser"), res.getInt("truue"),
				res.getInt("faalse"));
		res.close();
		daoStatCat.close();
		return dtoStatrat;
	}

	// проверка наличия записи
	public int resExists(int idStatcat, int idUser) throws SQLException {
		getExists().setInt(1, idStatcat);
		getExists().setInt(2, idUser);
		ResultSet res = getExists().executeQuery();
		res.next();
		int id = res.getInt(1);
		res.close();
		return id;
	}

	// сброс оценки по staticratingId
	public void rResetOcenka(int idStatrat) throws SQLException {
		getResetOcenka().setInt(1, idStatrat);
		getResetOcenka().executeUpdate();
	}

	// ПОЛУЧЕНИe оценки из конкретного столбца(truue или faalse)
	public int resultOcenka(int statrat_Id, String stolbec) throws SQLException {
		getOcenka(stolbec).setInt(1, statrat_Id);
		ResultSet res = getOcenka(stolbec).executeQuery();
		res.next();
		int ocenka = res.getInt(1);
		res.close();
		return ocenka;
	}

	// ИЗМЕНЕНИЕ оценки по staticratingId на +1
	public void rUpdateOcenka(String stolbec, int id_Statcat) throws SQLException {
		int ocenka = resultOcenka(id_Statcat, stolbec);
		ocenka++;
		getUpdateOcenka(stolbec).setInt(1, ocenka);
		getUpdateOcenka(stolbec).setInt(2, id_Statcat);
		getUpdateOcenka(stolbec).executeUpdate();
	}

	public void close() {
		try {
			if (creat != null) {
				creat.close();
			}
			if (exists != null) {
				exists.close();
			}
			if (readOcenka != null) {
				readOcenka.close();
			}
			if (resetOcenka != null) {
				resetOcenka.close();
			}
			if (updateOcenka != null) {
				updateOcenka.close();
			}
			if (readByUserStatcat != null) {
				readByUserStatcat.close();
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
