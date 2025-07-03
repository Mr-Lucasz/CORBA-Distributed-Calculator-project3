@echo off
echo ======================================================
echo    PARANDO TODOS OS SERVIDORES CORBA
echo ======================================================
echo.
echo Procurando e finalizando processos Java CORBA Calculator...

taskkill /f /fi "WINDOWTITLE eq Servidor-12345*" 2>nul
taskkill /f /fi "WINDOWTITLE eq Servidor-12346*" 2>nul  
taskkill /f /fi "WINDOWTITLE eq Servidor-12347*" 2>nul
taskkill /f /fi "WINDOWTITLE eq Servidor-12348*" 2>nul

echo.
echo Todos os servidores foram finalizados.
echo ======================================================
pause
