package com.hospital.appointmentservice.receptionist.service;

import com.hospital.appointmentservice.receptionist.dto.RoomDto;
import com.hospital.appointmentservice.admin.model.Appointment;
import com.hospital.appointmentservice.admin.model.Receptionist;
import com.hospital.appointmentservice.admin.model.Staff;
import com.hospital.appointmentservice.receptionist.repository.RoomReceptionRepository;
import com.hospital.appointmentservice.receptionist.repository.StaffRepository;
import com.hospital.appointmentservice.auth.dto.UserAccountDto;
import com.hospital.appointmentservice.auth.model.UserAccount;
import com.hospital.appointmentservice.auth.repository.UserAccountRepository;
import com.hospital.appointmentservice.doctor.dto.DoctorDetailDto;
import com.hospital.appointmentservice.doctor.repository.DoctorRepository;
import com.hospital.appointmentservice.patient.dto.PatientDto;
import com.hospital.appointmentservice.patient.repository.AppointmentRepository;
import com.hospital.appointmentservice.patient.repository.PatientRepository;
import com.hospital.appointmentservice.receptionist.dto.AppointmentDTO;
import com.hospital.appointmentservice.receptionist.dto.AppointmentResponse;
import com.hospital.appointmentservice.receptionist.dto.ReceptionistDto;
import com.hospital.appointmentservice.receptionist.dto.UpdateStatusAppointmentDto;
import com.hospital.appointmentservice.receptionist.service.impl.IAppointmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentService implements IAppointmentService {
    AppointmentRepository appointmentRepository;
    PatientRepository patientRepository;
    DoctorRepository doctorRepository;
    RoomReceptionRepository roomRepository;
    UserAccountRepository userAccountRepository;
    StaffRepository staffRepository;

    @Override
    public List<AppointmentResponse> getAppointments(String keyword, String status, Boolean isIncreaseScheduleDate) {

        // Gọi hàm tìm kiếm
        List<Appointment> appointments = appointmentRepository.searchAppointments(
                (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null,
                (status != null && !status.trim().isEmpty()) ? status.trim() : null,
                isIncreaseScheduleDate
        );

        return appointments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }



    @Override
    public AppointmentResponse getAppointmentById(UUID id) {
        return mapToResponse(appointmentRepository.findById(id).get());
    }
    public AppointmentResponse mapToResponse(Appointment a) {
        AppointmentResponse dto = new AppointmentResponse();
        dto.setId(a.getId());

        // Map Patient
        if (a.getPatient() != null) {
            PatientDto patientDto = new PatientDto();
            patientDto.setId(a.getPatient().getId().toString());
            patientDto.setFullName(a.getPatient().getFullName());
            patientDto.setGender(a.getPatient().getGender());
            dto.setPatient(patientDto);
        }

        // Map Doctor
        if (a.getDoctor() != null) {
            DoctorDetailDto doctorDto = new DoctorDetailDto();
            doctorDto.setId(a.getDoctor().getId());
            doctorDto.setFullName(a.getDoctor().getFullName());
            doctorDto.setSpecialty(a.getDoctor().getSpecialization());
            dto.setDoctor(doctorDto);
        }

        // Map Room
        if (a.getRoom() != null) {
            RoomDto roomDto = new RoomDto();
            roomDto.setId(a.getRoom().getId());
            roomDto.setRoomName(a.getRoom().getRoomName());
            roomDto.setRoomType(a.getRoom().getRoomType());
            dto.setRoom(roomDto);
        }

        // Map các thông tin còn lại
        dto.setScheduledTime(a.getScheduledTime());
        dto.setStatus(a.getStatus());
        dto.setCreatedRole(a.getCreatedRole());
        dto.setApprovalStatus(a.getApprovalStatus());
        dto.setApprovedAt(a.getApprovedAt());

        // Map CreatedBy
        if (a.getCreatedBy() != null) {
            UserAccountDto createdByDto = new UserAccountDto();
            createdByDto.setUsername(a.getCreatedBy().getUsername());
            dto.setCreatedBy(createdByDto);
        }

        // Map ApprovedBy
        if (a.getApprovedBy() != null) {
            ReceptionistDto approvedByDto = new ReceptionistDto();
            approvedByDto.setId(a.getApprovedBy().getId());
            approvedByDto.setFullName(a.getApprovedBy().getStaff().getFullName());
            dto.setApprovedBy(approvedByDto);
        }

        return dto;
    }
    @Override
    public String createAppointment(AppointmentDTO createAppointmentDTO, String username) {
        UserAccount user = userAccountRepository.findUserAccountByUsername(username);
        Appointment appointment = new Appointment();
        return auditAppointment(createAppointmentDTO, appointment, true, user.getId());
    }

    @Override
    public String updateAppointment(UUID id, AppointmentDTO updateAppointmentDTO, String username
    ) {
        // Find appointment by id
        // If not found, throw exception
        UserAccount user = userAccountRepository.findUserAccountByUsername(username);
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with id: " + id));
        return auditAppointment(updateAppointmentDTO, appointment, false, user.getId());
    }

    @Override
    public String deleteAppointment(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with id: " + id));
        appointmentRepository.delete(appointment);
        return "Delete success";
    }

    @Override
    public void updateStatusAppointment(UUID id, UpdateStatusAppointmentDto body, String username) {
        UserAccount user =  userAccountRepository.findUserAccountByUsername(username);
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        Staff staff = staffRepository.findByUserAccount(user);
        Receptionist receptionist = staff.getReceptionist();
        if(appointment.isPresent() && user != null) {
            if(body.getStatus().equals("APPROVED")){
                appointment.get().setApprovalStatus("APPROVED");
                appointment.get().setApprovedBy(receptionist);
                ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
                Instant vietnamNow = LocalDateTime.now(zoneId).atZone(zoneId).toInstant();

                appointment.get().setApprovedAt(vietnamNow);
            }
            appointment.get().setStatus(body.getStatus());
            appointmentRepository.save(appointment.get());
        }
    }

//    Staff staff = user.getStaff();
//    Receptionist receptionist = staff.getReceptionist();
//    Optional<Appointment> appointment = appointmentRepository.findById(id);
//
//        if(appointment.isPresent() && user != null) {
//        if(body.getStatus().equals("APPROVED")){
//            appointment.get().setApprovalStatus("APPROVED");
//            appointment.get().setApprovedBy(receptionist);
//            appointment.get().setApprovedAt(Instant.now());
//        }
//        appointment.get().setStatus(body.getStatus());
//        appointmentRepository.save(appointment.get());
//    }
//    @Override
//    public Appointment changeStatus(UUID id, String status) {
//        Appointment appointment = appointmentRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));
//        appointment.setStatus(status);
//        return appointmentRepository.save(appointment);
//    }

    private String auditAppointment(AppointmentDTO appointmentDTO, Appointment appointment, boolean isCreate, UUID id) {
        appointment.setPatient(patientRepository.findById(appointmentDTO.getPatientId()).orElse(null));
        appointment.setDoctor(doctorRepository.findById(appointmentDTO.getDoctorId()).orElse(null));
        appointment.setRoom(roomRepository.findById(appointmentDTO.getRoomId()).orElse(null));
        LocalDateTime scheduledTime = LocalDateTime.ofInstant(
                appointmentDTO.getScheduledTime(),
                ZoneId.of("Asia/Ho_Chi_Minh")
        );

        appointment.setScheduledTime(scheduledTime);

        if (isCreate) {
            appointment.setStatus("PENDING");
            appointment.setCreatedBy(userAccountRepository.findById(id).orElse(null));
            appointment.setCreatedRole(appointmentDTO.getCreatedRole());
        }

         appointmentRepository.save(appointment);
        return appointment.getId().toString();
    }



}
