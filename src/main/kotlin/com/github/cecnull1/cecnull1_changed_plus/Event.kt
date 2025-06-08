package com.github.cecnull1.cecnull1_changed_plus

import com.github.cecnull1.cecnull1_changed_plus.constant.Constant
import com.github.cecnull1.cecnull1_changed_plus.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus.constant.Constant.NBTKeys.BetterNeon.WFXC
import com.github.cecnull1.cecnull1_changed_plus.damage.DamageSource.SOUL_ATTACK
import com.github.cecnull1.cecnull1_changed_plus.damage.SoulAttack
import com.github.cecnull1.cecnull1_changed_plus.entity.AEntity
import com.github.cecnull1.cecnull1_changed_plus.entity.ModEntities
import com.github.cecnull1.cecnull1_changed_plus.entity.ModTransfurVariant
import com.github.cecnull1.cecnull1_changed_plus.utils.DismountingAble
import com.github.cecnull1.cecnull1_changed_plus.utils.MountAble
import com.github.cecnull1.cecnull1_changed_plus.utils.VariantTickPlusAble
import com.github.cecnull1.cecnull1lib.utils.InfixFunction.serverRun
import com.github.cecnull1.cecnull1lib.utils.nbt.getModData
import com.github.cecnull1.cecnull1lib.utils.nbt.set
import com.google.common.collect.Iterables
import net.ltxprogrammer.changed.entity.TransfurCause
import net.ltxprogrammer.changed.entity.TransfurContext
import net.ltxprogrammer.changed.entity.beast.DarkLatexYufeng
import net.ltxprogrammer.changed.entity.robot.Exoskeleton
import net.ltxprogrammer.changed.init.ChangedGameRules
import net.ltxprogrammer.changed.process.ProcessTransfur
import net.minecraft.core.NonNullList
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraftforge.event.TickEvent.PlayerTickEvent
import net.minecraftforge.event.entity.EntityMountEvent
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.event.entity.player.EntityItemPickupEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import java.util.Collections
import kotlin.jvm.optionals.getOrNull

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.FORGE)
object Event {
    @JvmStatic
    @SubscribeEvent
    fun onPlayerTick(event: PlayerTickEvent) {
        val player = event.player
        if (player is Player) {
            if (player.getModData(MODID).getBoolean(Constant.NBTKeys.BODY_WARNING)) {
                ProcessTransfur.ifPlayerTransfurred(player, {
                }) {
                    ProcessTransfur.progressTransfur(
                        player,
                        1f,
                        ModTransfurVariant.A_ENTITY_TRANSFUR_VARIANT.get(),
                        TransfurContext.hazard(TransfurCause.ATTACK_REPLICATE_LEFT)
                    )
                }
            }
            if (player.getModData(MODID).getBoolean(Constant.NBTKeys.FLYING)) {
                if (!player.abilities.flying) {
                    val abilities = player.abilities
                    abilities.flying = true
                    if (player is ServerPlayer) player.sendAbilitiesUpdate()
                }
            }

            ProcessTransfur.ifPlayerTransfurred(player) {
                val changedEntity = it.changedEntity
                when {
                    changedEntity is VariantTickPlusAble -> changedEntity.playerVariantTick(player, event.player.level)
                }
            }
            if (player.health.isNaN()) {
                player.health = 0.0f
                if (player is ServerPlayer) player.sendHealthUpdate()
            }
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onLivingTick(event: LivingEvent.LivingUpdateEvent) {
        if (event.entity::class == DarkLatexYufeng::class) {
            val aEntity = AEntity(ModEntities.A_ENTITY.get(), event.entity.level)
            aEntity.setPos(event.entity.position())
            aEntity.yRot = event.entity.yRot
            aEntity.xRot = event.entity.xRot
            event.entity.level.addFreshEntity(aEntity)
            event.entity.remove(Entity.RemovalReason.DISCARDED)
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onMount(event: EntityMountEvent) {
        val entityMounting: Entity = event.entityMounting ?: return
        val entityBeingMounted: Entity = event.entityBeingMounted ?: return
        if (entityMounting.isAlive && entityBeingMounted.isAlive && event.isDismounting) {
            when {
                entityBeingMounted is DismountingAble && !entityBeingMounted.canDismount() -> event.isCanceled = true
                entityBeingMounted.persistentData.getBoolean(WFXC) -> event.isCanceled = true
                entityMounting is Exoskeleton -> event.isCanceled = true
                entityMounting.getModData(MODID).getBoolean(Constant.NBTKeys.NO_DISMOUNTING) -> event.isCanceled = true
            }
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onInteract(event: PlayerInteractEvent.EntityInteract) {
        val target = event.target ?: return
        if (target is MountAble && target.canMount()) {
            event.player.startRiding(event.target ?: return)
        }
//        if (!event.world.isClientSide) {
//            event.player.startRiding(event.target ?: return)
//        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onEntityVariantAssigned(event: ProcessTransfur.EntityVariantAssigned.ChangedVariant) {
        val player = event.livingEntity
        val persistentData = player.persistentData
        val playerModData = player.getModData(MODID)
        if (playerModData.getBoolean(Constant.NBTKeys.BODY_WARNING)) {
            playerModData.remove(Constant.NBTKeys.BODY_WARNING)
        }
        persistentData[MODID] = playerModData
    }

    @JvmStatic
    @SubscribeEvent
    fun onLivingAttack(event: LivingAttackEvent) {
        val livingEntity = event.entityLiving
        val attacker = event.source.entity
        if (livingEntity is Player) ProcessTransfur.ifPlayerTransfurred(livingEntity) {
            if (it.`is`(ModTransfurVariant.SOUL_TRANSFUR_VARIANT)) {
                event.isCanceled = true
            }
        }
        if (attacker is Player) ProcessTransfur.ifPlayerTransfurred(attacker) {
            if (it.`is`(ModTransfurVariant.SOUL_TRANSFUR_VARIANT)) {
                // 取消攻击
                event.isCanceled = true
                // 获取 LivingEntity 的数据
                val persistentData = livingEntity.persistentData
                // 获取 LivingEntity 的 ModData
                val entityModData = livingEntity.getModData(MODID)
                // 获取 LivingEntity 的变体
                val entityVariant = ProcessTransfur.getEntityVariant(livingEntity).getOrNull()
                // 如果 LivingEntity 有变体，或 LivingEntity 是 Player
                if (entityVariant != null || livingEntity is Player) {
                    // 增加 LivingEntity 的攻击值
                    entityModData[Constant.NBTKeys.SOUL_SP_ATTACK_VALUE] =
                        entityModData.getDouble(Constant.NBTKeys.SOUL_SP_ATTACK_VALUE) + 1.0
                    // 如果 LivingEntity 的攻击值 >= LivingEntity 的生命值
                    if (entityModData.getDouble(Constant.NBTKeys.SOUL_SP_ATTACK_VALUE) >= livingEntity.health) {
                        // 移除 LivingEntity 的攻击值
                        entityModData.remove(Constant.NBTKeys.SOUL_SP_ATTACK_VALUE)
                        // 将 attacker 的变体改为 entityVariant
                        ProcessTransfur.setPlayerTransfurVariant(
                            attacker, entityVariant,
                            TransfurContext.hazard(TransfurCause.DEFAULT)
                        )
                        // 将 attacker 的当前飞行状态改为 false ，以防止变体设置后仍然处于 true 的情况
                        attacker.abilities.flying = false
                        // 实体间覆盖
                        attacker.setPos(livingEntity.x, livingEntity.y, livingEntity.z)
                        attacker.xRot = livingEntity.xRot
                        attacker.yRot = livingEntity.yRot
                        attacker.health = livingEntity.health
                        if (livingEntity is Player) {
                            attacker.foodData.foodLevel = livingEntity.foodData.foodLevel
                            attacker.foodData.setSaturation(livingEntity.foodData.saturationLevel)
                        } else {
                            attacker.foodData.foodLevel = 20
                            attacker.foodData.setSaturation(20f)
                        }
                        if (attacker is ServerPlayer) {
                            attacker.sendPositionUpdate()
                            attacker.sendHealthUpdate()
                        }
                        // 删除/杀死livingEntity
                        if (livingEntity is Player) livingEntity.hurt(SOUL_ATTACK, Float.POSITIVE_INFINITY)
                        else livingEntity.remove(Entity.RemovalReason.KILLED)
                        // 如果 livingEntity 的健康值为 NaN，则将其设置为 0
                        if (livingEntity.health.isNaN()) {
                            livingEntity.health = 0f
                        }
                    }
                }
                // 将 entityModData 存储到 persistentData 中
                persistentData[MODID] = entityModData
            }
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onLivingDeath(event: LivingDeathEvent) {
        val player = event.entityLiving as? Player
        player?.let {
            if (!ProcessTransfur.isPlayerTransfurred(player) || !player.level.gameRules.getBoolean(ChangedGameRules.RULE_KEEP_FORM)) {
                player.removeAllEffects()
                player.level.serverRun { level ->
                    val sourceEntity = event.source.entity
                    if (event.source !is SoulAttack || sourceEntity !is Player) player.forceInventory { itemStack ->
                        level.addFreshEntity(ItemEntity(level, player.x, player.y, player.z, itemStack.copy()))
                        itemStack.count = 0
                    } else {
                        player.inventory.items = sourceEntity.inventory.items
                        player.inventory.selected = sourceEntity.inventory.selected
                        player.inventory.offhand = sourceEntity.inventory.offhand
                        player.inventory.armor = sourceEntity.inventory.armor
                        sourceEntity.inventory.items = NonNullList.withSize<ItemStack>(sourceEntity.inventory.items.size, ItemStack(Items.AIR))
                        sourceEntity.inventory.selected = 0
                        sourceEntity.inventory.offhand = NonNullList.withSize<ItemStack>(sourceEntity.inventory.offhand.size, ItemStack(Items.AIR))
                        sourceEntity.inventory.armor = NonNullList.withSize<ItemStack>(sourceEntity.inventory.armor.size, ItemStack(Items.AIR))
                    }
                }
                ProcessTransfur.setPlayerTransfurVariant(
                    player,
                    ModTransfurVariant.SOUL_TRANSFUR_VARIANT.get(),
                    TransfurContext.hazard(TransfurCause.DEFAULT)
                )
                event.isCanceled = true
                player.health = player.maxHealth
                if (player is ServerPlayer) player.sendHealthUpdate()
            }
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onEntityPickup(event: EntityItemPickupEvent) {
        ProcessTransfur.ifPlayerTransfurred(event.player ?: return) {
            if (it.`is`(ModTransfurVariant.SOUL_TRANSFUR_VARIANT)) {
                event.isCanceled = true
            }
        }
    }
}

fun Player.forceInventory(f: (ItemStack) -> Unit) {
    for (itemStack in Iterables.concat(
        inventory.items,
        inventory.armor,
        inventory.offhand
    )) {
        itemStack ?: continue
        if(itemStack.count == 0) continue
        f(itemStack)
    }
}

fun ServerPlayer.sendHealthUpdate() {
    val clientBoundSetHealthPacket = ClientboundSetHealthPacket(
        maxHealth,
        foodData.foodLevel,
        foodData.saturationLevel
    )
    connection.send(clientBoundSetHealthPacket)
}

fun ServerPlayer.sendPositionUpdate() {
    val relativeArguments = Collections.emptySet<ClientboundPlayerPositionPacket.RelativeArgument>()
    val clientBoundPlayerPositionPacket = ClientboundPlayerPositionPacket(
        x,
        y,
        z,
        xRot,
        yRot,
        relativeArguments,
        id,
        false
    )
    connection.send(clientBoundPlayerPositionPacket)
}

fun ServerPlayer.sendAbilitiesUpdate() {
    val clientBoundPlayerAbilitiesPacket = ClientboundPlayerAbilitiesPacket(abilities)
    connection.send(clientBoundPlayerAbilitiesPacket)
}