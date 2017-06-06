package my.english.dto;

import java.io.Serializable;
import java.util.Comparator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DtoUrword implements Serializable {

	private int id;
	private DtoUnit unit;
	private DtoWord word;

	public DtoUrword() {

	}

	public DtoUrword(DtoUnit unit, DtoWord word) {
		this.setUnit(unit);
		this.setWord(word);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DtoUnit getUnit() {
		return unit;
	}

	public void setUnit(DtoUnit unit) {
		this.unit = unit;
	}

	public DtoWord getWord() {
		return word;
	}

	public void setWord(DtoWord word) {
		this.word = word;
	}

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", word.getId());
		json.put("word", word.getWord());
		json.put("translate", word.getTranslate());
		json.put("form2", word.getForm2());
		json.put("form3", word.getForm3());
		json.put("unit", unit.getId());
		return json;
	}

	@Override
	public String toString() {
		return "DtoUrword [id=" + id + ", unit=" + unit + ", word=" + word + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
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
		DtoUrword other = (DtoUrword) obj;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}

}