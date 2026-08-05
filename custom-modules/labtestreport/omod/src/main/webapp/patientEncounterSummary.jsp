<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<h2><spring:message code="labtestreport.patientEncounters.title" /></h2>

<style>
	#patientEncounterTable { border-collapse: collapse; width: 100%; }
	#patientEncounterTable th, #patientEncounterTable td { border: 1px solid #ccc; padding: 4px 8px; }
	#patientEncounterTable th { background-color: #f0f0f0; text-align: left; }
	#patientEncounterTable tbody tr { cursor: pointer; }
	#patientEncounterTable tbody tr:hover { background-color: #eef6fb; }
</style>

<form method="get" action="${pageContext.request.contextPath}/module/labtestreport/patientEncounters.form">
	<label for="startDate"><spring:message code="labtestreport.startDate" /></label>
	<input type="date" id="startDate" name="startDate" value="${startDate}" />
	<label for="endDate"><spring:message code="labtestreport.endDate" /></label>
	<input type="date" id="endDate" name="endDate" value="${endDate}" />
	<input type="submit" value="<spring:message code='labtestreport.filter'/>" />
</form>

<br />

<table id="patientEncounterTable">
	<thead>
		<tr>
			<th><spring:message code="labtestreport.patientEncounters.name" /></th>
			<th><spring:message code="labtestreport.drilldown.sex" /></th>
			<th><spring:message code="labtestreport.drilldown.nationalId" /></th>
			<th><spring:message code="labtestreport.drilldown.phoneNumber" /></th>
			<th><spring:message code="labtestreport.patientEncounters.age" /></th>
			<th><spring:message code="labtestreport.patientEncounters.encounterCount" /></th>
			<th><spring:message code="labtestreport.patientEncounters.mostRecentEncounterDate" /></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${rows}" var="row" varStatus="rowStatus">
			<c:url value="/spa/patient/${row.patientUuid}/chart" var="chartUrl" />
			<tr class="${rowStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}" onclick="window.location.href='${chartUrl}'">
				<td><a href="${chartUrl}">${row.name}</a></td>
				<td>${row.sex}</td>
				<td>${row.nationalId}</td>
				<td>${row.phoneNumber}</td>
				<td>${row.age}</td>
				<td>${row.visitCount}</td>
				<td>${row.mostRecentVisitDate}</td>
			</tr>
		</c:forEach>
		<c:if test="${empty rows}">
			<tr><td colspan="7"><spring:message code="labtestreport.drilldown.noPatients" /></td></tr>
		</c:if>
	</tbody>
</table>

<%@ include file="/WEB-INF/template/footer.jsp"%>
