package my.english.dao.jdbc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import my.english.dto.DtoFacts;

public class DaoFactsJdbc {
	private Connection connect;
	private PreparedStatement read;
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

	private PreparedStatement getRead() throws SQLException {
		if (read == null) {
			read = getConnect().prepareStatement("SELECT * from facts");
		}
		return read;
	}

	// Возвращает все факты
	public ArrayList<DtoFacts> getFacts() throws SQLException {
		ArrayList<DtoFacts> facts = new ArrayList<DtoFacts>();
		ResultSet res = getRead().executeQuery();
		while (res.next()) {
			DtoFacts fact = new DtoFacts(res.getInt("id"), res.getString("fact"));
			facts.add(fact);
		}
		res.close();
		return facts;
	}

	// Возвращает 8 случайных фактов из всех
	public ArrayList<DtoFacts> getEightRndFacts() {
		ArrayList<DtoFacts> facts = new ArrayList<DtoFacts>();
		TreeSet<Integer> generated = new TreeSet<Integer>();
		Random rndm = new Random();
		try {
			ArrayList<DtoFacts> allFacts = getFacts();
			int factsSize = allFacts.size();
			// если в списке не меньше 8ми
			if (factsSize >= 8) {
				while (generated.size() < 8) {
					int num = rndm.nextInt(factsSize);
					generated.add(num);
					System.out.println(num);
				}
				// если в списке меньше 8ми
			} else {
				while (generated.size() < factsSize) {
					int num = rndm.nextInt(factsSize);
					generated.add(num);
				}
			}
			Iterator<Integer> iter = generated.iterator();
			while (iter.hasNext()) {
				facts.add(allFacts.get(iter.next()));
			}
		} catch (SQLException e) {
			facts = new ArrayList<DtoFacts>();
			facts.add(new DtoFacts(1, "Первый словарь английского языка написан в 1755 году"));
			facts.add(new DtoFacts(2,
					"Английский - официальный язык неба. Совершая международные перелеты, пилоты общаются на английском вне зависимости от своего родного языка и национальности"));
			facts.add(new DtoFacts(3,
					"Некоторые английские слова используются только во множественном числе: glasses, scissors, trousers, jeans, pyjamas"));
			facts.add(new DtoFacts(4,
					"Crutch words - называют слова, которые помогают заполнить паузы во время разговора, дать себе время подумать или сделать акцент на каком-то слове. Сами по себе эти слова не несут никакого значения. Примеры: basically, literally, actually, like, I mean"));
			facts.add(new DtoFacts(5,
					"Слово therein включает в себя еще 9 слов, причем все идут по порядку: the, there, he, in, rein, her, here, ere, herein"));
			facts.add(new DtoFacts(6,
					"Шахматный термин checkmate произошел от фразы 'shāh māt' из персидского языка, что дословно значит 'the king is helpless' ('король беззащитен')"));
			facts.add(new DtoFacts(7, "Английский язык способствует расширению сознания"));
			facts.add(new DtoFacts(8, "Мужчины, знающие английский язык чаще следят за совим внешним видом"));
			e.printStackTrace();
			System.out.println("ошибка в daoFatcsJdbc, 8 фактов");
		} finally {
			close();
		}
		return facts;

	}

	public void close() {
		try {
			if (read != null) {
				read.close();
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
