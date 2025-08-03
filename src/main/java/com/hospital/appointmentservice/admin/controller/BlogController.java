package com.hospital.appointmentservice.admin.controller;

import com.hospital.appointmentservice.admin.dto.AuditBlogDto;
import com.hospital.appointmentservice.admin.dto.BlogDto;
import com.hospital.appointmentservice.admin.model.Blog;
import com.hospital.appointmentservice.admin.service.BlogService;
import com.hospital.appointmentservice.auth.dto.UserAccountDto;
import com.hospital.appointmentservice.auth.security.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/blogs")
// Annotation gen constructor
@RequiredArgsConstructor
// Annotation make field private and final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogController {
    JwtUtil jwtUtil;
    BlogService blogService;

    @GetMapping
    public ResponseEntity<?> listBlogs() {
        try {
            List<BlogDto> blogs = blogService.getAllBlogs();
            return ResponseEntity.status(HttpStatus.OK).body(blogs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errors in get blogs process" + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBlogDetail(@PathVariable UUID id) {
        try {
            BlogDto blogs = blogService.getBlogById(id);
            return ResponseEntity.status(HttpStatus.OK).body(blogs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errors in get blogsprocess" + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createNewBlog(@RequestBody AuditBlogDto newBlog, HttpServletRequest request) {
        try {
            String authorization = request.getHeader("Authorization");
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
            }

            String token = authorization.substring("Bearer ".length());
            Claims userClaims = jwtUtil.getAllClaims(token);
            String username = userClaims.getSubject();
            blogService.createBlog(newBlog, username);
            return ResponseEntity.status(HttpStatus.OK).body("Update blog successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errors in get blog process" + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBlog(
            @PathVariable UUID id, @RequestBody AuditBlogDto updateBlog, HttpServletRequest request) {
        try {
            blogService.updateBlog(id,updateBlog);
            return ResponseEntity.status(HttpStatus.OK).body("Update blog successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errors in get blog process" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> listBlogs(
            @PathVariable UUID id) {
        try {
            blogService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body("Delete blog successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errors in get blogs process" + e.getMessage());
        }
    }
}
