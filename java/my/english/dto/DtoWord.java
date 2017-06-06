package my.english.dto;

import java.io.Serializable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DtoWord implements Serializable {

	private int id;
	private String word;
	private String translate;
	private String form2;
	private String form3;
	// private DtoUnit unit;

	public DtoWord() {
	}

	public DtoWord(int id, String word, String translate) {
		this.id = id;
		this.setWord(word);
		this.setTranslate(translate);
	}

	public DtoWord(String word, String translate, String form2, String form3) {
		this.setWord(word);
		this.setTranslate(translate);
		this.setForm2(form2);
		this.setForm3(form3);
		// this.setUnit(unit);
	}

	public DtoWord(int id, String word, String translate, String form2, String form3) {
		this.setId(id);
		this.setWord(word);
		this.setTranslate(translate);
		this.setForm2(form2);
		this.setForm3(form3);
		// this.setUnit(unit);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getTranslate() {
		return translate;
	}

	public void setTranslate(String translate) {
		this.translate = translate;
	}

	public String getForm2() {
		return form2;
	}

	public void setForm2(String form2) {
		this.form2 = form2;
	}

	public String getForm3() {
		return form3;
	}

	public void setForm3(String form3) {
		this.form3 = form3;
	}

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", this.id);
		json.put("word", this.word);
		json.put("translate", this.translate);
		json.put("form2", this.form2);
		json.put("form3", this.form3);
		return json;
	}

	@Override
	public String toString() {
		return "DtoWord [id=" + id + ", word=" + word + ", translate=" + translate + ", form2=" + form2 + ", form3="
				+ form3 + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((form2 == null) ? 0 : form2.hashCode());
		result = prime * result + ((form3 == null) ? 0 : form3.hashCode());
		result = prime * result + id;
		result = prime * result + ((translate == null) ? 0 : translate.hashCode());
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DtoWord other = (DtoWord) obj;
		if (form2 == null) {
			if (other.form2 != null)
				return false;
		} else if (!form2.equals(other.form2))
			return false;
		if (form3 == null) {
			if (other.form3 != null)
				return false;
		} else if (!form3.equals(other.form3))
			return false;
		if (id != other.id)
			return false;
		if (translate == null) {
			if (other.translate != null)
				return false;
		} else if (!translate.equals(other.translate))
			return false;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}

}