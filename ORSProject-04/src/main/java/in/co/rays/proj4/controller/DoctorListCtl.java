package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.DoctorBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.DoctorModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

/**
 * DoctorListCtl is a servlet controller that manages listing, searching,
 * pagination, and deletion of Doctor records in the application.
 *
 * <p>
 * It extends {@link BaseCtl} and provides functionality for:
 * <ul>
 *   <li>Preloading doctor specialties (experties) for search filters</li>
 *   <li>Populating {@link DoctorBean} from search form parameters</li>
 *   <li>Handling GET requests to display the doctor list with optional search criteria</li>
 *   <li>Handling POST requests for search, pagination, deletion, reset, and new doctor redirect</li>
 * </ul>
 * </p>
 *
 * <p>
 * URL mapping: <code>/ctl/DoctorListCtl</code>
 * </p>
 */
@WebServlet(name = "DoctorListCtl", urlPatterns = {"/ctl/DoctorListCtl"})
public class DoctorListCtl extends BaseCtl {

    private static final Logger log = Logger.getLogger(DoctorListCtl.class.getName());

    /**
     * Preloads the list of doctor specialties (experties) for search filter dropdown.
     *
     * @param request the HttpServletRequest object
     */
    @Override
    protected void preload(HttpServletRequest request) {
        log.info("Inside preload method of DoctorListCtl");
        DoctorModel model = new DoctorModel();
        try {
            List<DoctorBean> doctorExpertiesList = model.list();
            HashMap<String, String> expertiesMap = new HashMap<>();
            Iterator<DoctorBean> it = doctorExpertiesList.iterator();

            while (it.hasNext()) {
                DoctorBean bean = it.next();
                expertiesMap.put(bean.getExperties(), bean.getExperties());
            }
            request.setAttribute("expertiesMap", expertiesMap);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Populates a {@link DoctorBean} from search form parameters.
     *
     * @param request the HttpServletRequest object
     * @return a DoctorBean populated with search criteria
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        log.info("Inside populateBean method of DoctorListCtl");
        DoctorBean bean = new DoctorBean();
        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setDob(DataUtility.getDate(request.getParameter("dob")));
        bean.setExperties(DataUtility.getString(request.getParameter("experties")));
        return bean;
    }

    /**
     * Handles HTTP GET requests to display the doctor list with optional search criteria.
     *
     * @param req the HttpServletRequest object
     * @param resp the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("Inside doGet method of DoctorListCtl");

        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
        DoctorBean bean = (DoctorBean) populateBean(req);
        DoctorModel model = new DoctorModel();

        try {
            List<DoctorBean> list = model.search(bean, pageNo, pageSize);
            List<DoctorBean> next = model.search(bean, pageNo + 1, pageSize);

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

    /**
     * Handles HTTP POST requests for searching, pagination, deletion,
     * reset, and redirection to new doctor form.
     *
     * @param req the HttpServletRequest object
     * @param resp the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("Inside doPost method of DoctorListCtl");

        List<DoctorBean> list = null;
        List<DoctorBean> next = null;

        int pageNo = DataUtility.getInt(req.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(req.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

        DoctorBean bean = (DoctorBean) populateBean(req);
        DoctorModel model = new DoctorModel();

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
                ServletUtility.redirect(ORSView.DOCTOR_LIST_CTL, req, resp);
                return;
            } else if (OP_NEW.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.DOCTOR_CTL, req, resp);
                return;
            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                pageNo = 1;
                String[] ids = req.getParameterValues("ids");
                if (ids != null && ids.length > 0) {
                    DoctorBean deleteBean = new DoctorBean();
                    for (String id : ids) {
                        deleteBean.setId(DataUtility.getLong(id));
                        model.deleteDoctor(deleteBean.getId());
                        ServletUtility.setSuccessMessage("Deleted Successfully !!!", req);
                    }
                } else {
                    ServletUtility.setErrorMessage("Select Atleast one Checkbox", req);
                }
            } else if (OP_BACK.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.DOCTOR_LIST_CTL, req, resp);
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

    /**
     * Returns the view (JSP page) associated with this controller.
     *
     * @return the doctor list view JSP path
     */
    @Override
    protected String getView() {
        log.info("Inside getView method of DoctorListCtl");
        return ORSView.DOCTOR_LIST_VIEW;
    }
}