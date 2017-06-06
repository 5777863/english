package my.english.dto;

import java.io.Serializable;

public class DtoStaticrating implements Serializable {

	private int id;
	private DtoStaticcategory staticcategory;
	private int idUser;
	private int truue;
	private int faalse;
	private int percent;

	public DtoStaticrating(int id, DtoStaticcategory staticcategory, int idUser, int truue, int faalse) {
		this.id = id;
		this.staticcategory = staticcategory;
		this.setUser(idUser);
		this.setTruue(truue);
		this.setFaalse(faalse);
		setPercent();
	}

	private void setPercent() {
		double good = this.getTruue();
		double bad = this.getFaalse();
		double summ = good + bad;
		if (good == 0) {
			this.percent = 0;
		} else {
			this.percent = (int) (100 / summ * good);
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DtoStaticcategory getStaticcategory() {
		return staticcategory;
	}

	public void setStaticcategory(DtoStaticcategory staticcategory) {
		this.staticcategory = staticcategory;
	}

	public int getUser() {
		return idUser;
	}

	public void setUser(int idUser) {
		this.idUser = idUser;
	}

	public int getTruue() {
		return truue;
	}

	public void setTruue(int truue) {
		this.truue = truue;
	}

	public int getFaalse() {
		return faalse;
	}

	public void setFaalse(int faalse) {
		this.faalse = faalse;
	}

	public int getPercent() {
		return percent;
	}

	@Override
	public String toString() {
		return "DtoStaticrating [staticcategory=" + staticcategory + ", idUser=" + idUser + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idUser;
		result = prime * result + ((staticcategory == null) ? 0 : staticcategory.hashCode());
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
		DtoStaticrating other = (DtoStaticrating) obj;
		if (idUser != other.idUser)
			return false;
		if (staticcategory == null) {
			if (other.staticcategory != null)
				return false;
		} else if (!staticcategory.equals(other.staticcategory))
			return false;
		return true;
	}
	
}
