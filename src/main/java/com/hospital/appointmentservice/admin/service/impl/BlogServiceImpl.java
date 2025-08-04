package com.hospital.appointmentservice.admin.service.impl;

import com.hospital.appointmentservice.admin.dto.AuditBlogDto;
import com.hospital.appointmentservice.admin.dto.BlogDto;
import com.hospital.appointmentservice.admin.model.Blog;
import com.hospital.appointmentservice.admin.repository.BlogRepository;
import com.hospital.appointmentservice.admin.service.BlogService;
import com.hospital.appointmentservice.auth.model.UserAccount;
import com.hospital.appointmentservice.auth.repository.UserAccountRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogServiceImpl implements BlogService {
    BlogRepository blogRepository;
    UserAccountRepository userAccountRepository;

    @Value("${file.upload-dir}")
    @NonFinal// Đường dẫn đến thư mục lưu trữ tệp ảnh (được cấu hình trong application.properties)
    String uploadDir;

    @Override
    public List<BlogDto> getAllBlogs() {
        List<Blog> blogs = blogRepository.findAll();
        return blogs.stream()
                .sorted(Comparator.comparing(Blog::getDateCreate).reversed()) // DESC
                .map(this::mapResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BlogDto getBlogById(UUID id) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        return mapResponse(blog);
    }

    @Override
    public void createBlog(AuditBlogDto auditBlog, String userName) {
        Blog blog = new Blog();
        blog.setImage(auditBlog.getImage());
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        UserAccount user = userAccountRepository.findUserAccountByUsername(userName);

        blog.setDateCreate(currentTime);
        blog.setTitle(auditBlog.getTitle());
        blog.setPostData(auditBlog.getPostData());
        blog.setUser(user);
        blogRepository.save(blog);
    }

//    @Override
//    public String storeImage(MultipartFile file, Long id) {
//        try {
//            // Tạo đường dẫn đến thư mục lưu trữ tệp ảnh
//            String fileName = "image post " + String.valueOf(id) + "." + FilenameUtils.getExtension(file.getOriginalFilename());
//            System.out.println(id);
//            Path targetPath = Paths.get(uploadDir + "/posts", fileName);
//
//            // Lưu tệp ảnh vào thư mục lưu trữ
//            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
//
//            // Trả về đường dẫn tới tệp ảnh vừa tải lên
//
//            return uploadDir + "/" + file.getOriginalFilename();
//        } catch (IOException e) {
//            e.printStackTrace();
//
//        }
//        return "Error when save image";
//    }

    @Override
    public void updateBlog(UUID blogId, AuditBlogDto updateBlog) {
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        Blog blog = blogRepository.findBlogById(blogId);
        if (blog == null) {
            throw new RuntimeException("Blog not found");
        }
        blog.setUpdateDate(currentTime);
        blog.setImage(updateBlog.getImage());
        blog.setPostData(updateBlog.getPostData());
        blog.setTitle(updateBlog.getTitle());
        blogRepository.save(blog);
    }

    @Override
    public void delete(UUID id) {
        blogRepository.deleteById(id);
    }

    private BlogDto mapResponse(Blog blog) {
        BlogDto dto = new BlogDto();
        dto.setId(blog.getId());
        dto.setImage(blog.getImage());
        dto.setTitle(blog.getTitle());
        dto.setDateCreate(blog.getDateCreate());
        dto.setPostData(blog.getPostData());
        dto.setUpdateDate(blog.getUpdateDate());
        return dto;
    }
}
