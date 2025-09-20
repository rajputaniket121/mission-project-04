package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.CourseBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.CourseModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "CourseListCtl",urlPatterns = {"/ctl/CourseListCtl"})
public class CourseListCtl extends BaseCtl{
	
	@Override
	protected void preload(HttpServletRequest request) {
		CourseModel model = new CourseModel();
		try {
			List<CourseBean> courseList = model.list();
			request.setAttribute("courseList", courseList);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		CourseBean bean = new CourseBean();
		bean.setId(DataUtility.getLong(request.getParameter("courseId")));
		return bean;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		CourseBean bean = (CourseBean) populateBean(req);
		CourseModel model = new CourseModel();
		try {
			List<CourseBean> list= model.search(bean, pageNo, pageSize);
			List<CourseBean> next= model.search(bean, pageNo+1, pageSize);
			if(list.isEmpty() || list==null) {
				ServletUtility.setErrorMessage("No Records found", req);
			}
			ServletUtility.setList(list, req);
			ServletUtility.setBean(bean, req);
			ServletUtility.setPageNo(pageNo, req);
			ServletUtility.setPageSize(pageSize, req);
			req.setAttribute("nextListSize", next.size());
		} catch (ApplicationException e) {
			e.printStackTrace();
			ServletUtility.handleException(e, req, resp);
			return;
		}
		ServletUtility.forward(getView(), req, resp);
	}
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<CourseBean> list = null;
		List<CourseBean> next = null;
		
		int pageNo = DataUtility.getInt(req.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(req.getParameter("pageSize"));
		
		pageNo = (pageNo==0)? 1 : pageNo;
		pageSize = (pageSize==0)? (DataUtility.getInt(PropertyReader.getValue("page.size")))  : pageSize;
		CourseBean bean = (CourseBean) populateBean(req);
		CourseModel model = new CourseModel();
		
		String op = DataUtility.getString(req.getParameter("operation"));
		
		try {
			
		if(OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {
			
			if(OP_SEARCH.equalsIgnoreCase(op)) {
				pageNo=1;
				
			}else if(OP_NEXT.equalsIgnoreCase(op)) {
				pageNo++;
				
			}else if(OP_PREVIOUS.equalsIgnoreCase(op) && pageNo>1) {
				pageNo--;
			}
			
			
		} else if(OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.COURSE_LIST_CTL, req, resp);
			return;
		}else if(OP_NEW.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.COURSE_CTL, req, resp);
			return;
			
		}else if(OP_DELETE.equalsIgnoreCase(op)) {
			pageNo=1;
			String[] ids = req.getParameterValues("ids");	
			if(ids!=null && ids.length>0) {
				CourseBean deleteBean = new CourseBean();
				for(String id  : ids) {
					deleteBean.setId(DataUtility.getLong(id));
					model.deleteCourse(deleteBean.getId());
					ServletUtility.setSuccessMessage("Deleted Successfully !!!", req);
				}
			}else {
				ServletUtility.setErrorMessage("Select Atleast one Checkbox", req);
			}
			
		}else if(OP_BACK.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.COURSE_LIST_CTL, req, resp);
			return;
		}
		
		list= model.search(bean, pageNo, pageSize);
		next = model.search(bean, pageNo+1, pageSize);
			if(list.isEmpty() || list==null) {
				ServletUtility.setErrorMessage("No Records found", req);
			}
			ServletUtility.setList(list, req);
			ServletUtility.setBean(bean, req);
			ServletUtility.setPageNo(pageNo, req);
			ServletUtility.setPageSize(pageSize, req);
			req.setAttribute("nextListSize", next.size());
		} catch (ApplicationException e) {
			e.printStackTrace();
			ServletUtility.handleException(e, req, resp);
			return;
		}
		ServletUtility.forward(getView(), req, resp);
	}
	
	
	@Override
	protected String getView() {
		return ORSView.COURSE_LIST_VIEW;
	}

}
