package com.robert.android.unioviscope.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.robert.android.unioviscope.domain.model.types.SubjectType;
import com.robert.android.unioviscope.domain.model.types.TemporalitySubjectType;

import java.io.Serializable;

public class Subject implements Parcelable, Serializable {

	private static final long serialVersionUID = 7344544060380624750L;

	private Long id;
	private String code;
	private String denomination;
	private Integer course;
	private TemporalitySubjectType temporality;
	private SubjectType type;
	private Double credits;

    // no-args constructor
    @SuppressWarnings("unused")
	Subject() {

	}

    public Subject(Long id) {
        this.id = id;
    }

    // parcel constructor
    @SuppressWarnings("unused")
    private Subject(Parcel in) {
        readFromParcel(in);
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    @SuppressWarnings("unused")
	public String getCode() {
		return code;
	}

	public String getDenomination() {
		return denomination;
	}

    @SuppressWarnings("unused")
	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}

    @SuppressWarnings("unused")
	public Integer getCourse() {
		return course;
	}

    @SuppressWarnings("unused")
	public void setCourse(Integer course) {
		this.course = course;
	}

    @SuppressWarnings("unused")
	public TemporalitySubjectType getTemporality() {
		return temporality;
	}

    @SuppressWarnings("unused")
	public void setTemporality(TemporalitySubjectType temporality) {
		this.temporality = temporality;
	}

    @SuppressWarnings("unused")
	public SubjectType getType() {
		return type;
	}

    @SuppressWarnings("unused")
	public void setType(SubjectType type) {
		this.type = type;
	}

    @SuppressWarnings("unused")
	public Double getCredits() {
		return credits;
	}

    @SuppressWarnings("unused")
	public void setCredits(Double credits) {
		this.credits = credits;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Subject other = (Subject) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(code);
        dest.writeString(denomination);
        dest.writeInt(course);
        dest.writeParcelable(temporality, flags);
        dest.writeParcelable(type, flags);
        dest.writeDouble(credits);
    }

    private void readFromParcel(Parcel in) {
        id = in.readLong();
        code = in.readString();
        denomination = in.readString();
        course = in.readInt();
        temporality = in.readParcelable(TemporalitySubjectType.class.getClassLoader());
        type = in.readParcelable(SubjectType.class.getClassLoader());
        credits = in.readDouble();

    }

    public static final Creator<Subject> CREATOR = new Creator<Subject>() {
        public Subject createFromParcel(Parcel in) {
            return new Subject(in);
        }

        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };
}
