package com.hospital.appointmentservice.auth.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


import java.time.Instant;

public class LoginAuditDto {
    private Long id;

    @Size(max = 100)
    private String username;

    //    LOGIN_SUCCESS, LOGOUT, LOGIN_FAILED, FIRST_LOGIN
    @Size(max = 50)
    private String event;

    @NotNull
    private Instant timestamp;

    @Size(max = 50)
    private String ipAddress;

    private String userAgent;

    public LoginAuditDto() {
    }

    public LoginAuditDto(Long id, String username, String event, Instant timestamp, String ipAddress, String userAgent) {
        this.id = id;
        this.username = username;
        this.event = event;
        this.timestamp = timestamp;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }

    public LoginAuditDto(String username, String event, Instant timestamp, String ipAddress, String userAgent) {
        this.username = username;
        this.event = event;
        this.timestamp = timestamp;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
