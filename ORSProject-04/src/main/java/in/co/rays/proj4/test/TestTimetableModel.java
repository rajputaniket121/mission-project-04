package in.co.rays.proj4.test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.proj4.bean.TimetableBean;
import in.co.rays.proj4.model.TimetableModel;

public class TestTimetableModel {
	
	public static void main(String[] args) throws SQLException {
		//testAddTimetable();
		//testUpdateTimetable();
		//testDeleteTimetable();
		//testFindByPk();
		//testSearch();
		testList();
	}
	
	
	public static void testAddTimetable() throws SQLException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		TimetableModel model = new TimetableModel();
		TimetableBean bean = new TimetableBean();
		bean.setSemester("1st");
		bean.setDescription("desc");
		bean.setExamDate(sdf.parse("12-09-2000"));
		bean.setExamTime("time");
		bean.setCourseId(1l);
		bean.setCourseName("java");
		bean.setSubjectId(2l);
		bean.setSubjectName("king");
		bean.setCreatedBy("Aniket");
		bean.setModifiedBy("Aniket");
		bean.setCreatedDateTime(new Timestamp(new Date().getTime()));
		bean.setModifiedDateTime(new Timestamp(new Date().getTime()));
		Long id =  model.addTimetable(bean);
		System.out.println("Timetable added with id "+id);
	}
	
	public static void testUpdateTimetable() throws SQLException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		TimetableModel model = new TimetableModel();
		TimetableBean bean = new TimetableBean();
		bean.setId(1l);
		bean.setSemester("1st");
		bean.setDescription("desc");
		bean.setExamDate(sdf.parse("12-09-2000"));
		bean.setExamTime("time");
		bean.setCourseId(1l);
		bean.setCourseName("java");
		bean.setSubjectId(2l);
		bean.setSubjectName("king");
		bean.setCreatedBy("Anurag");
		bean.setModifiedBy("Aniket");
		bean.setCreatedDateTime(new Timestamp(new Date().getTime()));
		bean.setModifiedDateTime(new Timestamp(new Date().getTime()));
		model.updateTimetable(bean);
	}
	
	public static void testDeleteTimetable() throws SQLException {
		TimetableModel model = new TimetableModel();
		model.deleteTimetable(2l);
	}
	
	public static void testFindByPk() throws SQLException {
		TimetableModel model = new TimetableModel();
		TimetableBean bean = model.findByPk(1l);
		System.out.println(bean.getId());
		System.out.println(bean.getSemester());
		System.out.println(bean.getDescription());
		System.out.println(bean.getExamDate());
		System.out.println(bean.getExamTime());
		System.out.println(bean.getCourseId());
		System.out.println(bean.getCourseName());
		System.out.println(bean.getSubjectId());
		System.out.println(bean.getSubjectName());
		System.out.println(bean.getCreatedBy());
		System.out.println(bean.getModifiedBy());
		System.out.println(bean.getCreatedDateTime());
		System.out.println(bean.getModifiedDateTime());
		
	}
	
	public static void testSearch() throws SQLException {
		TimetableModel model = new TimetableModel();
		List<TimetableBean> list = model.search(null, 1, 5);
		Iterator<TimetableBean> it = list.iterator();
		
		while(it.hasNext()) {
			TimetableBean bean =  it.next();
			System.out.println(bean.getId());
			System.out.println(bean.getSemester());
			System.out.println(bean.getDescription());
			System.out.println(bean.getExamDate());
			System.out.println(bean.getExamTime());
			System.out.println(bean.getCourseId());
			System.out.println(bean.getCourseName());
			System.out.println(bean.getSubjectId());
			System.out.println(bean.getSubjectName());
			System.out.println(bean.getCreatedBy());
			System.out.println(bean.getModifiedBy());
			System.out.println(bean.getCreatedDateTime());
			System.out.println(bean.getModifiedDateTime());
		}
	}
	
	public static void testList() throws SQLException {
		TimetableModel model = new TimetableModel();
		List<TimetableBean> list = model.list();
		Iterator<TimetableBean> it = list.iterator();
		
		while(it.hasNext()) {
			TimetableBean bean =  it.next();
			System.out.println(bean.getId());
			System.out.println(bean.getSemester());
			System.out.println(bean.getDescription());
			System.out.println(bean.getExamDate());
			System.out.println(bean.getExamTime());
			System.out.println(bean.getCourseId());
			System.out.println(bean.getCourseName());
			System.out.println(bean.getSubjectId());
			System.out.println(bean.getSubjectName());
			System.out.println(bean.getCreatedBy());
			System.out.println(bean.getModifiedBy());
			System.out.println(bean.getCreatedDateTime());
			System.out.println(bean.getModifiedDateTime());
		}
	}

}
