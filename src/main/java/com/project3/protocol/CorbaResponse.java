package com.project3.protocol;

import java.io.Serializable;

/**
 * Classe que representa uma resposta CORBA
 * Simula o retorno de chamadas de método remoto
 */
public class CorbaResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Object result;
    private Exception exception;
    private boolean success;
    private long timestamp;
    
    public CorbaResponse(Object result) {
        this.result = result;
        this.success = true;
        this.timestamp = System.currentTimeMillis();
    }
    
    public CorbaResponse(Exception exception) {
        this.exception = exception;
        this.success = false;
        this.timestamp = System.currentTimeMillis();
    }
    
    public Object getResult() {
        return result;
    }
    
    public Exception getException() {
        return exception;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        if (success) {
            return String.format("CorbaResponse{success=true, result=%s, timestamp=%d}", 
                result, timestamp);
        } else {
            return String.format("CorbaResponse{success=false, exception=%s, timestamp=%d}", 
                exception.getMessage(), timestamp);
        }
    }
}
