package my.english.services;

import my.english.dao.jdbc.*;
import my.english.dto.*;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

public class Import {

	public static void fromFile(BufferedReader bufRead, int idUser) throws IOException, SQLException {
		// BufferedReader in = null;
		ArrayList<String> allSpisok = new ArrayList<String>();

		DaoUnitJdbc urok = new DaoUnitJdbc();
		DaoWordJdbc word = new DaoWordJdbc();
		DaoUwordJdbc uword = new DaoUwordJdbc();
		int idWord;
		int unitId;
		try {
			// in = new BufferedReader(new FileReader(path));
			String i;
			while ((i = bufRead.readLine()) != null) {
				/*
				 * String strokaEncoding = new String(i.getBytes(codiName),
				 * "UTF-8"); System.out.println(strokaEncoding);
				 */
				allSpisok.add(i);
			}
			allSpisok.add("/n");
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		} finally {
			bufRead.close();
		}
		String unit = null;
		int sch = 0;
		ArrayList<ArrayList<String>> slovar = new ArrayList<ArrayList<String>>();
		int spisokSize = allSpisok.size();
		System.out.println(spisokSize);
		try {
			for (int i = 0; i < spisokSize; i++) {
				if (allSpisok.get(i).contains("***Unit") || i == spisokSize - 1) {
					System.out.println("счетчик " + sch);
					if (sch > 0) {
						ArrayList<String> t = new ArrayList<String>();
						// проверяем существет ли такой урок
						System.out.println("проверка наличия в БД" + unit);
						System.out.println("у усера с ИД" + idUser);
						int y = urok.rProverkaUnit(unit, idUser);
						System.out.println("существует если = " + y);
						if (y == 0) {
							// создаем unit
							System.out.println("Создаем урок " + unit);
							urok.CreatResult(unit, idUser);
							unitId = urok.resultId(unit, idUser).getId();
						} else {
							// получаем id unit
							unitId = urok.resultId(unit, idUser).getId();
						}
						Iterator<ArrayList<String>> iter = slovar.iterator();
						while (iter.hasNext()) {
							t = iter.next();
							System.out.println(t.get(0) + " " + t.get(1));
							// проверяем существование слова в БД
							int r = word.proverkaWord(t.get(0), t.get(1));//
							if (r == 0) {
								// заносим слово в БД
								word.CreatResult(t.get(0), t.get(1), null, null);
								// получем id слова
								idWord = word.getIdWord(t.get(0), t.get(1));
								uword.CreatResult(unitId, idWord);
							} else {
								// получаем id слова
								idWord = word.getIdWord(t.get(0), t.get(1));
								if (uword.proverkaUword(idWord, unitId) == 0) {
									uword.CreatResult(unitId, idWord);
								}
							}
						}
						sch = 0;
						unit = null;
						slovar.clear();
					}
					// Выбирает имена унитов
					if (i != spisokSize - 1) {
						StringTokenizer tok = new StringTokenizer(allSpisok.get(i), "=");
						int tokeNumber = 0;
						while (tok.hasMoreTokens()) {
							tokeNumber++;
							if (tokeNumber == 2) {
								unit = tok.nextToken();
							} else {
								tok.nextToken();
							}
						}
						sch++;
					}
				} else {
					ArrayList<String> vrem = new ArrayList<String>();
					StringTokenizer tokens = new StringTokenizer(allSpisok.get(i), "=");
					while (tokens.hasMoreTokens()) {
						vrem.add(tokens.nextToken());
					}
					slovar.add(vrem);
					vrem = null;
				}
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {
			urok.close();
			word.close();
			uword.close();
		}

	}

	// Необходимо передавать BufferedReader buf = new BufferedReader(new
	// InputStreamReader(file,"UTF-8")); с указанием кодировки читаемого файла

	/*
	 * запуск из main String path = "d://testStatic.txt"; FileInputStream file =
	 * new FileInputStream(path); BufferedReader buf = new BufferedReader(new
	 * InputStreamReader(file,"UTF-8")); Import.fromFileStatic(buf);
	 */

	public static void fromFileStatic(BufferedReader bufRead) throws IOException, SQLException {
		// BufferedReader in = null;
		ArrayList<String> allSpisok = new ArrayList<String>();
		DaoStaticunitJdbc urok = new DaoStaticunitJdbc();
		DaoWordJdbc word = new DaoWordJdbc();
		DaoStaticunitJdbc staticunit = new DaoStaticunitJdbc();
		DaoStaticcategoryJdbc daoStaticcategory = new DaoStaticcategoryJdbc();
		int idWord;
		try {
			// in = new BufferedReader(new FileReader(path));
			String i;
			while ((i = bufRead.readLine()) != null) {
				allSpisok.add(i);
			}
			allSpisok.add("/n");
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		} finally {
			bufRead.close();
		}
		String unit = null;
		String groupname = null;
		int sch = 0;
		int spisokSize = allSpisok.size();
		ArrayList<ArrayList<String>> slovar = new ArrayList<ArrayList<String>>();
		try {
			for (int i = 0; i < spisokSize; i++) {

				if (allSpisok.get(i).contains("**********Unit") || i == spisokSize - 1) {
					System.out.println("счетчик " + sch);
					if (sch > 0) {
						ArrayList<String> t = new ArrayList<String>();
						Iterator<ArrayList<String>> iter = slovar.iterator();
						while (iter.hasNext()) {
							t = iter.next();
							int kolSlov = t.size();
							String slovo;
							int staticcatId;
							// проверяем существует ли такая запись в
							// статиккатегори
							if (daoStaticcategory.proverkaStaticcategory(unit, groupname) != 0) {
								// если существует то получаем staticcategoryId
								staticcatId = daoStaticcategory.getStaticcategory(unit, groupname).getId();
							} else {
								// если НЕ существует то создаем запись
								daoStaticcategory.CreatResult(unit, groupname);
								// получаем staticcategoryId
								staticcatId = daoStaticcategory.getStaticcategory(unit, groupname).getId();
							}
							// проверяем слова неправильные глаголы
							if (kolSlov > 3) {
								slovo = t.get(0) + " " + t.get(1) + " " + t.get(2) + " " + t.get(3);
								System.out.println("проверяем на наличие в БД " + slovo + " ,unit= " + unit
										+ " ,groupname= " + groupname);
								// проверяем существование слова в БД
								int r = word.proverkaVerbs(t.get(0), t.get(3), t.get(1), t.get(2));
								if (r == 0) {
									System.out.println(slovo + " не найдено");
									// заносим слово в БД
									word.CreatResult(t.get(0), t.get(3), t.get(1), t.get(2));
									System.out.println("добавляем в БД " + slovo);
									// получем id слова
									idWord = word.getIdWordstatic(t.get(0), t.get(3), t.get(1), t.get(2));
									staticunit.CreatResult(idWord, staticcatId);
									System.out.println("добавили в БД " + slovo + " c id=" + idWord + " в unit=" + unit
											+ " таблицы staticunit");
								} else {
									System.out.println(slovo + " найдено");
									// получаем id слова
									idWord = word.getIdWordstatic(t.get(0), t.get(3), t.get(1), t.get(2));
									// проверяем есть ли данное слово и unit в
									// staticunit
									if (staticunit.proverkaUnit(idWord, staticcatId) == 0) {
										staticunit.CreatResult(idWord, staticcatId);
									}
								}
							} else {
								slovo = t.get(0) + " " + t.get(1);
								System.out.println("проверяем на наличие в БД " + slovo + " ,unit= " + unit
										+ " ,groupname= " + groupname);
								// проверяем существование слова в БД
								int r = word.proverkaWord(t.get(0), t.get(1));
								if (r == 0) {
									// заносим слово в БД
									word.CreatResult(t.get(0), t.get(1), null, null);
									// получем id слова
									idWord = word.getIdWord(t.get(0), t.get(1));
									staticunit.CreatResult(idWord, staticcatId);
								} else {
									// получаем id слова
									idWord = word.getIdWord(t.get(0), t.get(1));
									// проверяем есть ли данное слово и unit в
									// staticunit
									if (staticunit.proverkaUnit(idWord, staticcatId) == 0) {
										staticunit.CreatResult(idWord, staticcatId);
									}
								}
							}
						}
						sch = 0;
						unit = null;
						groupname = null;
						slovar.clear();
					}
					// Выбирает имена унитов
					if (i != spisokSize - 1) {
						StringTokenizer tok = new StringTokenizer(allSpisok.get(i), "=");
						int tokeNumber = 0;
						while (tok.hasMoreTokens()) {
							tokeNumber++;
							if (tokeNumber == 2) {
								unit = tok.nextToken();
							} else {
								tok.nextToken();
							}
						}
						sch++;
					}
				} else {
					if (allSpisok.get(i).contains("**********Groupname")) {
						// Выбирает имя Groupname
						StringTokenizer tok = new StringTokenizer(allSpisok.get(i), "=");
						int tokeNumber = 0;
						while (tok.hasMoreTokens()) {
							tokeNumber++;
							if (tokeNumber == 2) {
								groupname = tok.nextToken();
							} else {
								tok.nextToken();
							}
						}
					} else {
						ArrayList<String> vrem = new ArrayList<String>();
						StringTokenizer tokens = new StringTokenizer(allSpisok.get(i), "=");
						while (tokens.hasMoreTokens()) {
							vrem.add(tokens.nextToken());
						}
						slovar.add(vrem);
						vrem = null;
					}
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			// System.out.println(ex.printStackTrace(););
		} finally {
			urok.close();
			word.close();
		}

	}
}
