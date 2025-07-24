package com.hospital.appointmentservice.admin.model;

import com.hospital.appointmentservice.auth.model.UserAccount;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Staff {
    @Id
    @Column(name = "staff_id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "staff_id")
    private UserAccount userAccount;


    @Nationalized
    @Column(name = "full_name")
    private String fullName;

    @Size(max = 10)
    @Nationalized
    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "dob")
    private LocalDate dob;

    @Nationalized
    @Lob
    @Column(name = "email")
    private String email;

    @Size(max = 20)
    @Nationalized
    @Column(name = "phone", length = 20)
    private String phone;

    @Size(max = 50)
    @Nationalized
    @Column(name = "role", length = 50)
    private String role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;

    @Size(max = 50)
    @Nationalized
    @Column(name = "status", length = 50)
    private String status;

    @OneToOne(mappedBy = "staff")
    private Doctor doctor;

    @OneToOne(mappedBy = "staff")
    private Receptionist receptionist;

}