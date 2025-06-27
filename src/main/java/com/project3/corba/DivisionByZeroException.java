package com.project3.corba;

/**
 * Exception personalizada para divisão por zero
 */
public class DivisionByZeroException extends Exception {
    private String reason;
    
    public DivisionByZeroException(String reason) {
        super(reason);
        this.reason = reason;
    }
    
    public String getReason() {
        return reason;
    }
}
