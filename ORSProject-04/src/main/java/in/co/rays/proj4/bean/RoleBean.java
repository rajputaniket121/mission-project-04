package in.co.rays.proj4.bean;

public class RoleBean extends BaseBean{
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
	
	

}
