@echo off
chcp 65001 >nul
java -Dfile.encoding=UTF-8 -cp build minimarket.Minimarket
pause
