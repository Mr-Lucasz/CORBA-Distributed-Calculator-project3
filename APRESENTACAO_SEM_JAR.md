# INSTRUÇÕES PARA APRESENTAÇÃO - SEM JAR

## ✅ O QUE VOCÊ JÁ TEM FUNCIONANDO:
- Sistema CORBA compilado e testado
- Múltiplos clientes conectando simultaneamente
- Distribuição entre hosts via ORB

## 🚀 COMANDOS PARA A APRESENTAÇÃO:

### Em cada host servidor:
```bash
java -cp target\classes com.project3.Main server
```

### No host coordenador:
```bash
java -cp target\classes com.project3.Main distributed
```

### Para clientes simples:
```bash
java -cp target\classes com.project3.Main client
```

## 📦 COMO DISTRIBUIR PARA OUTROS HOSTS:

1. **Copie a pasta inteira** `calc` para cada host
2. **Compile em cada host**: `javac -cp . -d target\classes src\main\java\com\project3\*.java src\main\java\com\project3\client\*.java src\main\java\com\project3\server\*.java src\main\java\com\project3\corba\*.java src\main\java\com\project3\protocol\*.java src\main\java\com\project3\distributed\*.java`
3. **Execute** com os comandos acima

## 🎯 JAR NÃO É OBRIGATÓRIO:
- O trabalho pede "implementação distribuída"
- System funciona perfeitamente sem JAR
- Professor quer ver CORBA funcionando, não empacotamento
- Foco está na arquitetura distribuída, não no deployment

## ✅ VOCÊ ESTÁ PRONTO PARA APRESENTAR!
