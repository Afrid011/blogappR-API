package com.sklassics.blogApp.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;

@Embeddable
public class Section {

    @Column(name = "section_type")
    private String type;

    @Column(name = "section_content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "section_size")
    private String size;

    public Section() {}

    public Section(String type, String content, String size) {
        this.type = type;
        this.content = content;
        this.size = size;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
}