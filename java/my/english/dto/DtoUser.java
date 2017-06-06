package my.english.dto;

import java.io.Serializable;

import org.json.simple.JSONObject;

public class DtoUser implements Serializable {

	private int id;
	private String uname;
	private String upass;
	private String umail;
	private String active;
	private String solt;
	private String psw;

	public DtoUser() {

	}

	public DtoUser(int id, String uname) {
		this.setId(id);
		this.setUname(uname);
	}

	public DtoUser(int id, String uname, String upass, String umail) {
		this.setId(id);
		this.setUname(uname);
		this.setUpass(upass);
		this.setUmail(umail);
		// this.setActive(active);
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getUmail() {
		return umail;
	}

	public void setUmail(String umail) {
		this.umail = umail;
	}

	public String getUpass() {
		return upass;
	}

	public void setUpass(String upass) {
		this.upass = upass;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getSolt() {
		return solt;
	}

	public void setSolt(String solt) {
		this.solt = solt;
	}

	public String getPsw() {
		return psw;
	}

	public void setPsw(String psw) {
		this.psw = psw;
	}

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", id);
		json.put("name", uname);
		return json;
	}

	@Override
	public String toString() {
		return "DtoUser [id=" + id + ", uname=" + uname + ", upass=" + upass + ", umail=" + umail + ", active=" + active
				+ ", solt=" + solt + ", psw=" + psw + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result + id;
		result = prime * result + ((psw == null) ? 0 : psw.hashCode());
		result = prime * result + ((solt == null) ? 0 : solt.hashCode());
		result = prime * result + ((umail == null) ? 0 : umail.hashCode());
		result = prime * result + ((uname == null) ? 0 : uname.hashCode());
		result = prime * result + ((upass == null) ? 0 : upass.hashCode());
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
		DtoUser other = (DtoUser) obj;
		if (active == null) {
			if (other.active != null)
				return false;
		} else if (!active.equals(other.active))
			return false;
		if (id != other.id)
			return false;
		if (psw == null) {
			if (other.psw != null)
				return false;
		} else if (!psw.equals(other.psw))
			return false;
		if (solt == null) {
			if (other.solt != null)
				return false;
		} else if (!solt.equals(other.solt))
			return false;
		if (umail == null) {
			if (other.umail != null)
				return false;
		} else if (!umail.equals(other.umail))
			return false;
		if (uname == null) {
			if (other.uname != null)
				return false;
		} else if (!uname.equals(other.uname))
			return false;
		if (upass == null) {
			if (other.upass != null)
				return false;
		} else if (!upass.equals(other.upass))
			return false;
		return true;
	}

}