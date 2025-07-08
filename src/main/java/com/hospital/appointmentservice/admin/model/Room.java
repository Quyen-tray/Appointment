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
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "room_id", nullable = false)
    private UUID id;

    @Nationalized
    @Lob
    @Column(name = "room_name")
    private String roomName;
    
    public String getName(){
        return roomName;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Size(max = 50)
    @Nationalized
    @Column(name = "room_type", length = 50)
    private String roomType;

    @OneToMany(mappedBy = "room")
    private Set<Appointment> appointments = new LinkedHashSet<>();

    //UPDATE
    @Column(name = "room_number")
    private String roomNumber;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "status")
    private String status;

    @Column(name = "description")
    private String description;

}