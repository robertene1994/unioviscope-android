package com.robert.android.unioviscope.domain.model.types;

import android.os.Parcel;
import android.os.Parcelable;

public enum SubjectType implements Parcelable {

	BASIC_FORMATION, OBLIGATORY, OPTIONAL, FINAL_PROJECT, EXTERNAL_PRACTICES;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(ordinal());
	}

	public static final Creator<SubjectType> CREATOR = new Creator<SubjectType>() {
		@Override
		public SubjectType createFromParcel(Parcel in) {
			return SubjectType.values()[in.readInt()];
		}

		@Override
		public SubjectType[] newArray(int size) {
			return new SubjectType[size];
		}
	};
}
