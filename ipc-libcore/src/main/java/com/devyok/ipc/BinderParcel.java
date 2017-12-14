package com.devyok.ipc;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;


class BinderParcel implements Parcelable {

    private final IBinder mBinder;

    private BinderParcel(Parcel source) {
        mBinder = source.readStrongBinder();
    }

    public BinderParcel(IBinder binder) {
        this.mBinder = binder;
    }

    public IBinder getIbinder() {
        return mBinder;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStrongBinder(mBinder);
    }

    public static final Parcelable.Creator<BinderParcel> CREATOR = new Parcelable.Creator<BinderParcel>() {

        @Override
        public BinderParcel createFromParcel(Parcel source) {
            return new BinderParcel(source);
        }

        @Override
        public BinderParcel[] newArray(int size) {
            return new BinderParcel[size];
        }

    };

}
