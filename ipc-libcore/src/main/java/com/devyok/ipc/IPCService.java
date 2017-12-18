package com.devyok.ipc;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
/**
 * @author DengWei
 */
public abstract class IPCService extends ContentProvider{

	public abstract IBinder getService(String selection);
	
	public abstract void onServiceCreate();

	@Override
	public boolean onCreate() {
		onServiceCreate();
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		IBinder binder = getService(selection);
		BinderCusrsor binderCursor = BinderCusrsor.create(binder);
		return binderCursor;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

}
