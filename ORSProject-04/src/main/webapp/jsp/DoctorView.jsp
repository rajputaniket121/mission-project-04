<%@page import="in.co.rays.proj4.controller.DoctorCtl"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<html>
<head>
<title>DoctorView</title>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>
	<form action="<%=ORSView.DOCTOR_CTL%>" method="post">

		<%@ include file="Header.jsp"%>

		<jsp:useBean id="bean" class="in.co.rays.proj4.bean.DoctorBean"
			scope="request"></jsp:useBean>

		<div align="center">
			<h1 align="center" style="margin-bottom: -15; color: navy">
				<%
					if (bean != null && bean.getId() >0) {
				%>Update<%
					} else {
				%>Add<%
					}
				%>
				Doctor
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

			<input type="hidden" name="id" value="<%=bean.getId()%>"> <input
				type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
			<input type="hidden" name="modifiedBy"
				value="<%=bean.getModifiedBy()%>"> <input type="hidden"
				name="createdDatetime"
				value="<%=DataUtility.getTimestamp(bean.getCreatedDateTime())%>">
			<input type="hidden" name="modifiedDatetime"
				value="<%=DataUtility.getTimestamp(bean.getModifiedDateTime())%>">

			<table>
				<tr>
					<th align="left">Name<span style="color: red">*</span></th>
					<td><input type="text" name="name"
						placeholder="Enter Name"
						value="<%=DataUtility.getStringData(bean.getName())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("name", request)%></font></td>
				</tr>
				<tr>
					<th align="left" >Date of Birth<span style="color: red">*</span></th>
					<td><input style="width: 100%" type="text" name="dob" id="udate"
						value="<%=DataUtility.getDateString(bean.getDob())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("dob", request)%></font></td>
				</tr>
				<tr>
					<th align="left">Mobile No<span style="color: red">*</span></th>
					<td><input type="text" name="mobile" maxlength="10"
						placeholder="Enter Mobile No."
						value="<%=DataUtility.getStringData(bean.getMobile())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("mobile", request)%></font></td>
				</tr>
				<tr>
					<th align="left">Experties<span style="color: red">*</span></th>
					<td>
						<%
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("Cardiology", "Cardiology");
						map.put("Dermatology", "Dermatology");
						map.put("Endocrinology", "Endocrinology");
						map.put("Family Medicine", "Family Medicine");
						map.put("Gastroenterology", "Gastroenterology");
						map.put("Hematology", "Hematology");
						map.put("Infectious Diseases", "Infectious Diseases");
						map.put("Internal Medicine", "Internal Medicine");
						map.put("Nephrology", "Nephrology");
						map.put("Neurology", "Neurology");
						map.put("Oncology", "Oncology");
						map.put("Ophthalmology", "Ophthalmology");
						map.put("Orthopedics", "Orthopedics");
						map.put("Otolaryngology", "Otolaryngology");
						map.put("Pediatrics", "Pediatrics");
						map.put("Psychiatry", "Psychiatry");
						map.put("Pulmonology", "Pulmonology");
						map.put("Radiology", "Radiology");
						map.put("Surgery", "Surgery");
						map.put("Urology", "Urology");
						
						String htmlList = HTMLUtility.getList("experties", bean.getExperties(), map);
						%> <%=htmlList%>
					</td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("experties", request)%></font></td>
				</tr>
			
				<tr>
					<th></th>
					<td></td>
				</tr>
				<tr>
					<th></th>
					<%
						if (bean != null && bean.getId() >0){
					%>
					<td align="left" colspan="2"><input type="submit"
						name="operation" value="<%=DoctorCtl.OP_UPDATE%>"> <input
						type="submit" name="operation" value="<%=DoctorCtl.OP_CANCEL%>">
						<%
							} else {
						%>
					<td align="left" colspan="2"><input type="submit"
						name="operation" value="<%=DoctorCtl.OP_SAVE%>"> <input
						type="submit" name="operation" value="<%=DoctorCtl.OP_RESET%>">
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