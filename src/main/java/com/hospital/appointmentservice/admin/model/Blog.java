package com.hospital.appointmentservice.admin.model;

import com.hospital.appointmentservice.auth.model.UserAccount;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="Blog")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "blog_id", nullable = false)
    private UUID id;

    @Column(name = "post_data", columnDefinition = "NVARCHAR(MAX)")
    private String postData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @Column(name = "image", length = 256)
    private String image;

    @Column(name = "date_create")
    private LocalDateTime dateCreate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "title", length = 256)
    private String title;
}
