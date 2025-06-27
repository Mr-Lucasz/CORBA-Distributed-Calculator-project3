package com.project3.server;

import com.project3.corba.ICalculator;
import com.project3.corba.CalculatorImpl;
import com.project3.corba.DivisionByZeroException;
import com.project3.protocol.CorbaRequest;
import com.project3.protocol.CorbaResponse;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Servidor CORBA que simula o Object Request Broker (ORB)
 * Aceita conexões de clientes e processa requisições remotas
 */
public class CalculatorServer {
    private static final int DEFAULT_PORT = 12345;
    private final int port;
    private final ICalculator calculator;
    private ServerSocket serverSocket;
    private boolean isRunning = false;
    private final ExecutorService threadPool;
    
    public CalculatorServer(int port) {
        this.port = port;
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            System.out.printf("[SERVER] Initializing server on %s (%s):%d%n", hostName, hostAddress, port);
            this.calculator = new CalculatorImpl(hostAddress, port);
        } catch (UnknownHostException e) {
            throw new RuntimeException("Failed to get host information", e);
        }
        this.threadPool = Executors.newFixedThreadPool(10);
    }
    
    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        isRunning = true;
        
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        System.out.printf("%n=== CORBA Calculator Server Started ===%n");
        System.out.printf("Host: %s%n", hostAddress);
        System.out.printf("Port: %d%n", port);
        System.out.printf("IOR: corba:///%s:%d/Calculator%n", hostAddress, port);
        System.out.printf("Status: Ready to accept client connections%n");
        System.out.println("==========================================");
        
        while (isRunning) {
            try {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(new ClientHandler(clientSocket));
            } catch (IOException e) {
                if (isRunning) {
                    System.err.println("[SERVER] Error accepting client connection: " + e.getMessage());
                }
            }
        }
    }
    
    public void stop() {
        isRunning = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("[SERVER] Error closing server socket: " + e.getMessage());
        }
        threadPool.shutdown();
        System.out.println("[SERVER] Server stopped.");
    }
    
    /**
     * Handler para processar requisições de clientes individuais
     */
    private class ClientHandler implements Runnable {
        private final Socket clientSocket;
        
        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }
        
        @Override
        public void run() {
            String clientAddress = clientSocket.getRemoteSocketAddress().toString();
            System.out.printf("[SERVER] New client connected: %s%n", clientAddress);
            
            try (ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
                 ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream())) {
                
                while (!clientSocket.isClosed()) {
                    try {
                        CorbaRequest request = (CorbaRequest) input.readObject();
                        System.out.printf("[SERVER] Processing request from %s: %s%n", 
                            request.getClientId(), request);
                        
                        CorbaResponse response = processRequest(request);
                        output.writeObject(response);
                        output.flush();
                        
                    } catch (EOFException e) {
                        // Cliente desconectou
                        break;
                    } catch (ClassNotFoundException e) {
                        System.err.printf("[SERVER] Error deserializing request from %s: %s%n", 
                            clientAddress, e.getMessage());
                        break;
                    } catch (Exception e) {
                        System.err.printf("[SERVER] Error processing request from %s: %s%n", 
                            clientAddress, e.getMessage());
                        CorbaResponse errorResponse = new CorbaResponse(e);
                        output.writeObject(errorResponse);
                        output.flush();
                    }
                }
                
            } catch (IOException e) {
                System.err.printf("[SERVER] Error handling client %s: %s%n", 
                    clientAddress, e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                    System.out.printf("[SERVER] Client disconnected: %s%n", clientAddress);
                } catch (IOException e) {
                    System.err.println("[SERVER] Error closing client socket: " + e.getMessage());
                }
            }
        }
        
        private CorbaResponse processRequest(CorbaRequest request) {
            try {
                String methodName = request.getMethodName();
                Object[] params = request.getParameters();
                
                switch (methodName) {
                    case "add":
                        double addResult = calculator.add((Double) params[0], (Double) params[1]);
                        return new CorbaResponse(addResult);
                        
                    case "subtract":
                        double subResult = calculator.subtract((Double) params[0], (Double) params[1]);
                        return new CorbaResponse(subResult);
                        
                    case "multiply":
                        double mulResult = calculator.multiply((Double) params[0], (Double) params[1]);
                        return new CorbaResponse(mulResult);
                        
                    case "divide":
                        double divResult = calculator.divide((Double) params[0], (Double) params[1]);
                        return new CorbaResponse(divResult);
                        
                    case "getServerInfo":
                        String info = calculator.getServerInfo();
                        return new CorbaResponse(info);
                        
                    default:
                        throw new IllegalArgumentException("Unknown method: " + methodName);
                }
                
            } catch (DivisionByZeroException e) {
                return new CorbaResponse(e);
            } catch (Exception e) {
                return new CorbaResponse(e);
            }
        }
    }
    
    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number. Using default port " + DEFAULT_PORT);
            }
        }
        
        CalculatorServer server = new CalculatorServer(port);
        
        // Shutdown hook para parar o servidor graciosamente
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("%n[SERVER] Shutting down server...");
            server.stop();
        }));
        
        try {
            server.start();
        } catch (IOException e) {
            System.err.println("[SERVER] Failed to start server: " + e.getMessage());
            System.exit(1);
        }
    }
}
