package in.co.rays.proj4.test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.proj4.bean.StudentBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.StudentModel;

public class TestStudentModel {
	
	public static void main(String[] args) throws ParseException, ApplicationException, DuplicateRecordException {
//		testAddStudent();
//		testUpdateStudent();
//		testDeleteStudent();
//		testFindByPk();
//		testFindByEmail();
//		testSearch();
		//testList();
	}
	
	
	public static void testAddStudent() throws ParseException, ApplicationException, DuplicateRecordException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		StudentModel model = new StudentModel();
		StudentBean bean = new StudentBean();
		bean.setFirstName("Aniket");
		bean.setLastName("Rajput");
		bean.setDob(sdf.parse("12-02-2000"));
		bean.setGender("Male");
		bean.setMobileNo("7898037387");
		bean.setEmail("aniket@gmail.com");
		bean.setCollegeId(1l);;
		bean.setCreatedBy("Aniket");
		bean.setModifiedBy("Aniket");
		bean.setCreatedDateTime(new Timestamp(new Date().getTime()));
		bean.setModifiedDateTime(new Timestamp(new Date().getTime()));
		Long id =  model.addStudent(bean);
		System.out.println("Student added with id "+id);
	}
	
	public static void testUpdateStudent() throws  ParseException, ApplicationException, DuplicateRecordException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		StudentModel model = new StudentModel();
		StudentBean bean = new StudentBean();
		bean.setId(1l);
		bean.setFirstName("Aniket");
		bean.setLastName("Rajput");
		bean.setDob(sdf.parse("12-02-2000"));
		bean.setGender("Male");
		bean.setMobileNo("7898037387");
		bean.setEmail("aniket@gmail.com");
		bean.setCollegeId(2l);;
		bean.setCreatedBy("Aniket");
		bean.setModifiedBy("Aniket");
		bean.setCreatedDateTime(new Timestamp(new Date().getTime()));
		bean.setModifiedDateTime(new Timestamp(new Date().getTime()));
		model.updateStudent(bean);
	}
	
	public static void testDeleteStudent() throws ApplicationException {
		StudentModel model = new StudentModel();
		model.deleteStudent(2l);
	}
	
	public static void testFindByPk() throws  ApplicationException {
		StudentModel model = new StudentModel();
		StudentBean bean = model.findByPk(1l);
		System.out.println(bean.getId());
		System.out.println(bean.getFirstName());
		System.out.println(bean.getLastName());
		System.out.println(bean.getDob());
		System.out.println(bean.getGender());
		System.out.println(bean.getMobileNo());
		System.out.println(bean.getEmail());
		System.out.println(bean.getCollegeId());
		System.out.println(bean.getCollegeName());
		System.out.println(bean.getCreatedBy());
		System.out.println(bean.getModifiedBy());
		System.out.println(bean.getCreatedDateTime());
		System.out.println(bean.getModifiedDateTime());
	}
	
	public static void testFindByEmail() throws ApplicationException {
		StudentModel model = new StudentModel();
		StudentBean bean = model.findByEmail("aniket@gmail.com");
		System.out.println(bean.getId());
		System.out.println(bean.getFirstName());
		System.out.println(bean.getLastName());
		System.out.println(bean.getDob());
		System.out.println(bean.getGender());
		System.out.println(bean.getMobileNo());
		System.out.println(bean.getEmail());
		System.out.println(bean.getCollegeId());
		System.out.println(bean.getCollegeName());
		System.out.println(bean.getCreatedBy());
		System.out.println(bean.getModifiedBy());
		System.out.println(bean.getCreatedDateTime());
		System.out.println(bean.getModifiedDateTime());
		
	}
	
	public static void testSearch() throws ApplicationException {
		StudentModel model = new StudentModel();
		List<StudentBean> list = model.search(null, 1, 5);
		Iterator<StudentBean> it = list.iterator();
		
		while(it.hasNext()) {
			StudentBean bean =  it.next();
			System.out.println(bean.getId());
			System.out.println(bean.getFirstName());
			System.out.println(bean.getLastName());
			System.out.println(bean.getDob());
			System.out.println(bean.getGender());
			System.out.println(bean.getMobileNo());
			System.out.println(bean.getEmail());
			System.out.println(bean.getCollegeId());
			System.out.println(bean.getCollegeName());
			System.out.println(bean.getCreatedBy());
			System.out.println(bean.getModifiedBy());
			System.out.println(bean.getCreatedDateTime());
			System.out.println(bean.getModifiedDateTime());
		}
	}
	
	public static void testList() throws ApplicationException {
		StudentModel model = new StudentModel();
		List<StudentBean> list = model.list();
		Iterator<StudentBean> it = list.iterator();
		
		while(it.hasNext()) {
			StudentBean bean =  it.next();
			System.out.println(bean.getId());
			System.out.println(bean.getFirstName());
			System.out.println(bean.getLastName());
			System.out.println(bean.getDob());
			System.out.println(bean.getGender());
			System.out.println(bean.getMobileNo());
			System.out.println(bean.getEmail());
			System.out.println(bean.getCollegeId());
			System.out.println(bean.getCollegeName());
			System.out.println(bean.getCreatedBy());
			System.out.println(bean.getModifiedBy());
			System.out.println(bean.getCreatedDateTime());
			System.out.println(bean.getModifiedDateTime());
		}
	}

}
