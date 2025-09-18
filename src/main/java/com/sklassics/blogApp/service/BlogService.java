package com.sklassics.blogApp.service;

import com.sklassics.blogApp.entity.Blog;
import com.sklassics.blogApp.entity.Section;
import com.sklassics.blogApp.entity.User;
import com.sklassics.blogApp.repository.BlogRepository;
import com.sklassics.blogApp.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    public BlogService(BlogRepository blogRepository, UserRepository userRepository) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
    }

    // ✅ get all blogs newest first
    public List<Map<String, Object>> getAllBlogsWithAuthorNames() {
        List<Blog> blogs = blogRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "id"));
        List<Map<String, Object>> result = new ArrayList<>();

        for (Blog blog : blogs) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", blog.getId());
            map.put("title", blog.getTitle());
            map.put("cardImage", blog.getCardImage());
            map.put("sections", blog.getSections());
            map.put("authorId", blog.getAuthor().getId());
            map.put("authorName", blog.getAuthor().getUsername());
            result.add(map);
        }
        return result;
    }

    // ✅ get blog by ID
    public Blog getBlogById(Long id) {
        return blogRepository.findById(id).orElse(null);
    }

    // ✅ create blog
    public Blog createBlog(String title, String cardImage, List<String> sectionTypes, List<String> sectionContents, Long authorId) {
        if (sectionTypes == null || sectionTypes.isEmpty()) {
            throw new RuntimeException("Blog must have at least one section");
        }

        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setCardImage(cardImage);

        List<Section> sections = new ArrayList<>();
        for (int i = 0; i < sectionTypes.size(); i++) {
            String type = sectionTypes.get(i);
            String content = sectionContents.get(i);
            String size = "heading".equals(type) ? "h2" : null;
            sections.add(new Section(type, content, size));
        }
        blog.setSections(sections);

        userRepository.findById(authorId).ifPresent(blog::setAuthor);

        return blogRepository.save(blog);
    }

    // ✅ update blog (keep old image if none)
    public boolean updateBlog(Long id, String title, String cardImage, List<String> sectionTypes, List<String> sectionContents) {
        Optional<Blog> blogOpt = blogRepository.findById(id);
        if (blogOpt.isPresent()) {
            Blog blog = blogOpt.get();
            blog.setTitle(title);
            if (cardImage != null && !cardImage.isEmpty()) {
                blog.setCardImage(cardImage);
            }

            List<Section> sections = new ArrayList<>();
            if (sectionTypes != null) {
                for (int i = 0; i < sectionTypes.size(); i++) {
                    String type = sectionTypes.get(i);
                    String content = sectionContents.get(i);
                    String size = "heading".equals(type) ? "h2" : null;
                    sections.add(new Section(type, content, size));
                }
                blog.setSections(sections);
            }

            blogRepository.save(blog);
            return true;
        }
        return false;
    }

    // ✅ delete blog + remove uploaded image
    public boolean deleteBlog(Long id) {
        Optional<Blog> blogOpt = blogRepository.findById(id);
        if (blogOpt.isPresent()) {
            Blog blog = blogOpt.get();
            try {
                if (blog.getCardImage() != null) {
                    Files.deleteIfExists(Paths.get("src/main/resources/static/" + blog.getCardImage()));
                }
            } catch (Exception e) {
                // ignore
            }
            blogRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ✅ get blogs by author
    public List<Blog> getBlogsByAuthor(Long authorId) {
        return blogRepository.findByAuthorId(authorId);
    }
}