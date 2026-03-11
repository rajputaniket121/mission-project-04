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
import in.co.rays.proj4.bean.MeetingRoomBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.MeetingRoomModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "MeetingRoomListCtl", urlPatterns = {"/ctl/MeetingRoomListCtl"})
public class MeetingRoomListCtl extends BaseCtl {

    @Override
    protected void preload(HttpServletRequest request) {
        MeetingRoomModel model = new MeetingRoomModel();
        try {
            List<MeetingRoomBean> roomList = model.list();
            HashMap<String, String> statusMap = new HashMap<>();
            HashMap<String, String> capacityMap = new HashMap<>();
            Iterator<MeetingRoomBean> it = roomList.iterator();

            while (it.hasNext()) {
                MeetingRoomBean bean = it.next();
                statusMap.put(bean.getRoomStatus(), bean.getRoomStatus());
                capacityMap.put(String.valueOf(bean.getCapacity()), String.valueOf(bean.getCapacity()) + " Persons");
            }
            request.setAttribute("statusMap", statusMap);
            request.setAttribute("capacityMap", capacityMap);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        MeetingRoomBean bean = new MeetingRoomBean();
        bean.setRoomCode(DataUtility.getString(request.getParameter("roomCode")));
        bean.setRoomName(DataUtility.getString(request.getParameter("roomName")));
        bean.setCapacity(DataUtility.getInt(request.getParameter("capacity")));
        bean.setRoomStatus(DataUtility.getString(request.getParameter("roomStatus")));
        return bean;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
        MeetingRoomBean bean = (MeetingRoomBean) populateBean(req);
        MeetingRoomModel model = new MeetingRoomModel();

        try {
            List<MeetingRoomBean> list = model.search(bean, pageNo, pageSize);
            List<MeetingRoomBean> next = model.search(bean, pageNo + 1, pageSize);

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

        List<MeetingRoomBean> list = null;
        List<MeetingRoomBean> next = null;

        int pageNo = DataUtility.getInt(req.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(req.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

        MeetingRoomBean bean = (MeetingRoomBean) populateBean(req);
        MeetingRoomModel model = new MeetingRoomModel();

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
                ServletUtility.redirect(ORSView.MEETING_ROOM_LIST_CTL, req, resp);
                return;
            } else if (OP_NEW.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.MEETING_ROOM_CTL, req, resp);
                return;
            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                pageNo = 1;
                String[] ids = req.getParameterValues("ids");
                if (ids != null && ids.length > 0) {
                    MeetingRoomBean deleteBean = new MeetingRoomBean();
                    for (String id : ids) {
                        deleteBean.setId(DataUtility.getLong(id));
                        model.deleteMeetingRoom(deleteBean.getId());
                        ServletUtility.setSuccessMessage("Deleted Successfully !!!", req);
                    }
                } else {
                    ServletUtility.setErrorMessage("Select Atleast one Checkbox", req);
                }
            } else if (OP_BACK.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.MEETING_ROOM_LIST_CTL, req, resp);
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
        return ORSView.MEETING_ROOM_LIST_VIEW;
    }
}