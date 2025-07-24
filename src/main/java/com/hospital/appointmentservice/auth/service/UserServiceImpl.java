package com.hospital.appointmentservice.auth.service;

import com.hospital.appointmentservice.auth.dto.ResetPassDTO;
import com.hospital.appointmentservice.auth.model.UserAccount;
import com.hospital.appointmentservice.auth.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void resetPassword(String userName, ResetPassDTO requestDto) {
        UserAccount user = userAccountRepository.findUserAccountByUsername(userName);

        String currentEndCodedPassword = user.getPasswordHash();

        // check oldpasswork
        if (!passwordEncoder.matches(requestDto.getOldPassword(), currentEndCodedPassword)) {
            throw new IllegalArgumentException("Mật khẩu cũ không đúng!");
        }

        // check input blank
        String newPassword = requestDto.getNewPassword();
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("Mật khẩu mới được bỏ trống!");
        }

        // check newpassword must diffirent oldpassword
        if (requestDto.getOldPassword().equals(newPassword)) {
            throw new IllegalArgumentException("Mật khẩu mới không được trùng mật khẩu cũ!");
        }

        // check strong newpassword
        if (!newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            throw new IllegalArgumentException(
                    "Mật khẩu mới phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt!");
        }


        // update password
        String newEncodedPassword = passwordEncoder.encode(newPassword);
        user.setPasswordHash(newEncodedPassword);
        userAccountRepository.save(user);
    }
}
