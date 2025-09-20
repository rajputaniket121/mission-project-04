<%@page import="in.co.rays.proj4.controller.TimetableCtl"%>
<%@page import="in.co.rays.proj4.bean.SubjectBean"%>
<%@page import="in.co.rays.proj4.bean.CourseBean"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="in.co.rays.proj4.controller.CourseCtl"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>

<html>
<head>
    <title>Add Timetable</title>
    <style type="text/css">
        .panel-default1 {
            border-color: red;
        }
    </style>
</head>
<body>
    <form action="<%=ORSView.TIMETABLE_CTL%>" method="post">
        <%@ include file="Header.jsp"%>

        <jsp:useBean id="bean" class="in.co.rays.proj4.bean.TimetableBean" scope="request"></jsp:useBean>
        
        <%
         List<CourseBean> courseList = (List<CourseBean>) request.getAttribute("courseList");
         List<SubjectBean> subjectList = (List<SubjectBean>) request.getAttribute("subjectList");
        %>

        <div align="center">
            <h1 align="center" style="margin-bottom: -15; color: navy">
           <%= (bean != null && bean.getId() > 0) ? "Update" : "Add" %>
                <%-- <%
                    if (bean != null && bean.getId() > 0) {
                %>
                    Update
                <%
                    } else {
                %>
                    Add
                <%
                    }
                %> --%>
                Timetable
            </h1>

            <div style="height: 15px; margin-bottom: 12px">
                <h3 align="center">
                    <font color="green">
                        <%=ServletUtility.getSuccessMessage(request)%>
                    </font>
                </h3>
                <h3 align="center">
                    <font color="red">
                        <%=ServletUtility.getErrorMessage(request)%>
                    </font>
                </h3>
            </div>

            <input type="hidden" name="id" value="<%=bean.getId()%>">
            <input type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
            <input type="hidden" name="modifiedBy" value="<%=bean.getModifiedBy()%>">
            <input type="hidden" name="createdDatetime" value="<%=DataUtility.getTimestamp(bean.getCreatedDateTime())%>">
            <input type="hidden" name="modifiedDatetime" value="<%=DataUtility.getTimestamp(bean.getModifiedDateTime())%>">

            <table>
                <tr>
                    <th align="left">Course<span style="color: red">*</span></th>
                    <td align="center">
                        <%=HTMLUtility.getList("courseId", String.valueOf(bean.getCourseId()),courseList) %>
                    </td>
                    <td style="position: fixed;">
                        <font color="red">
                            <%=ServletUtility.getErrorMessage("courseId", request)%>
                        </font>
                    </td>
                </tr>
                
                
                 <tr>
                    <th align="left">Subject<span style="color: red">*</span></th>
                    <td align="center">
                        <%=HTMLUtility.getList("subjectId", String.valueOf(bean.getSubjectId()),subjectList) %>
                    </td>
                    <td style="position: fixed;">
                        <font color="red">
                            <%=ServletUtility.getErrorMessage("subjectId", request)%>
                        </font>
                    </td>
                </tr>

                <tr>
                    <th align="left">Semester<span style="color: red">*</span></th>
                    <td>
                    <%
                    LinkedHashMap<String, String> semesterMap = new LinkedHashMap<String,String>();
                    semesterMap.put("1","1");
                    semesterMap.put("2","2");
                    semesterMap.put("3","3");
                    semesterMap.put("4","4");
                    semesterMap.put("5","5");
                    semesterMap.put("6","6");
                    semesterMap.put("7","7");
                    semesterMap.put("8","8");
                    String semesterList = HTMLUtility.getList("semester", String.valueOf(bean.getSemester()), semesterMap);
                    %>
                        <%=semesterList%>
                    </td>
                    <td style="position: fixed;">
                        <font color="red">
                            <%=ServletUtility.getErrorMessage("courseId", request)%>
                        </font>
                    </td>
                </tr>
                
                <tr>
					<th align="left" >Date of Birth<span style="color: red">*</span></th>
					<td><input style="width: 100%" type="text" id="udate" name="examDate"
						value="<%=DataUtility.getDateString(bean.getExamDate())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("examDate", request)%></font></td>
				</tr>
                
				<tr>
					<th align="left">Exam Time<span style="color: red">*</span></th>
					<td>
						<%
							LinkedHashMap<String, String> map1 = new LinkedHashMap<String, String>();
							map1.put("08:00 AM to 11:00 AM", "08:00 AM to 11:00 AM");
							map1.put("12:00 PM to 03:00 PM", "12:00 PM to 03:00 PM");
							map1.put("04:00 PM to 07:00 PM", "04:00 PM to 07:00 PM");

							String htmlList1 = HTMLUtility.getList("examTime", bean.getExamTime(), map1);
						%> <%=htmlList1%>
					</td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("examTime", request)%></font></td>
				</tr>

                <tr>
                    <th align="left">Description<span style="color: red">*</span></th>
                    <td align="center">
                        <textarea style="width: 170px; resize: none;" name="description" rows="3"
                            placeholder="Enter Short description"><%=DataUtility.getStringData(bean.getDescription()).trim()%></textarea>
                    </td>
                    <td style="position: fixed;">
                        <font color="red">
                            <%=ServletUtility.getErrorMessage("description", request)%>
                        </font>
                    </td>
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
                            <input type="submit" name="operation" value="<%=TimetableCtl.OP_UPDATE%>">
                            <input type="submit" name="operation" value="<%=TimetableCtl.OP_CANCEL%>">
                        </td>
                    <%
                        } else {
                    %>
                        <td align="left" colspan="2">
                            <input type="submit" name="operation" value="<%=TimetableCtl.OP_SAVE%>">
                            <input type="submit" name="operation" value="<%=TimetableCtl.OP_RESET%>">
                        </td>
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