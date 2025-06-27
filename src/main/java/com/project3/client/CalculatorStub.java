package com.project3.client;

import com.project3.corba.Calculator;
import com.project3.corba.DivisionByZeroException;
import com.project3.protocol.CorbaRequest;
import com.project3.protocol.CorbaResponse;

import java.io.*;
import java.net.*;

/**
 * Stub/Proxy do cliente CORBA
 * Esta classe representa o lado cliente do ORB
 * Encapsula a comunicação remota e torna transparente para o cliente
 */
public class CalculatorStub implements Calculator {
    private final String serverHost;
    private final int serverPort;
    private final String clientId;
    
    public CalculatorStub(String serverHost, int serverPort, String clientId) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.clientId = clientId;
    }
    
    @Override
    public double add(double a, double b) {
        return (Double) invokeRemoteMethod("add", new Object[]{a, b});
    }
    
    @Override
    public double subtract(double a, double b) {
        return (Double) invokeRemoteMethod("subtract", new Object[]{a, b});
    }
    
    @Override
    public double multiply(double a, double b) {
        return (Double) invokeRemoteMethod("multiply", new Object[]{a, b});
    }
    
    @Override
    public double divide(double a, double b) throws DivisionByZeroException {
        Object result = invokeRemoteMethod("divide", new Object[]{a, b});
        return (Double) result;
    }
    
    @Override
    public String getServerInfo() {
        return (String) invokeRemoteMethod("getServerInfo", new Object[]{});
    }
    
    /**
     * Método privado que encapsula toda a lógica de invocação remota
     * Simula o comportamento do ORB do lado cliente
     */
    private Object invokeRemoteMethod(String methodName, Object[] parameters) {
        try (Socket socket = new Socket(serverHost, serverPort);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {
            
            // Criar e enviar requisição
            CorbaRequest request = new CorbaRequest(methodName, parameters, clientId);
            System.out.printf("[CLIENT-%s] Invoking remote method: %s%n", clientId, methodName);
            
            output.writeObject(request);
            output.flush();
            
            // Receber resposta
            CorbaResponse response = (CorbaResponse) input.readObject();
            
            if (response.isSuccess()) {
                System.out.printf("[CLIENT-%s] Method %s executed successfully%n", clientId, methodName);
                return response.getResult();
            } else {
                Exception exception = response.getException();
                System.err.printf("[CLIENT-%s] Remote method %s failed: %s%n", 
                    clientId, methodName, exception.getMessage());
                
                // Re-throw specific exceptions
                if (exception instanceof DivisionByZeroException) {
                    throw new RuntimeException(exception);
                } else {
                    throw new RuntimeException("Remote method invocation failed", exception);
                }
            }
            
        } catch (IOException | ClassNotFoundException e) {
            String errorMsg = String.format("Failed to invoke remote method %s: %s", methodName, e.getMessage());
            System.err.printf("[CLIENT-%s] %s%n", clientId, errorMsg);
            throw new RuntimeException(errorMsg, e);
        }
    }
    
    /**
     * Testa a conectividade com o servidor
     */
    public boolean testConnection() {
        try {
            getServerInfo();
            return true;
        } catch (Exception e) {
            System.err.printf("[CLIENT-%s] Connection test failed: %s%n", clientId, e.getMessage());
            return false;
        }
    }
    
    /**
     * Retorna informações sobre a conexão
     */
    public String getConnectionInfo() {
        return String.format("Connected to CORBA server at %s:%d (Client ID: %s)", 
            serverHost, serverPort, clientId);
    }
}
