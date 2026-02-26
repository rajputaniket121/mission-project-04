package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.RouteBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.RouteModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

/**
 * RouteCtl is a servlet controller that handles CRUD operations
 * for Route records in the application.
 */
@WebServlet(name = "RouteCtl", urlPatterns = "/ctl/RouteCtl")
public class RouteCtl extends BaseCtl {

    /**
     * Preloads source and destination cities for dropdown.
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
     * Validates route form data.
     *
     * @param request the HttpServletRequest object containing form data
     * @return true if form data is valid, false otherwise
     */
    @Override
    protected boolean validate(HttpServletRequest request) {
        boolean pass = true;
        String op = request.getParameter("operation");

        if (OP_LOG_OUT.equalsIgnoreCase(op) || OP_RESET.equalsIgnoreCase(op)) {
            return pass;
        }

        if (DataValidator.isNull(request.getParameter("routeCode"))) {
            request.setAttribute("routeCode", PropertyReader.getValue("error.require", "Route Code"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("source"))) {
            request.setAttribute("source", PropertyReader.getValue("error.require", "Source"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("destination"))) {
            request.setAttribute("destination", PropertyReader.getValue("error.require", "Destination"));
            pass = false;
        } else if (request.getParameter("source").equalsIgnoreCase(request.getParameter("destination"))) {
            request.setAttribute("destination", "Source and Destination cannot be same");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("distance"))) {
            request.setAttribute("distance", PropertyReader.getValue("error.require", "Distance"));
            pass = false;
        } else if (!DataValidator.isDouble(request.getParameter("distance"))) {
            request.setAttribute("distance", "Please enter valid distance");
            pass = false;
        }

        return pass;
    }

    /**
     * Populates a {@link RouteBean} with request parameters.
     *
     * @param request the HttpServletRequest object
     * @return a RouteBean populated with form data
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        RouteBean bean = new RouteBean();
        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setRouteCode(DataUtility.getString(request.getParameter("routeCode")));
        bean.setSource(DataUtility.getString(request.getParameter("source")));
        bean.setDestination(DataUtility.getString(request.getParameter("destination")));
        bean.setDistance(DataUtility.getDouble(request.getParameter("distance")));
        populateDTO(bean, request);
        return bean;
    }

    /**
     * Handles HTTP GET requests.
     * Loads route data by ID if provided and forwards to route view.
     *
     * @param req the HttpServletRequest object
     * @param resp the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long id = DataUtility.getLong(req.getParameter("id"));
        RouteModel model = new RouteModel();
        if (id > 0) {
            try {
                RouteBean bean = model.findByPk(id);
                ServletUtility.setBean(bean, req);
            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, req, resp);
                return;
            }
        }
        ServletUtility.forward(getView(), req, resp);
    }

    /**
     * Handles HTTP POST requests for saving, updating, resetting, or cancelling route data.
     *
     * @param req the HttpServletRequest object
     * @param resp the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String op = DataUtility.getString(req.getParameter("operation"));
        RouteModel model = new RouteModel();

        if (OP_SAVE.equalsIgnoreCase(op)) {
            RouteBean bean = (RouteBean) populateBean(req);
            try {
                model.addRoute(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Route Added Successfully !!!", req);
            } catch (DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Route Code Already Exist !!!", req);
            } catch (ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.ROUTE_CTL, req, resp);
            return;
        } else if (OP_UPDATE.equalsIgnoreCase(op)) {
            RouteBean bean = (RouteBean) populateBean(req);
            try {
                model.updateRoute(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Route Updated Successfully !!!", req);
            } catch (DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Route Code Already Exist !!!", req);
            } catch (ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.ROUTE_LIST_CTL, req, resp);
            return;
        }

        ServletUtility.forward(getView(), req, resp);
    }

    /**
     * Returns the view (JSP page) associated with this controller.
     *
     * @return the route view JSP path
     */
    @Override
    protected String getView() {
        return ORSView.ROUTE_VIEW;
    }
}