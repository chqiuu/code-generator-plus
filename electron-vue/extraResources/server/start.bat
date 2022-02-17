
@echo off
echo set app name
set appName=code-generator-plus
echo set jarFile 
set jarFile=code-generator-plus.jar
echo find %appName% to kill before start process.
for /f "usebackq tokens=1-2" %%a in (`jps -l ^| findstr %appName%`) do (
	set pid=%%a
	set image_name=%%b
)
if not defined pid (echo process %appName% does not exists) else (
	echo prepare to kill %image_name%
	echo start kill %pid% ...
	taskkill /f /pid %pid%
)

echo start %jarFile%
start jre\bin\java -Xms1024m -Xmx2048m -Ddruid.mysql.usePingMethod=false -Dsun.lang.ClassLoader.allowArraySyntax=true -Duser.timezone=GMT+08 -jar %jarFile%
for /f "usebackq tokens=1-2" %%a in (`jps -l ^| findstr %jarFile%`) do (
	set pid=%%a
	set image_name=%%b
	echo %%a >%appName%.pid
)
exit