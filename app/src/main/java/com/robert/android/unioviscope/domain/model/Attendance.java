package com.robert.android.unioviscope.domain.model;

import java.io.Serializable;
import java.util.Date;

public class Attendance implements Serializable {

	private Long id;
	private Boolean confirmed;
	private Date date;
	private String comment;
	private Boolean faceRecognitionOff;
	private Session session;

	// no-args constructor
	@SuppressWarnings("unused")
	Attendance() {

	}

	public Attendance(Long id) {
		this.id = id;
	}

	@SuppressWarnings("unused")
    public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
    public void setId(Long id) {
		this.id = id;
	}

	public Boolean isConfirmed() {
		return confirmed;
	}

    @SuppressWarnings("unused")
	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

    @SuppressWarnings("unused")
	public Date getDate() {
		return date != null ? (Date) date.clone() : null;
	}

    @SuppressWarnings("unused")
	public void setDate(Date date) {
		this.date = date;
	}

    @SuppressWarnings("unused")
	public String getComment() {
		return comment;
	}

    @SuppressWarnings("unused")
	public void setComment(String comment) {
		this.comment = comment;
	}

    @SuppressWarnings("unused")
	public Boolean isFaceRecognitionOff() {
		return faceRecognitionOff;
	}

    @SuppressWarnings("unused")
	public void setFaceRecognition(Boolean faceRecognition) {
		this.faceRecognitionOff = faceRecognition;
	}

	@SuppressWarnings("unused")
    public Session getSession() {
		return session;
	}
	
	@SuppressWarnings("unused")
    public void setSession(Session session) {
		this.session = session;
	}
}
