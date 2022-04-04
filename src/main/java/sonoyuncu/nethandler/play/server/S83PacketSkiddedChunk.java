package sonoyuncu.nethandler.play.server;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.world.chunk.Chunk;

import java.io.IOException;

public class S83PacketSkiddedChunk implements Packet<INetHandlerPlayClient> {

    private int chunkX;
    private int chunkZ;
    private S21PacketChunkData.Extracted extractedData;
    private boolean field_149279_g;
    private int extra;

    public S83PacketSkiddedChunk() {
        this.extra = 0;
    }


    public S83PacketSkiddedChunk(Chunk hX2, boolean bl, int n, int n2) {
        this.extra = 0;
        this.chunkX = hX2.xPosition;
        this.chunkZ = hX2.zPosition;
        this.field_149279_g = bl;
        this.extractedData = S21PacketChunkData.func_179756_a(hX2, bl, !hX2.getWorld().provider.getHasNoSky(), n);
        this.extra = n2;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.chunkX = buf.readInt();
        this.chunkZ = buf.readInt();
        this.field_149279_g = buf.readBoolean();
        this.extractedData = new S21PacketChunkData.Extracted();
        this.extractedData.dataSize = buf.readShort();
        this.extractedData.data = buf.readByteArray();
        this.extra = buf.readInt();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeInt(this.chunkX);
        buf.writeInt(this.chunkZ);
        buf.writeBoolean(this.field_149279_g);
        buf.writeShort(((short) (this.extractedData.dataSize & 0xFFFF)));
        buf.writeByteArray(this.extractedData.data);
        buf.writeInt(this.extra);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler) {

    }

}
