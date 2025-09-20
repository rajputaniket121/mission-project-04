package in.co.rays.proj4.bean;

public class RoleBean extends BaseBean{
	
	public static final Long ADMIN = 1l;
	public static final Long STUDENT = 2l;
	public static final Long USER = 3l;
	public static final Long COLLEGE = 4l; 
	public static final Long FACULTY = 5l; 
	
	private String name;
	private String description;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "RoleBean [name=" + name + ", description=" + description + "]";
	}
	@Override
	public String getKey() {
		return id+"";
	}
	@Override
	public String getValue() {
		return name;
	}
	
	

}
