# Guia do Sistema Distribuído CORBA Calculator

## Pré-requisitos
1. Java 11+ instalado
2. Projeto compilado: `mvn clean package`
3. JAR gerado em: `target/CORBA-Distributed-Calculator.jar`

## Método 1: Usando Scripts Automáticos (Windows)

### Iniciar Servidores
```batch
.\start_servers_distributed.bat
```
Este script iniciará automaticamente 4 servidores em janelas separadas nas portas:
- 127.0.0.1:12345
- 127.0.0.1:12346
- 127.0.0.1:12347
- 127.0.0.1:12348

### Iniciar Sistema Distribuído
Em um novo PowerShell/CMD:
```batch
java -jar target/CORBA-Distributed-Calculator.jar distributed
```

### Parar Servidores
```batch
.\stop_servers.bat
```

## Método 2: Manual (Windows/Linux/Mac)

### Passo 1: Iniciar Servidores Manualmente

**Terminal/PowerShell 1:**
```bash
java -jar target/CORBA-Distributed-Calculator.jar server 12345
```

**Terminal/PowerShell 2:**
```bash
java -jar target/CORBA-Distributed-Calculator.jar server 12346
```

**Terminal/PowerShell 3:**
```bash
java -jar target/CORBA-Distributed-Calculator.jar server 12347
```

**Terminal/PowerShell 4:**
```bash
java -jar target/CORBA-Distributed-Calculator.jar server 12348
```

### Passo 2: Iniciar Coordenador Distribuído

**Novo Terminal/PowerShell:**
```bash
java -jar target/CORBA-Distributed-Calculator.jar distributed
```

### Passo 3: Configurar Hosts

Quando solicitado, digite:
```
Host 1 - IP: 127.0.0.1
Porta: 12345

Host 2 - IP: 127.0.0.1  
Porta: 12346

Host 3 - IP: 127.0.0.1
Porta: 12347

Host 4 - IP: 127.0.0.1
Porta: 12348

Host 5 - IP: sair
```

## Verificação

Se tudo estiver correto, você verá:
```
=== CONECTANDO AOS HOSTS ===
Conectando a 127.0.0.1:12345... OK
Conectando a 127.0.0.1:12346... OK  
Conectando a 127.0.0.1:12347... OK
Conectando a 127.0.0.1:12348... OK
Hosts conectados: 4 de 4
```

## Solução de Problemas

### "Connection refused"
- ✅ Verifique se todos os servidores estão rodando
- ✅ Verifique se as portas estão corretas
- ✅ Verifique se o firewall não está bloqueando

### "Port already in use"
- ✅ Use portas diferentes para cada servidor
- ✅ Verifique processos em execução: `netstat -an | findstr :12345`

### "Class version error"
- ✅ Recompile o projeto: `mvn clean package`
- ✅ Verifique versão do Java: `java -version`

## Demonstração no Laboratório

Para demonstrar em hosts diferentes:
1. Copie o JAR para cada host
2. Inicie um servidor em cada host na porta 12345
3. No host coordenador, use os IPs reais dos outros hosts
4. Execute o sistema distribuído

Exemplo:
```
Host 1 - IP: 192.168.1.100
Host 2 - IP: 192.168.1.101
Host 3 - IP: 192.168.1.102
Host 4 - IP: 192.168.1.103
```
