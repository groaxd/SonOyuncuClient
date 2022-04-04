package sonoyuncu.impl.assets;

import com.google.common.collect.Maps;
import sonoyuncu.api.assets.AssetsNode;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author Groax
 * @since 4/3/2022
 */
public class Manager {
    public static Map<String, AssetsNode> assetsMap = Maps.newConcurrentMap();
    public static String current = "cl"; /* Default: cl */

    public static boolean cancel(java.lang.String string, java.io.File file) {
        if (string.startsWith("so:lwc/")) {
            getAssetsNode(file);
            return true;
        }
        return false;
    }

    public static void getAssetsNode(final File file) {
        final AssetsNode assetsNode = new AssetsNode(null);
        try {
            assetsNode.calculate(file);
            assetsMap.put(assetsNode.name, assetsNode);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static AssetsNode getAssetsNode(String var0) {
        return assetsMap.get(var0);
    }


    public static AssetsNode getDefaultAssetsNode() {
        return getAssetsNode(current);
    }
}
