package com.project3.corba;

/**
 * Interface que define as operações da calculadora
 * Esta interface representa o contrato definido no IDL
 */
public interface Calculator {
    double add(double a, double b);
    double subtract(double a, double b);
    double multiply(double a, double b);
    double divide(double a, double b) throws DivisionByZeroException;
    String getServerInfo();
}
