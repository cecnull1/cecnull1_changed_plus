package com.github.cecnull1.cecnull1_changed_plus

import com.github.cecnull1.cecnull1_changed_plus.constant.Constant
import com.github.cecnull1.cecnull1_changed_plus.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus.constant.Constant.NBTKeys.BetterNeon.WFXC
import com.github.cecnull1.cecnull1_changed_plus.entity.ModTransfurVariant
import com.github.cecnull1.cecnull1_changed_plus.utils.DismountingAble
import com.github.cecnull1.cecnull1_changed_plus.utils.MountAble
import com.github.cecnull1.cecnull1_changed_plus.utils.VariantTickPlusAble
import com.github.cecnull1.cecnull1lib.utils.nbt.getModData
import com.github.cecnull1.cecnull1lib.utils.nbt.set
import net.ltxprogrammer.changed.entity.TransfurCause
import net.ltxprogrammer.changed.entity.TransfurContext
import net.ltxprogrammer.changed.entity.robot.Exoskeleton
import net.ltxprogrammer.changed.init.ChangedGameRules
import net.ltxprogrammer.changed.process.ProcessTransfur
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraftforge.event.TickEvent.PlayerTickEvent
import net.minecraftforge.event.entity.EntityMountEvent
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
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
            ProcessTransfur.ifPlayerTransfurred(player) {
                val changedEntity = it.changedEntity
                when {
                    changedEntity is VariantTickPlusAble -> changedEntity.playerVariantTick(player, event.player.level)
                }
            }
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
                // 如果 LivingEntity 有变体
                if (entityVariant != null) {
                    // 增加 LivingEntity 的攻击值
                    entityModData[Constant.NBTKeys.SOUL_SP_ATTACK_VALUE] =
                        entityModData.getDouble(Constant.NBTKeys.SOUL_SP_ATTACK_VALUE) + 1.0
                    // 如果 LivingEntity 的攻击值 >= LivingEntity 的生命值
                    if (entityModData.getDouble(Constant.NBTKeys.SOUL_SP_ATTACK_VALUE) >= livingEntity.health) {
                        // 移除 LivingEntity 的攻击值
                        entityModData.remove(Constant.NBTKeys.SOUL_SP_ATTACK_VALUE)
                        // 将 attacker 的变体改为 entityVariant
                        ProcessTransfur.setPlayerTransfurVariant(
                            attacker, entityVariant, TransfurContext.hazard(
                                TransfurCause.DEFAULT
                            )
                        )
                        // 将 attacker 的当前飞行状态改为 false ，以防止变体设置后仍然处于 true 的情况
                        attacker.abilities.flying = false
                        // 实体间覆盖
                        attacker.setPos(livingEntity.x, livingEntity.y, livingEntity.z)
                        attacker.xRot = livingEntity.xRot
                        attacker.yRot = livingEntity.yRot
                        attacker.customName = livingEntity.customName
                        if (livingEntity is Player) attacker.customName = livingEntity.displayName
                        // 恢复状态
                        attacker.health = attacker.maxHealth
                        attacker.foodData.foodLevel = 20
                        attacker.foodData.setSaturation(5f)
                        // 删除/杀死livingEntity
                        if (livingEntity is Player) livingEntity.kill()
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
                event.isCanceled = true
                player.health = player.maxHealth
                ProcessTransfur.changeTransfur(player, ModTransfurVariant.SOUL_TRANSFUR_VARIANT.get())
                player.customName = null
            }
        }
    }
}