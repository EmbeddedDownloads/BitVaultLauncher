/*
 * Copyright (C) 2014 The Android Open Source Project
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
package com.google.android.exoplayer.extractor;

import android.os.Environment;
import android.util.Log;

import com.google.android.exoplayer.C;
import com.google.android.exoplayer.upstream.DataSource;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An {@link ExtractorInput} that wraps a {@link DataSource}.
 */
public final class DefaultExtractorInput implements ExtractorInput {

    private static final byte[] SCRATCH_SPACE = new byte[4096];

    private final DataSource dataSource;
    private final long streamLength;

    private long position;
    private byte[] peekBuffer;
    private int peekBufferPosition;
    private int peekBufferLength;
    private static List<byte[]> listOfStreamByte = new ArrayList<byte[]>();
    private File mRecordingFile = null;

    /**
     * @param dataSource The wrapped {@link DataSource}.
     * @param position   The initial position in the stream.
     * @param length     The length of the stream, or {@link C#LENGTH_UNBOUNDED} if it is unknown.
     */
    public DefaultExtractorInput(DataSource dataSource, long position, long length) {
        this.dataSource = dataSource;
        this.position = position;
        this.streamLength = length;
        peekBuffer = new byte[8 * 1024];
    }

    @Override
    public int read(byte[] target, int offset, int length) throws IOException, InterruptedException {
        int bytesRead = readFromPeekBuffer(target, offset, length);
        if (bytesRead == 0) {
            bytesRead = readFromDataSource(target, offset, length, 0, true);

        }
        commitBytesRead(bytesRead);
        return bytesRead;
    }

    @Override
    public boolean readFully(byte[] target, int offset, int length, boolean allowEndOfInput)
            throws IOException, InterruptedException {
        int bytesRead = readFromPeekBuffer(target, offset, length);
        while (bytesRead < length && bytesRead != C.RESULT_END_OF_INPUT) {
            bytesRead = readFromDataSource(target, offset, length, bytesRead, allowEndOfInput);
        }
        /**
         * Recording Method
         */
//        getStreamBuffer(target);

        commitBytesRead(bytesRead);
        return bytesRead != C.RESULT_END_OF_INPUT;
    }

    @Override
    public void readFully(byte[] target, int offset, int length)
            throws IOException, InterruptedException {
        readFully(target, offset, length, false);
    }

    @Override
    public int skip(int length) throws IOException, InterruptedException {
        int bytesSkipped = skipFromPeekBuffer(length);
        if (bytesSkipped == 0) {
            bytesSkipped =
                    readFromDataSource(SCRATCH_SPACE, 0, Math.min(length, SCRATCH_SPACE.length), 0, true);
        }
        commitBytesRead(bytesSkipped);
        return bytesSkipped;
    }

    @Override
    public boolean skipFully(int length, boolean allowEndOfInput)
            throws IOException, InterruptedException {
        int bytesSkipped = skipFromPeekBuffer(length);
        while (bytesSkipped < length && bytesSkipped != C.RESULT_END_OF_INPUT) {
            bytesSkipped = readFromDataSource(SCRATCH_SPACE, -bytesSkipped,
                    Math.min(length, bytesSkipped + SCRATCH_SPACE.length), bytesSkipped, allowEndOfInput);
        }
        commitBytesRead(bytesSkipped);
        return bytesSkipped != C.RESULT_END_OF_INPUT;
    }

    @Override
    public void skipFully(int length) throws IOException, InterruptedException {
        skipFully(length, false);
    }

    @Override
    public boolean peekFully(byte[] target, int offset, int length, boolean allowEndOfInput)
            throws IOException, InterruptedException {
        if (!advancePeekPosition(length, allowEndOfInput)) {
            return false;
        }
        System.arraycopy(peekBuffer, peekBufferPosition - length, target, offset, length);
        return true;
    }

    @Override
    public void peekFully(byte[] target, int offset, int length)
            throws IOException, InterruptedException {
        peekFully(target, offset, length, false);
    }

    @Override
    public boolean advancePeekPosition(int length, boolean allowEndOfInput)
            throws IOException, InterruptedException {
        ensureSpaceForPeek(length);
        int bytesPeeked = Math.min(peekBufferLength - peekBufferPosition, length);
        peekBufferLength += length - bytesPeeked;
        while (bytesPeeked < length) {
            bytesPeeked = readFromDataSource(peekBuffer, peekBufferPosition, length, bytesPeeked,
                    allowEndOfInput);
            if (bytesPeeked == C.RESULT_END_OF_INPUT) {
                return false;
            }
        }
        peekBufferPosition += length;
        return true;
    }

    @Override
    public void advancePeekPosition(int length) throws IOException, InterruptedException {
        advancePeekPosition(length, false);
    }

    @Override
    public void resetPeekPosition() {
        peekBufferPosition = 0;
    }

    @Override
    public long getPeekPosition() {
        return position + peekBufferPosition;
    }

    @Override
    public long getPosition() {
        return position;
    }

    @Override
    public long getLength() {
        return streamLength;
    }

    /**
     * Ensures {@code peekBuffer} is large enough to store at least {@code length} bytes from the
     * current peek position.
     */
    private void ensureSpaceForPeek(int length) {
        int requiredLength = peekBufferPosition + length;
        if (requiredLength > peekBuffer.length) {
            peekBuffer = Arrays.copyOf(peekBuffer, Math.max(peekBuffer.length * 2, requiredLength));
        }
    }

    /**
     * Skips from the peek buffer.
     *
     * @param length The maximum number of bytes to skip from the peek buffer.
     * @return The number of bytes skipped.
     */
    private int skipFromPeekBuffer(int length) {
        int bytesSkipped = Math.min(peekBufferLength, length);
        updatePeekBuffer(bytesSkipped);
        return bytesSkipped;
    }

    /**
     * Reads from the peek buffer
     *
     * @param target A target array into which data should be written.
     * @param offset The offset into the target array at which to write.
     * @param length The maximum number of bytes to read from the peek buffer.
     * @return The number of bytes read.
     */
    private int readFromPeekBuffer(byte[] target, int offset, int length) {
        int peekBytes = 0;
        try {
            if (peekBufferLength == 0) {
                return 0;
            }
            peekBytes = Math.min(peekBufferLength, length);
            System.arraycopy(peekBuffer, 0, target, offset, peekBytes);
            updatePeekBuffer(peekBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return peekBytes;
    }

    /**
     * Updates the peek buffer's length, position and contents after consuming data.
     *
     * @param bytesConsumed The number of bytes consumed from the peek buffer.
     */
    private void updatePeekBuffer(int bytesConsumed) {
        peekBufferLength -= bytesConsumed;
        peekBufferPosition = 0;
        System.arraycopy(peekBuffer, bytesConsumed, peekBuffer, 0, peekBufferLength);
    }

    /**
     * Starts or continues a read from the data source.
     *
     * @param target           A target array into which data should be written.
     * @param offset           The offset into the target array at which to write.
     * @param length           The maximum number of bytes to read from the input.
     * @param bytesAlreadyRead The number of bytes already read from the input.
     * @param allowEndOfInput  True if encountering the end of the input having read no data is
     *                         allowed, and should result in {@link C#RESULT_END_OF_INPUT} being returned. False if it
     *                         should be considered an error, causing an {@link EOFException} to be thrown.
     * @return The total number of bytes read so far, or {@link C#RESULT_END_OF_INPUT} if
     * {@code allowEndOfInput} is true and the input has ended having read no bytes.
     * @throws EOFException         If the end of input was encountered having partially satisfied the read
     *                              (i.e. having read at least one byte, but fewer than {@code length}), or if no bytes were
     *                              read and {@code allowEndOfInput} is false.
     * @throws IOException          If an error occurs reading from the input.
     * @throws InterruptedException If the thread is interrupted.
     */
    private int readFromDataSource(byte[] target, int offset, int length, int bytesAlreadyRead,
                                   boolean allowEndOfInput) throws InterruptedException, IOException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        int bytesRead = dataSource.read(target, offset + bytesAlreadyRead, length - bytesAlreadyRead);
        if (bytesRead == C.RESULT_END_OF_INPUT) {
            if (bytesAlreadyRead == 0 && allowEndOfInput) {
                return C.RESULT_END_OF_INPUT;
            }
            throw new EOFException();
        }


        return bytesAlreadyRead + bytesRead;
    }

    /**
     * Advances the position by the specified number of bytes read.
     *
     * @param bytesRead The number of bytes read.
     */
    private void commitBytesRead(int bytesRead) {
        if (bytesRead != C.RESULT_END_OF_INPUT) {
            position += bytesRead;
        } else {

        }
    }


    /**
     * Process by which we can store casted video into local storage
     *
     * @param mStreamByte
     * @Anshuman
     */
    private void getStreamBuffer(byte[] mStreamByte) {
        try {
            if (mRecordingFile == null)
                mRecordingFile = new File(Environment.getExternalStorageDirectory(), "DVDO_01.mp4");
            if (!mRecordingFile.exists()) {
                mRecordingFile.createNewFile();
            }
            writeToFile(mStreamByte);
            /**
             * Old Flow for HLS Video only
             */
            FileOutputStream fos = new FileOutputStream(mRecordingFile, true);
            fos.write(mStreamByte);
            fos.close();
            /**
             *Now Store Byte array into Arraylist
             */
            listOfStreamByte.add(mStreamByte);

            Log.d("DEBUG", "Buffer :" + mStreamByte.length + "  List Size :" + listOfStreamByte.size());

//            if (listOfStreamByte.size() == 8000) {
//                Log.d("DEBUG", "File Byte Stored Successfully. Now Process to make video from it.");
            File mresultFile = new File(Environment.getExternalStorageDirectory(), "DVDO_02.mp4");
            if (!mresultFile.exists())
                mresultFile.createNewFile();
//                byte[] mResultData = concatenateByteArrays(listOfStreamByte);
            FileOutputStream out = new FileOutputStream(mresultFile, true);
            out.write(mStreamByte);
            out.close();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(byte[] data) {

        try {
            String filename = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "DVDO_04.txt";
            File mresultFile = new File(filename);
            if (!mresultFile.exists())
                mresultFile.createNewFile();
            FileWriter fw = new FileWriter(mresultFile, true);
            fw.write(data.toString() + "\n\n");
            fw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
//        try {
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_APPEND));
//            outputStreamWriter.write(data);
//            outputStreamWriter.close();
//        }
//        catch (IOException e) {
//            Log.e("Exception", "File write failed: " + e.toString());
//        }
    }

//    private void writeInFile(byte[] target) {
//        try {
//            fos = new FileOutputStream(mFiel, true);
//            fos.write(target);
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//    }

    public byte[] concatenateByteArrays(List<byte[]> blocks) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        for (byte[] b : blocks) {
            os.write(b, 0, b.length);
        }
        return os.toByteArray();
    }
}