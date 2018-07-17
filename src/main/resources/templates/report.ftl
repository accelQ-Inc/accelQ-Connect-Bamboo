<html>
<head>
	<title>Test Report</title>
</head>
<body>
	<h2>${report.name}</h2>
	<h5>Passed: ${report.passCount}</h5>
	<h5>Failed: ${report.failCount}</h5>
	<br/><br/>
	<table>
		<tr>
			<th>Test Case Name</th>
			<th>Status</th>
			<th>Exec Time</th>
		</tr>
		<#list report.tests as test>
			<tr>
				<td>${test[0]}</td>
				<td>${test[1]}</td>
				<td>${test[2]}</td
			</tr>
		</#list>
	</table>
</body>
</html>