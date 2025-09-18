package com.sklassics.blogApp.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "blogs")
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String cardImage;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ElementCollection
    @CollectionTable(name = "blog_sections", joinColumns = @JoinColumn(name = "blog_id"))
    private List<Section> sections;

    public Blog() {}

    public Blog(String title, String cardImage, User author, List<Section> sections) {
        this.title = title;
        this.cardImage = cardImage;
        this.author = author;
        this.sections = sections;
    }

    // Getters and Setters
    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCardImage() { return cardImage; }
    public void setCardImage(String cardImage) { this.cardImage = cardImage; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public List<Section> getSections() { return sections; }
    public void setSections(List<Section> sections) { this.sections = sections; }
}