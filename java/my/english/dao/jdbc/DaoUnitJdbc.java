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

public class DaoUnitJdbc {
	private Connection connect;
	private PreparedStatement creat;
	private PreparedStatement updateOcenka;
	// обнуляем значение оценки(поля труе фолс)
	private PreparedStatement resetOcenka;
	private PreparedStatement updateUnitName;
	private PreparedStatement readOcenka;
	private PreparedStatement read;
	// Возвращает unit по его Id
	private PreparedStatement readId;
	// запрос всех унитов по пользователю
	private PreparedStatement readAll;
	private PreparedStatement proverkaId;
	private PreparedStatement proverkaUnit;
	// удаляем юнит по iduser unitname
	private PreparedStatement delete;
	// существуют ли у усера словари
	private PreparedStatement existsTestsByUserId;

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

	// создание юнита
	private PreparedStatement getCreat() throws SQLException {
		if (creat == null) {
			creat = getConnect().prepareStatement("insert into unit(unit, iduser) values (?, ?)");
		}
		return creat;
	}

	// проверяем существует лм по unitId
	private PreparedStatement getProverkaId() throws SQLException {
		if (proverkaId == null) {
			proverkaId = getConnect().prepareStatement("select count(*) FROM unit WHERE id = ? limit 1");
		}
		return proverkaId;
	}

	// получаем кол-во по unitName и idUser
	private PreparedStatement getProverkaUnit() throws SQLException {
		if (proverkaUnit == null) {
			proverkaUnit = getConnect()
					.prepareStatement("SELECT exists (SELECT * FROM unit WHERE unit = ? and iduser = ? )");

		}
		return proverkaUnit;
	}

	// выборка по иени и id пользователя
	private PreparedStatement getResult() throws SQLException {
		if (read == null) {
			read = getConnect().prepareStatement("select * from unit where unit=? and iduser = ?");
		}
		return read;
	}

	// удаляем unit(по iduser unitname)
	private PreparedStatement getDelete() throws SQLException {
		if (delete == null) {
			delete = getConnect().prepareStatement("DELETE from unit WHERE unit = ? and iduser=?");
		}
		return delete;
	}

	// выборка по иени и id пользователя
	private PreparedStatement getResultId() throws SQLException {
		if (readId == null) {
			readId = getConnect().prepareStatement("select * from unit where id = ?");
		}
		return readId;
	}

	// Возвращает unit по его Id
	private PreparedStatement getResultAll() throws SQLException {
		if (readAll == null) {
			readAll = getConnect().prepareStatement("select * from unit where iduser = ?");
		}
		return readAll;
	}

	// проверяем есь ли у пользвателя собственные словари
	private PreparedStatement getExistsTestsByUserId() throws SQLException {
		if (existsTestsByUserId == null) {
			existsTestsByUserId = getConnect().prepareStatement("SELECT exists (select id from unit where iduser = ?)");
		}
		return existsTestsByUserId;
	}

	// ИЗМЕНЕНИЕ оценки по unitId
	private PreparedStatement getUpdateOcenka(String stolbec) throws SQLException {
		if (updateOcenka == null) {
			updateOcenka = getConnect().prepareStatement("Update unit set " + stolbec + "=? where id=?");
		}
		return updateOcenka;
	}

	// сбрасываем оценку по unitId
	private PreparedStatement getResetOcenka() throws SQLException {
		if (resetOcenka == null) {
			resetOcenka = getConnect().prepareStatement("Update unit set truue=0, faalse=0 where id=?");
		}
		return resetOcenka;
	}

	// ИЗМЕНЕНИЕ название unit
	private PreparedStatement getUpdateUnitName() throws SQLException {
		if (updateUnitName == null) {
			updateUnitName = getConnect().prepareStatement("Update unit set unit=? where unit=? and iduser=?");
		}
		return updateUnitName;
	}

	// ПОЛУЧЕНИ оценки из конкретного столбца(truue или faalse)
	private PreparedStatement getOcenka(String stolbec) throws SQLException {
		if (readOcenka == null) {
			readOcenka = getConnect().prepareStatement("select " + stolbec + " from unit where id = ?");
		}
		return readOcenka;
	}

	public void CreatResult(String unit, int iduser) throws SQLException {
		creat = getConnect().prepareStatement("insert into unit(unit, iduser) values (?, ?)");
		creat.setString(1, unit);
		creat.setInt(2, iduser);
		creat.executeUpdate();
		// getCreat().close();
	}

	// ИЗМЕНЕНИЕ оценки по unitId на +1
	public void rUpdateOcenka(String stolbec, int idUnit) throws SQLException {
		int ocenka = resultOcenka(idUnit, stolbec);
		ocenka++;
		getUpdateOcenka(stolbec).setInt(1, ocenka);
		getUpdateOcenka(stolbec).setInt(2, idUnit);
		getUpdateOcenka(stolbec).executeUpdate();
	}

	// сброс оценки по unitId
	public void rResetOcenka(int idUnit) throws SQLException {
		getResetOcenka().setInt(1, idUnit);
		getResetOcenka().executeUpdate();
	}

	// ИЗМЕНЕНИЕ названия unit
	public void rUpdateName(String newName, String oldName, int idUser) throws SQLException {
		getUpdateUnitName().setString(1, newName);
		getUpdateUnitName().setString(2, oldName);
		getUpdateUnitName().setInt(3, idUser);
		getUpdateUnitName().executeUpdate();
	}

	// ПОЛУЧЕНИe оценки из конкретного столбца(truue или faalse)
	public int resultOcenka(int unitId, String stolbec) throws SQLException {
		getOcenka(stolbec).setInt(1, unitId);
		ResultSet res = getOcenka(stolbec).executeQuery();
		res.next();
		int ocenka = res.getInt(1);
		res.close();
		return ocenka;
	}

	// проверяет наличие unit в БД по unitId&userId
	public int rProverkaId(int unitId) throws SQLException {
		int kol = 0;
		getProverkaId().setInt(1, unitId);
		ResultSet res = getProverkaId().executeQuery();
		while (res.next()) {
			kol = res.getInt("COUNT(*)");
		}
		res.close();
		return kol;
	}

	// удаление unit
	public void resultDelete(String unitName, int idUser) throws SQLException {
		getDelete().setString(1, unitName);
		getDelete().setInt(2, idUser);
		getDelete().executeUpdate();
	}

	// проверяет наличие unit в БД по unit(name)&userId
	public int rProverkaUnit(String unit, int iduser) throws SQLException {
		getProverkaUnit().setString(1, unit);
		getProverkaUnit().setInt(2, iduser);
		ResultSet res = getProverkaUnit().executeQuery();
		res.next();
		int exist = res.getInt(1);
		res.close();
		return exist;
	}

	public DtoUnit resultUnitId(int unitId, int iduser) throws SQLException {
		getResultId().setInt(1, unitId);
		ResultSet res = getResultId().executeQuery();
		res.next();
		int id = res.getInt("id");
		String unitN = res.getString("unit");
		int truue = res.getInt("truue");
		int faalse = res.getInt("faalse");
		res.close();
		DaoUserJdbc daoUser = new DaoUserJdbc();
		DtoUser user = daoUser.ReadNickId(iduser);
		DtoUnit dtounit = new DtoUnit(id, unitN, user, truue, faalse);
		daoUser.close();
		return dtounit;
	}

	public DtoUnit resultId(String unit, int iduser) throws SQLException {
		getResult().setString(1, unit);
		getResult().setInt(2, iduser);
		ResultSet res = getResult().executeQuery();
		res.next();
		int id = res.getInt("id");
		String unitN = res.getString("unit");
		int truue = res.getInt("truue");
		int faalse = res.getInt("faalse");
		res.close();
		DaoUserJdbc daoUser = new DaoUserJdbc();
		DtoUser user = daoUser.ReadNickId(iduser);
		DtoUnit dtounit = new DtoUnit(id, unitN, user, truue, faalse);
		daoUser.close();
		return dtounit;
	}

	// проверяет существуют ли словари у пользователя
	public int rExistsTestsByUserId(DtoUser user) throws SQLException {
		getExistsTestsByUserId().setInt(1, user.getId());
		ResultSet res = getExistsTestsByUserId().executeQuery();
		res.next();
		int exist = res.getInt(1);
		res.close();
		return exist;
	}

	// получаем все unit пользователя по его id кроме Статик Юнит
	public ArrayList<DtoUnit> resultAllButStatic(DtoUser user) throws SQLException {
		ArrayList<DtoUnit> allUnit = new ArrayList<DtoUnit>();
		getResultAll().setInt(1, user.getId());
		ResultSet res = getResultAll().executeQuery();
		while (res.next()) {
			DtoUnit dtounit = new DtoUnit(res.getInt("id"), res.getString("unit"), user, res.getInt("truue"),
					res.getInt("faalse"));
			allUnit.add(dtounit);
		}
		res.close();
		return allUnit;
	}

	public void close() {
		try {
			if (creat != null) {
				creat.close();
			}
			if (updateOcenka != null) {
				updateOcenka.close();
			}
			if (updateUnitName != null) {
				updateUnitName.close();
			}
			if (readOcenka != null) {
				readOcenka.close();
			}
			if (resetOcenka != null) {
				resetOcenka.close();
			}
			if (read != null) {
				read.close();
			}
			if (delete != null) {
				delete.close();
			}
			if (readId != null) {
				readId.close();
			}
			if (readAll != null) {
				readAll.close();
			}
			if (proverkaId != null) {
				proverkaId.close();
			}
			if (proverkaUnit != null) {
				proverkaUnit.close();
			}
			if (existsTestsByUserId != null) {
				existsTestsByUserId.close();
			}
			if (connect != null) {
				connect.close();
			}
		} catch (SQLException er) {
			er.printStackTrace();
			LOG.error(er.getStackTrace());
		//	System.out.println("ошибка в DaoUnitJ, блок close");
		}
	}
}