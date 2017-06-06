package my.english.dto;

import java.io.Serializable;

import org.json.simple.JSONObject;

public class DtoStaticunit implements Serializable {

	private int id;
	private DtoWord word;
	private int idStatcat;

	public DtoStaticunit(DtoWord word, int idStatcat) {
		this.word = word;
		this.setIdStatcat(idStatcat);
	}

	public DtoStaticunit(int id, DtoWord word, int idStatcat) {
		this.id = id;
		this.word = word;
		this.setIdStatcat(idStatcat);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DtoWord getWord() {
		return word;
	}

	public void setWord(DtoWord word) {
		this.word = word;
	}

	public int getIdStatcat() {
		return idStatcat;
	}

	public void setIdStatcat(int idStatcat) {
		this.idStatcat = idStatcat;
	}

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", word.getId());
		json.put("word", word.getWord());
		json.put("translate", word.getTranslate());
		json.put("form2", word.getForm2());
		json.put("form3", word.getForm3());
		json.put("idstatcat", idStatcat);
		return json;
	}

	@Override
	public String toString() {
		return "DtoStaticunit [id=" + id + ", word=" + word + ", idStatcat=" + idStatcat + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idStatcat;
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
		DtoStaticunit other = (DtoStaticunit) obj;
		if (idStatcat != other.idStatcat)
			return false;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}
	
	

}
