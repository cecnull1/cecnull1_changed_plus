package com.github.cecnull1.cecnull1_changed_plus_v2

import com.github.cecnull1.cecnull1_changed_plus_v2.block.ModBlocks
import com.github.cecnull1.cecnull1_changed_plus_v2.capability.HAState
import com.github.cecnull1.cecnull1_changed_plus_v2.capability.HAStateProvider
import com.github.cecnull1.cecnull1_changed_plus_v2.capability.haEnabled
import com.github.cecnull1.cecnull1_changed_plus_v2.capability.haItem
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.NBTKeys.BetterNeon.WFXC
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModTransfurVariant
import com.github.cecnull1.cecnull1_changed_plus_v2.packet.NetworkHandler
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.*
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.TransfurContextUtils.toTransfurContext
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.TransfurData.Companion.toTransfurDataOrNull
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.TransfurData.Companion.transfurData
import com.github.cecnull1.cecnull1lib.utils.nbt.getModData
import com.github.cecnull1.cecnull1lib.utils.nbt.set
import com.google.common.collect.Iterables
import net.ltxprogrammer.changed.entity.TransfurCause
import net.ltxprogrammer.changed.init.ChangedBlocks
import net.ltxprogrammer.changed.process.ProcessTransfur
import net.minecraft.client.Minecraft
import net.minecraft.core.NonNullList
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.RelativeMovement
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.TickEvent.PlayerTickEvent
import net.minecraftforge.event.entity.EntityMountEvent
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.event.entity.player.EntityItemPickupEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.event.level.BlockEvent
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
                player.ifPlayerNotTransfurred {
                    player.progressTransfur(1f, TransfurData(
                        ModTransfurVariant.A_ENTITY_TRANSFUR_VARIANT.get(),
                        context = TransfurCause.ATTACK_REPLICATE_LEFT.toTransfurContext()
                    )
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

            player.ifPlayerTransfurred {
                val changedEntity = it.changedEntity
                when {
                    changedEntity is VariantTickPlusAble -> changedEntity.playerVariantTick(player, event.player.level())
                }
            }
            if (player.health.isNaN()) {
                player.health = 0.0f
                if (player is ServerPlayer) player.sendHealthUpdate()
            }
            if (player.haItem.isEmpty) {
                if (player.haEnabled) {
                    player.haEnabled = false
                    player.haItem = player.mainHandItem.copy()
                    player.mainHandItem.count = 0
                    player.haEnabled = true
                }
            }
            fun sync() {
                if (event.phase != TickEvent.Phase.END) return
                if (event.side.isClient && event.player == Minecraft.getInstance().player) {
                    NetworkHandler.sendToServer()
                }
            }
            sync()
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onLivingTick(event: LivingEvent.LivingTickEvent) {
    }

    @JvmStatic
    @SubscribeEvent
    fun onMount(event: EntityMountEvent) {
        val entityMounting: Entity = event.entityMounting ?: return
        val entityBeingMounted: Entity = event.entityBeingMounted ?: return
        if (entityMounting.isAlive && entityBeingMounted.isAlive && event.isDismounting) {
            when {
                entityBeingMounted is DismountAble && !entityBeingMounted.canDismount() -> event.isCanceled = true
                entityBeingMounted.persistentData.getBoolean(WFXC) -> event.isCanceled = true
                entityMounting.getModData(MODID).getBoolean(Constant.NBTKeys.NO_DISMOUNTING) -> event.isCanceled = true
            }
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onInteract(event: PlayerInteractEvent.EntityInteract) {
        val target = event.target ?: return
        if (target is MountAble && target.canMount()) {
            event.entity.startRiding(event.target ?: return)
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
        val livingEntity = event.entity ?: return
        val attacker = event.source.entity
        (livingEntity as? Player)?.ifPlayerTransfurred {
            if (it.`is`(ModTransfurVariant.SOUL_TRANSFUR_VARIANT)) {
                event.isCanceled = true
            }
        }
        (attacker as? Player)?.ifPlayerTransfurred {
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
                        attacker.transfurData = entityVariant.toTransfurDataOrNull()
                        // 将 attacker 的当前飞行状态改为 false ，以防止变体设置后仍然处于 true 的情况
                        attacker.abilities.flying = false
                        // 实体间覆盖
                        attacker.movePosToTarget(livingEntity)
                        attacker.removeAllEffects()
                        livingEntity.activeEffectsMap.values.forEach {
                            attacker.addEffect(MobEffectInstance(it))
                        }
                        livingEntity.removeAllEffects()
                        attacker.health = livingEntity.health
                        if (livingEntity is Player) {
                            attacker.moveItemToTarget(livingEntity)
                            attacker.foodData.foodLevel = livingEntity.foodData.foodLevel
                            attacker.foodData.setSaturation(livingEntity.foodData.saturationLevel)
                            attacker.experienceLevel = livingEntity.experienceLevel
                            attacker.experienceProgress = livingEntity.experienceProgress
                        } else {
                            attacker.foodData.foodLevel = 20
                            attacker.foodData.setSaturation(20f)
                        }
                        if (attacker is ServerPlayer) {
                            attacker.sendPositionUpdate()
                            attacker.sendHealthUpdate()
                        }
                        // 删除/杀死livingEntity
                        if (livingEntity is Player) {
                            livingEntity.hurt(
                                livingEntity.damageSources().lava(),  // 使用 DamageSources 获取岩浆伤害
                                Float.POSITIVE_INFINITY
                            )
                        }
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
    fun onEntityPickup(event: EntityItemPickupEvent) {
        event.entity?.ifPlayerTransfurred {
            if (it.`is`(ModTransfurVariant.SOUL_TRANSFUR_VARIANT)) {
                event.isCanceled = true
            }
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onBlockBreak(event: BlockEvent.BreakEvent) {
        val level = event.level ?: return
        if (!level.isClientSide) {
            when (event.state.block) {
                ChangedBlocks.WHITE_LATEX_BLOCK.get() -> {
                    if (level.random.nextInt(4) == 0) {
                        level.setBlock(
                            event.pos, ModBlocks.WHITE_LATEX_BLOCK_V2.get().defaultBlockState(),
                            Block.UPDATE_NEIGHBORS or Block.UPDATE_CLIENTS or Block.UPDATE_IMMEDIATE
                        )

                        if (level is Level) {
                            val newNotCanDismountBoat = ModEntities.NOT_CAN_DISMOUNT_BOAT.get().create(level)
                            if (newNotCanDismountBoat != null) {
                                newNotCanDismountBoat.setPos(event.pos.x + 0.5, event.pos.y + 0.5, event.pos.z + 0.5)
                                level.addFreshEntity(newNotCanDismountBoat)
                                if (level.random.nextBoolean() == true) {
                                    event.player?.startRiding(newNotCanDismountBoat)
                                }
                            }
                        }
                    }
                }

                ModBlocks.WHITE_LATEX_BLOCK_V2.get() -> {
                    if (level is Level) {
                        val pureWhiteLatexYufeng = ModEntities.PURE_WHITE_LATEX_YUFENG.get().create(level)
                        if (pureWhiteLatexYufeng != null) {
                            pureWhiteLatexYufeng.setPos(event.pos.x + 0.5, event.pos.y + 0.5, event.pos.z + 0.5)
                            level.addFreshEntity(pureWhiteLatexYufeng)
                        }
                    }
                }
                else -> {}
            }
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onAttachCapabilities(event: AttachCapabilitiesEvent<Entity>) {
        if (event.`object` is Player) {
            if (!event.`object`.getCapability(HAStateProvider.PLAYER_HA_STATE).isPresent) {
                event.addCapability(
                    ResourceLocation(MODID, "ha_state"),
                    HAStateProvider()
                )
            }
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onPlayerCloned(event: PlayerEvent.Clone) {
        if (event.isWasDeath) {
            event.original.getCapability(HAStateProvider.PLAYER_HA_STATE).ifPresent {
                oldState ->
                event.original.getCapability(HAStateProvider.PLAYER_HA_STATE).ifPresent {
                    newState ->
                    newState.copyFrom(oldState)
                }
            }
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onRegisterCapabilities(event: RegisterCapabilitiesEvent) {
        event.register(HAState::class.java)
    }

    @JvmStatic
    @SubscribeEvent
    fun onPlayerLogin(event: PlayerEvent.PlayerLoggedInEvent) {
        // 玩家登录时同步数据
        if (!event.entity.level().isClientSide) {
            NetworkHandler.sendToClient(event.entity)
        }
    }
}

fun Player.movePosToTarget(
    livingEntity: LivingEntity
) {
    setPos(livingEntity.x, livingEntity.y, livingEntity.z)
    xRot = livingEntity.xRot
    yRot = livingEntity.yRot
}

fun Player.moveItemToTarget(
    sourceEntity: Player
) {
    inventory.items = sourceEntity.inventory.items
    inventory.selected = sourceEntity.inventory.selected
    inventory.offhand = sourceEntity.inventory.offhand
    inventory.armor = sourceEntity.inventory.armor
    sourceEntity.inventory.items =
        NonNullList.withSize<ItemStack>(sourceEntity.inventory.items.size, ItemStack(Items.AIR))
    sourceEntity.inventory.selected = 0
    sourceEntity.inventory.offhand =
        NonNullList.withSize<ItemStack>(sourceEntity.inventory.offhand.size, ItemStack(Items.AIR))
    sourceEntity.inventory.armor =
        NonNullList.withSize<ItemStack>(sourceEntity.inventory.armor.size, ItemStack(Items.AIR))
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
    val relativeArguments = setOf<RelativeMovement>()
    val clientBoundPlayerPositionPacket = ClientboundPlayerPositionPacket(
        x,
        y,
        z,
        xRot,
        yRot,
        relativeArguments,
        id
    )
    connection.send(clientBoundPlayerPositionPacket)
}

fun ServerPlayer.sendAbilitiesUpdate() {
    val clientBoundPlayerAbilitiesPacket = ClientboundPlayerAbilitiesPacket(abilities)
    connection.send(clientBoundPlayerAbilitiesPacket)
}