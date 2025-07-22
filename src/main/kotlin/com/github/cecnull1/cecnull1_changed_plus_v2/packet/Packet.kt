package com.github.cecnull1.cecnull1_changed_plus_v2.packet

import com.github.cecnull1.cecnull1_changed_plus_v2.capability.HAState
import com.github.cecnull1.cecnull1_changed_plus_v2.capability.haEnabled
import com.github.cecnull1.cecnull1_changed_plus_v2.capability.haItem
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import net.minecraft.client.Minecraft
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
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
                player.haEnabled = data.getBoolean("hasHA")
                player.haItem = ItemStack.of(data.getCompound("haItem"))
            }
        }
        context.get().packetHandled = true
    }
}

object HaStateNetworkHandler {
    private const val PROTOCOL_VERSION = "1"
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
    }

    fun sendToClient(player: Player) {
        CHANNEL.sendTo(
            SyncHaStateMessage(
                playerId = player.id,
                data = HAState().apply {
                    hasHA = player.haEnabled
                    haItem = player.haItem
                }.let { state ->
                    CompoundTag().apply { state.saveNBTData(this) }
                }
            ),
            (player as ServerPlayer).connection.connection,
            NetworkDirection.PLAY_TO_CLIENT
        )
    }

    fun sendToServer() {
        CHANNEL.sendToServer(SyncHaStateMessage(Minecraft.getInstance().player?.id ?: -1, null))
    }
}