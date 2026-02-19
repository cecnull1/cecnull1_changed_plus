@file:Suppress("USELESS_IS_CHECK", "UNUSED_PARAMETER", "UNUSED_VARIABLE")
package com.github.cecnull1.cecnull1_changed_plus_v2.mixin

import com.github.cecnull1.cecnull1_cforge.core.CForgeEventCore.post
import com.github.cecnull1.cecnull1_cforge.core.ComponentContainer
import com.github.cecnull1.cecnull1_cforge.core.ComponentCore.getComponent
import com.github.cecnull1.cecnull1_cforge.core.ComponentCore.hasComponent
import com.github.cecnull1.cecnull1_cforge.core.PipeCtrl.then
import com.github.cecnull1.cecnull1_changed_plus_v2.bus
import com.github.cecnull1.cecnull1_changed_plus_v2.cbor.format
import com.github.cecnull1.cecnull1_changed_plus_v2.component.AutoMove
import com.github.cecnull1.cecnull1_changed_plus_v2.component.Flying
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.BBlockMoveEntity
import com.github.cecnull1.cecnull1_changed_plus_v2.event.CLivingTickEvent
import com.github.cecnull1.cecnull1_changed_plus_v2.event.CPlayerTickEvent
import com.github.cecnull1.cecnull1_changed_plus_v2.event.LivingTravelEvent
import com.github.cecnull1.cecnull1_changed_plus_v2.event.TakeOffEvent
import com.github.cecnull1.cecnull1_changed_plus_v2.gamerule.ModGameRule
import com.github.cecnull1.cecnull1_changed_plus_v2.item.NotCanTakeOffWetsuit
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.*
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.MPlayerExtendedData.Companion.haArmorItems
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.MPlayerExtendedData.Companion.haItem
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.MPlayerExtendedData.Companion.hasArmorHA
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.MPlayerExtendedData.Companion.hasHA
import com.github.cecnull1.cecnull1lib.utils.changed.playerTransfurVariant
import com.github.cecnull1.cecnull1lib.utils.nbt.asCompoundTag
import com.github.cecnull1.cecnull1lib.utils.nbt.buildNBT
import com.github.cecnull1.cecnull1lib.utils.nbt.set
import com.llamalad7.mixinextras.injector.ModifyExpressionValue
import com.llamalad7.mixinextras.injector.ModifyReturnValue
import kotlinx.serialization.ExperimentalSerializationApi
import net.ltxprogrammer.changed.ability.AccessChestAbilityInstance
import net.ltxprogrammer.changed.data.AccessorySlotType
import net.ltxprogrammer.changed.data.AccessorySlots
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance
import net.ltxprogrammer.changed.process.ProcessTransfur
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.ServerGamePacketListenerImpl
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityDimensions
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.vehicle.Boat
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.phys.Vec3
import net.minecraftforge.event.TickEvent
import net.minecraftforge.fluids.FluidType
import net.minecraftforge.registries.RegistryObject
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Pseudo
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.Unique
import org.spongepowered.asm.mixin.gen.Accessor
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(Entity::class)
abstract class EntityMixin: EntityExtendedComponent {
    @get:Unique private inline val self get() = this as Entity

//    @ModifyReturnValue(method = ["m_20202_"], at = [At("RETURN")], remap = false)
//    fun getVehicle(entity: Entity?): Entity? {
//        if (entity is BBlockMoveEntity && entity.vehicle != null) {
//            return entity.vehicle
//        }
//        return entity
//    }

//    @ModifyReturnValue(method = ["m_146895_"], at = [At("RETURN")], remap = false)
//    fun getFirstPassenger(entity: Entity?): Entity? {
//        if (entity is BBlockMoveEntity && entity.firstPassenger != null) {
//            return entity.firstPassenger
//        }
//        return entity
//    }


    @Inject(method = ["m_5830_"], at = [At("HEAD")], remap = false, cancellable = true)
    fun isInWall(cir: CallbackInfoReturnable<Boolean>) {
        if (self is Player) {
            if (self.vehicle is BBlockMoveEntity) {
                cir.mreturn(false) // 防止Player本身因为卡个正着而无法特定操作
            }
        }
    }

//    @ModifyExpressionValue(
//        method = ["*"],
//        at = [At(
//            value = "FIELD",
//            target = "Lnet/minecraft/world/entity/Entity;f_19794_:Z",
//            remap = false
//        )],
//        remap = false
//    )
//    fun isNoPhysics(original: Boolean): Boolean {
//        return (this is Player && (
//                when(this.playerTransfurVariant?.changedEntity) {
//                    is Soul -> true
//                    else -> false
//                })) || original
//    }

    @Inject(method = ["m_7998_(Lnet/minecraft/world/entity/Entity;Z)Z"], at = [At("HEAD")], cancellable = true, remap = false)
    private fun startRiding(entity: Entity, b: Boolean, ci: CallbackInfoReturnable<Boolean>) {
        if (!entity.isRemoved && !self.isRemoved) {
            if ((entity as? IOnMount)?.onMount(MountType.Mount(self, entity)) == false) ci.mreturn(false)
            if ((self.vehicle as? IOnMount)?.onMount(MountType.Move(self, entity)) == false) ci.mreturn(false)
        }
    }

    @Inject(method = ["m_8127_()V"], at = [At("HEAD")], cancellable = true, remap = false)
    private fun stopRiding(ci: CallbackInfo) {
        if (self.vehicle?.isRemoved == false && !self.isRemoved) {
            if ((self.vehicle as? IOnMount)?.onMount(MountType.Dismount(self)) == false) ci.cancel()
        }
    }

    @Inject(method = ["m_20256_"], at = [At("HEAD")], remap = false, cancellable = true)
    fun setDeltaMovementS(vec: Vec3, ci: CallbackInfo) {
        if (vec == Vec3.ZERO && (self is Player && self.vehicle is BBlockMoveEntity && !self.level.isClientSide)) {
            ci.cancel()
        }
    }

    @set:Unique
    @get:Unique
    @field:Unique
    override var components: ComponentContainer = ComponentContainer()

    @OptIn(ExperimentalSerializationApi::class)
    @Inject(method = ["m_20240_"], at = [At("HEAD")], remap = false)
    private fun modifySaveTag(original: CompoundTag, cir: CallbackInfoReturnable<CompoundTag>) {
        if (this is IPlayerExtendedData) this.cecnull1PlayerExtendedSave(original)
        original.putByteArray("LivingEntityCbor", format.encodeToByteArray(
            EntityExtendedComponentSer,
            components.toComponentsSer()
        ))
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Inject(method = ["m_20258_"], at = [At("HEAD")], remap = false)
    private fun injectLoadData(tag: CompoundTag, ci: CallbackInfo) {
        if (this is IPlayerExtendedData) cecnull1PlayerExtendedLoad(tag)
        val c = tag.getByteArray("LivingEntityCbor")
        if (c.isNotEmpty()) components = format.decodeFromByteArray(
            EntityExtendedComponentSer, c
        ).toComponentContainer()
    }
}

@Mixin(LivingEntity::class)
abstract class LivingEntityMixin {


    @Inject(method = ["m_8119_"], at = [At("HEAD")], cancellable = true, remap = false)
    fun tickStart(ci: CallbackInfo) {
        val entity = this as LivingEntity
        CLivingTickEvent(entity, TickEvent.Phase.START).post(bus)
    }

    @Inject(method = ["m_8119_"], at = [At("RETURN")], cancellable = true, remap = false)
    fun tickEnd(ci: CallbackInfo) {
        val entity = this as LivingEntity
        CLivingTickEvent(entity, TickEvent.Phase.END).post(bus)
    }

    @Inject(method = ["m_21205_"], at = [At("RETURN")], cancellable = true, remap = false)
    private fun getMainHandItem(cir: CallbackInfoReturnable<ItemStack>) {
        this as? Player then {
            if (this.isAlive) {
                if (this.hasHA) cir.mreturn(this.haItem)
            }
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
            this as? Player then {
                if (this.isAlive) {
                    if (this.hasHA) cir.mreturn(this.haItem)
                }
            }
        }
    }

    @Inject(method = ["m_21211_"], at = [At("RETURN")], cancellable = true, remap = false)
    private fun getUseItem(cir: CallbackInfoReturnable<ItemStack>) {
        this as? Player then {
            if (this.isAlive) {
                if (this.hasHA) cir.mreturn(this.haItem)
            }
        }
    }

    @Inject(method = ["m_21312_"], at = [At("HEAD")], cancellable = true, remap = false)
    private fun swapHandItems(ci: CallbackInfo) {
        this as? Player then {
            if (this.isAlive) {
                if (this.hasHA && !this.haItem.isEmpty) ci.cancel()
            }
        }
    }

    @ModifyReturnValue(method = ["m_21255_"], at = [At("RETURN")], remap = false)
    fun isFallFlying(old: Boolean): Boolean {
        this as? Player then {
            return (this.playerTransfurVariant?.changedEntity as? IFlying)?.isFallFlying(old) ?: old
        }
        return old
    }

    @Inject(method = ["m_7023_"], at = [At("HEAD")], remap = false, cancellable = true)
    fun travel(vec3: Vec3, ci: CallbackInfo) {
        if (LivingTravelEvent(this as LivingEntity, vec3, TickEvent.Phase.START).apply {
            post(bus)
        }.isCanceled) ci.cancel()
    }

    @Inject(method = ["m_7023_"], at = [At("RETURN")], remap = false, cancellable = true)
    fun travelr(vec3: Vec3, ci: CallbackInfo) {
        if (LivingTravelEvent(this as LivingEntity, vec3, TickEvent.Phase.END).apply {
            post(bus)
        }.isCanceled) ci.cancel()
    }

    @Inject(method = ["m_274466_"], at = [At("RETURN")], remap = false, cancellable = true)
    fun riddenTravelr(player: Player, vec3: Vec3, ci: CallbackInfo) {
        if (LivingTravelEvent(this as LivingEntity, vec3, TickEvent.Phase.END, player).apply {
            post(bus)
        }.isCanceled) ci.cancel()
    }

    @Inject(method = ["m_274466_"], at = [At("HEAD")], remap = false, cancellable = true)
    fun riddenTravel(player: Player, vec3: Vec3, ci: CallbackInfo) {
        if (LivingTravelEvent(this as LivingEntity, vec3, TickEvent.Phase.START, player).apply {
            post(bus)
        }.isCanceled) ci.cancel()
    }

    @Inject(method = ["m_274312_"], at = [At("RETURN")], remap = false, cancellable = true)
    fun getRiddenInput(player: Player, vec3: Vec3, cir: CallbackInfoReturnable<Vec3>) {
        (this as EntityExtendedComponent).components.getComponent<AutoMove>("AutoMove".toRL()) then {
            val self = (this@LivingEntityMixin as LivingEntity)
            if (self.passengers.isNotEmpty() && this.isEnabled) {
                cir.mreturn(Vec3(0.0, 0.0, 1.0/this.speed))
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

    override fun cecnull1PlayerExtendedSave(tag: CompoundTag) {
        tag[MODID] = buildNBT {
            this["HAState"] = `cecnull1$cecnull1_changed_plus_v2$MPlayerExtendedData`.haState.serialize()
        }
    }

    override fun cecnull1PlayerExtendedLoad(tag: CompoundTag) {
        `cecnull1$cecnull1_changed_plus_v2$MPlayerExtendedData`.haState.deserialize(
            tag[MODID].asCompoundTag()["HAState"].asCompoundTag()
        )
    }

//    @Inject(method = ["m_7380_"], at = [At("HEAD")], remap = false)
//    private fun injectSaveData(tag: CompoundTag, ci: CallbackInfo) {
//
//    }
//
//    @Inject(method = ["m_7378_"], at = [At("HEAD")], remap = false)
//    private fun injectLoadData(tag: CompoundTag, ci: CallbackInfo) {
//
//    }

    @Inject(method = ["m_6844_"], at = [At("RETURN")], cancellable = true, remap = false)
    private fun getItemBySlot(slot: EquipmentSlot, cir: CallbackInfoReturnable<ItemStack>) {
        this as Player then {
            if (this.isAlive) {
                if (this.hasHA) {
                    if (slot.index == EquipmentSlot.MAINHAND.index) cir.mreturn(this.haItem)
                }
                if (this.hasArmorHA) {
                    if (slot.type != EquipmentSlot.Type.HAND) cir.mreturn(this.haArmorItems[slot])
                }
            }
        }
    }

    @Inject(method = ["m_6168_"], at = [At("HEAD")], cancellable = true, remap = false)
    private fun getArmorSlots(cir: CallbackInfoReturnable<NonNullList<ItemStack>>) {
        (this as Player) then {
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

    @ModifyReturnValue(method = ["m_6069_"], at = [At("RETURN")], remap = false)
    private fun isSwimming(old: Boolean): Boolean {
        (this as Player) then {
            if (this.accessorySlotsFast()?.fieldItems?.values?.any {
                it.item is NotCanTakeOffWetsuit
            }?: false) {
                if (this.isInWater) {
                    return(true)
                }
            }
            if ((this.playerTransfurVariant?.changedEntity as? ISwimming)?.isSwimming(old) == true) return(true)
        }
        return old
    }

    @Inject(method = ["m_8119_"], at = [At("HEAD")], cancellable = true, remap = false)
    fun tickStart(ci: CallbackInfo) {
        val entity = this as Player
        CPlayerTickEvent(entity, TickEvent.Phase.START).post(bus)
    }

    @Inject(method = ["m_8119_"], at = [At("TAIL")], cancellable = true, remap = false)
    fun tickEnd(ci: CallbackInfo) {
        val entity = this as Player
        CPlayerTickEvent(entity, TickEvent.Phase.END).post(bus)
    }

    @Inject(method = ["m_36342_"], at = [At("HEAD")], cancellable = true, remap = false)
    private fun wantsToStopRiding(cir: CallbackInfoReturnable<Boolean>) {
        if (((this as Player).vehicle as? IOnMount)?.onMount(MountType.PlayerSelfDismount(this)) == false) cir.mreturn(false)
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
        return ((this as? Player != null) && ((this as EntityExtendedComponent).components.getComponent<Flying>(Constant.NBTKeys.FLYING.toRL())?.boolean == true)) || original
    }
}

@Mixin(Inventory::class)
open class InventoryMixin {
    @Inject(method = ["m_36056_"], at = [At("RETURN")], cancellable = true, remap =  false)
    fun getCarried(cir: CallbackInfoReturnable<ItemStack>) {
        (this as Inventory) then {
            val player = this.player
            if (player.isAlive && player.hasHA) cir.mreturn(player.haItem)
        }
    }

    @Inject(method = ["m_36052_"], at = [At("HEAD")], cancellable = true, remap = false)
    fun getArmor(index: Int, cir: CallbackInfoReturnable<ItemStack>) {
        (this as Inventory) then {
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
        (this as Slot) then {
            val event = TakeOffEvent(player, this, this.item)
            event.post(bus)
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
        (this as Slot) then {
            val event = TakeOffEvent(player, this, this.item)
            event.post(bus)
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
        (this as ServerGamePacketListenerImpl) then {
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

@Mixin(ServerPlayer::class)
abstract class ServerPlayerMixin {
    @get:Unique private inline val self get() = this as ServerPlayer

    @Inject(method = ["m_9015_"], at = [At("HEAD")], cancellable = true, remap = false)
    private fun restoreFrom(player: ServerPlayer, restore: Boolean, callbacl: CallbackInfo) {
        val b1 = player.level.gameRules.getBoolean(ModGameRule.realKeepForm)
        val oldVariant = player.playerTransfurVariant
        val b2 = oldVariant?.changedEntity is RealKeepForm && player.level.gameRules.getBoolean(ModGameRule.canRealKeepForm)
        if (b1 || restore || b2) {
            oldVariant?.let { oldVariant: TransfurVariantInstance<*> ->
                val newVariant = ProcessTransfur.setPlayerTransfurVariant(
                    self,
                    oldVariant.parent,
                    oldVariant.transfurContext,
                    oldVariant.transfurProgression
                )
                if (newVariant == null) return@let
                newVariant.load(oldVariant.save())
                newVariant.handleRespawn()
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
    }
}

@Mixin(TransfurVariantInstance::class)
abstract class TransfurVariantInstanceMixin {
    @get:Unique
    inline val self get() = (this as TransfurVariantInstance<*>)

    @field:Shadow
    @get:Unique
    private val host: Player? = null

    @ModifyExpressionValue(
        method = ["tickBreathing"],
        at = [At(
            value = "INVOKE",
            target = $$"Lnet/ltxprogrammer/changed/entity/variant/TransfurVariant$BreatheMode;canBreatheWater()Z"
        )],
        remap = false
    )
    private fun optimisticCanBreatheWater(original: Boolean): Boolean = original
}

@Mixin(AccessChestAbilityInstance::class)
abstract class AccessChestAbilityInstanceMixin {
    @Inject(method = ["onRemove"], at = [At("HEAD")], cancellable = true, remap = false)
    private fun onRemove(ci: CallbackInfo) {
        this as AccessChestAbilityInstance then {
            if (entity.isDeadOrDying && (entity.level.gameRules.getBoolean(ModGameRule.realKeepForm))) {
                clearContent()
                ci.cancel()
            }
        }
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

@Mixin(Entity::class, remap = false)
interface EntityAccessor {
    @get:Accessor("f_19815_", remap = false)
    val dimensions: EntityDimensions
}

@Mixin(RegistryObject::class, remap = false)
interface RegistryObjectAccessor {
    @get:Accessor("value")
    val rawValue: Any?  // ← 用 Any? 接住，不猜泛型
}

@Mixin(ModelPart::class, remap = false)
interface ModelPartAccessor {
    @get:Accessor("f_104213_")
    val children: Map<String, ModelPart>
}

private inline fun <reified T> CallbackInfoReturnable<T>.mreturn(returnValue: T?) {
    if (returnValue != null) {
        this.returnValue = returnValue
        cancel()
    }
}