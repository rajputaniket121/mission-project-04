<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.controller.SchoolAttendanceListCtl"%>
<%@page import="in.co.rays.proj4.controller.BaseCtl"%>
<%@page import="in.co.rays.proj4.bean.SchoolAttendanceBean"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<html>
<head>
    <title>Attendance List</title>
    <link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>
    <%@include file="Header.jsp"%>
    <jsp:useBean id="bean" class="in.co.rays.proj4.bean.SchoolAttendanceBean" scope="request"></jsp:useBean>

    <div align="center">
        <h1 align="center" style="margin-bottom: -15; color: navy;">Attendance List</h1>

        <div style="height: 15px; margin-bottom: 12px">
            <h3>
                <font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
            </h3>
            <h3>
                <font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
            </h3>
        </div>

        <form action="<%=ORSView.SCHOOL_ATTENDANCE_LIST_CTL%>" method="post">
            <%
                int pageNo = ServletUtility.getPageNo(request);
                int pageSize = ServletUtility.getPageSize(request);
                int index = ((pageNo - 1) * pageSize) + 1;
                int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());
                HashMap<String, String> statusMap = (HashMap<String, String>) request.getAttribute("statusMap");
                HashMap<String, String> classMap = (HashMap<String, String>) request.getAttribute("classMap");
                List<SchoolAttendanceBean> list = (List<SchoolAttendanceBean>) ServletUtility.getList(request);
                Iterator<SchoolAttendanceBean> it = list.iterator();

                if (list.size() != 0) {
            %>

            <input type="hidden" name="pageNo" value="<%=pageNo%>">
            <input type="hidden" name="pageSize" value="<%=pageSize%>">

            <table style="width: 100%">
                <tr>
                    <td align="center">
                        <label><b>Student Name :</b></label>
                        <input type="text" name="studentName" placeholder="Enter Student Name" value="<%=ServletUtility.getParameter("studentName", request)%>">
                        &emsp;
                        <label><b>Class :</b></label>
                        <%=HTMLUtility.getList("className", String.valueOf(bean.getClassName()), classMap)%>
                        &emsp;
                        <label><b>Status :</b></label>
                        <%=HTMLUtility.getList("attendanceStatus", String.valueOf(bean.getAttendanceStatus()), statusMap)%>
                        &emsp;&nbsp;
                        <input type="submit" name="operation" value="<%=SchoolAttendanceListCtl.OP_SEARCH%>">&nbsp;
                        <input type="submit" name="operation" value="<%=SchoolAttendanceListCtl.OP_RESET%>">
                    </td>
                </tr>
            </table>
            <br>

            <table border="1" style="width: 100%; border: groove;">
                <tr style="background-color: #e1e6f1e3;">
                    <th width="5%"><input type="checkbox" id="selectall" /></th>
                    <th width="5%">S.No</th>
                    <th width="20%">Student Name</th>
                    <th width="10%">Class</th>
                    <th width="15%">Attendance Date</th>
                    <th width="15%">Status</th>
                    <th width="25%">Remarks</th>
                    <th width="5%">Edit</th>
                </tr>

                <%
                    while (it.hasNext()) {
                        bean = (SchoolAttendanceBean) it.next();
                %>
                <tr>
                    <td style="text-align: center;">
                        <input type="checkbox" class="case" name="ids" value="<%=bean.getId()%>">
                    </td>
                    <td style="text-align: center;"><%=index++%></td>
                    <td style="text-align: center;"><%=bean.getStudentName()%></td>
                    <td style="text-align: center;"><%=bean.getClassName()%></td>
                    <td style="text-align: center;"><%=DataUtility.getDateString(bean.getAttendanceDate())%></td>
                    <td style="text-align: center;"><%=bean.getAttendanceStatus()%></td>
                    <td style="text-align: center;"><%=bean.getRemarks() != null ? bean.getRemarks() : ""%></td>
                    <td style="text-align: center;">
                        <a href="SchoolAttendanceCtl?id=<%=bean.getId()%>">Edit</a>
                    </td>
                </tr>
                <%
                    }
                %>
            </table>

            <table style="width: 100%">
                <tr>
                    <td style="width: 25%">
                        <input type="submit" name="operation" value="<%=SchoolAttendanceListCtl.OP_PREVIOUS%>" <%=pageNo > 1 ? "" : "disabled"%>>
                    </td>
                    <td align="center" style="width: 25%">
                        <input type="submit" name="operation" value="<%=SchoolAttendanceListCtl.OP_NEW%>">
                    </td>
                    <td align="center" style="width: 25%">
                        <input type="submit" name="operation" value="<%=SchoolAttendanceListCtl.OP_DELETE%>">
                    </td>
                    <td style="width: 25%" align="right">
                        <input type="submit" name="operation" value="<%=SchoolAttendanceListCtl.OP_NEXT%>" <%=nextPageSize != 0 ? "" : "disabled"%>>
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
                        <input type="submit" name="operation" value="<%=SchoolAttendanceListCtl.OP_BACK%>">
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