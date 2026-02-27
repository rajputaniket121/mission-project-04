package in.co.rays.proj4.bean;

import java.util.Date;

public class SchoolAttendanceBean extends BaseBean {
    private String studentName;
    private String className;
    private Date attendanceDate;
    private String attendanceStatus;
    private String remarks;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String getKey() {
        return id + "";
    }

    @Override
    public String getValue() {
        return studentName + " (" + attendanceStatus + ")";
    }

    @Override
    public String toString() {
        return "SchoolAttendanceBean [studentName=" + studentName + ", className=" + className + ", attendanceDate="
                + attendanceDate + ", attendanceStatus=" + attendanceStatus + ", remarks=" + remarks + "]";
    }
}