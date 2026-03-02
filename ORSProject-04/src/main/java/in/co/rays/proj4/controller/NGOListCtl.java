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
import in.co.rays.proj4.bean.NGOBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.NGOModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "NGOListCtl", urlPatterns = {"/ctl/NGOListCtl"})
public class NGOListCtl extends BaseCtl {

    @Override
    protected void preload(HttpServletRequest request) {
        NGOModel model = new NGOModel();
        try {
            List<NGOBean> ngoList = model.list();
            HashMap<String, String> focusAreaMap = new HashMap<>();
            Iterator<NGOBean> it = ngoList.iterator();

            while (it.hasNext()) {
                NGOBean bean = it.next();
                focusAreaMap.put(bean.getFocusArea(), bean.getFocusArea());
            }
            request.setAttribute("focusAreaMap", focusAreaMap);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        NGOBean bean = new NGOBean();
        bean.setNgoName(DataUtility.getString(request.getParameter("ngoName")));
        bean.setCity(DataUtility.getString(request.getParameter("city")));
        bean.setFocusArea(DataUtility.getString(request.getParameter("focusArea")));
        return bean;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
        NGOBean bean = (NGOBean) populateBean(req);
        NGOModel model = new NGOModel();

        try {
            List<NGOBean> list = model.search(bean, pageNo, pageSize);
            List<NGOBean> next = model.search(bean, pageNo + 1, pageSize);

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

        List<NGOBean> list = null;
        List<NGOBean> next = null;

        int pageNo = DataUtility.getInt(req.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(req.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

        NGOBean bean = (NGOBean) populateBean(req);
        NGOModel model = new NGOModel();

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
                ServletUtility.redirect(ORSView.NGO_LIST_CTL, req, resp);
                return;
            } else if (OP_NEW.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.NGO_CTL, req, resp);
                return;
            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                pageNo = 1;
                String[] ids = req.getParameterValues("ids");
                if (ids != null && ids.length > 0) {
                    NGOBean deleteBean = new NGOBean();
                    for (String id : ids) {
                        deleteBean.setId(DataUtility.getLong(id));
                        model.deleteNGO(deleteBean.getId());
                        ServletUtility.setSuccessMessage("Deleted Successfully !!!", req);
                    }
                } else {
                    ServletUtility.setErrorMessage("Select Atleast one Checkbox", req);
                }
            } else if (OP_BACK.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.NGO_LIST_CTL, req, resp);
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
        return ORSView.NGO_LIST_VIEW;
    }
}