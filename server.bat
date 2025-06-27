@echo off
echo Iniciando servidor CORBA...
java -cp target\classes com.project3.Main server %1
pause
