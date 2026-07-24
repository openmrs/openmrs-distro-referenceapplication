<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<h2><spring:message code="labtestreport.sessionAttendance.title" /></h2>

<style>
	#sessionAttendanceTable { border-collapse: collapse; width: 100%; }
	#sessionAttendanceTable th, #sessionAttendanceTable td { border: 1px solid #ccc; padding: 4px 8px; text-align: center; }
	#sessionAttendanceTable td.sessionType, #sessionAttendanceTable td.sessionSubject { text-align: left; }
	#sessionAttendanceTable th { background-color: #f0f0f0; }
	#sessionAttendanceTable tr.dayHeader td { background-color: #d0e4f5; font-weight: bold; text-align: left; }
	#sessionAttendanceTable tr.dailyTotal td { background-color: #fde9c8; font-weight: bold; }
</style>

<form method="get" action="${pageContext.request.contextPath}/module/labtestreport/sessionAttendance.form">
	<label for="startDate"><spring:message code="labtestreport.startDate" /></label>
	<input type="date" id="startDate" name="startDate" value="${startDate}" />
	<label for="endDate"><spring:message code="labtestreport.endDate" /></label>
	<input type="date" id="endDate" name="endDate" value="${endDate}" />
	<input type="submit" value="<spring:message code='labtestreport.filter'/>" />
</form>

<br />

<table id="sessionAttendanceTable">
	<thead>
		<tr>
			<th rowspan="2"><spring:message code="labtestreport.sessionAttendance.sessionType" /></th>
			<th rowspan="2"><spring:message code="labtestreport.sessionAttendance.sessionSubject" /></th>
			<th rowspan="2"><spring:message code="labtestreport.sessionAttendance.totalAttendees" /></th>
			<th colspan="2">0-4</th>
			<th colspan="2">5-14</th>
			<th colspan="2">15-18</th>
			<th colspan="2">19-49</th>
			<th colspan="2">50-65</th>
			<th colspan="2">65+</th>
			<th rowspan="2"><spring:message code="labtestreport.total" /></th>
		</tr>
		<tr>
			<c:forEach begin="1" end="6">
				<th>M</th>
				<th>F</th>
			</c:forEach>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${dayBlocks}" var="block">
			<tr class="dayHeader">
				<td colspan="16"><spring:message code="labtestreport.sessionAttendance.day" />:
					<fmt:formatDate value="${block.date}" pattern="yyyy-MM-dd" /></td>
			</tr>
			<c:forEach items="${block.rows}" var="row" varStatus="rowStatus">
				<fmt:formatDate value="${row.sessionDate}" pattern="yyyy-MM-dd" var="rowDate" />
				<tr class="${rowStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}">
					<td class="sessionType">${row.sessionType}</td>
					<td class="sessionSubject">${row.sessionSubject}</td>
					<td>
						<c:choose>
							<c:when test="${row.totalAttendees > 0}">
								<c:url value="/module/labtestreport/sessionAttendanceDrilldown.form" var="totalUrl">
									<c:param name="sessionDate" value="${rowDate}" />
									<c:param name="sessionType" value="${row.sessionType}" />
									<c:param name="startDate" value="${startDate}" />
									<c:param name="endDate" value="${endDate}" />
								</c:url>
								<a href="${totalUrl}">${row.totalAttendees}</a>
							</c:when>
							<c:otherwise>${row.totalAttendees}</c:otherwise>
						</c:choose>
					</td>
					<c:forEach items="${columns}" var="col">
						<c:set var="cellCount" value="${row.counts[col.key]}" />
						<td>
							<c:choose>
								<c:when test="${cellCount > 0}">
									<c:url value="/module/labtestreport/sessionAttendanceDrilldown.form" var="cellUrl">
										<c:param name="sessionDate" value="${rowDate}" />
										<c:param name="sessionType" value="${row.sessionType}" />
										<c:param name="ageGroup" value="${col.ageGroup}" />
										<c:param name="gender" value="${col.gender}" />
										<c:param name="startDate" value="${startDate}" />
										<c:param name="endDate" value="${endDate}" />
									</c:url>
									<a href="${cellUrl}">${cellCount}</a>
								</c:when>
								<c:otherwise>${cellCount}</c:otherwise>
							</c:choose>
						</td>
					</c:forEach>
					<td>
						<c:choose>
							<c:when test="${row.total > 0}">
								<c:url value="/module/labtestreport/sessionAttendanceDrilldown.form" var="grandTotalUrl">
									<c:param name="sessionDate" value="${rowDate}" />
									<c:param name="sessionType" value="${row.sessionType}" />
									<c:param name="startDate" value="${startDate}" />
									<c:param name="endDate" value="${endDate}" />
								</c:url>
								<a href="${grandTotalUrl}">${row.total}</a>
							</c:when>
							<c:otherwise>${row.total}</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
			<tr class="dailyTotal">
				<td colspan="2"><spring:message code="labtestreport.sessionAttendance.totalOfTheDay" /></td>
				<td>${block.dailyTotal.totalAttendees}</td>
				<c:forEach items="${columns}" var="col">
					<td>${block.dailyTotal.counts[col.key]}</td>
				</c:forEach>
				<td>${block.dailyTotal.total}</td>
			</tr>
		</c:forEach>
		<c:if test="${empty dayBlocks}">
			<tr>
				<td colspan="16"><spring:message code="labtestreport.drilldown.noPatients" /></td>
			</tr>
		</c:if>
	</tbody>
</table>

<%@ include file="/WEB-INF/template/footer.jsp"%>
