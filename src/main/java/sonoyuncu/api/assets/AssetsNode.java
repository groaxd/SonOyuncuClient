package sonoyuncu.api.assets;

import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import org.apache.commons.io.IOUtils;
import sonoyuncu.api.assets.Assets;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class AssetsNode {
    public String name;
    public Map<String, Assets> assetsMap;
    public String hash;

    public AssetsNode(final String name) {
        this.name = name;
        this.assetsMap = Maps.newHashMap();
    }


    public Assets getAssets(final int n, final int n2) {
        return this.assetsMap.get(n + ":" + n2);
    }

    public void setAssets(final int n, final int n2, final Assets jp) {
        this.assetsMap.put(n + ":" + n2, jp);
    }

    public void calculate(final File file) throws IOException {
        this.assetsMap = Maps.newHashMap();
        InputStream input = null;
        DataInputStream input2 = null;
        try {
            input = new FileInputStream(file);
            input2 = new DataInputStream(input);
            final byte[] array = new byte[input2.readInt()];
            input2.read(array, 0, array.length);
            this.name = new String(array, StandardCharsets.UTF_8);
            int n = input2.readInt();
            for (long i = 0L; i < n; ++i) {
                final int int1 = input2.readInt();
                final int int2 = input2.readInt();
                final byte[] array2 = new byte[input2.readInt()];
                input2.read(array2, 0, array2.length);
                this.setAssets(int1, int2, new Assets(array2, input2.readInt()));
            }
        } finally {
            IOUtils.closeQuietly(input2);
            IOUtils.closeQuietly(input);
            this.hash = Files.hash(file, Hashing.sha1()).toString();
        }
    }


}
