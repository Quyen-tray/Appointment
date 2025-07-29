package com.hospital.appointmentservice.doctor.service.Iplm;

import com.hospital.appointmentservice.admin.model.Appointment;
import com.hospital.appointmentservice.admin.model.Receptionist;
import com.hospital.appointmentservice.admin.model.Staff;
import com.hospital.appointmentservice.auth.dto.UserAccountDto;
import com.hospital.appointmentservice.auth.model.UserAccount;
import com.hospital.appointmentservice.auth.repository.UserAccountRepository;
import com.hospital.appointmentservice.doctor.dto.DoctorDetailDto;
import com.hospital.appointmentservice.doctor.service.DoctorAppointmentService;
import com.hospital.appointmentservice.patient.dto.PatientDto;
import com.hospital.appointmentservice.patient.repository.AppointmentRepository;
import com.hospital.appointmentservice.receptionist.dto.AppointmentResponse;
import com.hospital.appointmentservice.receptionist.dto.ReceptionistDto;
import com.hospital.appointmentservice.receptionist.dto.RoomDto;
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
public class DoctorAppointmentServiceImpl implements DoctorAppointmentService {
    AppointmentRepository appointmentRepository;
    UserAccountRepository userAccountRepository;

    @Override
    public List<AppointmentResponse> getTodayAppointmentsForDoctor(String userName) {
        UserAccount user = userAccountRepository.findUserAccountByUsername(userName);

        LocalDate today = LocalDate.now(); // Ngày hôm nay (theo system time)
        LocalDateTime startOfDay = today.atStartOfDay(); // 00:00
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        List<Appointment> appointments = appointmentRepository.findAppointmentsByDoctorAndDay(user.getStaff().getDoctor().getId(), startOfDay, endOfDay);

        return appointments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateDoctorAppointment(UUID appointmentId, String note) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if(appointment.isPresent()) {

            appointmentRepository.save(appointment.get());
        }
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
            patientDto.setPhone(a.getPatient().getPhone());
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
}
