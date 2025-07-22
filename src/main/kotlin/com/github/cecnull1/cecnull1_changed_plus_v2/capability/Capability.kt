package com.github.cecnull1.cecnull1_changed_plus_v2.capability

import com.github.cecnull1.cecnull1_changed_plus_v2.packet.HaStateNetworkHandler
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.common.capabilities.CapabilityToken
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.common.util.INBTSerializable
import net.minecraftforge.common.util.LazyOptional

data class ExtendedPlayerData(
    val haState: HAState = HAState(),
) {
    fun saveNBTData(nbt: CompoundTag) {
        haState.saveNBTData(nbt)
    }

    fun loadNBTData(nbt: CompoundTag) {
        haState.loadNBTData(nbt)
    }

    fun copyFrom(other: ExtendedPlayerData) {
        haState.copyFrom(other.haState)
    }
}

data class HAState(
    var hasHA: Boolean = false,
    var haItem: ItemStack = ItemStack.EMPTY
) {
    fun copyFrom(other: HAState) {
        hasHA = other.hasHA
        haItem = other.haItem
    }

    fun saveNBTData(nbt: CompoundTag) {
        nbt.putBoolean("hasHA", hasHA)
        nbt.put("haItem", haItem.serializeNBT())
    }

    fun loadNBTData(nbt: CompoundTag) {
        hasHA = nbt.getBoolean("hasHA")
        haItem = ItemStack.of(nbt.getCompound("haItem"))
    }
}

class ExtendedPlayerDataProvider : ICapabilityProvider, INBTSerializable<CompoundTag> {
    companion object {
        @JvmField
        val EXTENDED_PLAYER_DATA: Capability<ExtendedPlayerData> =
            CapabilityManager.get<ExtendedPlayerData>(object : CapabilityToken<ExtendedPlayerData>() {})
    }

    private var extendedPlayerData: ExtendedPlayerData? = null

    private val optional: LazyOptional<ExtendedPlayerData> = LazyOptional.of(this::createExtendedPlayerData)

    private fun createExtendedPlayerData(): ExtendedPlayerData {
        val naStateL = extendedPlayerData ?: ExtendedPlayerData()
        extendedPlayerData = ExtendedPlayerData()
        return naStateL
    }

    override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        return if (cap == EXTENDED_PLAYER_DATA) {
            optional.cast()
        } else {
            LazyOptional.empty()
        }
    }

    override fun serializeNBT(): CompoundTag {
        val nbt = CompoundTag()
        createExtendedPlayerData().saveNBTData(nbt)
        return nbt
    }

    override fun deserializeNBT(nbt: CompoundTag) {
        createExtendedPlayerData().loadNBTData(nbt)
    }
}

var Player.haEnabled
    get(): Boolean {
        return this.getCapability(ExtendedPlayerDataProvider.EXTENDED_PLAYER_DATA).orElse(ExtendedPlayerData()).haState.hasHA
    }
    set(value) {
        val old = this.haEnabled
        if (old != value) {
            this.getCapability(ExtendedPlayerDataProvider.EXTENDED_PLAYER_DATA).orElse(ExtendedPlayerData()).haState.hasHA = value
            // 每次更新时同步到客户端
            if (!level().isClientSide) {
                HaStateNetworkHandler.sendToClient(this)
            }
        }
    }

var Player.haItem
    get(): ItemStack {
        return this.getCapability(ExtendedPlayerDataProvider.EXTENDED_PLAYER_DATA).orElse(ExtendedPlayerData()).haState.haItem
    }
    set(value) {
        val old = this.haItem
        if (old !== value && !ItemStack.matches(old, value)) {
            this.getCapability(ExtendedPlayerDataProvider.EXTENDED_PLAYER_DATA)
                .orElse(ExtendedPlayerData()).haState.haItem = value
            // 每次更新时同步到客户端
            if (!level().isClientSide) {
                HaStateNetworkHandler.sendToClient(this)
            }
        }
    }