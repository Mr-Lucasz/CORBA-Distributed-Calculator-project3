package com.project3.protocol;

import java.io.Serializable;

/**
 * Classe que representa uma requisição CORBA
 * Simula o empacotamento de chamadas de método remoto
 */
public class CorbaRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String methodName;
    private Object[] parameters;
    private String clientId;
    private long timestamp;
    
    public CorbaRequest(String methodName, Object[] parameters, String clientId) {
        this.methodName = methodName;
        this.parameters = parameters;
        this.clientId = clientId;
        this.timestamp = System.currentTimeMillis();
    }
    
    public String getMethodName() {
        return methodName;
    }
    
    public Object[] getParameters() {
        return parameters;
    }
    
    public String getClientId() {
        return clientId;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return String.format("CorbaRequest{method='%s', client='%s', timestamp=%d}", 
            methodName, clientId, timestamp);
    }
}
