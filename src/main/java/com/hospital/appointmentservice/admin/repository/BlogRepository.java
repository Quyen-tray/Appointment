package com.hospital.appointmentservice.admin.repository;

import com.hospital.appointmentservice.admin.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BlogRepository extends JpaRepository<Blog, UUID> {
    List<Blog> findAll();


    Blog findBlogById(UUID id);
}
