package com.example.androidvelo.event;

public class Event {

    private String nameEvent;
    private String eventId;
    private String description;
    private String distance;
    private String address;
    private String imageUrl; // Новое поле для URL изображения

    public Event() {
        // Необходимый конструктор по умолчанию для вызовов DataSnapshot.getValue(Event.class)
    }

    public Event(String nameEvent, String description, String distance, String address, String imageUrl) {
        this.nameEvent = nameEvent;
        this.description = description;
        this.distance = distance;
        this.address = address;
        this.imageUrl = imageUrl; // Инициализация нового поля
    }

    public String getNameEvent() {
        return nameEvent;
    }

    public void setNameEvent(String nameEvent) {
        this.nameEvent = nameEvent;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
