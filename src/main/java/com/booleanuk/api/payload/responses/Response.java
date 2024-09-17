package com.booleanuk.api.payload.responses;

public class Response<T> {
    protected String status;
    protected T data;

    public void set(T data){
        this.status = "success";
        this.data = data;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
