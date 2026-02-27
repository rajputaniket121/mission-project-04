package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.SchoolAttendanceBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.SchoolAttendanceModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "SchoolAttendanceListCtl", urlPatterns = {"/ctl/SchoolAttendanceListCtl"})
public class SchoolAttendanceListCtl extends BaseCtl {

    @Override
    protected void preload(HttpServletRequest request) {
        SchoolAttendanceModel model = new SchoolAttendanceModel();
        try {
            List<SchoolAttendanceBean> attendanceList = model.list();
            HashMap<String, String> statusMap = new HashMap<>();
            HashMap<String, String> classMap = new HashMap<>();
            Iterator<SchoolAttendanceBean> it = attendanceList.iterator();

            while (it.hasNext()) {
                SchoolAttendanceBean bean = it.next();
                statusMap.put(bean.getAttendanceStatus(), bean.getAttendanceStatus());
                classMap.put(bean.getClassName(), bean.getClassName());
            }
            request.setAttribute("statusMap", statusMap);
            request.setAttribute("classMap", classMap);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        SchoolAttendanceBean bean = new SchoolAttendanceBean();
        bean.setStudentName(DataUtility.getString(request.getParameter("studentName")));
        bean.setClassName(DataUtility.getString(request.getParameter("className")));
        bean.setAttendanceStatus(DataUtility.getString(request.getParameter("attendanceStatus")));
        bean.setAttendanceDate(DataUtility.getDate(request.getParameter("attendanceDate")));
        return bean;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
        SchoolAttendanceBean bean = (SchoolAttendanceBean) populateBean(req);
        SchoolAttendanceModel model = new SchoolAttendanceModel();

        try {
            List<SchoolAttendanceBean> list = model.search(bean, pageNo, pageSize);
            List<SchoolAttendanceBean> next = model.search(bean, pageNo + 1, pageSize);

            if (list.isEmpty() || list == null) {
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

        List<SchoolAttendanceBean> list = null;
        List<SchoolAttendanceBean> next = null;

        int pageNo = DataUtility.getInt(req.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(req.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

        SchoolAttendanceBean bean = (SchoolAttendanceBean) populateBean(req);
        SchoolAttendanceModel model = new SchoolAttendanceModel();

        String op = DataUtility.getString(req.getParameter("operation"));

        try {
            if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {
                if (OP_SEARCH.equalsIgnoreCase(op)) {
                    pageNo = 1;
                } else if (OP_NEXT.equalsIgnoreCase(op)) {
                    pageNo++;
                } else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
                    pageNo--;
                }
            } else if (OP_RESET.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.SCHOOL_ATTENDANCE_LIST_CTL, req, resp);
                return;
            } else if (OP_NEW.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.SCHOOL_ATTENDANCE_CTL, req, resp);
                return;
            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                pageNo = 1;
                String[] ids = req.getParameterValues("ids");
                if (ids != null && ids.length > 0) {
                    SchoolAttendanceBean deleteBean = new SchoolAttendanceBean();
                    for (String id : ids) {
                        deleteBean.setId(DataUtility.getLong(id));
                        model.deleteAttendance(deleteBean.getId());
                        ServletUtility.setSuccessMessage("Deleted Successfully !!!", req);
                    }
                } else {
                    ServletUtility.setErrorMessage("Select Atleast one Checkbox", req);
                }
            } else if (OP_BACK.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.SCHOOL_ATTENDANCE_LIST_CTL, req, resp);
                return;
            }

            list = model.search(bean, pageNo, pageSize);
            next = model.search(bean, pageNo + 1, pageSize);

            if (list.isEmpty() || list == null) {
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
        return ORSView.SCHOOL_ATTENDANCE_LIST_VIEW;
    }
}