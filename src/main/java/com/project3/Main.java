package com.project3;

import com.project3.client.CalculatorClient;
import com.project3.server.CalculatorServer;
import com.project3.distributed.DistributedCalculatorCoordinator;

/**
 * Classe principal para executar o sistema CORBA distribuído
 * 
 * Uso:
 * java -cp target/classes com.project3.Main server [porta]
 * java -cp target/classes com.project3.Main client [host] [porta]
 * java -cp target/classes com.project3.Main distributed
 */
public class Main {
    private static final int DEFAULT_PORT = 12345;
    
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }
        
        String mode = args[0].toLowerCase();
        
        switch (mode) {
            case "server":
                startServer(args);
                break;
            case "client":
                startClient(args);
                break;
            case "distributed":
                startDistributedSystem(args);
                break;
            default:
                System.err.println("Modo inválido: " + mode);
                printUsage();
        }
    }
    
    private static void startServer(String[] args) {
        int port = DEFAULT_PORT;
        
        if (args.length > 1) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Número de porta inválido. Usando porta padrão " + DEFAULT_PORT);
            }
        }
        
        System.out.println("=== Iniciando Servidor CORBA Calculator ===");
        CalculatorServer.main(new String[]{String.valueOf(port)});
    }
    
    private static void startClient(String[] args) {
        System.out.println("=== Iniciando Cliente CORBA Calculator ===");
        CalculatorClient.main(new String[]{});
    }
    
    private static void startDistributedSystem(String[] args) {
        System.out.println("=== Iniciando Sistema Distribuído ===");
        try {
            DistributedCalculatorCoordinator coordinator = new DistributedCalculatorCoordinator();
            coordinator.start();
        } catch (Exception e) {
            System.err.println("Erro ao iniciar sistema distribuído: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void printUsage() {
        System.out.println("════════════════════════════════════════════════════════");
        System.out.println("           APLICAÇÃO CORBA CALCULATOR");
        System.out.println("════════════════════════════════════════════════════════");
        System.out.println("Uso:");
        System.out.println("  java -cp target/classes com.project3.Main server [porta]");
        System.out.println("  java -cp target/classes com.project3.Main client");
        System.out.println("  java -cp target/classes com.project3.Main distributed");
        System.out.println();
        System.out.println("Modos de Operação:");
        System.out.println("  server      - Inicia servidor CORBA em um host");
        System.out.println("  client      - Inicia cliente simples");
        System.out.println("  distributed - Inicia coordenador distribuído (RECOMENDADO)");
        System.out.println();
        System.out.println("Exemplos:");
        System.out.println("  java -cp target/classes com.project3.Main server");
        System.out.println("  java -cp target/classes com.project3.Main server 12345");
        System.out.println("  java -cp target/classes com.project3.Main client");
        System.out.println("  java -cp target/classes com.project3.Main distributed");
        System.out.println();
        System.out.println("Para demonstração no laboratório:");
        System.out.println("  1. Execute 'server' em 5+ hosts diferentes");
        System.out.println("  2. Execute 'distributed' no host coordenador");
        System.out.println("  3. Configure os IPs dos hosts servidores");
        System.out.println();
        System.out.println("Valores padrão:");
        System.out.println("  Porta do servidor: " + DEFAULT_PORT);
        System.out.println("════════════════════════════════════════════════════════");
    }
}