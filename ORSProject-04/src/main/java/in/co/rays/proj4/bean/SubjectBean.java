package in.co.rays.proj4.bean;

public class SubjectBean extends BaseBean{
	private String name;
	private Long courseId;
	private String courseName;
	private String description;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getCourseId() {
		return courseId;
	}
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	@Override
	public String toString() {
		return "SubjectBean [name=" + name + ", courseId=" + courseId + ", courseName=" + courseName + ", description="
				+ description + "]";
	}
	
	

}
