/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.os;

import static android.system.OsConstants.AF_UNIX;
import static android.system.OsConstants.SEEK_SET;
import static android.system.OsConstants.SOCK_STREAM;
import static android.system.OsConstants.S_ISLNK;
import static android.system.OsConstants.S_ISREG;

import android.system.OsConstants;
import android.system.StructStat;
import android.util.Log;

import dalvik.system.CloseGuard;

import libcore.io.IoUtils;

import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.nio.ByteOrder;

/**
 * The FileDescriptor returned by {@link Parcel#readFileDescriptor}, allowing
 * you to close it when done with it.
 */
// A minimal implementation to keep native methods alive
public class ParcelFileDescriptor implements Parcelable, Closeable
{

	@Override
	public int describeContents()
	{
		// TODO: Implement this method
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		// TODO: Implement this method
	}

	@Override
	public void close() throws IOException
	{
		// TODO: Implement this method
	}

    private static final String TAG = "ParcelFileDescriptor";

    private final FileDescriptor mFd;

    /**
     * Optional socket used to communicate close events, status at close, and
     * detect remote process crashes.
     */
    private FileDescriptor mCommFd;

    /**
     * Wrapped {@link ParcelFileDescriptor}, if any. Used to avoid
     * double-closing {@link #mFd}.
     */
    private final ParcelFileDescriptor mWrapped;

    /**
     * Maximum {@link #mStatusBuf} size; longer status messages will be
     * truncated.
     */
    private static final int MAX_STATUS = 1024;

    /**
     * Temporary buffer used by {@link #readCommStatus(FileDescriptor, byte[])},
     * allocated on-demand.
     */
    private byte[] mStatusBuf;

    private volatile boolean mClosed;

    private final CloseGuard mGuard = CloseGuard.get();

    /**
     * For use with {@link #open}: if {@link #MODE_CREATE} has been supplied and
     * this file doesn't already exist, then create the file with permissions
     * such that any application can read it.
     *
     * @deprecated Creating world-readable files is very dangerous, and likely
     *             to cause security holes in applications. It is strongly
     *             discouraged; instead, applications should use more formal
     *             mechanism for interactions such as {@link ContentProvider},
     *             {@link BroadcastReceiver}, and {@link android.app.Service}.
     *             There are no guarantees that this access mode will remain on
     *             a file, such as when it goes through a backup and restore.
     */
    @Deprecated
    public static final int MODE_WORLD_READABLE = 0x00000001;

    /**
     * For use with {@link #open}: if {@link #MODE_CREATE} has been supplied and
     * this file doesn't already exist, then create the file with permissions
     * such that any application can write it.
     *
     * @deprecated Creating world-writable files is very dangerous, and likely
     *             to cause security holes in applications. It is strongly
     *             discouraged; instead, applications should use more formal
     *             mechanism for interactions such as {@link ContentProvider},
     *             {@link BroadcastReceiver}, and {@link android.app.Service}.
     *             There are no guarantees that this access mode will remain on
     *             a file, such as when it goes through a backup and restore.
     */
    @Deprecated
    public static final int MODE_WORLD_WRITEABLE = 0x00000002;

    /**
     * For use with {@link #open}: open the file with read-only access.
     */
    public static final int MODE_READ_ONLY = 0x10000000;

    /**
     * For use with {@link #open}: open the file with write-only access.
     */
    public static final int MODE_WRITE_ONLY = 0x20000000;

    /**
     * For use with {@link #open}: open the file with read and write access.
     */
    public static final int MODE_READ_WRITE = 0x30000000;

    /**
     * For use with {@link #open}: create the file if it doesn't already exist.
     */
    public static final int MODE_CREATE = 0x08000000;

    /**
     * For use with {@link #open}: erase contents of file when opening.
     */
    public static final int MODE_TRUNCATE = 0x04000000;

    /**
     * For use with {@link #open}: append to end of file while writing.
     */
    public static final int MODE_APPEND = 0x02000000;

    /**
     * Create a new ParcelFileDescriptor wrapped around another descriptor. By
     * default all method calls are delegated to the wrapped descriptor.
     */
    public ParcelFileDescriptor(ParcelFileDescriptor wrapped) {
        // We keep a strong reference to the wrapped PFD, and rely on its
        // finalizer to trigger CloseGuard. All calls are delegated to wrapper.
        mWrapped = wrapped;
        mFd = null;
        mCommFd = null;
        mClosed = true;
    }

    /** {@hide} */
    public ParcelFileDescriptor(FileDescriptor fd) {
        this(fd, null);
    }

    /** {@hide} */
    public ParcelFileDescriptor(FileDescriptor fd, FileDescriptor commChannel) {
        if (fd == null) {
            throw new NullPointerException("FileDescriptor must not be null");
        }
        mWrapped = null;
        mFd = fd;
        mCommFd = commChannel;
        mGuard.open("close");
    }

    /**
     * Create a new ParcelFileDescriptor accessing a given file.
     *
     * @param file The file to be opened.
     * @param mode The desired access mode, must be one of
     *            {@link #MODE_READ_ONLY}, {@link #MODE_WRITE_ONLY}, or
     *            {@link #MODE_READ_WRITE}; may also be any combination of
     *            {@link #MODE_CREATE}, {@link #MODE_TRUNCATE},
     *            {@link #MODE_WORLD_READABLE}, and
     *            {@link #MODE_WORLD_WRITEABLE}.
     * @return a new ParcelFileDescriptor pointing to the given file.
     * @throws FileNotFoundException if the given file does not exist or can not
     *             be opened with the requested mode.
     * @see #parseMode(String)
     */
    public static ParcelFileDescriptor open(File file, int mode) throws FileNotFoundException {
        final FileDescriptor fd = null;
		// openInternal(file, mode);
        if (fd == null) return null;

        return new ParcelFileDescriptor(fd);
    }
}
