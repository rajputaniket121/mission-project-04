package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.CollegeBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.CollegeModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "CollegeListCtl", urlPatterns = {"/ctl/CollegeListCtl"})
public class CollegeListCtl extends BaseCtl {
    
    private static final Logger log = Logger.getLogger(CollegeListCtl.class);
    
    @Override
    protected void preload(HttpServletRequest request) {
        log.debug("CollegeListCtl preload method started");
        CollegeModel model = new CollegeModel();
        try {
            log.debug("Loading college list for dropdown");
            List<CollegeBean> collegeList = model.list();
            request.setAttribute("collegeList", collegeList);
            log.info("College list loaded successfully");
        } catch (ApplicationException e) {
            log.error("Error loading college list: " + e.getMessage(), e);
            e.printStackTrace();
        }
        log.debug("CollegeListCtl preload method ended");
    }
    
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        log.debug("CollegeListCtl populateBean method started");
        CollegeBean bean = new CollegeBean();
        bean.setId(DataUtility.getLong(request.getParameter("collegeId")));
        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setCity(DataUtility.getString(request.getParameter("city")));
        log.debug("CollegeListCtl populateBean method ended");
        return bean;
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        log.debug("CollegeListCtl doGet method started");
        
        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
        CollegeBean bean = (CollegeBean) populateBean(request);
        CollegeModel model = new CollegeModel();
        
        try {
            log.debug("Searching colleges with page no: " + pageNo);
            List<CollegeBean> list = model.search(bean, pageNo, pageSize);
            List<CollegeBean> next = model.search(bean, pageNo + 1, pageSize);
            
            if (list == null || list.size() == 0) {
                log.info("No records found for the search criteria");
                ServletUtility.setErrorMessage("No record found ", request);
            } else {
                log.info("Found " + list.size() + " colleges matching the criteria");
                request.setAttribute("nextListSize", next.size());
                ServletUtility.setList(list, request);
            }
            
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.forward(getView(), request, response);
            
        } catch (ApplicationException e) {
            log.error("Error in college list search", e);
            ServletUtility.handleException(e, request, response);
            return;
        }
        log.debug("CollegeListCtl doGet method ended");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        log.debug("CollegeListCtl doPost method started");
        
        String op = DataUtility.getString(request.getParameter("operation"));
        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
        
        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
        
        CollegeBean bean = (CollegeBean) populateBean(request);
        String[] ids = request.getParameterValues("ids");
        CollegeModel model = new CollegeModel();
        
        if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {
            
            if (OP_SEARCH.equalsIgnoreCase(op)) {
                log.debug("Search operation requested");
                pageNo = 1;
            } else if (OP_NEXT.equalsIgnoreCase(op)) {
                log.debug("Next page requested");
                pageNo++;
            } else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
                log.debug("Previous page requested");
                pageNo--;
            }
            
        } else if (OP_NEW.equalsIgnoreCase(op)) {
            log.debug("New college operation requested");
            ServletUtility.redirect(ORSView.COLLEGE_CTL, request, response);
            return;
            
        } else if (OP_DELETE.equalsIgnoreCase(op)) {
            log.debug("Delete operation requested");
            if (ids != null && ids.length > 0) {
                log.debug("Deleting " + ids.length + " colleges");
                CollegeBean deleteBean = new CollegeBean();
                for (String id : ids) {
                    deleteBean.setId(DataUtility.getInt(id));
                    try {
                        model.deleteCollege(deleteBean.getId());
                        log.info("College deleted with ID: " + id);
                    } catch (ApplicationException e) {
                        log.error("Error deleting college with ID: " + id, e);
                        ServletUtility.handleException(e, request, response);
                        return;
                    }
                }
                ServletUtility.setSuccessMessage("Selected college(s) deleted successfully", request);
            } else {
                log.warn("No college selected for deletion");
                ServletUtility.setErrorMessage("Select at least one record", request);
            }
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            log.debug("Reset operation requested");
            ServletUtility.redirect(ORSView.COLLEGE_LIST_CTL, request, response);
            return;
        }
        
        try {
            log.debug("Fetching college list for page: " + pageNo);
            List<CollegeBean> list = model.search(bean, pageNo, pageSize);
            List<CollegeBean> next = model.search(bean, pageNo + 1, pageSize);
            
            if (list == null || list.size() == 0) {
                log.info("No records found for the search criteria");
                if (!OP_DELETE.equalsIgnoreCase(op)) {
                    ServletUtility.setErrorMessage("No record found ", request);
                }
            } else {
                log.info("Found " + list.size() + " colleges matching the criteria");
                request.setAttribute("nextListSize", next.size());
                ServletUtility.setList(list, request);
            }
            
            ServletUtility.setBean(bean, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            
        } catch (ApplicationException e) {
            log.error("Error in college list operation", e);
            ServletUtility.handleException(e, request, response);
            return;
        }
        
        ServletUtility.forward(getView(), request, response);
        log.debug("CollegeListCtl doPost method ended");
    }
    
    @Override
    protected String getView() {
        log.debug("CollegeListCtl getView method called");
        return ORSView.COLLEGE_LIST_VIEW;
    }
}
