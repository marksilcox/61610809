package com.example.SO61610809;

import org.springframework.integration.ip.tcp.serializer.ByteArrayLengthHeaderSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Int32ArrayLengthHeaderSerializer extends ByteArrayLengthHeaderSerializer {

    @Override
    protected int readHeader(InputStream inputStream) throws IOException {
        byte[] size = new byte[HEADER_SIZE_INT];
        int read = inputStream.read(size, 0, HEADER_SIZE_INT);
        if (read < 0) {
            return 0;
        }
        int messageLength = ByteBuffer.wrap(size).order(ByteOrder.LITTLE_ENDIAN).getInt();
        if (messageLength < 0) {
            throw new IllegalArgumentException(String.format("Length header: %d is negative", messageLength));
        }
        return messageLength;
    }

    @Override
    protected void writeHeader(OutputStream outputStream, int length) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE_INT).order(ByteOrder.LITTLE_ENDIAN).putInt(length);
        outputStream.write(buffer.array());
    }
}
