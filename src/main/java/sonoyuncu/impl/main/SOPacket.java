package sonoyuncu.impl.main;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import io.netty.buffer.Unpooled;
import mchorse.mclib.math.functions.limit.Min;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.*;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldSettings;
import sonoyuncu.impl.event.ThePacket;
import sonoyuncu.impl.event.TheTwoBuffer;
import sonoyuncu.nethandler.login.client.C80PacketSessionIDReceiver;
import sonoyuncu.nethandler.login.server.S80PacketSessionIDGenerator;
import sonoyuncu.nethandler.play.client.C83PacketCipherRelease;
import sonoyuncu.nethandler.play.server.S80PacketProfile;
import sonoyuncu.nethandler.play.server.S81PacketScoreboard;
import sonoyuncu.nethandler.play.server.S82PacketChingChong;
import sonoyuncu.nethandler.play.server.S83PacketSkiddedChunk;
import sonoyuncu.util.ReflectionUtil;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Groax
 * @since 4/4/2022
 */
public enum SOPacket {
    /* I don't need to use asm or mixin shits */
    INSTANCE;

    private State state;
    public final EventBus eventBus = new EventBus();

    protected final Minecraft game = Minecraft.getInstance();

    public void initialize() {
        /* Register impl(s) */
        this.eventBus.register(this);


        /* Usage: EnumPacketDirection Class, Packet Id, Packet Class */

        this.setState(State.play_client);
        this.registerCustomPacket(EnumPacketDirection.SERVERBOUND, 83, C83PacketCipherRelease.class);


        this.setState(State.play_server);
        this.initPlayServerImportant();
        /*
        You don't need to register another play_server packets rn
         */

        this.setState(State.login_server);
        this.registerCustomPacket(EnumPacketDirection.CLIENTBOUND, 80, S80PacketSessionIDGenerator.class);


        this.setState(State.login_client);
        this.registerCustomPacket(EnumPacketDirection.SERVERBOUND, 80, C80PacketSessionIDReceiver.class);
    }

    public void initPlayServerImportant() {
        this.registerCustomPacket(EnumPacketDirection.CLIENTBOUND, 80, S80PacketProfile.class);
        this.registerCustomPacket(EnumPacketDirection.CLIENTBOUND, 81, S81PacketScoreboard.class);
        this.registerCustomPacket(EnumPacketDirection.CLIENTBOUND, 82, S82PacketChingChong.class);
        this.registerCustomPacket(EnumPacketDirection.CLIENTBOUND, 83, S83PacketSkiddedChunk.class);
    }

    private void registerCustomPacket(EnumPacketDirection direction, Integer id, Class<? extends Packet<?>> clazz) {
        Map<EnumPacketDirection, BiMap<Integer, Class<? extends Packet<?>>>> map = ReflectionUtil.get(ReflectionUtil.getField("directionMaps", EnumConnectionState.class), EnumConnectionState.PLAY);
        if (map == null) return;
        BiMap<Integer, Class<? extends Packet<?>>> bimap = map.computeIfAbsent(direction, k -> HashBiMap.create());

        if (!bimap.containsValue(clazz))
            bimap.put(id, clazz);
    }

    public final boolean isOnSonOyuncu() {
        if (Minecraft.getMinecraft().isSingleplayer()) return false;

        return Minecraft.getMinecraft().getCurrentServerData().serverIP.contains("sonoyuncu");
    }

    public final State getState() {
        return state;
    }

    public final void setState(State State) {
        this.state = State;
    }

    @Subscribe
    public void writePacket(TheTwoBuffer theEvent)
    {
        Packet<?> packet = theEvent.packet;
        PacketBuffer buf = theEvent.buf;
        
        /* Cancelling the packet(s) due to don't write data */
        
        if(packet instanceof C02PacketUseEntity)
        {
            C02PacketUseEntity packetUseEntity = (C02PacketUseEntity) packet;
            theEvent.cancelled = true;
            buf.writeVarIntToBuffer(packetUseEntity.entityId);
            C02PacketUseEntity.Action action = ReflectionUtil.get(ReflectionUtil.getField("action", C02PacketUseEntity.class), packetUseEntity);
            Vec3 hitVec = ReflectionUtil.get(ReflectionUtil.getField("hitVec", C02PacketUseEntity.class), packetUseEntity);
            buf.writeEnumValue(action);

            if (action == C02PacketUseEntity.Action.INTERACT_AT)
            {
                buf.writeFloat((float)hitVec.xCoord);
                buf.writeFloat((float)hitVec.yCoord);
                buf.writeFloat((float)hitVec.zCoord);
            }
            if(this.isOnSonOyuncu())
            {
                Entity entity =  game.theWorld.getEntityByID(packetUseEntity.entityId);

                buf.writeDouble(entity.getEntityBoundingBox().getAverageEdgeLength());
            }
        }
        if(packet instanceof C03PacketPlayer)
        {
            C03PacketPlayer c03PacketPlayer = (C03PacketPlayer) packet;
            theEvent.cancelled = true;

            Boolean onGround = ReflectionUtil.get(ReflectionUtil.getField("onGround", C03PacketPlayer.class), c03PacketPlayer);

            buf.writeByte(onGround.booleanValue() ? 1 : 0);
            if(isOnSonOyuncu()) {
                buf.writeFloat(game.thePlayer.distanceWalkedModified - game.thePlayer.prevDistanceWalkedModified);
            }
        }
        if(packet instanceof C03PacketPlayer.C06PacketPlayerPosLook)
        {
            C03PacketPlayer.C06PacketPlayerPosLook c03PacketPlayer = (C03PacketPlayer.C06PacketPlayerPosLook) packet;
            theEvent.cancelled = true;
            buf.writeDouble(c03PacketPlayer.x);
            buf.writeDouble(c03PacketPlayer.y);
            buf.writeDouble(c03PacketPlayer.z);
            buf.writeFloat(c03PacketPlayer.yaw);
            buf.writeFloat(c03PacketPlayer.pitch);
            Boolean onGround = ReflectionUtil.get(ReflectionUtil.getField("onGround", C03PacketPlayer.class), c03PacketPlayer);
            buf.writeByte(onGround.booleanValue() ? 1 : 0);
            if(isOnSonOyuncu()) {
                buf.writeFloat(0);
            }
            if(isOnSonOyuncu()) {
                buf.writeShort(Short.MIN_VALUE);
            }
        }
        if(packet instanceof C09PacketHeldItemChange)
        {
            C09PacketHeldItemChange c03PacketPlayer = (C09PacketHeldItemChange) packet;
            theEvent.cancelled = true;
            Integer slotId = ReflectionUtil.get(ReflectionUtil.getField("slotId", C09PacketHeldItemChange.class), c03PacketPlayer);

            buf.writeShort(slotId.intValue());
            if(isOnSonOyuncu())
            {
                buf.writeInt(0);
            }
        }
    }

    @Subscribe
    public void receivePacket(ThePacket theEvent)
    {
        Packet<?> packet = theEvent.packet;
        if(packet instanceof S01PacketJoinGame && theEvent.read)
        {
            /* Cancelling the packet due to don't process packet */
            theEvent.cancelled = true;
            S01PacketJoinGame packetIn = (S01PacketJoinGame) packet;
            this.game.playerController = new PlayerControllerMP(this.game, this.game.thePlayer.sendQueue);
            ReflectionUtil.set(ReflectionUtil.getField("clientWorldController", NetHandlerPlayClient.class), this.game.thePlayer.sendQueue, new WorldClient(this.game.thePlayer.sendQueue,
                    new WorldSettings(0L, packetIn.getGameType(), false, packetIn.isHardcoreMode(),
                            packetIn.getWorldType()),
                    packetIn.getDimension(), packetIn.getDifficulty(), this.game.mcProfiler));
            this.game.gameSettings.difficulty = packetIn.getDifficulty();
            this.game.loadWorld(ReflectionUtil.get(ReflectionUtil.getField("clientWorldController", NetHandlerPlayClient.class), this.game.thePlayer.sendQueue));
            this.game.thePlayer.dimension = packetIn.getDimension();
            this.game.displayGuiScreen(new GuiDownloadTerrain(this.game.thePlayer.sendQueue));
            this.game.thePlayer.setEntityId(packetIn.getEntityId());
            this.game.thePlayer.sendQueue.currentServerMaxPlayers = packetIn.getMaxPlayers();
            this.game.thePlayer.setReducedDebug(packetIn.isReducedDebugInfo());
            this.game.playerController.setGameType(packetIn.getGameType());
            this.game.gameSettings.sendSettingsToServer();;
            if(isOnSonOyuncu())
            {
                String thething = "the thing";
                this.game.thePlayer.sendQueue.addToSendQueue(new C83PacketCipherRelease("sysinf", thething.getBytes(StandardCharsets.UTF_8)));
            }
            this.game.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.copiedBuffer("Teyyap".getBytes())))));
        }
    }

    private enum State {
        play_client, play_server, login_client, login_server;
    }
}
