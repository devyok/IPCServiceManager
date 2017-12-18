package com.devyok.ipc;


import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.IBinder;
/**
 * @author DengWei
 */
class BinderCusrsor extends MatrixCursor {

	public static final String BINDER_CURSOR = "BinderCusrsor";
	final Bundle mBinderExtra = new Bundle();

	private static final String[] DEFAULT_COLUMNS = { "ID" };

	static final BinderCusrsor create(IBinder binder) {

		return new BinderCusrsor(DEFAULT_COLUMNS, binder);
	}
	
	private BinderCusrsor(String[] columnNames, IBinder binder) {
		super(columnNames);
		mBinderExtra.putParcelable(BINDER_CURSOR, new BinderParcel(binder));
	}

	@Override
	public Bundle getExtras() {
		return mBinderExtra;
	}

}
