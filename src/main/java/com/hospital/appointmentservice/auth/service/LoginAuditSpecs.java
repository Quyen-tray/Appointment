package com.hospital.appointmentservice.auth.service;

import com.hospital.appointmentservice.auth.model.Login_audit;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class LoginAuditSpecs {
    public static Specification<Login_audit> filter(String username, String event, String ip,
                                                    LocalDateTime from, LocalDateTime to) {
        Specification<Login_audit> spec = (root, query, cb) -> cb.conjunction(); // Always true

        if (username != null && !username.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%"));
        }

        if (event != null && !event.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("event"), event));
        }

        if (ip != null && !ip.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("ipAddress"), ip));
        }

        if (from != null && to != null) {
            spec = spec.and((root, query, cb) ->
                    cb.between(root.get("timestamp"), from, to));
        } else if (from != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("timestamp"), from));
        } else if (to != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("timestamp"), to));
        }

        return spec;
    }

    public static Specification<Login_audit> usernameContains(String username) {
        return (root, query, cb) -> {
            if (username == null || username.isBlank()) return null;
            return cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%");
        };
    }

    public static Specification<Login_audit> eventEquals(String event) {
        return (root, query, cb) -> {
            if (event == null || event.isBlank()) return null;
            return cb.equal(root.get("event"), event);
        };
    }

    public static Specification<Login_audit> ipEquals(String ip) {
        return (root, query, cb) -> {
            if (ip == null || ip.isBlank()) return null;
            return cb.equal(root.get("ipAddress"), ip);
        };
    }

    public static Specification<Login_audit> timestampBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> {
            if (from != null && to != null) {
                return cb.between(root.get("timestamp"), from, to);
            } else if (from != null) {
                return cb.greaterThanOrEqualTo(root.get("timestamp"), from);
            } else if (to != null) {
                return cb.lessThanOrEqualTo(root.get("timestamp"), to);
            }
            return null;
        };
    }
}
