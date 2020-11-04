package com.robert.android.unioviscope.domain.model.types;

import android.os.Parcel;
import android.os.Parcelable;

public enum TemporalitySubjectType implements Parcelable {

	FIRST_SEMESTER, SECOND_SEMESTER;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(ordinal());
	}

	public static final Creator<TemporalitySubjectType> CREATOR = new Creator<TemporalitySubjectType>() {
		@Override
		public TemporalitySubjectType createFromParcel(Parcel in) {
			return TemporalitySubjectType.values()[in.readInt()];
		}

		@Override
		public TemporalitySubjectType[] newArray(int size) {
			return new TemporalitySubjectType[size];
		}
	};
}
