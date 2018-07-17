echo off
setlocal

echo Checking for Atlassian SDK...
WHERE atlas-run
IF %ERRORLEVEL% NEQ 0 (
    echo Atlassian SDK not installed.
    echo Please visit "https://developer.atlassian.com/server/framework/atlassian-sdk/install-the-atlassian-sdk-on-a-windows-system/" for installation procedure.
    endlocal
    pause > nul
    exit
)

For /F "eol=# tokens=1,* delims==" %%A IN (data.properties) DO (
	set %%A=%%B
)

echo Starting Bamboo loaded with accelQ Plugin on Port 8954
cd /d "%project_location%"
call atlas-run --http-port %bamboo_server_port%

endlocal
pause > nul
exit