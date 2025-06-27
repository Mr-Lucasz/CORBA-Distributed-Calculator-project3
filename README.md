# CORBA - Common Object Request Broker Architecture

## Técnica Implementada
**Objetos distribuídos e invocação remota através de CORBA** (Trabalho #10)

## Conceitos CORBA Demonstrados
- **Object Request Broker (ORB)**: Gerencia comunicação entre objetos distribuídos
- **Transparência de Localização**: Cliente não sabe onde objeto está fisicamente
- **Transparência de Acesso**: Mesma interface para objetos locais e remotos
- **Interface Definition Language (IDL)**: Definição de interfaces independente de linguagem
- **Stub/Skeleton**: Proxies para comunicação remota

## Arquitetura do Sistema
```
Cliente (ORB) ←→ Socket/TCP ←→ Servidor (Objeto Remoto)
     ↓                              ↓
  Stub Proxy                   Skeleton
     ↓                              ↓
Interface Calculator           CalculatorImpl
```

## Execução no Laboratório (5+ hosts)

### 1. Em cada host servidor:
```bash
server.bat
```
- Inicia servidor CORBA
- Registra objeto Calculator
- Escuta na porta 12345

### 2. No host coordenador:
```bash
distributed.bat  
```
- Sistema pede IPs dos servidores
- Cria stubs para objetos remotos
- Distribui chamadas via ORB

## Mensagens CORBA Trocadas
1. **Lookup**: Cliente localiza objeto remoto
2. **Request**: Chamada de método empacotada
3. **Response**: Resultado serializado
4. **Exception**: Tratamento de erros distribuídos

## Comandos
- `compile.bat` - Compila IDL e classes Java
- `server.bat` - Servidor CORBA  
- `distributed.bat` - Cliente distribuído ORB

## Demonstração
O sistema mostra transparência CORBA executando a mesma operação em hosts diferentes, mantendo interface única para o cliente.
