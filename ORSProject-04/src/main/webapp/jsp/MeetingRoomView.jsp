<%@page import="in.co.rays.proj4.controller.MeetingRoomCtl"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<html>
<head>
<title>Meeting Room View</title>
<link rel="icon" type="image/png"
    href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>
    <form action="<%=ORSView.MEETING_ROOM_CTL%>" method="post">

        <%@ include file="Header.jsp"%>

        <jsp:useBean id="bean" class="in.co.rays.proj4.bean.MeetingRoomBean"
            scope="request"></jsp:useBean>

        <div align="center">
            <h1 align="center" style="margin-bottom: -15; color: navy">
                <%
                    if (bean != null && bean.getId() > 0) {
                %>Update<%
                    } else {
                %>Add<%
                    }
                %>
                Meeting Room
            </h1>

            <div style="height: 15px; margin-bottom: 12px">
                <H3 align="center">
                    <font color="red"> <%=ServletUtility.getErrorMessage(request)%>
                    </font>
                </H3>

                <H3 align="center">
                    <font color="green"> <%=ServletUtility.getSuccessMessage(request)%>
                    </font>
                </H3>
            </div>
            <%
                HashMap<String, String> statusMap = (HashMap<String, String>) request.getAttribute("statusMap");
                HashMap<String, String> capacityMap = (HashMap<String, String>) request.getAttribute("capacityMap");
            %>

            <input type="hidden" name="id" value="<%=bean.getId()%>"> 
            <input type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
            <input type="hidden" name="modifiedBy" value="<%=bean.getModifiedBy()%>"> 
            <input type="hidden" name="createdDatetime" value="<%=DataUtility.getTimestamp(bean.getCreatedDateTime())%>">
            <input type="hidden" name="modifiedDatetime" value="<%=DataUtility.getTimestamp(bean.getModifiedDateTime())%>">

            <table>
                <tr>
                    <th align="left">Room Code<span style="color: red">*</span></th>
                    <td><input type="text" name="roomCode"
                        placeholder="Enter Room Code"
                        value="<%=DataUtility.getStringData(bean.getRoomCode())%>"></td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("roomCode", request)%></font></td>
                </tr>
                <tr>
                    <th align="left">Room Name<span style="color: red">*</span></th>
                    <td><input type="text" name="roomName"
                        placeholder="Enter Room Name"
                        value="<%=DataUtility.getStringData(bean.getRoomName())%>"></td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("roomName", request)%></font></td>
                </tr>
                <tr>
                    <th align="left">Capacity<span style="color: red">*</span></th>
                    <td><%=HTMLUtility.getList("capacity", String.valueOf(bean.getCapacity()), capacityMap)%></td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("capacity", request)%></font></td>
                </tr>
                <tr>
                    <th align="left">Room Status<span style="color: red">*</span></th>
                    <td><%=HTMLUtility.getList("roomStatus", bean.getRoomStatus(), statusMap)%></td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("roomStatus", request)%></font></td>
                </tr>

                <tr>
                    <th></th>
                    <td></td>
                </tr>
                <tr>
                    <th></th>
                    <%
                        if (bean != null && bean.getId() > 0) {
                    %>
                    <td align="left" colspan="2">
                        <input type="submit" name="operation" value="<%=MeetingRoomCtl.OP_UPDATE%>"> 
                        <input type="submit" name="operation" value="<%=MeetingRoomCtl.OP_CANCEL%>">
                    <%
                        } else {
                    %>
                    <td align="left" colspan="2">
                        <input type="submit" name="operation" value="<%=MeetingRoomCtl.OP_SAVE%>"> 
                        <input type="submit" name="operation" value="<%=MeetingRoomCtl.OP_RESET%>">
                    <%
                        }
                    %>
                </tr>
            </table>
        </div>
    </form>
    <%@include file="Footer.jsp"%>
</body>
</html>