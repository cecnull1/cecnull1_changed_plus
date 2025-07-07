package com.github.cecnull1.cecnull1_changed_plus.capability

import com.github.cecnull1.cecnull1_changed_plus.packet.NetworkHandler
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

class HAStateProvider : ICapabilityProvider, INBTSerializable<CompoundTag> {
    companion object {
        @JvmField
        var PLAYER_HA_STATE: Capability<HAState> =
            CapabilityManager.get<HAState>(object : CapabilityToken<HAState>() {})
    }

    private var haState: HAState? = null

    private val optional: LazyOptional<HAState> = LazyOptional.of(this::createHAState)

    private fun createHAState(): HAState {
        val naStateL = haState ?: HAState()
        haState = naStateL
        return naStateL
    }

    override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        return if (cap == PLAYER_HA_STATE) {
            optional.cast()
        } else {
            LazyOptional.empty()
        }
    }

    override fun serializeNBT(): CompoundTag {
        val nbt = CompoundTag()
        createHAState().saveNBTData(nbt)
        return nbt
    }

    override fun deserializeNBT(nbt: CompoundTag) {
        createHAState().loadNBTData(nbt)
    }
}

var Player.haEnabled
    get(): Boolean {
        return this.getCapability(HAStateProvider.PLAYER_HA_STATE).orElse(HAState()).hasHA
    }
    set(value) {
        this.getCapability(HAStateProvider.PLAYER_HA_STATE).orElse(HAState()).hasHA = value
        // 每次更新时同步到客户端
        if (!level.isClientSide) {
            NetworkHandler.sendToClient(this)
        }
    }

var Player.haItem
    get(): ItemStack {
        return this.getCapability(HAStateProvider.PLAYER_HA_STATE).orElse(HAState()).haItem
    }
    set(value) {
        this.getCapability(HAStateProvider.PLAYER_HA_STATE).orElse(HAState()).haItem = value
        // 每次更新时同步到客户端
        if (!level.isClientSide) {
            NetworkHandler.sendToClient(this)
        }
    }