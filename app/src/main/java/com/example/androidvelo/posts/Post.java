package com.example.androidvelo.posts;

public class Post {
    private String id;
    private String content;
    private String imageUrl; // Новое поле для хранения URL изображения

    public Post() {
        // Пустой конструктор требуется для использования метода setValue() в Firebase
    }

    public Post(String id, String content, String imageUrl) {
        this.id = id;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
