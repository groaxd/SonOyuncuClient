package sonoyuncu.nethandler.login.server;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.network.login.INetHandlerLoginServer;
import net.minecraft.util.CryptManager;

import java.io.IOException;
import java.security.PublicKey;


public class S80PacketSessionIDGenerator implements Packet<INetHandlerLoginClient> {
   private byte hash;
   private PublicKey publicKey;
   private byte[] secretKey;
   private byte[] ignored;

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.hash = var1.readByte();
      this.publicKey =  CryptManager.decodePublicKey(var1.readByteArray());
      this.secretKey = var1.readByteArray();
      this.ignored = new byte[22];
      var1.readBytes(this.ignored, 0, this.ignored.length);
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
   }

   @Override
   public void processPacket(INetHandlerLoginClient handler) {
      //handler.handle(this);
   }
}
