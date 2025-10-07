package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.RoleBean;
import in.co.rays.proj4.bean.UserBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.RoleModel;
import in.co.rays.proj4.model.UserModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

/**
 * UserListCtl is a servlet controller class to manage the display and operations
 * on the list of users in the system.
 *
 * <p>
 * Responsibilities include:
 * <ul>
 *   <li>Preloading role list for filters</li>
 *   <li>Searching users based on filters and pagination</li>
 *   <li>Deleting selected users</li>
 *   <li>Handling navigation operations like next, previous, reset, new, and back</li>
 *   <li>Forwarding results to the user list view with proper messages</li>
 * </ul>
 * </p>
 *
 * <p>
 * Supports GET and POST HTTP methods:
 * <ul>
 *   <li>GET: Loads the initial list of users</li>
 *   <li>POST: Handles search, pagination, deletion, and navigation operations</li>
 * </ul>
 * </p>
 *
 * @author 
 */
@WebServlet(name = "UserListCtl", urlPatterns = { "/ctl/UserListCtl" })
public class UserListCtl extends BaseCtl {

    private static final Logger log = Logger.getLogger(UserListCtl.class);

    /**
     * Preloads data for the view such as role list.
     * 
     * @param request HttpServletRequest object
     */
    @Override
    protected void preload(HttpServletRequest request) {
        log.debug("Inside preload method of UserListCtl");
        RoleModel model = new RoleModel();
        try {
            List<RoleBean> roleList = model.list();
            request.setAttribute("roleList", roleList);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Populates a {@link UserBean} from request parameters for search/filter operations.
     * 
     * @param request HttpServletRequest object
     * @return UserBean populated with filter values
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        log.debug("Inside populateBean method of UserListCtl");
        UserBean bean = new UserBean();
        bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
        bean.setLogin(DataUtility.getString(request.getParameter("login")));
        bean.setRoleId(DataUtility.getLong(request.getParameter("roleId")));
        bean.setDob(DataUtility.getDate(request.getParameter("dob")));
        return bean;
    }

    /**
     * Handles GET request to display the initial user list.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Inside doGet method of UserListCtl");
        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
        UserBean bean = (UserBean) populateBean(req);
        UserModel model = new UserModel();
        try {
            List<UserBean> list = model.search(bean, pageNo, pageSize);
            List<UserBean> next = model.search(bean, pageNo + 1, pageSize);
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
     * Handles POST requests to process search, pagination, deletion, and navigation.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Inside doPost method of UserListCtl");
        List<UserBean> list = null;
        List<UserBean> next = null;

        int pageNo = DataUtility.getInt(req.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(req.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? (DataUtility.getInt(PropertyReader.getValue("page.size"))) : pageSize;

        UserBean bean = new UserBean();
        UserModel model = new UserModel();

        String op = DataUtility.getString(req.getParameter("operation"));

        try {
            if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {
                if (OP_SEARCH.equalsIgnoreCase(op)) {
                    pageNo = 1;
                    bean = (UserBean) populateBean(req);
                } else if (OP_NEXT.equalsIgnoreCase(op)) {
                    pageNo++;
                } else if (OP_PREVIOUS.equalsIgnoreCase(op)) {
                    pageNo--;
                }
            } else if (OP_RESET.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.USER_LIST_CTL, req, resp);
                return;
            } else if (OP_NEW.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.USER_CTL, req, resp);
                return;
            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                String[] ids = req.getParameterValues("ids");
                if (ids != null && ids.length > 0) {
                    pageNo = 1;
                    UserBean deleteBean = new UserBean();
                    for (String id : ids) {
                        deleteBean.setId(DataUtility.getLong(id));
                        model.deleteUser(deleteBean.getId());
                        ServletUtility.setSuccessMessage("Deleted Successfully !!!", req);
                    }
                } else {
                    ServletUtility.setErrorMessage("Select Atleast one Checkbox", req);
                }
            } else if (OP_BACK.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.USER_LIST_CTL, req, resp);
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
     * Returns the view page for displaying the user list.
     * 
     * @return JSP page path for the user list
     */
    @Override
    protected String getView() {
        log.debug("Inside getView method of UserListCtl");
        return ORSView.USER_LIST_VIEW;
    }
}