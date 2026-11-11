# UTF-8编码环境启动脚本 - PowerShell版本
Write-Host "========================================" -ForegroundColor Green
Write-Host " UTF-8编码环境启动脚本" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

# 设置控制台编码为UTF-8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
[Console]::InputEncoding = [System.Text.Encoding]::UTF8

# 设置环境变量
$env:JAVA_TOOL_OPTIONS = "-Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8"
$env:LANG = "ja_JP.UTF-8"
$env:LC_ALL = "ja_JP.UTF-8"

Write-Host "当前编码设置:" -ForegroundColor Yellow
Write-Host "  控制台输出编码: UTF-8" -ForegroundColor White
Write-Host "  控制台输入编码: UTF-8" -ForegroundColor White
Write-Host "  文件编码: UTF-8" -ForegroundColor White
Write-Host "  系统编码: UTF-8" -ForegroundColor White
Write-Host ""

# 切换到项目目录
Set-Location "$PSScriptRoot\.."

# 运行应用
Write-Host "启动Spring Boot应用..." -ForegroundColor Cyan
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 -Dconsole.encoding=UTF-8"

Write-Host ""
Write-Host "应用已停止，按任意键退出..." -ForegroundColor Yellow
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")