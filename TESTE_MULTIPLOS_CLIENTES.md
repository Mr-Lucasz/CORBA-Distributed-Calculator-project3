# GUIA DE TESTE - MÚLTIPLOS CLIENTES

## PASSO 1: Abrir o Servidor
1. Abra um prompt de comando
2. Navegue para: c:\Users\C919409\OneDrive - Anheuser-Busch InBev\My Documents\calc
3. Execute: start_server.bat
4. DEIXE ESTA JANELA ABERTA!

## PASSO 2: Testar Cliente Simples
1. Abra OUTRO prompt de comando
2. Navegue para a mesma pasta
3. Execute: start_client.bat
4. Digite localhost quando pedir o IP
5. Pressione Enter para porta padrão
6. Teste algumas operações

## PASSO 3: Testar Múltiplos Clientes
1. Abra MAIS prompts de comando
2. Execute start_client.bat em cada um
3. Todos conectarão no mesmo servidor
4. Teste operações simultâneas

## PASSO 4: Testar Sistema Distribuído
1. Abra um prompt
2. Execute: start_distributed.bat
3. Digite localhost quando pedir IPs dos servidores
4. Digite 'sair' quando terminar de adicionar hosts

## COMANDOS DIRETOS:
- Servidor: java -cp target\classes com.project3.Main server
- Cliente: java -cp target\classes com.project3.Main client  
- Distribuído: java -cp target\classes com.project3.Main distributed

## DICA:
- Sempre rode o servidor PRIMEIRO
- Deixe o servidor rodando em uma janela separada
- Abra quantos clientes quiser em outras janelas
