package com;

public class WorkOrder {
    private int id;
    private String description;
    private String senderName;
    private Status status;
    private static int currentID = 5000;

    public WorkOrder() {
        id = currentID;
        currentID+=10;
    }

    public WorkOrder(String description, String senderName) {
        this.description = description;
        this.senderName = senderName;
        id = currentID;
        currentID++;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}