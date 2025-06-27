package com.project3.client;

import com.project3.corba.Calculator;
import com.project3.corba.DivisionByZeroException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Cliente CORBA interativo para a calculadora distribuída
 * Demonstra o uso transparente de objetos remotos
 */
public class CalculatorClient {
    private static final int DEFAULT_PORT = 12345;
    private final Calculator calculator;
    private final String clientId;
    
    public CalculatorClient(String serverHost, int serverPort) {
        try {
            String localHost = InetAddress.getLocalHost().getHostAddress();
            this.clientId = String.format("%s-%d", localHost, System.currentTimeMillis() % 10000);
        } catch (UnknownHostException e) {
            throw new RuntimeException("Failed to get local host information", e);
        }
        
        this.calculator = new CalculatorStub(serverHost, serverPort, clientId);
        System.out.printf("=== Cliente CORBA Calculator Inicializado ===%n");
        System.out.printf("ID do Cliente: %s%n", clientId);
        System.out.printf("Servidor: %s:%d%n", serverHost, serverPort);
    }
    
    public void start() {
        // Testar conexão
        System.out.println("Testando conexão com o servidor...");
        CalculatorStub stub = (CalculatorStub) calculator;
        if (!stub.testConnection()) {
            System.err.println("Falha ao conectar com o servidor. Verifique se o servidor está rodando.");
            return;
        }
        
        System.out.println("Conexão bem-sucedida!");
        System.out.println("Informações do Servidor: " + calculator.getServerInfo());
        System.out.println("==========================================");
        
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    performAddition(scanner);
                    break;
                case "2":
                    performSubtraction(scanner);
                    break;
                case "3":
                    performMultiplication(scanner);
                    break;
                case "4":
                    performDivision(scanner);
                    break;
                case "5":
                    showServerInfo();
                    break;
                case "6":
                    performBatchOperations();
                    break;
                case "0":
                    running = false;
                    System.out.println("Até logo!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
            
            if (running) {
                System.out.println("Pressione Enter para continuar...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }
    
    private void printMenu() {
        System.out.println("\n=== Menu Calculadora CORBA ===");
        System.out.println("1. Adição");
        System.out.println("2. Subtração");
        System.out.println("3. Multiplicação");
        System.out.println("4. Divisão");
        System.out.println("5. Informações do Servidor");
        System.out.println("6. Demo de Operações em Lote");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }
    
    private void performAddition(Scanner scanner) {
        try {
            System.out.print("Digite o primeiro número: ");
            double a = Double.parseDouble(scanner.nextLine());
            System.out.print("Digite o segundo número: ");
            double b = Double.parseDouble(scanner.nextLine());
            
            double result = calculator.add(a, b);
            System.out.printf("Resultado: %.2f + %.2f = %.2f%n", a, b, result);
        } catch (NumberFormatException e) {
            System.out.println("Formato de número inválido. Digite números válidos.");
        } catch (Exception e) {
            System.err.println("Erro ao realizar adição: " + e.getMessage());
        }
    }
    
    private void performSubtraction(Scanner scanner) {
        try {
            System.out.print("Digite o primeiro número: ");
            double a = Double.parseDouble(scanner.nextLine());
            System.out.print("Digite o segundo número: ");
            double b = Double.parseDouble(scanner.nextLine());
            
            double result = calculator.subtract(a, b);
            System.out.printf("Resultado: %.2f - %.2f = %.2f%n", a, b, result);
        } catch (NumberFormatException e) {
            System.out.println("Formato de número inválido. Digite números válidos.");
        } catch (Exception e) {
            System.err.println("Erro ao realizar subtração: " + e.getMessage());
        }
    }
    
    private void performMultiplication(Scanner scanner) {
        try {
            System.out.print("Digite o primeiro número: ");
            double a = Double.parseDouble(scanner.nextLine());
            System.out.print("Digite o segundo número: ");
            double b = Double.parseDouble(scanner.nextLine());
            
            double result = calculator.multiply(a, b);
            System.out.printf("Resultado: %.2f * %.2f = %.2f%n", a, b, result);
        } catch (NumberFormatException e) {
            System.out.println("Formato de número inválido. Digite números válidos.");
        } catch (Exception e) {
            System.err.println("Erro ao realizar multiplicação: " + e.getMessage());
        }
    }
    
    private void performDivision(Scanner scanner) {
        try {
            System.out.print("Digite o dividendo: ");
            double a = Double.parseDouble(scanner.nextLine());
            System.out.print("Digite o divisor: ");
            double b = Double.parseDouble(scanner.nextLine());
            
            double result = calculator.divide(a, b);
            System.out.printf("Resultado: %.2f / %.2f = %.2f%n", a, b, result);
        } catch (NumberFormatException e) {
            System.out.println("Formato de número inválido. Digite números válidos.");
        } catch (RuntimeException e) {
            if (e.getCause() instanceof DivisionByZeroException) {
                System.err.println("Erro de divisão por zero: " + e.getCause().getMessage());
            } else {
                System.err.println("Erro ao realizar divisão: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Erro ao realizar divisão: " + e.getMessage());
        }
    }
    
    private void showServerInfo() {
        try {
            String info = calculator.getServerInfo();
            System.out.println("Informações do Servidor:");
            System.out.println(info);
        } catch (Exception e) {
            System.err.println("Erro ao obter informações do servidor: " + e.getMessage());
        }
    }
    
    private void performBatchOperations() {
        System.out.println("Executando operações em lote para demonstrar CORBA...");
        
        try {
            // Demonstrar várias operações em sequência
            double[] numbers = {10.0, 5.0, 3.0, 2.0};
            
            System.out.println("\nDemo de Operações em Lote:");
            System.out.println("==========================");
            
            // Operações aritméticas básicas
            double sum = calculator.add(numbers[0], numbers[1]);
            System.out.printf("%.1f + %.1f = %.2f%n", numbers[0], numbers[1], sum);
            
            double diff = calculator.subtract(numbers[0], numbers[1]);
            System.out.printf("%.1f - %.1f = %.2f%n", numbers[0], numbers[1], diff);
            
            double product = calculator.multiply(numbers[0], numbers[1]);
            System.out.printf("%.1f * %.1f = %.2f%n", numbers[0], numbers[1], product);
            
            double quotient = calculator.divide(numbers[0], numbers[1]);
            System.out.printf("%.1f / %.1f = %.2f%n", numbers[0], numbers[1], quotient);
            
            // Operações mais complexas
            System.out.println("\nOperações Complexas:");
            double complexResult = calculator.add(
                calculator.multiply(numbers[0], numbers[1]),
                calculator.divide(numbers[2], numbers[3])
            );
            System.out.printf("(%.1f * %.1f) + (%.1f / %.1f) = %.2f%n", 
                numbers[0], numbers[1], numbers[2], numbers[3], complexResult);
            
            // Teste de divisão por zero
            System.out.println("\nTestando Divisão por Zero:");
            try {
                calculator.divide(numbers[0], 0.0);
            } catch (RuntimeException e) {
                System.out.println("Exceção capturada como esperado: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("Erro nas operações em lote: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        try (Scanner inputScanner = new Scanner(System.in)) {
            System.out.println("=== Cliente CORBA Calculator ===");
            System.out.println("Configure a conexão com o servidor:");
            
            // Solicitar IP do servidor
            String serverHost = "localhost";
            System.out.print("Digite o IP do servidor CORBA (ou Enter para localhost): ");
            String inputHost = inputScanner.nextLine().trim();
            if (!inputHost.isEmpty()) {
                serverHost = inputHost;
            }
            
            // Solicitar porta do servidor
            int serverPort = DEFAULT_PORT;
            System.out.print("Digite a porta do servidor (ou Enter para " + DEFAULT_PORT + "): ");
            String inputPort = inputScanner.nextLine().trim();
            if (!inputPort.isEmpty()) {
                try {
                    serverPort = Integer.parseInt(inputPort);
                } catch (NumberFormatException e) {
                    System.err.println("Porta inválida. Usando porta padrão " + DEFAULT_PORT);
                }
            }
            
            System.out.println();
            System.out.println("=== Configuração de Conexão ===");
            System.out.printf("Servidor: %s:%d%n", serverHost, serverPort);
            System.out.println("===============================");
            System.out.println();
            
            System.out.println("Iniciando Cliente CORBA Calculator...");
            System.out.printf("Conectando ao servidor: %s:%d%n", serverHost, serverPort);
            
            try {
                CalculatorClient client = new CalculatorClient(serverHost, serverPort);
                client.start();
            } catch (Exception e) {
                System.err.println("Falha ao iniciar cliente: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
