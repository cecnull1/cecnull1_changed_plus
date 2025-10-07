package com.github.cecnull1.cecnull1_changed_plus_v2.mixin

import com.github.cecnull1.cecnull1_cforge.core.CForgeEventBus.post
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.AEntity
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModTransfurVariant
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.Soul
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.meiyun
import com.github.cecnull1.cecnull1_changed_plus_v2.event.CLivingTickEvent
import com.github.cecnull1.cecnull1_changed_plus_v2.event.CPlayerTickEvent
import com.github.cecnull1.cecnull1_changed_plus_v2.event.TakeOffEvent
import com.github.cecnull1.cecnull1_changed_plus_v2.item.NotCanTakeOffWetsuit
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.*
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.MPlayerExtendedData.Companion.haArmorItems
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.MPlayerExtendedData.Companion.haItem
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.MPlayerExtendedData.Companion.hasArmorHA
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.MPlayerExtendedData.Companion.hasHA
import com.github.cecnull1.cecnull1lib.utils.changed.*
import com.github.cecnull1.cecnull1lib.utils.nbt.asCompoundTag
import com.github.cecnull1.cecnull1lib.utils.nbt.buildNBT
import com.github.cecnull1.cecnull1lib.utils.nbt.getModData
import com.github.cecnull1.cecnull1lib.utils.nbt.set
import com.llamalad7.mixinextras.injector.ModifyExpressionValue
import net.ltxprogrammer.changed.data.AccessorySlotType
import net.ltxprogrammer.changed.data.AccessorySlots
import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.ServerGamePacketListenerImpl
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.vehicle.Boat
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.event.TickEvent
import net.minecraftforge.fluids.FluidType
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Pseudo
import org.spongepowered.asm.mixin.Unique
import org.spongepowered.asm.mixin.gen.Accessor
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(Entity::class)
abstract class EntityMixin {
    @ModifyExpressionValue(
        method = ["*"],
        at = [At(
            value = "FIELD",
            target = "Lnet/minecraft/world/entity/Entity;f_19794_:Z",
            remap = false
        )],
        remap = false
    )
    fun isNoPhysics(original: Boolean): Boolean {
        return (this is Player && (
                when(this.playerTransfurVariant?.changedEntity) {
                    is Soul -> true
                    else -> false
                })) || original
    }
}

@Mixin(LivingEntity::class)
abstract class LivingEntityMixin {
    @Inject(method = ["m_8119_"], at = [At("HEAD")], cancellable = true, remap = false)
    fun tickStart(ci: CallbackInfo) {
        val entity = this as LivingEntity
        CLivingTickEvent(entity, TickEvent.Phase.START).post()
    }

    @Inject(method = ["m_8119_"], at = [At("TAIL")], cancellable = true, remap = false)
    fun tickEnd(ci: CallbackInfo) {
        val entity = this as LivingEntity
        CLivingTickEvent(entity, TickEvent.Phase.END).post()
    }

    @Inject(method = ["m_21205_"], at = [At("RETURN")], cancellable = true, remap = false)
    private fun getMainHandItem(cir: CallbackInfoReturnable<ItemStack>) {
        if (this is Player && this.isAlive) {
            if (this.hasHA) cir.mreturn(this.haItem)
        }
    }

    @Inject(
        method = ["m_21120_"],
        at = [At("RETURN")],
        cancellable = true,
        remap = false
    )
    private fun getItemInHand(hand: InteractionHand, cir: CallbackInfoReturnable<ItemStack>) {
        if (hand == InteractionHand.MAIN_HAND) {
            if (this is Player && this.isAlive) {
                if (this.hasHA) cir.mreturn(this.haItem)
            }
        }
    }

    @Inject(method = ["m_21211_"], at = [At("RETURN")], cancellable = true, remap = false)
    private fun getUseItem(cir: CallbackInfoReturnable<ItemStack>) {
        if (this is Player && this.isAlive) {
            if (this.hasHA) cir.mreturn(this.haItem)

        }
    }

    @Inject(method = ["m_21312_"], at = [At("HEAD")], cancellable = true, remap = false)
    private fun swapHandItems(ci: CallbackInfo) {
        if (this is Player && this.isAlive) {
            if (this.hasHA && !this.haItem.isEmpty) ci.cancel()
        }
    }

    @Inject(method = ["m_21255_"], at = [At("HEAD")], cancellable = true, remap = false)
    fun isFallFlying(cir: CallbackInfoReturnable<Boolean>) {
        if (this is Player) {
            this.ifPlayerTransfurred {
                if (it.changedEntity is AEntity && this.meiyun()) {
                    cir.mreturn(true)
                }
            }
        }
    }
}

@Mixin(Player::class)
@Pseudo
open class PlayerMixin: IPlayerExtendedData {
//    @Unique private var `cecnull1$cecnull1_changed_plus_v2$canDismount`: Boolean = false
//        @Unique get
//        @Unique set

    @Unique
    private val `cecnull1$cecnull1_changed_plus_v2$MPlayerExtendedData`: MPlayerExtendedData = MPlayerExtendedData()
        @Unique get

    override var mPlayerExtendedData: MPlayerExtendedData
        get() = `cecnull1$cecnull1_changed_plus_v2$MPlayerExtendedData`
        set(value) = `cecnull1$cecnull1_changed_plus_v2$MPlayerExtendedData`.haState.copyFrom(value.haState)

    override fun save(tag: CompoundTag) {
        tag[MODID] = buildNBT {
            this["HAState"] = `cecnull1$cecnull1_changed_plus_v2$MPlayerExtendedData`.haState.serialize()
        }
    }

    override fun load(tag: CompoundTag) {
        `cecnull1$cecnull1_changed_plus_v2$MPlayerExtendedData`.haState.deserialize(
            tag[MODID].asCompoundTag()["HAState"].asCompoundTag()
        )
    }

    @Inject(method = ["m_7380_"], at = [At("HEAD")], remap = false)
    private fun injectSaveData(tag: CompoundTag, ci: CallbackInfo) {
        if (this is IPlayerExtendedData) save(tag)
    }

    @Inject(method = ["m_7378_"], at = [At("HEAD")], remap = false)
    private fun injectLoadData(tag: CompoundTag, ci: CallbackInfo) {
        if (this is IPlayerExtendedData) load(tag)
    }

    @Inject(method = ["m_6844_"], at = [At("RETURN")], cancellable = true, remap = false)
    private fun getItemBySlot(slot: EquipmentSlot, cir: CallbackInfoReturnable<ItemStack>) {
        if (this is Player && this.isAlive) {

            if (this.hasHA) {
                if (slot.index == EquipmentSlot.MAINHAND.index) cir.mreturn(this.haItem)
            }
            if (this.hasArmorHA) {
                if (slot.type != EquipmentSlot.Type.HAND) cir.mreturn(this.haArmorItems[slot])
            }
        }
    }

    @Inject(method = ["m_6168_"], at = [At("HEAD")], cancellable = true, remap = false)
    private fun getArmorSlots(cir: CallbackInfoReturnable<NonNullList<ItemStack>>) {
        if (this is Player) {
            if (this.hasArmorHA) {
                cir.returnValue = NonNullList.of(
                    this.haArmorItems[EquipmentSlot.HEAD]?:ItemStack.EMPTY,
                    this.haArmorItems[EquipmentSlot.CHEST]?:ItemStack.EMPTY,
                    this.haArmorItems[EquipmentSlot.LEGS]?:ItemStack.EMPTY,
                    this.haArmorItems[EquipmentSlot.FEET]?:ItemStack.EMPTY
                )
                cir.cancel()
            }
        }
    }

    @Inject(method = ["m_6069_"], at = [At("HEAD")], cancellable = true, remap = false)
    private fun isSwimming(cir: CallbackInfoReturnable<Boolean>) {
        if (this is Player) {
            if (this.accessorySlotsFast()?.fieldItems?.values?.any {
                it.item is NotCanTakeOffWetsuit
            }?: false) {
                if (!this.isPlayerTransfurred) {
                    transfur(
                        transfurData = TransfurData(
                            variant = ModTransfurVariant.PURE_WHITE_LATEX_YUFENG_BY_NCDBOAT_TRANSFUR_VARIANT.get(),
                            keepConscious = false
                        )
                    )
                }
                if (this.isInWater) {
                    cir.mreturn(true)
                }
            }
        }
    }

    @Inject(method = ["m_8119_"], at = [At("HEAD")], cancellable = true, remap = false)
    fun tickStart(ci: CallbackInfo) {
        val entity = this as Player
        CPlayerTickEvent(entity, TickEvent.Phase.START).post()
    }

    @Inject(method = ["m_8119_"], at = [At("TAIL")], cancellable = true, remap = false)
    fun tickEnd(ci: CallbackInfo) {
        val entity = this as Player
        CPlayerTickEvent(entity, TickEvent.Phase.END).post()
    }

    @ModifyExpressionValue(
        method = ["*"],
        at = [At(
            value = "FIELD",
            target = "Lnet/minecraft/world/entity/player/Abilities;f_35935_:Z",
            remap = false
        )],
        remap = false
    )
    fun isFlying(original: Boolean): Boolean {
        return (this is Player && (
                when(this.playerTransfurVariant?.changedEntity) {
                    is Soul -> true
                    else -> false
                } || this.getModData(MODID).getBoolean(Constant.NBTKeys.FLYING))) || original
    }
}

@Mixin(Inventory::class)
open class InventoryMixin {
    @Inject(method = ["m_36056_"], at = [At("RETURN")], cancellable = true, remap =  false)
    fun getCarried(cir: CallbackInfoReturnable<ItemStack>) {
        if (this is Inventory) {
            val player = this.player
            if (player.isAlive && player.hasHA) cir.mreturn(player.haItem)
        }
    }

    @Inject(method = ["m_36052_"], at = [At("HEAD")], cancellable = true, remap = false)
    fun getArmor(index: Int, cir: CallbackInfoReturnable<ItemStack>) {
        if (this is Inventory) {
            val player = this.player
            if (player.isAlive && player.hasArmorHA) {
                try {
                    player.haArmorItems[EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, index)]?.let {
                        cir.mreturn(it)
                    }
                } catch (_: IllegalArgumentException) {
                }
            }
        }
    }
}

@Mixin(targets = ["net/minecraft/world/inventory/InventoryMenu$1"], remap = false)
abstract class `InventoryMenu$1Mixin` {
    /**
     * 在装备槽位的mayPickup方法头部注入
     * 方法签名：m_8010_(Lnet/minecraft/world/entity/player/Player;)Z
     */
    @Inject(method = ["m_8010_(Lnet/minecraft/world/entity/player/Player;)Z"], at = [At("HEAD")], cancellable = true, remap = false)
    open fun mayPickup(player: Player, cir: CallbackInfoReturnable<Boolean>) {
        if (this is Slot) {
            val event = TakeOffEvent(player, this, this.item)
            event.post()
            if ((this.item.item as? ICanTakeOff)?.canTakeOff(player, this, this.item) == false || event.isCanceled) {
                cir.returnValue = false
            }
        }
    }
}

@Mixin(targets = ["net/ltxprogrammer/changed/world/inventory/AccessoryAccessMenu$1"], remap = false)
abstract class `AccessoryAccessMenu$1Mixin` {
    @Inject(method = ["m_8010_(Lnet/minecraft/world/entity/player/Player;)Z"], at = [At("HEAD")], cancellable = true, remap = false)
    open fun mayPickup(player: Player, cir: CallbackInfoReturnable<Boolean>) {
        if (this is Slot) {
            val event = TakeOffEvent(player, this, this.item)
            event.post()
            if ((this.item.item as? ICanTakeOff)?.canTakeOff(player, this, this.item) == false || event.isCanceled) {
                cir.returnValue = false
            }
        }
    }
}

@Mixin(ServerGamePacketListenerImpl::class)
abstract class MixinServerGamePacketListenerImpl {
    @Inject(method = ["m_7502_"], at = [At("HEAD")], cancellable = true, remap = false)
    private fun blockOffhandSwapAtHead(
        packet: ServerboundPlayerActionPacket,
        ci: CallbackInfo
    ) {
        if (this is ServerGamePacketListenerImpl) {
            // 获取玩家实例
            val player: ServerPlayer = this.player

            if (packet.action == Action.SWAP_ITEM_WITH_OFFHAND && player.hasHA && !player.haItem.isEmpty) {
                // 1. 保留时间更新逻辑
                player.resetLastActionTime()

                // 2. 保留旁观者检查
                if (player.isSpectator) {
                    return
                }

                // 3. 手动停止物品使用 (重要!)
                player.stopUsingItem()

                // 取消数据包处理
                ci.cancel()
            }
        }
    }
}

@Mixin(Block::class)
abstract class BlockMixin

@Mixin(Fluid::class)
abstract class FluidMixin

@Mixin(FluidType::class)
abstract class FluidTypeMixin {
    @Inject(
        method = ["supportsBoating(Lnet/minecraft/world/entity/vehicle/Boat;)Z"],
        at = [At("HEAD")],
        remap = false
    )
    private fun onSupportsBoating(boat: Boat, cir: CallbackInfoReturnable<Boolean>) {
    }

    @Inject(
        method = ["motionScale"],
        at = [At("HEAD")],
        remap = false,
    )
    private fun motionScale(entity: Entity, cir: CallbackInfoReturnable<Double>) {
    }

    @Inject(
        method = ["canSwim"],
        at = [At("HEAD")],
        remap = false,
        cancellable = true
    )
    private fun canSwim(entity: Entity, cir: CallbackInfoReturnable<Boolean>) {
        cir.mreturn(true)
    }

    @Inject(
        method = ["canDrownIn"],
        at = [At("HEAD")],
        remap = false,
        cancellable = true
    )
    private fun canDrownIn(entity: LivingEntity, cir: CallbackInfoReturnable<Boolean>) {
        cir.mreturn(false)
    }
}

@Mixin(AccessorySlots::class, remap = false)
interface AccessorySlotsAccessor {
    @Accessor("items")
    fun getItems(): Map<AccessorySlotType, ItemStack>
}

@Mixin(LivingEntity::class, remap = false)
interface LivingEntityAccessor {
    @get:Accessor("f_20899_", remap = false)
    val isJumping: Boolean
}

private inline fun <reified T> CallbackInfoReturnable<T>.mreturn(returnValue: T?) {
    if (returnValue != null) {
        this.returnValue = returnValue
        cancel()
    }
}