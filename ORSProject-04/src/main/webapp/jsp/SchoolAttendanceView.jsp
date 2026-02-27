<%@page import="in.co.rays.proj4.controller.SchoolAttendanceCtl"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<html>
<head>
<title>School Attendance View</title>
<link rel="icon" type="image/png"
    href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>
    <form action="<%=ORSView.SCHOOL_ATTENDANCE_CTL%>" method="post">

        <%@ include file="Header.jsp"%>

        <jsp:useBean id="bean" class="in.co.rays.proj4.bean.SchoolAttendanceBean"
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
                Attendance
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
                HashMap<String, String> classMap = (HashMap<String, String>) request.getAttribute("classMap");
            %>

            <input type="hidden" name="id" value="<%=bean.getId()%>"> 
            <input type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
            <input type="hidden" name="modifiedBy" value="<%=bean.getModifiedBy()%>"> 
            <input type="hidden" name="createdDatetime" value="<%=DataUtility.getTimestamp(bean.getCreatedDateTime())%>">
            <input type="hidden" name="modifiedDatetime" value="<%=DataUtility.getTimestamp(bean.getModifiedDateTime())%>">

            <table>
                <tr>
                    <th align="left">Student Name<span style="color: red">*</span></th>
                    <td><input type="text" name="studentName"
                        placeholder="Enter Student Name"
                        value="<%=DataUtility.getStringData(bean.getStudentName())%>"></td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("studentName", request)%></font></td>
                </tr>
                <tr>
                    <th align="left">Class Name<span style="color: red">*</span></th>
                    <td><%=HTMLUtility.getList("className", bean.getClassName(), classMap)%></td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("className", request)%></font></td>
                </tr>
                <tr>
                    <th align="left">Attendance Date<span style="color: red">*</span></th>
                    <td><input style="width: 100%" type="text" name="attendanceDate" id="udate"
                        value="<%=DataUtility.getDateString(bean.getAttendanceDate())%>"></td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("attendanceDate", request)%></font></td>
                </tr>
                <tr>
                    <th align="left">Attendance Status<span style="color: red">*</span></th>
                    <td><%=HTMLUtility.getList("attendanceStatus", bean.getAttendanceStatus(), statusMap)%></td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("attendanceStatus", request)%></font></td>
                </tr>
                <tr>
                    <th align="left">Remarks</th>
                    <td><textarea name="remarks" rows="3" cols="22"><%=DataUtility.getStringData(bean.getRemarks())%></textarea></td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("remarks", request)%></font></td>
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
                        <input type="submit" name="operation" value="<%=SchoolAttendanceCtl.OP_UPDATE%>"> 
                        <input type="submit" name="operation" value="<%=SchoolAttendanceCtl.OP_CANCEL%>">
                    <%
                        } else {
                    %>
                    <td align="left" colspan="2">
                        <input type="submit" name="operation" value="<%=SchoolAttendanceCtl.OP_SAVE%>"> 
                        <input type="submit" name="operation" value="<%=SchoolAttendanceCtl.OP_RESET%>">
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