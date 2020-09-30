package org.noear.solon.boot.smartsocket;

import org.noear.solon.core.SocketMessage;
import org.noear.solon.core.XContextEmpty;
import org.noear.solon.core.XMethod;
import org.smartboot.socket.transport.AioSession;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;

public class AioContext extends XContextEmpty {
    private InetSocketAddress _inetSocketAddress;
    private AioSession _session;
    private SocketMessage _message;

    public AioContext(AioSession session, SocketMessage message) {
        _session = session;
        _message = message;

        try {
            _inetSocketAddress = session.getRemoteAddress();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Object request() {
        return _session;
    }

    @Override
    public String ip() {
        if (_inetSocketAddress == null)
            return null;
        else
            return _inetSocketAddress.getAddress().getHostAddress();
    }

    @Override
    public boolean isMultipart() {
        return false;
    }

    @Override
    public String method() {
        return XMethod.SOCKET.name;
    }

    @Override
    public String protocol() {
        return "SOCKET";
    }

    @Override
    public URI uri() {
        if (_uri == null) {
            _uri = URI.create(url());
        }

        return _uri;
    }

    private URI _uri;

    @Override
    public String path() {
        return uri().getPath();
    }

    @Override
    public String url() {
        return _message.resourceDescriptor;
    }

    @Override
    public long contentLength() {
        if (_message.content == null) {
            return 0;
        } else {
            return _message.content.length;
        }
    }

    @Override
    public String contentType() {
        return headerMap().get("Content-Type");
    }

    @Override
    public InputStream bodyAsStream() throws IOException {
        return new ByteArrayInputStream(_message.content);
    }

    //==============

    @Override
    public Object response() {
        return _session;
    }

    @Override
    public void contentType(String contentType) {
        headerSet("Content-Type", contentType);
    }


    ByteArrayOutputStream _outputStream = new ByteArrayOutputStream();

    @Override
    public OutputStream outputStream() {
        return _outputStream;
    }

    @Override
    public void output(byte[] bytes) {
        try {
            outputStream().write(bytes);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void output(InputStream stream) {
        try {
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = stream.read(buff, 0, 100)) > 0) {
                outputStream().write(buff, 0, rc);
            }

        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }


    @Override
    protected void commit() throws IOException {
        if (_session.isInvalid() == false) {
            SocketMessage msg =  SocketMessageUtils.wrap(_message.key, _message.resourceDescriptor, _outputStream.toByteArray());

            _session.writeBuffer().writeAndFlush(SocketMessageUtils.encode(msg).array());
        }
    }

    @Override
    public void close() throws IOException {
        _session.close();
    }
}
