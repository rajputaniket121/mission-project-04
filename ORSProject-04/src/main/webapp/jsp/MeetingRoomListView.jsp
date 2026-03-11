<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.controller.MeetingRoomListCtl"%>
<%@page import="in.co.rays.proj4.controller.BaseCtl"%>
<%@page import="in.co.rays.proj4.bean.MeetingRoomBean"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<html>
<head>
    <title>Meeting Room List</title>
    <link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>
    <%@include file="Header.jsp"%>
    <jsp:useBean id="bean" class="in.co.rays.proj4.bean.MeetingRoomBean" scope="request"></jsp:useBean>

    <div align="center">
        <h1 align="center" style="margin-bottom: -15; color: navy;">Meeting Room List</h1>

        <div style="height: 15px; margin-bottom: 12px">
            <h3>
                <font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
            </h3>
            <h3>
                <font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
            </h3>
        </div>

        <form action="<%=ORSView.MEETING_ROOM_LIST_CTL%>" method="post">
            <%
                int pageNo = ServletUtility.getPageNo(request);
                int pageSize = ServletUtility.getPageSize(request);
                int index = ((pageNo - 1) * pageSize) + 1;
                int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());
                HashMap<String, String> statusMap = (HashMap<String, String>) request.getAttribute("statusMap");
                HashMap<String, String> capacityMap = (HashMap<String, String>) request.getAttribute("capacityMap");
                List<MeetingRoomBean> list = (List<MeetingRoomBean>) ServletUtility.getList(request);
                Iterator<MeetingRoomBean> it = list.iterator();

                if (list.size() != 0) {
            %>

            <input type="hidden" name="pageNo" value="<%=pageNo%>">
            <input type="hidden" name="pageSize" value="<%=pageSize%>">

            <table style="width: 100%">
                <tr>
                    <td align="center">
                        <label><b>Room Code :</b></label>
                        <input type="text" name="roomCode" placeholder="Enter Room Code" value="<%=ServletUtility.getParameter("roomCode", request)%>">
                        &emsp;
                        <label><b>Room Name :</b></label>
                        <input type="text" name="roomName" placeholder="Enter Room Name" value="<%=ServletUtility.getParameter("roomName", request)%>">
                        &emsp;
                        <label><b>Capacity :</b></label>
                        <%=HTMLUtility.getList("capacity", String.valueOf(bean.getCapacity()), capacityMap)%>
                        &emsp;
                        <label><b>Status :</b></label>
                        <%=HTMLUtility.getList("roomStatus", String.valueOf(bean.getRoomStatus()), statusMap)%>
                        &emsp;&nbsp;
                        <input type="submit" name="operation" value="<%=MeetingRoomListCtl.OP_SEARCH%>">&nbsp;
                        <input type="submit" name="operation" value="<%=MeetingRoomListCtl.OP_RESET%>">
                    </td>
                </tr>
            </table>
            <br>

            <table border="1" style="width: 100%; border: groove;">
                <tr style="background-color: #e1e6f1e3;">
                    <th width="5%"><input type="checkbox" id="selectall" /></th>
                    <th width="5%">S.No</th>
                    <th width="15%">Room Code</th>
                    <th width="20%">Room Name</th>
                    <th width="15%">Capacity</th>
                    <th width="20%">Room Status</th>
                    <th width="5%">Edit</th>
                </tr>

                <%
                    while (it.hasNext()) {
                        bean = (MeetingRoomBean) it.next();
                %>
                <tr>
                    <td style="text-align: center;">
                        <input type="checkbox" class="case" name="ids" value="<%=bean.getId()%>">
                    </td>
                    <td style="text-align: center;"><%=index++%></td>
                    <td style="text-align: center;"><%=bean.getRoomCode()%></td>
                    <td style="text-align: center;"><%=bean.getRoomName()%></td>
                    <td style="text-align: center;"><%=bean.getCapacity()%> Persons</td>
                    <td style="text-align: center;"><%=bean.getRoomStatus()%></td>
                    <td style="text-align: center;">
                        <a href="MeetingRoomCtl?id=<%=bean.getId()%>">Edit</a>
                    </td>
                </tr>
                <%
                    }
                %>
            </table>

            <table style="width: 100%">
                <tr>
                    <td style="width: 25%">
                        <input type="submit" name="operation" value="<%=MeetingRoomListCtl.OP_PREVIOUS%>" <%=pageNo > 1 ? "" : "disabled"%>>
                    </td>
                    <td align="center" style="width: 25%">
                        <input type="submit" name="operation" value="<%=MeetingRoomListCtl.OP_NEW%>">
                    </td>
                    <td align="center" style="width: 25%">
                        <input type="submit" name="operation" value="<%=MeetingRoomListCtl.OP_DELETE%>">
                    </td>
                    <td style="width: 25%" align="right">
                        <input type="submit" name="operation" value="<%=MeetingRoomListCtl.OP_NEXT%>" <%=nextPageSize != 0 ? "" : "disabled"%>>
                    </td>
                </tr>
            </table>

            <%
                }
                if (list.size() == 0) {
            %>
            <table>
                <tr>
                    <td align="right">
                        <input type="submit" name="operation" value="<%=MeetingRoomListCtl.OP_BACK%>">
                    </td>
                </tr>
            </table>
            <%
                }
            %>
        </form>
    </div>
    <%@include file="Footer.jsp"%>
</body>
</html>