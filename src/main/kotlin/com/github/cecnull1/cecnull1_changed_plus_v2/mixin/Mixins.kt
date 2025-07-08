package com.github.cecnull1.cecnull1_changed_plus_v2.mixin

import com.github.cecnull1.cecnull1_changed_plus_v2.capability.haEnabled
import com.github.cecnull1.cecnull1_changed_plus_v2.capability.haItem
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.ServerGamePacketListenerImpl
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable


@Mixin(LivingEntity::class)
abstract class LivingEntityMixin {
    @Inject(method = ["m_21205_"], at = [At("RETURN")], cancellable = true, remap = false)
    private fun getMainHandItem(cir: CallbackInfoReturnable<ItemStack>) {
        if (this is Player && this.isAlive) {
            if (this.haEnabled) {
                cir.returnValue = this.haItem
                cir.cancel()
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
            if (this is Player && this.isAlive) {
                if (this.haEnabled) {
                    cir.returnValue = this.haItem
                    cir.cancel()
                }
            }
        }
    }

    @Inject(method = ["m_21211_"], at = [At("RETURN")], cancellable = true)
    private fun getUseItem(cir: CallbackInfoReturnable<ItemStack>) {
        if (this is Player && this.isAlive) {
            if (this.haEnabled) {
                cir.returnValue = this.haItem
                cir.cancel()
            }
        }
    }

    @Inject(method = ["m_6117_"], at = [At("RETURN")], cancellable = true, remap =  false)
    private fun isUsingItem(cir: CallbackInfoReturnable<Boolean>) {
        if (this is Player && this.isAlive) {
            if (this.haEnabled) {
                cir.returnValue = !this.haItem.isEmpty && this.haItem.item.getUseAnimation(this.haItem) != null
                cir.cancel()
            }
        }
    }

    @Inject(method = ["m_21312_"], at = [At("HEAD")], cancellable = true, remap = false)
    private fun swapHandItems(ci: CallbackInfo) {
        if (this is Player && this.isAlive) {
            if (this.haEnabled && !this.haItem.isEmpty) {
                ci.cancel()
            }
        }
    }
}

@Mixin(Player::class)
open class PlayerMixin {
    @Inject(method = ["m_6844_"], at = [At("RETURN")], cancellable = true, remap = false)
    private fun getItemBySlot(slot: EquipmentSlot, cir: CallbackInfoReturnable<ItemStack>) {
        if (this is Player && this.isAlive && slot == EquipmentSlot.MAINHAND) {
            if (this.haEnabled) {
                cir.returnValue = this.haItem
                cir.cancel()
            }
        }
    }
}

@Mixin(Inventory::class)
open class InventoryMixin {
    @Inject(method = ["m_36056_"], at = [At("RETURN")], cancellable = true, remap =  false)
    fun getCarried(cir: CallbackInfoReturnable<ItemStack>) {
        if (this is Inventory) {
            val player = this.player
            if (player.isAlive && player.haEnabled) {
                cir.returnValue = player.haItem
                cir.cancel()
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

            if (packet.action == Action.SWAP_ITEM_WITH_OFFHAND && player.haEnabled && !player.haItem.isEmpty) {
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