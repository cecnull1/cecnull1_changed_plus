package com.github.cecnull1.cecnull1_changed_plus_v2.packet

import com.github.cecnull1.cecnull1_changed_plus_v2.Cecnull1_changed_plus
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.HAState
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.IPlayerExtendedData
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.deserializerEntityComponent
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.serializerEntityComponent
import net.minecraft.client.Minecraft
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraftforge.common.capabilities.CapabilityToken
import net.minecraftforge.network.NetworkDirection
import net.minecraftforge.network.NetworkEvent
import net.minecraftforge.network.NetworkRegistry
import net.minecraftforge.network.simple.SimpleChannel
import java.util.function.Supplier

class SyncHaStateMessage(
    private val playerId: Int,
    private val data: CompoundTag? = null
) {
    constructor(buf: FriendlyByteBuf) : this(
        playerId = buf.readInt(),
        data = buf.readNbt()
    )

    fun encode(buf: FriendlyByteBuf) {
        buf.writeInt(playerId)
        buf.writeNbt(data)
    }

    fun handle(context: Supplier<NetworkEvent.Context>) {
        context.get().enqueueWork {
            val player = Minecraft.getInstance().level?.getEntity(playerId) as? Player
            if (player != null && data != null) {
                if (player is IPlayerExtendedData) {
                    player.mPlayerExtendedData.haState.deserialize(data)
                }
            }
        }
        context.get().packetHandled = true
    }
}

object NetworkHandler {
    const val PROTOCOL_VERSION = "1"
    private val CHANNEL: SimpleChannel = NetworkRegistry.newSimpleChannel(
        ResourceLocation(MODID, "sync_ha_state"),
        { PROTOCOL_VERSION },
        { it == PROTOCOL_VERSION },
        { it == PROTOCOL_VERSION }
    )

    private var messageId = 0

    fun register() {
         CHANNEL.registerMessage(
            messageId++,
             SyncHaStateMessage::class.java,
            SyncHaStateMessage::encode,
            ::SyncHaStateMessage,
            SyncHaStateMessage::handle
        )
        CHANNEL.registerMessage(
            messageId++,
            SyncComponentsMessage::class.java,
            SyncComponentsMessage::encode,
            ::SyncComponentsMessage,
            SyncComponentsMessage::handle
        )
    }

    fun haStateSendToClient(player: Player) {
        CHANNEL.sendTo(
            SyncHaStateMessage(
                playerId = player.id,
                data = HAState().apply {
                    copyFrom(player)
                }.serialize()
            ),
            (player as ServerPlayer).connection.connection,
            NetworkDirection.PLAY_TO_CLIENT
        )
    }

    fun haStateSendToServer() {
        CHANNEL.sendToServer(SyncHaStateMessage(Minecraft.getInstance().player?.id ?: -1, null))
    }

    fun componentsSendToClient(entity: Entity) {
        CHANNEL.sendTo(
            SyncComponentsMessage(
                entityId = entity.id,
                data = CompoundTag().apply {
                    serializerEntityComponent(entity, Cecnull1_changed_plus.entityComponentMap, this)
                }
            ),
            (entity as? ServerPlayer)?.connection?.connection?:return,
            NetworkDirection.PLAY_TO_CLIENT
        )
    }

    fun componentsSendToServer() {
        CHANNEL.sendToServer(SyncComponentsMessage(Minecraft.getInstance().player?.id ?: -1, null))
    }
}

class SyncComponentsMessage(
    private val entityId: Int,
    private val data: CompoundTag? = null
) {
    constructor(buf: FriendlyByteBuf) : this(
        entityId = buf.readInt(),
        data = buf.readNbt()
    )

    fun encode(buf: FriendlyByteBuf) {
        buf.writeInt(entityId)
        buf.writeNbt(data)
    }

    fun handle(context: Supplier<NetworkEvent.Context>) {
        context.get().enqueueWork {
            val player = Minecraft.getInstance().level?.getEntity(entityId) as? Player
            if (player != null && data != null) {
                deserializerEntityComponent(player, Cecnull1_changed_plus.entityComponentMap, data)
            }
        }
        context.get().packetHandled = true
    }
}
