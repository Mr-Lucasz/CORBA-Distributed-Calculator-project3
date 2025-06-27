package com.project3.corba;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Implementação da interface Calculator usando sockets para comunicação remota
 * Esta classe simula o comportamento do CORBA ORB (Object Request Broker)
 */
public class CalculatorImpl implements Calculator {
    private String serverInfo;
    
    public CalculatorImpl(String hostName, int port) {
        this.serverInfo = String.format("Calculator Server running on %s:%d (Started: %s)", 
            hostName, port, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
    
    @Override
    public double add(double a, double b) {
        double result = a + b;
        System.out.printf("[SERVER] Addition: %.2f + %.2f = %.2f%n", a, b, result);
        return result;
    }
    
    @Override
    public double subtract(double a, double b) {
        double result = a - b;
        System.out.printf("[SERVER] Subtraction: %.2f - %.2f = %.2f%n", a, b, result);
        return result;
    }
    
    @Override
    public double multiply(double a, double b) {
        double result = a * b;
        System.out.printf("[SERVER] Multiplication: %.2f * %.2f = %.2f%n", a, b, result);
        return result;
    }
    
    @Override
    public double divide(double a, double b) throws DivisionByZeroException {
        if (b == 0.0) {
            System.out.printf("[SERVER] Division by zero attempted: %.2f / %.2f%n", a, b);
            throw new DivisionByZeroException("Cannot divide by zero");
        }
        double result = a / b;
        System.out.printf("[SERVER] Division: %.2f / %.2f = %.2f%n", a, b, result);
        return result;
    }
    
    @Override
    public String getServerInfo() {
        System.out.println("[SERVER] Server info requested");
        return serverInfo;
    }
}
