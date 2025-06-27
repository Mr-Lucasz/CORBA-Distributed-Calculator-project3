package com.project3.distributed;

import com.project3.corba.ICalculator;
import com.project3.client.CalculatorStub;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Coordenador para sistema distribuído de calculadora
 */
public class DistributedCalculatorCoordinator {
    private final Map<String, HostInfo> connectedHosts;
    private final String coordinatorId;
    private final Scanner scanner;
    private List<ICalculator> activeCalculators;
    
    public DistributedCalculatorCoordinator() {
        this.connectedHosts = new HashMap<>();
        this.scanner = new Scanner(System.in);
        this.activeCalculators = new ArrayList<>();
        
        try {
            String localHost = InetAddress.getLocalHost().getHostAddress();
            this.coordinatorId = String.format("COORDINATOR-%s-%d", localHost, System.currentTimeMillis() % 10000);
        } catch (UnknownHostException e) {
            throw new RuntimeException("Falha ao obter informações do host local", e);
        }
    }
    
    public void start() {
        System.out.println("=== SISTEMA DISTRIBUIDO - CALCULADORA ===");
        System.out.println("Coordenador: " + coordinatorId);
        System.out.println();
        
        discoverHosts();
        connectToHosts();
        
        if (!activeCalculators.isEmpty()) {
            startDistributedCalculator();
        } else {
            System.err.println("Nenhum host disponível.");
        }
    }
    
    private void discoverHosts() {
        System.out.println("=== CONFIGURACAO DE HOSTS ===");
        System.out.println("Digite os IPs dos hosts servidores (minimo 5 recomendado):");
        
        boolean addingHosts = true;
        int hostCounter = 1;
        
        while (addingHosts) {
            System.out.printf("Host %d - IP (ou 'sair'): ", hostCounter);
            String hostIp = scanner.nextLine().trim();
            
            if (hostIp.equalsIgnoreCase("sair")) {
                if (connectedHosts.isEmpty()) {
                    System.out.println("Pelo menos um host deve ser configurado!");
                    continue;
                }
                break;
            }
            
            if (hostIp.isEmpty()) continue;
            
            System.out.print("Porta (Enter para 12345): ");
            String portInput = scanner.nextLine().trim();
            int port = 12345;
            
            if (!portInput.isEmpty()) {
                try {
                    port = Integer.parseInt(portInput);
                } catch (NumberFormatException e) {
                    System.out.println("Porta inválida. Usando 12345.");
                }
            }
            
            String hostKey = hostIp + ":" + port;
            if (!connectedHosts.containsKey(hostKey)) {
                HostInfo hostInfo = new HostInfo(hostIp, port, "HOST-" + hostCounter);
                connectedHosts.put(hostKey, hostInfo);
                System.out.println("Host adicionado: " + hostKey);
                hostCounter++;
            }
        }
        
        System.out.println("Total de hosts: " + connectedHosts.size());
    }
    
    private void connectToHosts() {
        System.out.println("=== CONECTANDO AOS HOSTS ===");
        
        for (Map.Entry<String, HostInfo> entry : connectedHosts.entrySet()) {
            HostInfo host = entry.getValue();
            try {
                System.out.printf("Conectando a %s:%d... ", host.getIp(), host.getPort());
                
                String clientId = coordinatorId + "-" + host.getName();
                ICalculator calculator = new CalculatorStub(host.getIp(), host.getPort(), clientId);
                
                if (calculator instanceof CalculatorStub) {
                    CalculatorStub stub = (CalculatorStub) calculator;
                    if (stub.testConnection()) {
                        activeCalculators.add(calculator);
                        host.setConnected(true);
                        System.out.println("OK");
                    } else {
                        System.out.println("FALHA");
                    }
                }
            } catch (Exception e) {
                System.out.println("ERRO: " + e.getMessage());
            }
        }
        
        System.out.printf("Hosts conectados: %d de %d%n", activeCalculators.size(), connectedHosts.size());
    }
    
    private void startDistributedCalculator() {
        System.out.println("=== CALCULADORA DISTRIBUIDA ===");
        System.out.printf("Sistema com %d hosts ativos%n", activeCalculators.size());
        
        boolean running = true;
        
        while (running) {
            printMenu();
            System.out.print("Opcao: ");
            String option = scanner.nextLine().trim();
            
            switch (option) {
                case "1":
                    performOperation("add");
                    break;
                case "2":
                    performOperation("subtract");
                    break;
                case "3":
                    performOperation("multiply");
                    break;
                case "4":
                    performOperation("divide");
                    break;
                case "5":
                    showSystemInfo();
                    break;
                case "6":
                    demonstrateCORBA();
                    break;
                case "7":
                    testTransparency();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Opcao invalida!");
            }
        }
    }
    
    private void printMenu() {
        System.out.println("\n=== MENU CORBA DISTRIBUIDO ===");
        System.out.println("1. Adicao Distribuida");
        System.out.println("2. Subtracao Distribuida");
        System.out.println("3. Multiplicacao Distribuida");
        System.out.println("4. Divisao Distribuida");
        System.out.println("5. Info do Sistema CORBA");
        System.out.println("6. Demonstracao CORBA");
        System.out.println("7. Teste de Transparencia");
        System.out.println("0. Sair");
        System.out.printf("ORB - Object Request Broker ativo com %d hosts\n", activeCalculators.size());
    }
    
    private ICalculator getNextCalculator() {
        if (activeCalculators.isEmpty()) {
            throw new RuntimeException("Nenhum host ativo");
        }
        ICalculator calculator = activeCalculators.get(0);
        activeCalculators.add(activeCalculators.remove(0));
        return calculator;
    }
    
    private void performOperation(String operation) {
        try {
            System.out.print("Primeiro numero: ");
            double a = Double.parseDouble(scanner.nextLine());
            System.out.print("Segundo numero: ");
            double b = Double.parseDouble(scanner.nextLine());
            
            ICalculator calculator = getNextCalculator();
            String hostInfo = getHostInfo(calculator);
            
            System.out.println("\n=== DETALHES DA OPERACAO CORBA ===");
            System.out.printf("Cliente: %s\n", coordinatorId);
            System.out.printf("Servidor: %s\n", hostInfo);
            System.out.printf("Metodo: %s(%.2f, %.2f)\n", operation, a, b);
            System.out.println("Enviando requisicao CORBA...");
            
            long startTime = System.currentTimeMillis();
            double result;
            
            switch (operation) {
                case "add":
                    result = calculator.add(a, b);
                    System.out.printf("Resultado: %.2f + %.2f = %.2f\n", a, b, result);
                    break;
                case "subtract":
                    result = calculator.subtract(a, b);
                    System.out.printf("Resultado: %.2f - %.2f = %.2f\n", a, b, result);
                    break;
                case "multiply":
                    result = calculator.multiply(a, b);
                    System.out.printf("Resultado: %.2f * %.2f = %.2f\n", a, b, result);
                    break;
                case "divide":
                    result = calculator.divide(a, b);
                    System.out.printf("Resultado: %.2f / %.2f = %.2f\n", a, b, result);
                    break;
            }
            
            long endTime = System.currentTimeMillis();
            System.out.printf("Tempo de resposta: %d ms\n", endTime - startTime);
            System.out.println("=== OPERACAO CONCLUIDA ===\n");
            
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }
    
    private void showSystemInfo() {
        System.out.println("=== INFO DO SISTEMA ===");
        System.out.printf("Coordenador: %s\n", coordinatorId);
        System.out.printf("Hosts configurados: %d\n", connectedHosts.size());
        System.out.printf("Hosts ativos: %d\n", activeCalculators.size());
        
        for (int i = 0; i < activeCalculators.size(); i++) {
            ICalculator calc = activeCalculators.get(i);
            try {
                String serverInfo = calc.getServerInfo();
                System.out.printf("%d. %s\n", i + 1, serverInfo);
            } catch (Exception e) {
                System.out.printf("%d. [Erro: %s]\n", i + 1, e.getMessage());
            }
        }
    }
    
    private void demonstrateCORBA() {
        System.out.println("=== DEMONSTRACAO CORBA ===");
        System.out.println("CORBA - Common Object Request Broker Architecture");
        System.out.println("Demonstrando transparencia de localizacao e acesso\n");
        
        try {
            System.out.println("1. TRANSPARENCIA DE LOCALIZACAO:");
            System.out.println("   Cliente nao sabe onde o objeto esta fisicamente");
            
            for (int i = 0; i < Math.min(3, activeCalculators.size()); i++) {
                ICalculator calc = getNextCalculator();
                String hostInfo = getHostInfo(calc);
                
                System.out.printf("   Chamada %d: Executando em %s\n", i+1, hostInfo);
                double result = calc.add(10 + i, 5);
                System.out.printf("   Resultado: %.0f\n", result);
                Thread.sleep(1000);
            }
            
            System.out.println("\n2. TRANSPARENCIA DE ACESSO:");
            System.out.println("   Mesma interface para objetos locais e remotos");
            ICalculator calc = getNextCalculator();
            String serverInfo = calc.getServerInfo();
            System.out.printf("   Informacoes do servidor: %s\n", serverInfo);
            
            System.out.println("\n3. OBJECT REQUEST BROKER (ORB):");
            System.out.printf("   ORB gerencia %d objetos remotos\n", activeCalculators.size());
            System.out.println("   ORB roteia chamadas automaticamente");
            
        } catch (Exception e) {
            System.err.println("Erro na demonstracao: " + e.getMessage());
        }
    }
    
    private void testTransparency() {
        System.out.println("=== TESTE DE TRANSPARENCIA CORBA ===");
        System.out.println("Executando a mesma operacao em diferentes hosts");
        System.out.println("para demonstrar transparencia de localizacao\n");
        
        try {
            double a = 100.0;
            double b = 25.0;
            
            for (int i = 0; i < activeCalculators.size(); i++) {
                ICalculator calc = getNextCalculator();
                String hostInfo = getHostInfo(calc);
                
                System.out.printf("Host %s:\n", hostInfo);
                System.out.printf("  Multiplicacao: %.0f * %.0f = ", a, b);
                
                long startTime = System.currentTimeMillis();
                double result = calc.multiply(a, b);
                long endTime = System.currentTimeMillis();
                
                System.out.printf("%.0f (tempo: %dms)\n", result, endTime - startTime);
                
                // Mostrar que o resultado é sempre o mesmo, independente do host
                if (result == 2500.0) {
                    System.out.println("  ✓ Transparencia mantida - resultado consistente");
                }
                
                Thread.sleep(800);
            }
            
            System.out.println("\nTESTE CONCLUIDO:");
            System.out.println("- Mesma operacao executada em hosts diferentes");
            System.out.println("- Cliente usa interface unica (transparencia)");
            System.out.println("- ORB gerencia distribuicao automaticamente");
            
        } catch (Exception e) {
            System.err.println("Erro no teste: " + e.getMessage());
        }
    }
    
    private String getHostInfo(ICalculator calculator) {
        try {
            String serverInfo = calculator.getServerInfo();
            if (serverInfo.contains("@")) {
                return serverInfo.split("@")[1].split(" ")[0];
            }
            return "Host-Desconhecido";
        } catch (Exception e) {
            return "Host-Erro";
        }
    }
    
    // Classe auxiliar
    private static class HostInfo {
        private final String ip;
        private final int port;
        private final String name;
        
        public HostInfo(String ip, int port, String name) {
            this.ip = ip;
            this.port = port;
            this.name = name;
        }
        
        public String getIp() { return ip; }
        public int getPort() { return port; }
        public String getName() { return name; }
        public void setConnected(boolean connected) { /* placeholder */ }
    }
}
