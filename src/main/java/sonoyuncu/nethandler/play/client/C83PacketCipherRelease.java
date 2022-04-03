package sonoyuncu.nethandler.play.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import com.google.gson.JsonObject;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.world.WorldServer;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class C83PacketCipherRelease implements Packet<INetHandlerPlayServer> {

    private String saltyStr = "NettyIO";
    private byte[] salt = new byte[]{67, 118, -107, -57, 91, -41, 69, 23};
    private String mode = "PBEWithMD5AndDES";
    private SecretKey key = getSK(saltyStr);
    private Cipher cipher = initCipher(salt, key);

    /* sysinf & mcefInfo */
    private String channel;

    private byte[] data;

    public C83PacketCipherRelease() {
    }

    public C83PacketCipherRelease(final String channel, final byte[] data) {
        this.channel = channel;
        this.data = data;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException {
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer mn) throws IOException {
        mn.writeString(this.channel);
        final List<byte[]> channel = a(this.data);
        mn.writeInt(channel.get(1).length);
        mn.writeInt(channel.get(0).length);
        mn.writeBytes((byte[]) channel.get(0));
        mn.writeBytes((byte[]) channel.get(1));
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler) {

    }

    private SecretKey getSK(final String s) {
        try {
            return SecretKeyFactory.getInstance(mode).generateSecret(new PBEKeySpec(s.toCharArray()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private Cipher initCipher(final byte[] array, final SecretKey secretKey) {
        final int n = 42;
        try {
            final PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(array, n);
            final Cipher instance = Cipher.getInstance(mode);
            instance.init(1, secretKey, pbeParameterSpec);
            return instance;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<byte[]> a(final byte[] array) {
        try {
            final ArrayList<byte[]> list = new ArrayList<byte[]>();
            list.add(saltyStr.getBytes());
            list.add(cipher.doFinal(array));
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


}
