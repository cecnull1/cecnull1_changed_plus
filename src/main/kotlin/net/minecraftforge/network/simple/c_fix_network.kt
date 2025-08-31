@file:JvmName("Cecnull1FixNetworkHack")

package net.minecraftforge.network.simple

import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent
import java.util.*
import java.util.function.BiConsumer
import java.util.function.Function
import java.util.function.Supplier
import kotlin.reflect.KClass

fun <MSG : Any> SimpleChannel.registerMessageFix(
    index: Int,
    messageType: KClass<MSG>,
    encoder: BiConsumer<MSG, FriendlyByteBuf>,
    decoder: Function<FriendlyByteBuf, MSG>,
    messageConsumer: BiConsumer<MSG, Supplier<NetworkEvent.Context>>
) {
    registerMessage<MSG>(
        index,
        messageType.java,
        encoder,
        decoder,
        messageConsumer,
        Optional.empty()
    )
}