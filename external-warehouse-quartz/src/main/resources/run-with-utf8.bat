@echo off
chcp 65001 >nul
set JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8
set LANG=ja_JP.UTF-8
set LC_ALL=ja_JP.UTF-8

echo ========================================
echo  UTF-8编码环境启动脚本
echo ========================================
echo.
echo 当前编码设置:
echo  控制台编码: 65001 (UTF-8)
echo  文件编码: UTF-8
echo  系统编码: UTF-8
echo.

cd /d %~dp0
cd ..

mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 -Dconsole.encoding=UTF-8"

pause