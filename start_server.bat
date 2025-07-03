@echo off
echo ===========================================
echo    INICIANDO SERVIDOR CORBA - CALCULADORA
echo ===========================================
echo.
echo Iniciando servidor CORBA Calculator...
echo Este host atuara como servidor para o sistema distribuido.
REM Compilar se necessário
if not exist "target\classes\com\project3\Main.class" (
    echo Compilando projeto...
    call compile.bat