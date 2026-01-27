@file:OptIn(ExperimentalSerializationApi::class)
package com.github.cecnull1.cecnull1_changed_plus_v2.cbor

import com.github.cecnull1.cecnull1_cforge.core.data.ResourceLocation
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.EntityExtendedComponentSer
import com.github.cecnull1.cecnull1_changed_plus_v2.component.Flying
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.IComponentSer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.modules.polymorphic
import java.util.*

val mcModule = SerializersModule {
    contextual(RLSer)   // 把 RL 的序列化器绑定到上下文
    contextual(UUIDSerializer)
    contextual(EntityExtendedComponentSer)
    polymorphic(IComponentSer::class) {
        subclass(Flying::class, Flying.serializer())
    }
}

val format = Cbor {
    serializersModule = mcModule
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

object RLSer: KSerializer<ResourceLocation> {
    override val descriptor = PrimitiveSerialDescriptor("ResourceLocation", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ResourceLocation) {
        // 序列化为 "modid:path"
        encoder.encodeString("${value.modId}:${value.path}")
    }

    override fun deserialize(decoder: Decoder): ResourceLocation {
        val str = decoder.decodeString()
        return ResourceLocation.fromString(str)
    }
}