package in.co.rays.proj4.bean;

import java.util.Date;

public class TimetableBean extends BaseBean {
	private String semester;
	private String description;
	private Date examDate;
	private String examTime;
	private Long courseId;
	private String courseName;
	private Long subjectId;
	private String subjectName;
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getExamDate() {
		return examDate;
	}
	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}
	public String getExamTime() {
		return examTime;
	}
	public void setExamTime(String examTime) {
		this.examTime = examTime;
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
	public Long getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	
	
	@Override
	public String toString() {
		return "TimetableBean [semester=" + semester + ", description=" + description + ", examDate=" + examDate
				+ ", examTime=" + examTime + ", courseId=" + courseId + ", courseName=" + courseName + ", subjectId="
				+ subjectId + ", subjectName=" + subjectName + "]";
	}

	
	
}
