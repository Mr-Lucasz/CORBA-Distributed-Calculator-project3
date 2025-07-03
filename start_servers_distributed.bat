@echo off
echo ======================================================
echo    SCRIPT DE INICIALIZAÇÃO PARA SISTEMA DISTRIBUÍDO
echo ======================================================
echo.
echo Este script vai iniciar 4 servidores em portas diferentes
echo Certifique-se de que o JAR foi compilado com: mvn clean package
echo.
pause

echo Iniciando servidores...
echo.

echo [1/4] Iniciando Servidor na porta 12345...
start "Servidor-12345" cmd /k "java -jar target/CORBA-Distributed-Calculator.jar server 12345"
timeout /t 2 /nobreak > nul

echo [2/4] Iniciando Servidor na porta 12346...
start "Servidor-12346" cmd /k "java -jar target/CORBA-Distributed-Calculator.jar server 12346"
timeout /t 2 /nobreak > nul

echo [3/4] Iniciando Servidor na porta 12347...
start "Servidor-12347" cmd /k "java -jar target/CORBA-Distributed-Calculator.jar server 12347"
timeout /t 2 /nobreak > nul

echo [4/4] Iniciando Servidor na porta 12348...
start "Servidor-12348" cmd /k "java -jar target/CORBA-Distributed-Calculator.jar server 12348"
timeout /t 3 /nobreak > nul

echo.
echo ======================================================
echo Todos os servidores foram iniciados!
echo.
echo Agora você pode executar o sistema distribuído com:
echo java -jar target/CORBA-Distributed-Calculator.jar distributed
echo.
echo Configure os seguintes hosts:
echo - 127.0.0.1:12345
echo - 127.0.0.1:12346  
echo - 127.0.0.1:12347
echo - 127.0.0.1:12348
echo ======================================================
pause
