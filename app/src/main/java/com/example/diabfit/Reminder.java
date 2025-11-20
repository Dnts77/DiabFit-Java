package com.example.diabfit;


public class Reminder {

    private int id;
    private String description;
    private long reminderTime;
    private int pendingIntentId;


    public Reminder() {
    }


    public Reminder(int id, String description, long reminderTime, int pendingIntentId) {
        this.id = id;
        this.description = description;
        this.reminderTime = reminderTime;
        this.pendingIntentId = pendingIntentId;
    }

   //Getters & Setters: Indalécio Vibes
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(long reminderTime) {
        this.reminderTime = reminderTime;
    }

    public int getPendingIntentId() {
        return pendingIntentId;
    }

    public void setPendingIntentId(int pendingIntentId) {
        this.pendingIntentId = pendingIntentId;
    }
}
