<%@page import="in.co.rays.proj4.bean.CourseBean"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.proj4.controller.SubjectCtl"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="in.co.rays.proj4.controller.CourseCtl"%>
<%@page import="in.co.rays.proj4.controller.BaseCtl"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>

<html>
<head>
    <title>Subject</title>
    <style type="text/css">
        .panel-default1 {
            border-color: red;
        }
    </style>
</head>
<body>
    <form action="<%=ORSView.SUBJECT_CTL%>" method="post">
        <%@ include file="Header.jsp"%>

        <jsp:useBean id="bean" class="in.co.rays.proj4.bean.SubjectBean" scope="request"></jsp:useBean>
        
        <%
         List<CourseBean> courseList = (List<CourseBean>) request.getAttribute("courseList");
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
                Subject
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
                    <th align="left">Name<span style="color: red">*</span></th>
                    <td align="center">
                        <input type="text" name="name" placeholder="Enter Subject Name" value="<%=DataUtility.getStringData(bean.getName())%>">
                    </td>
                    <td style="position: fixed;">
                        <font color="red">
                            <%=ServletUtility.getErrorMessage("name", request)%>
                        </font>
                    </td>
                </tr>

                <tr>
                    <th align="left">Course<span style="color: red">*</span></th>
                    <td>
                        <%=HTMLUtility.getList("courseId", String.valueOf(bean.getCourseId()), courseList)%>
                    </td>
                    <td style="position: fixed;">
                        <font color="red">
                            <%=ServletUtility.getErrorMessage("courseId", request)%>
                        </font>
                    </td>
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
                            <input type="submit" name="operation" value="<%=SubjectCtl.OP_UPDATE%>">
                            <input type="submit" name="operation" value="<%=SubjectCtl.OP_CANCEL%>">
                        </td>
                    <%
                        } else {
                    %>
                        <td align="left" colspan="2">
                            <input type="submit" name="operation" value="<%=SubjectCtl.OP_SAVE%>">
                            <input type="submit" name="operation" value="<%=SubjectCtl.OP_RESET%>">
                        </td>
                    <%
                        }
                    %>
                </tr>
            </table>
        </div>
    </form>
</body>
</html>