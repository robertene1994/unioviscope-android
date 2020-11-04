package com.robert.android.unioviscope.domain.model;

import java.io.Serializable;
import java.util.Date;

public class Session implements Serializable {

	private static final long serialVersionUID = 3009841805357377486L;

	private Long id;
	private Date start;
	private Date end;
	private String location;
	private String description;
	private Attendance attendance;

	// no-args constructor
	@SuppressWarnings("unused")
	Session() {

	}

	public Session(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
    public void setId(Long id) {
		this.id = id;
	}

	public Date getStart() {
		return start != null ? (Date) start.clone() : null;
	}

    @SuppressWarnings("unused")
    public void setStart(Date start) {
        this.start = start;
    }

	public Date getEnd() {
		return end != null ? (Date) end.clone() : null;
	}

    @SuppressWarnings("unused")
	public void setEnd(Date end) {
		this.end = end;
	}

    @SuppressWarnings("unused")
	public String getLocation() {
		return location;
	}

    @SuppressWarnings("unused")
    public void setLocation(String location) {
        this.location = location;
    }

    @SuppressWarnings("unused")
	public String getDescription() {
		return description;
	}

    @SuppressWarnings("unused")
	public void setDescription(String description) {
		this.description = description;
	}

	public Attendance getAttendance() {
		return attendance;
	}

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Session)) return false;
        Session session = (Session) o;
        return id.equals(session.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
