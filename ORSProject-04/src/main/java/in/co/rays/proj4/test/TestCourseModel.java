package in.co.rays.proj4.test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.proj4.bean.CourseBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.CourseModel;

public class TestCourseModel {
	
	public static void main(String[] args) throws ApplicationException,DuplicateRecordException {
		//testAddCourse();
		//testUpdateCourse();
		//testDeleteCourse();
		//testFindByPk();
		//testFindByName();
		//testSearch();
		//testList();
	}
	
	
	public static void testAddCourse() throws ApplicationException, DuplicateRecordException  {
		CourseModel model = new CourseModel();
		CourseBean bean = new CourseBean();
		bean.setName("Java");
		bean.setDescription("Python course");
		bean.setDuration("3 months");
		bean.setCreatedBy("Aniket");
		bean.setModifiedBy("Aniket");
		bean.setCreatedDateTime(new Timestamp(new Date().getTime()));
		bean.setModifiedDateTime(new Timestamp(new Date().getTime()));
		Long id =  model.addCourse(bean);
		System.out.println("Course added with id "+id);
	}
	
	public static void testUpdateCourse() throws ApplicationException, DuplicateRecordException {
		CourseModel model = new CourseModel();
		CourseBean bean = new CourseBean();
		bean.setId(1l);
		bean.setName("Python");
		bean.setDescription("Core python");
		bean.setDuration("2 months");
		bean.setCreatedBy("Anurag");
		bean.setModifiedBy("Aniket");
		bean.setCreatedDateTime(new Timestamp(new Date().getTime()));
		bean.setModifiedDateTime(new Timestamp(new Date().getTime()));
		model.updateCourse(bean);
	}
	
	public static void testDeleteCourse() throws ApplicationException  {
		CourseModel model = new CourseModel();
		model.deleteCourse(2l);
	}
	
	public static void testFindByPk() throws ApplicationException {
		CourseModel model = new CourseModel();
		CourseBean bean = model.findByPk(1l);
		System.out.println(bean.getId());
		System.out.println(bean.getName());
		System.out.println(bean.getDescription());
		System.err.println(bean.getDuration());
		System.out.println(bean.getCreatedBy());
		System.out.println(bean.getModifiedBy());
		System.out.println(bean.getCreatedDateTime());
		System.out.println(bean.getModifiedDateTime());
		
	}
	
	public static void testFindByName() throws ApplicationException  {
		CourseModel model = new CourseModel();
		CourseBean bean = model.findByName("Java");
		System.out.println(bean.getId());
		System.out.println(bean.getName());
		System.out.println(bean.getDescription());
		System.out.println(bean.getDuration());
		System.out.println(bean.getCreatedBy());
		System.out.println(bean.getModifiedBy());
		System.out.println(bean.getCreatedDateTime());
		System.out.println(bean.getModifiedDateTime());
		
	}
	
	public static void testSearch() throws ApplicationException  {
		CourseModel model = new CourseModel();
		List<CourseBean> list = model.search(null, 1, 5);
		Iterator<CourseBean> it = list.iterator();
		
		while(it.hasNext()) {
			CourseBean bean =  it.next();
			System.out.println(bean.getId());
			System.out.println(bean.getName());
			System.out.println(bean.getDescription());
			System.out.println(bean.getDuration());
			System.out.println(bean.getCreatedBy());
			System.out.println(bean.getModifiedBy());
			System.out.println(bean.getCreatedDateTime());
			System.out.println(bean.getModifiedDateTime());
		}
	}
	
	public static void testList() throws ApplicationException  {
		CourseModel model = new CourseModel();
		List<CourseBean> list = model.list();
		Iterator<CourseBean> it = list.iterator();
		
		while(it.hasNext()) {
			CourseBean bean =  it.next();
			System.out.println(bean.getId());
			System.out.println(bean.getName());
			System.out.println(bean.getDescription());
			System.out.println(bean.getDuration());
			System.out.println(bean.getCreatedBy());
			System.out.println(bean.getModifiedBy());
			System.out.println(bean.getCreatedDateTime());
			System.out.println(bean.getModifiedDateTime());
		}
	}

}
