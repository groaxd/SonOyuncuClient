package sonoyuncu.nethandler.login.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.network.login.INetHandlerLoginServer;
import net.minecraft.util.CryptManager;
import net.minecraft.util.IChatComponent;
import com.google.common.base.*;
import java.io.*;
import java.security.PublicKey;

import net.minecraft.*;

import javax.crypto.SecretKey;

public class C80PacketSessionIDReceiver implements Packet<INetHandlerLoginServer> {
    private byte data;
    private byte[] secretKeyEncrypted;
    private byte[] verifyTokenEncrypted1;
    private byte[] verifyTokenEncrypted;
    private byte data0;
    private byte[] verifyTokenEncrypted0;

    public C80PacketSessionIDReceiver() {
        this.secretKeyEncrypted = new byte[0];
        this.verifyTokenEncrypted1 = new byte[0];
        this.verifyTokenEncrypted = new byte[0];
        this.verifyTokenEncrypted0 = new byte[0];
    }

    public C80PacketSessionIDReceiver(byte var1, SecretKey var2, PublicKey var3, byte[] var4, String var5, byte var6, String var7) {
        this(var1, var2, var3, var4, var5.getBytes(Charsets.UTF_8), var6, var7);
    }

    public C80PacketSessionIDReceiver(byte data, SecretKey secretKey, PublicKey publicKey, byte[] arrby, byte[] arrby2, byte data0, java.lang.String string) {
        this.secretKeyEncrypted = new byte[0];
        this.verifyTokenEncrypted1 = new byte[0];
        this.verifyTokenEncrypted = new byte[0];
        this.verifyTokenEncrypted0 = new byte[0];
        this.data = data;
        this.secretKeyEncrypted = CryptManager.encryptData((java.security.Key)publicKey, (byte[])secretKey.getEncoded());
        try {
            this.verifyTokenEncrypted1 = CryptManager.encryptData((java.security.Key)publicKey, (byte[])arrby);
            this.data = data0;
            this.verifyTokenEncrypted = CryptManager.encryptData((java.security.Key)secretKey, (byte[])arrby2);
            this.verifyTokenEncrypted0 = CryptManager.encryptData((java.security.Key)secretKey, (byte[])string.getBytes(com.google.common.base.Charsets.UTF_8));
        }
        catch (Exception nR2) {
            throw new RuntimeException("Session ID Alinamadi.");
        }
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
    }
    
    public void writePacketData(PacketBuffer var1) throws IOException
    {
        var1.writeByte(this.data);
        var1.writeByteArray(this.secretKeyEncrypted);
        var1.writeByteArray(this.verifyTokenEncrypted1);
        var1.writeByte(this.data0);
        var1.writeByteArray(this.verifyTokenEncrypted);
        var1.writeByteArray(this.verifyTokenEncrypted0);
    }

	public void processPacket(INetHandlerLoginServer handler) {
	}

}
