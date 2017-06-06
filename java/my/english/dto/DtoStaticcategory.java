package my.english.dto;

import java.io.Serializable;

public class DtoStaticcategory implements Serializable {
	private int id;
	private String unitname;
	private String groupname;

	public DtoStaticcategory(String groupname) {
		this.groupname = groupname;
	}

	public DtoStaticcategory(int id, String unitname, String groupname) {
		this.id = id;
		this.unitname = unitname;
		this.groupname = groupname;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	@Override
	public String toString() {
		return "DtoStaticcategory [id=" + id + ", unitname=" + unitname + ", groupname=" + groupname + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupname == null) ? 0 : groupname.hashCode());
			result = prime * result + ((unitname == null) ? 0 : unitname.hashCode());
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
		DtoStaticcategory other = (DtoStaticcategory) obj;
		if (groupname == null) {
			if (other.groupname != null)
				return false;
		} else if (!groupname.equals(other.groupname))
			return false;
		if (id != other.id)
			return false;
		if (unitname == null) {
			if (other.unitname != null)
				return false;
		} else if (!unitname.equals(other.unitname))
			return false;
		return true;
	}

}
