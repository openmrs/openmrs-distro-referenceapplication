<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<h2><spring:message code="labtestreport.drilldown.title" /></h2>

<p>
	<c:if test="${not empty category}">${category} &raquo; </c:if>
	${labTest}
	<c:if test="${not empty ageGroup}"> &raquo; ${ageGroup}</c:if>
	<c:if test="${not empty gender}"> (${gender})</c:if>
	<c:if test="${not empty startDate or not empty endDate}">
		<br /><spring:message code="labtestreport.startDate" />: ${empty startDate ? '...' : startDate}
		&ndash; <spring:message code="labtestreport.endDate" />: ${empty endDate ? '...' : endDate}
	</c:if>
</p>

<table cellpadding="2" cellspacing="0" class="box">
	<tr>
		<th><spring:message code="labtestreport.drilldown.name" /></th>
		<th><spring:message code="labtestreport.drilldown.identifier" /></th>
	</tr>
	<c:set var="i" value="0" />
	<c:forEach items="${patients}" var="patient">
		<tr class="${i % 2 == 0 ? 'evenRow' : 'oddRow'}">
			<td>
				<a href="${pageContext.request.contextPath}/spa/patient/${patient.patientUuid}/chart/visits">
					${patient.givenName} ${patient.familyName}
				</a>
			</td>
			<td>${patient.identifier}</td>
		</tr>
		<c:set var="i" value="${i + 1}" />
	</c:forEach>
	<c:if test="${empty patients}">
		<tr><td colspan="2"><spring:message code="labtestreport.drilldown.noPatients" /></td></tr>
	</c:if>
</table>

<br />
<c:url value="${empty backUrl ? '/module/labtestreport/summary.form' : backUrl}" var="computedBackUrl">
	<c:param name="startDate" value="${startDate}" />
	<c:param name="endDate" value="${endDate}" />
</c:url>
<a href="${computedBackUrl}"><spring:message code="labtestreport.backToReport" /></a>

<%@ include file="/WEB-INF/template/footer.jsp"%>
