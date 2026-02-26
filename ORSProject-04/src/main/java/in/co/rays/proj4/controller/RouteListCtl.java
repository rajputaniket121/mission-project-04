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
import in.co.rays.proj4.bean.RouteBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.RouteModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

/**
 * RouteListCtl is a servlet controller that manages listing, searching,
 * pagination, and deletion of Route records in the application.
 */
@WebServlet(name = "RouteListCtl", urlPatterns = {"/ctl/RouteListCtl"})
public class RouteListCtl extends BaseCtl {

    /**
     * Preloads the list of source and destination cities for search filter dropdown.
     *
     * @param request the HttpServletRequest object
     */
    @Override
    protected void preload(HttpServletRequest request) {
        HashMap<String, String> cityMap = new HashMap<>();
        cityMap.put("Mumbai", "Mumbai");
        cityMap.put("Delhi", "Delhi");
        cityMap.put("Bangalore", "Bangalore");
        cityMap.put("Chennai", "Chennai");
        cityMap.put("Kolkata", "Kolkata");
        cityMap.put("Hyderabad", "Hyderabad");
        cityMap.put("Pune", "Pune");
        cityMap.put("Ahmedabad", "Ahmedabad");
        cityMap.put("Jaipur", "Jaipur");
        cityMap.put("Lucknow", "Lucknow");
        request.setAttribute("cityMap", cityMap);
    }

    /**
     * Populates a {@link RouteBean} from search form parameters.
     *
     * @param request the HttpServletRequest object
     * @return a RouteBean populated with search criteria
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        RouteBean bean = new RouteBean();
        bean.setRouteCode(DataUtility.getString(request.getParameter("routeCode")));
        bean.setSource(DataUtility.getString(request.getParameter("source")));
        bean.setDestination(DataUtility.getString(request.getParameter("destination")));
        return bean;
    }

    /**
     * Handles HTTP GET requests to display the route list with optional search criteria.
     *
     * @param req the HttpServletRequest object
     * @param resp the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
        RouteBean bean = (RouteBean) populateBean(req);
        RouteModel model = new RouteModel();

        try {
            List<RouteBean> list = model.search(bean, pageNo, pageSize);
            List<RouteBean> next = model.search(bean, pageNo + 1, pageSize);

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
     * reset, and redirection to new route form.
     *
     * @param req the HttpServletRequest object
     * @param resp the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<RouteBean> list = null;
        List<RouteBean> next = null;

        int pageNo = DataUtility.getInt(req.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(req.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

        RouteBean bean = (RouteBean) populateBean(req);
        RouteModel model = new RouteModel();

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
                ServletUtility.redirect(ORSView.ROUTE_LIST_CTL, req, resp);
                return;
            } else if (OP_NEW.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.ROUTE_CTL, req, resp);
                return;
            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                pageNo = 1;
                String[] ids = req.getParameterValues("ids");
                if (ids != null && ids.length > 0) {
                    RouteBean deleteBean = new RouteBean();
                    for (String id : ids) {
                        deleteBean.setId(DataUtility.getLong(id));
                        model.deleteRoute(deleteBean.getId());
                        ServletUtility.setSuccessMessage("Deleted Successfully !!!", req);
                    }
                } else {
                    ServletUtility.setErrorMessage("Select Atleast one Checkbox", req);
                }
            } else if (OP_BACK.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.ROUTE_LIST_CTL, req, resp);
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
     * @return the route list view JSP path
     */
    @Override
    protected String getView() {
        return ORSView.ROUTE_LIST_VIEW;
    }
}