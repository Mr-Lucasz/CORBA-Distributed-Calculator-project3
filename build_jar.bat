@echo off
echo =========================================
echo     GERANDO JAR - CORBA CALCULATOR
echo =========================================
echo.

echo Limpando build anterior...
mvn clean

echo.
echo Compilando e gerando JAR executavel...
mvn package

echo.
if exist "target\CORBA-Distributed-Calculator.jar" (
    echo ✅ JAR gerado com sucesso!
    echo Arquivo: target\CORBA-Distributed-Calculator.jar
    echo.
    echo COMO USAR O JAR:
    echo java -jar target\CORBA-Distributed-Calculator.jar server
    echo java -jar target\CORBA-Distributed-Calculator.jar client
    echo java -jar target\CORBA-Distributed-Calculator.jar distributed
) else (
    echo ❌ Erro ao gerar JAR!
)

echo.
pause
