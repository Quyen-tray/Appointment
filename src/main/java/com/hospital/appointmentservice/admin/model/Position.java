package com.hospital.appointmentservice.admin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"Position\"")
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "position_id", nullable = false)
    private UUID id;

    @Size(max = 100)
    @Nationalized
    @Column(name = "title", length = 100)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "rank")
    private Integer rank;

    @Nationalized
    @Lob
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "position")
    private Set<com.hospital.appointmentservice.admin.model.Staff> staff = new LinkedHashSet<>();

}