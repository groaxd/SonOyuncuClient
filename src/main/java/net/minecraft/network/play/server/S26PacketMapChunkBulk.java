package net.minecraft.network.play.server;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.chunk.Chunk;
import sonoyuncu.api.assets.Assets;
import sonoyuncu.impl.assets.Manager;
import sonoyuncu.impl.main.SOPacket;

import java.io.IOException;
import java.util.List;

public class S26PacketMapChunkBulk implements Packet<INetHandlerPlayClient> {
    private int[] xPositions;
    private int[] zPositions;
    private S21PacketChunkData.Extracted[] chunksData;
    private boolean isOverworld;

    public S26PacketMapChunkBulk() {
    }

    public S26PacketMapChunkBulk(List<Chunk> chunks) {
        int i = chunks.size();
        this.xPositions = new int[i];
        this.zPositions = new int[i];
        this.chunksData = new S21PacketChunkData.Extracted[i];
        this.isOverworld = !((Chunk) chunks.get(0)).getWorld().provider.getHasNoSky();

        for (int j = 0; j < i; ++j) {
            Chunk chunk = chunks.get(j);
            S21PacketChunkData.Extracted s21packetchunkdata$extracted = S21PacketChunkData.func_179756_a(chunk, true, this.isOverworld, 65535);
            this.xPositions[j] = chunk.xPosition;
            this.zPositions[j] = chunk.zPosition;
            this.chunksData[j] = s21packetchunkdata$extracted;
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException {
        int mode = -1;
        if (SOPacket.INSTANCE.isOnSonOyuncu()) {
            mode = buf.readByte();
        }
        this.isOverworld = buf.readBoolean();
        int i = buf.readVarIntFromBuffer();
        this.xPositions = new int[i];
        this.zPositions = new int[i];
        this.chunksData = new S21PacketChunkData.Extracted[i];

        for (int j = 0; j < i; j++) {
            this.xPositions[j] = buf.readInt();
            this.zPositions[j] = buf.readInt();
            this.chunksData[j] = new S21PacketChunkData.Extracted();
            if (mode == -1) {
                this.chunksData[j].dataSize = buf.readShort() & 65535;
                this.chunksData[j].data = new byte[S21PacketChunkData.func_180737_a(Integer.bitCount(this.chunksData[j].dataSize), this.isOverworld, true)];
                continue;
            }
            if (mode == 2) {
                this.chunksData[j].dataSize = buf.readVarIntFromBuffer();
                this.chunksData[j].data = new byte[buf.readVarIntFromBuffer()];
                continue;
            }
        }

        for (int k = 0; k < i; k++) {
            if (mode == 0) {
                final Assets a = Manager.getDefaultAssetsNode().getAssets(this.xPositions[k], this.zPositions[k]);
                final S21PacketChunkData.Extracted pz = this.chunksData[k];
                pz.dataSize = a == null ? 0 : a.size;
                pz.data = a == null ? new byte[256] : a.array;
            } else {
                if (mode == 2) {
                    buf.readBytes(this.chunksData[k].data, 0, this.chunksData[k].dataSize);
                    buf.readBytes(this.chunksData[k].data, this.chunksData[k].dataSize, this.chunksData[k].data.length - this.chunksData[k].dataSize);
                }
                buf.readBytes(this.chunksData[k].data);
            }
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeBoolean(this.isOverworld);
        buf.writeVarIntToBuffer(this.chunksData.length);

        for (int i = 0; i < this.xPositions.length; ++i) {
            buf.writeInt(this.xPositions[i]);
            buf.writeInt(this.zPositions[i]);
            buf.writeShort((short) (this.chunksData[i].dataSize & 65535));
        }

        for (int j = 0; j < this.xPositions.length; ++j) {
            buf.writeBytes(this.chunksData[j].data);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleMapChunkBulk(this);
    }

    public int getChunkX(int p_149255_1_) {
        return this.xPositions[p_149255_1_];
    }

    public int getChunkZ(int p_149253_1_) {
        return this.zPositions[p_149253_1_];
    }

    public int getChunkCount() {
        return this.xPositions.length;
    }

    public byte[] getChunkBytes(int p_149256_1_) {
        return this.chunksData[p_149256_1_].data;
    }

    public int getChunkSize(int p_179754_1_) {
        return this.chunksData[p_179754_1_].dataSize;
    }
}
