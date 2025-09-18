package com.sklassics.blogApp.controller;

import com.sklassics.blogApp.entity.Blog;
import com.sklassics.blogApp.service.BlogService;
import com.sklassics.blogApp.util.FileUploadUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/blogs")
public class BlogRestController {

    private final BlogService blogService;

    public BlogRestController(BlogService blogService) {
        this.blogService = blogService;
    }

    // 📋 Get all blogs (newest first)
    @GetMapping
    public ResponseEntity<?> getAllBlogs() {
        return ResponseEntity.ok(blogService.getAllBlogsWithAuthorNames());
    }

    // 📄 Get blog by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getBlogById(@PathVariable Long id) {
        Blog blog = blogService.getBlogById(id);
        if (blog != null) return ResponseEntity.ok(blog);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blog not found");
    }

    // 🧑‍💻 Get blogs by author
    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<Blog>> getBlogsByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(blogService.getBlogsByAuthor(authorId));
    }

    // ➕ Create blog
    @PostMapping
    public ResponseEntity<?> createBlog(@RequestParam String title,
                                        @RequestParam("card_image") MultipartFile cardImage,
                                        @RequestParam List<String> sectionTypes,
                                        @RequestParam List<String> sectionContents,
                                        @RequestParam Long authorId) {
        try {
            if (sectionTypes == null || sectionTypes.isEmpty()) {
                return ResponseEntity.badRequest().body("Blog must have at least one section");
            }

            String fileName = FileUploadUtil.cleanFileName(cardImage.getOriginalFilename());
            String uploadPath = "src/main/resources/static/uploads/";
            String savedPath = FileUploadUtil.saveFile(uploadPath, fileName, cardImage);

            Blog blog = blogService.createBlog(title, savedPath, sectionTypes, sectionContents, authorId);
            return ResponseEntity.status(HttpStatus.CREATED).body(blog);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image upload failed");
        }
    }

    // ✏️ Update blog
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBlog(@PathVariable Long id,
                                        @RequestParam String title,
                                        @RequestParam(value = "card_image", required = false) MultipartFile cardImage,
                                        @RequestParam(required = false) List<String> sectionTypes,
                                        @RequestParam(required = false) List<String> sectionContents) {
        try {
            String fileName = null;
            if (cardImage != null && !cardImage.isEmpty()) {
                fileName = FileUploadUtil.cleanFileName(cardImage.getOriginalFilename());
                String uploadPath = "src/main/resources/static/uploads/";
                FileUploadUtil.saveFile(uploadPath, fileName, cardImage);
            }

            boolean updated = blogService.updateBlog(id, title, fileName != null ? "uploads/" + fileName : "", sectionTypes, sectionContents);
            if (updated) return ResponseEntity.ok("Blog updated successfully");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blog not found");

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image upload failed");
        }
    }

    // ❌ Delete blog
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlog(@PathVariable Long id) {
        boolean deleted = blogService.deleteBlog(id);
        if (deleted) return ResponseEntity.ok("Blog deleted successfully");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blog not found");
    }
}