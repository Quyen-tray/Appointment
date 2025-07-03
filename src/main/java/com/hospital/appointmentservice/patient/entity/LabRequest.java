package com.hospital.appointmentservice.patient.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "LabRequest") // Tên bảng chính xác theo DB, chữ hoa/chữ thường tùy cấu hình DB
public class LabRequest {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "lab_id", updatable = false, nullable = false)
    private UUID labId;

    // Quan hệ ManyToOne với MedicalVisit
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id", nullable = false)
    private MedicalVisit visit;

    @Column(name = "requested_by")
    private String requestedBy; // hoặc UUID nếu bạn muốn tham chiếu đến entity khác

    @Column(name = "room_id")
    private UUID roomId;

    @Column(name = "test_type")
    private String testType;

    @Column(name = "result")
    private String result;

    @Column(name = "status")
    private String status;

    // Có thể thêm constructor, builder… Lombok đã hỗ trợ
}
