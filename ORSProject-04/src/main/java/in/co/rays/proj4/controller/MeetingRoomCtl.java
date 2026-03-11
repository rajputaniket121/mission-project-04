package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.MeetingRoomBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.MeetingRoomModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "MeetingRoomCtl", urlPatterns = "/ctl/MeetingRoomCtl")
public class MeetingRoomCtl extends BaseCtl {

    @Override
    protected void preload(HttpServletRequest request) {
        HashMap<String, String> statusMap = new HashMap<>();
        statusMap.put("Available", "Available");
        statusMap.put("Not Available", "Not Available");
        request.setAttribute("statusMap", statusMap);
        
        HashMap<String, String> capacityMap = new HashMap<>();
        capacityMap.put("4", "4 Persons");
        capacityMap.put("6", "6 Persons");
        capacityMap.put("10", "10 Persons");
        request.setAttribute("capacityMap", capacityMap);
    }

    @Override
    protected boolean validate(HttpServletRequest request) {
        boolean pass = true;
        String op = request.getParameter("operation");

        if (OP_LOG_OUT.equalsIgnoreCase(op) || OP_RESET.equalsIgnoreCase(op)) {
            return pass;
        }

        if (DataValidator.isNull(request.getParameter("roomCode"))) {
            request.setAttribute("roomCode", PropertyReader.getValue("error.require", "Room Code"));
            pass = false;
        }else if (DataValidator.isValidPattern(request.getParameter("roomCode"))) {
			request.setAttribute("roomCode", "Invalid Format");
			pass = false;
		}

        if (DataValidator.isNull(request.getParameter("roomName"))) {
            request.setAttribute("roomName", PropertyReader.getValue("error.require", "Room Name"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("capacity"))) {
            request.setAttribute("capacity", PropertyReader.getValue("error.require", "Capacity"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("roomStatus"))) {
            request.setAttribute("roomStatus", PropertyReader.getValue("error.require", "Room Status"));
            pass = false;
        }

        return pass;
    }

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        MeetingRoomBean bean = new MeetingRoomBean();
        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setRoomCode(DataUtility.getString(request.getParameter("roomCode")));
        bean.setRoomName(DataUtility.getString(request.getParameter("roomName")));
        bean.setCapacity(DataUtility.getInt(request.getParameter("capacity")));
        bean.setRoomStatus(DataUtility.getString(request.getParameter("roomStatus")));
        populateDTO(bean, request);
        return bean;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = DataUtility.getLong(req.getParameter("id"));
        MeetingRoomModel model = new MeetingRoomModel();
        if (id > 0) {
            try {
                MeetingRoomBean bean = model.findByPk(id);
                ServletUtility.setBean(bean, req);
            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, req, resp);
                return;
            }
        }
        ServletUtility.forward(getView(), req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String op = DataUtility.getString(req.getParameter("operation"));
        MeetingRoomModel model = new MeetingRoomModel();

        if (OP_SAVE.equalsIgnoreCase(op)) {
            MeetingRoomBean bean = (MeetingRoomBean) populateBean(req);
            try {
                model.addMeetingRoom(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Meeting Room Added Successfully !!!", req);
            } catch (DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Room Code Already Exist !!!", req);
            } catch (ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.MEETING_ROOM_CTL, req, resp);
            return;
        } else if (OP_UPDATE.equalsIgnoreCase(op)) {
            MeetingRoomBean bean = (MeetingRoomBean) populateBean(req);
            try {
                model.updateMeetingRoom(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Meeting Room Updated Successfully !!!", req);
            } catch (DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Room Code Already Exist !!!", req);
            } catch (ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.MEETING_ROOM_LIST_CTL, req, resp);
            return;
        }

        ServletUtility.forward(getView(), req, resp);
    }

    @Override
    protected String getView() {
        return ORSView.MEETING_ROOM_VIEW;
    }
}