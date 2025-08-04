package com.hospital.appointmentservice.patient.service.iplm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import com.hospital.appointmentservice.patient.repository.AppointmentRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.appointmentservice.admin.model.Doctor;
import com.hospital.appointmentservice.doctor.repository.DoctorRepository;
import com.hospital.appointmentservice.patient.dto.AppointmentDto;
import com.hospital.appointmentservice.patient.dto.AppointmentRequestDto;
import com.hospital.appointmentservice.patient.dto.RelativeDto;
import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.patient.entity.Relative;
import com.hospital.appointmentservice.patient.repository.PatientRepository;
import com.hospital.appointmentservice.patient.repository.RelativeRepository;
import com.hospital.appointmentservice.patient.service.PatientAppointmentService;
import com.hospital.appointmentservice.admin.model.Appointment;
import com.hospital.appointmentservice.admin.model.Department;

@Service
public class PatientAppointmentServiceImpl implements PatientAppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public List<Appointment> getAppointmentsByPatientId(UUID patientid) {
        return appointmentRepository.findByPatient_Id(patientid);
    }

    @Override
    public Appointment getAppointmentById(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId).orElse(null);
    }

    @Override
    public Page<Appointment> getAppointmentsByPatientId(UUID patientId, String doctorName, LocalDate startDate,
            LocalDate endDate, String status, String examiner, Pageable pageable) {
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.plusDays(1).atStartOfDay() : null;
        return appointmentRepository.findByFilters(patientId, doctorName, startDateTime, endDateTime, status, examiner,
                pageable);
    }

    @Override
    public void saveAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private RelativeRepository relativeRepository;

    @Override
    public Appointment createAppointment(String username, AppointmentRequestDto dto) {
        Patient patient = patientRepository.findByUser_Username(username);
        if (patient == null) {
            throw new RuntimeException("Patient not found!");
        }

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found!"));

        Department department = doctor.getStaff().getDepartment();

        boolean hasPendingInSameDept;

        List<String> blockingStatuses = List.of("PENDING", "APPROVED");

        if (dto.getRelativeId() != null) {
            hasPendingInSameDept = appointmentRepository
                    .existsByRelative_IdAndDoctor_Staff_DepartmentAndStatusIn(
                            dto.getRelativeId(), department, blockingStatuses);
        } else {
            hasPendingInSameDept = appointmentRepository
                    .existsByPatient_IdAndDoctor_Staff_DepartmentAndStatusIn(
                            patient.getId(), department, blockingStatuses);
        }

        // kiểm tra bệnh nhân đã có lịch tại thời điểm này chưa (ở bất kỳ khoa nào)
        boolean hasSameTime;

        if (dto.getRelativeId() != null) {
            hasSameTime = appointmentRepository
                    .existsByRelative_IdAndScheduledTime(dto.getRelativeId(), dto.getScheduledTime());
        } else {
            hasSameTime = appointmentRepository
                    .existsByPatient_IdAndScheduledTime(patient.getId(), dto.getScheduledTime());
        }

        if (hasSameTime) {
            if (dto.getRelativeId() != null) {
                throw new RuntimeException("Người thân của bạn đã có lịch hẹn khác vào thời điểm này!");
            } else {
                throw new RuntimeException("Bạn đã có lịch hẹn khác vào thời điểm này!");
            }
        }

        if (hasPendingInSameDept) {
            if (dto.getRelativeId() != null) {
                throw new RuntimeException(
                        "Người thân của bạn đã có lịch hẹn chưa khám ở khoa này. Vui lòng hoàn tất trước khi đặt tiếp.");
            } else {
                throw new RuntimeException(
                        "Bạn đã có lịch hẹn chưa khám ở khoa này. Vui lòng hoàn tất trước khi đặt tiếp.");
            }
        }

        boolean exists = appointmentRepository.existsByDoctorAndScheduledTime(doctor, dto.getScheduledTime());
        if (exists) {
            throw new RuntimeException("Bác sĩ đã có lịch hẹn vào giờ này!");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setScheduledTime(dto.getScheduledTime());
        appointment.setStatus("PENDING");
        appointment.setReason(dto.getReason());
        appointment.setCreatedBy(patient.getUser());
        appointment.setCreatedRole("PATIENT");

        if (dto.getRelativeId() != null) {
            Relative relative = relativeRepository.findByIdAndPatient_User_Username(dto.getRelativeId(), username)
                    .orElseThrow(() -> new RuntimeException("Relative not found!"));
            appointment.setRelative(relative);
        }

        appointmentRepository.save(appointment);
        return appointment;
    }

    @Override
    public AppointmentDto getAppointmentDetailById(UUID appointmentId, String username) {
        Patient patient = patientRepository.findByUser_Username(username);
        if (patient == null) {
            throw new RuntimeException("Không tìm thấy bệnh nhân!");
        }
        Appointment appointment = getAppointmentById(appointmentId);

        if (appointment == null) {
            throw new RuntimeException("Không tìm thấy lịch hẹn!");
        }

        if (!appointment.getPatient().getId().equals(patient.getId())) {
            throw new RuntimeException("Bạn không có quyền xem lịch hẹn này!");
        }

        AppointmentDto dto = new AppointmentDto();
        dto.setId(appointment.getId());
        dto.setPatientName(patient.getFullName());
        dto.setDoctorName(appointment.getDoctor().getFullName());
        dto.setRoomName(appointment.getRoom() != null ? appointment.getRoom().getName() : "Chưa có phòng!");
        dto.setScheduledTime(appointment.getScheduledTime().toString());
        dto.setStatus(appointment.getStatus());

        if (appointment.getRelative() != null) {
            dto.setPatientName(appointment.getRelative().getFullName());

            RelativeDto relativeDto = new RelativeDto();
            relativeDto.setId(appointment.getRelative().getId());
            relativeDto.setFullName(appointment.getRelative().getFullName());

            dto.setRelative(relativeDto);
        } else {
            dto.setPatientName(patient.getFullName());
            dto.setRelative(null);
        }

        return dto;
    }

    @Override
    public void updateScheduledTime(UUID appointmentId, UUID patientId, LocalDateTime newTime) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn!"));
        if (!appointment.getPatient().getId().equals(patientId)) {
            throw new RuntimeException("Không có quyền sửa lịch hẹn này!");
        }

        if (newTime == null || newTime.isBefore(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")))) {
            throw new RuntimeException("Thời gian không hợp lệ");
        }

        appointment.setScheduledTime(newTime);
        appointmentRepository.save(appointment);
    }
}
