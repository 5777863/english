package my.english.dto;

import java.io.Serializable;

public class DtoFacts implements Serializable {

	private int id;
	private String fact;

	public DtoFacts(int id, String fact) {
		this.id = id;
		this.fact = fact;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFact() {
		return fact;
	}

	public void setFact(String fact) {
		this.fact = fact;
	}

	@Override
	public String toString() {
		return "DtoFacts [id=" + id + ", fact=" + fact + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fact == null) ? 0 : fact.hashCode());
		result = prime * result + id;
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
		DtoFacts other = (DtoFacts) obj;
		if (fact == null) {
			if (other.fact != null)
				return false;
		} else if (!fact.equals(other.fact))
			return false;
		if (id != other.id)
			return false;
		return true;
	}
}
