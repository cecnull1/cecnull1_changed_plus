@file:OptIn(ExperimentalSerializationApi::class)
package com.github.cecnull1.cecnull1_changed_plus_v2.cbor

import com.github.cecnull1.cecnull1_cforge.core.ResourceLocation
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.component.ComponentsSer
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.component.EntityExtendedComponentSer
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.component.Flying
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.component.IComponentSer
import kotlinx.serialization.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.modules.polymorphic
import java.util.*
import java.util.concurrent.ConcurrentHashMap

val mcModule = SerializersModule {
    contextual(ResourceLocation.serializer())   // 把 RL 的序列化器绑定到上下文
    contextual(UUIDSerializer)
    contextual(EntityExtendedComponentSer)
    polymorphic(IComponentSer::class) {
        subclass(Flying::class, Flying.serializer())
    }
}

object UUIDSerializer: KSerializer<UUID> {
    override val descriptor: SerialDescriptor = SerialDescriptor("UUID", listSerialDescriptor<Long>())
    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeSerializableValue(
            ListSerializer(Long.serializer()),
            listOf(value.mostSignificantBits, value.leastSignificantBits)
        )
    }

    override fun deserialize(decoder: Decoder): UUID {
        val (most, least) = decoder.decodeSerializableValue(
            ListSerializer(Long.serializer())
        )
        return UUID(most, least)
    }
}

val format = Cbor {
    serializersModule = mcModule
}