package in.co.rays.proj4.bean;

import java.sql.Timestamp;

public class BaseBean {
	private Long id;
	private String createdBy;
	private String modifiedBy;
	private Timestamp createdDateTime;
	private Timestamp modifiedDateTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Timestamp getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(Timestamp createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public Timestamp getModifiedDateTime() {
		return modifiedDateTime;
	}
	public void setModifiedDateTime(Timestamp modifiedDateTime) {
		this.modifiedDateTime = modifiedDateTime;
	}
	@Override
	public String toString() {
		return "BaseBean [id=" + id + ", created_by=" + createdBy + ", modified_by=" + modifiedBy
				+ ", createdDateTime=" + createdDateTime + ", modifiedDateTime=" + modifiedDateTime + "]";
	}
	
	

}
