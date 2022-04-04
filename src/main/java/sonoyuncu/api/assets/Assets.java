package sonoyuncu.api.assets;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Assets {
    public byte[] array;
    public int size;

    public Assets(final byte[] array, final int size) {
        this.array = array;
        this.size = size;
    }

    public byte[] size() {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.array);
            GzipCompressorInputStream gzipCompressorInputStream = new GzipCompressorInputStream((InputStream) byteArrayInputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            IOUtils.copy((InputStream) gzipCompressorInputStream, (OutputStream) byteArrayOutputStream);
            byteArrayOutputStream.flush();
            IOUtils.closeQuietly((InputStream) gzipCompressorInputStream);
            IOUtils.closeQuietly((InputStream) byteArrayInputStream);
            IOUtils.closeQuietly((OutputStream) byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception exception) {
            exception.printStackTrace();
            return new byte[256];
        }
    }

}
