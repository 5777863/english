package my.english.dto;

import java.io.Serializable;

import org.json.simple.JSONObject;

public class DtoUnit implements Serializable {

	private int id;
	private String unit;
	private DtoUser user;
	private int truue;
	private int faalse;
	private int percent;

	public DtoUnit(String unit, DtoUser user) {
		this.unit = unit;
		this.user = user;
	}

	public DtoUnit(int id, String unit, DtoUser user, int truue, int faalse) {
		this.setId(id);
		this.unit = unit;
		this.user = user;
		this.truue = truue;
		this.faalse = faalse;
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public DtoUser getUser() {
		return user;
	}

	public void setUser(DtoUser user) {
		this.user = user;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPercent() {
		return percent;
	}

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", this.id);
		json.put("unit", this.unit);
		json.put("iduser", this.user.getId());
		json.put("percent", this.percent);
		return json;
	}

	@Override
	public String toString() {
		return "DtoUnit [id=" + id + ", unit=" + unit + ", user=" + user + ", truue=" + truue + ", faalse=" + faalse
				+ ", percent=" + percent + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + percent;
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		DtoUnit other = (DtoUnit) obj;
		if (percent != other.percent)
			return false;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	
}