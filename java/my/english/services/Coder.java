package my.english.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

//класс для хеширования пароля
public class Coder {
	// метод генерирует соль
	public static String generateString(int length) {
		String characters = "qwertyuiopasdfghjklzxcvbnm1234567890QWERTYUIOASDFGHJKLZXCVBNM";
		Random rnd = new Random();
		char[] text = new char[length];
		for (int i = 0; i < length; i++) {
			text[i] = characters.charAt(rnd.nextInt(characters.length()));
		}
		return new String(text);
	}

	// метод хеширует пароль пользователя+соль
	public static String getHash(String userpass) throws NoSuchAlgorithmException, IOException {
		MessageDigest sha;
		StringBuffer hexString = new StringBuffer();
		try {
			sha = MessageDigest.getInstance("sha-256");
			try {
				// указываем кодировку на слуай если пользователь вводит русские
				// символы
				sha.update(userpass.getBytes("utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			byte messageDigest[] = sha.digest();
			for (int i = 0; i < messageDigest.length; i++) {
				hexString.append(Integer.toHexString((0xF0 & messageDigest[i]) >> 4));
				hexString.append(Integer.toHexString(0x0F & messageDigest[i]));
			}
		} catch (NoSuchAlgorithmException e) {
			return e.toString();
		}
		return hexString.toString();
	}

}