package com.robert.android.unioviscope.domain.model;

import java.io.Serializable;

public class AttendanceCertificate implements Serializable {

    private static final long serialVersionUID = 3009841805357377486L;

    private String userName;
    private String token;
    private Long scanned;

    // no-args constructor
    @SuppressWarnings("unused")
    AttendanceCertificate() {

    }

    public AttendanceCertificate(String userName, String token, Long scanned) {
        this.userName = userName;
        this.token = token;
        this.scanned = scanned;
    }

    @SuppressWarnings("unused")
    public String getUserName() {
        return userName;
    }

    @SuppressWarnings("unused")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @SuppressWarnings("unused")
    public String getToken() {
        return token;
    }

    @SuppressWarnings("unused")
    public void setToken(String token) {
        this.token = token;
    }

    @SuppressWarnings("unused")
    public Long getScanned() {
        return scanned;
    }

    @SuppressWarnings("unused")
    public void setScanned(Long scanned) {
        this.scanned = scanned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttendanceCertificate)) return false;
        AttendanceCertificate that = (AttendanceCertificate) o;
        return userName.equals(that.userName) && token.equals(that.token);
    }

    @Override
    public int hashCode() {
        int result = userName.hashCode();
        result = 31 * result + token.hashCode();
        return result;
    }
}
