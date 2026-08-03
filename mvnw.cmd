@echo off
setlocal

if defined MAVEN_HOME if exist "%MAVEN_HOME%\bin\mvn.cmd" (
  "%MAVEN_HOME%\bin\mvn.cmd" %*
  exit /b %ERRORLEVEL%
)

where mvn.cmd >nul 2>nul
if %ERRORLEVEL% EQU 0 (
  mvn.cmd %*
  exit /b %ERRORLEVEL%
)

echo Maven was not found. Install Maven 3.9+ or set MAVEN_HOME.
exit /b 1
