package com.hospital.appointmentservice.admin.service;


import com.hospital.appointmentservice.admin.dto.AuditBlogDto;
import com.hospital.appointmentservice.admin.dto.BlogDto;
import java.util.List;
import java.util.UUID;

public interface BlogService {
    List<BlogDto> getAllBlogs();
    BlogDto getBlogById(UUID id);
    void createBlog(AuditBlogDto blog, String userName);
//    String storeImage(MultipartFile file, Long id);
    void updateBlog(UUID blogId,AuditBlogDto p);
    void delete(UUID id);
}
