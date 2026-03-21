package com.github.cecnull1.cecnull1_changed_plus_v2.packet

import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.renderer.newrl
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.HAState
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.IPlayerExtendedData
import net.minecraft.client.Minecraft
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
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
        newrl(MODID, "sync_ha_state"),
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
}