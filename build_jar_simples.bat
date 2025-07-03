@echo off
echo =========================================
echo   GERANDO JAR - CORBA CALCULATOR (SEM MAVEN)
echo =========================================
echo.

echo Limpando build anterior...
if exist "build" rmdir /s /q build
if exist "CORBA-Distributed-Calculator.jar" del "CORBA-Distributed-Calculator.jar"

echo.
echo Criando diretorio de build...
mkdir build

echo.
echo Compilando classes Java...
javac -cp . -d build src\main\java\com\project3\*.java src\main\java\com\project3\client\*.java src\main\java\com\project3\server\*.java src\main\java\com\project3\corba\*.java src\main\java\com\project3\protocol\*.java src\main\java\com\project3\distributed\*.java

if errorlevel 1 (
    echo ❌ Erro na compilacao!
    pause
    exit /b 1
)

echo.
echo Copiando recursos...
if exist "src\main\resources" (
    xcopy "src\main\resources\*" "build\" /s /y
)

echo.
echo Criando arquivo MANIFEST...
echo Main-Class: com.project3.Main > build\MANIFEST.MF
echo. >> build\MANIFEST.MF

echo.
echo Gerando JAR executavel...
cd build
jar cfm ..\CORBA-Distributed-Calculator.jar MANIFEST.MF *
cd ..

echo.
if exist "CORBA-Distributed-Calculator.jar" (
    echo ✅ JAR gerado com sucesso!
    echo Arquivo: CORBA-Distributed-Calculator.jar
    echo Tamanho: 
    dir "CORBA-Distributed-Calculator.jar" | findstr "CORBA"
    echo.
    echo COMO USAR O JAR:
    echo java -jar CORBA-Distributed-Calculator.jar server
    echo java -jar CORBA-Distributed-Calculator.jar client
    echo java -jar CORBA-Distributed-Calculator.jar distributed
    echo.
    echo TESTE RAPIDO:
    echo java -jar CORBA-Distributed-Calculator.jar
) else (
    echo ❌ Erro ao gerar JAR!
)

echo.
pause
