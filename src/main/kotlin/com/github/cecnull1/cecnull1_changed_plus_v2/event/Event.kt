package com.github.cecnull1.cecnull1_changed_plus_v2.event

import com.github.cecnull1.cecnull1_cforge.core.PipeCore.process
import com.github.cecnull1.cecnull1_cforge.core.PipeCtrl.then
import com.github.cecnull1.cecnull1_changed_plus_v2.block.BBlockEntity
import com.github.cecnull1.cecnull1_changed_plus_v2.block.ModBlocks
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.NBTKeys.BetterNeon.WFXC
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.*
import com.github.cecnull1.cecnull1_changed_plus_v2.gamerule.ModGameRule
import com.github.cecnull1.cecnull1_changed_plus_v2.item.NotCanTakeOffWetsuit
import com.github.cecnull1.cecnull1_changed_plus_v2.packet.HaStateNetworkHandler
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.*
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.MPlayerExtendedData.Companion.haArmorItems
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.MPlayerExtendedData.Companion.haItem
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.MPlayerExtendedData.Companion.hasArmorHA
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.MPlayerExtendedData.Companion.hasHA
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.MPlayerExtendedData.Companion.wuDiTime
import com.github.cecnull1.cecnull1lib.utils.changed.*
import com.github.cecnull1.cecnull1lib.utils.changed.TransfurContextUtils.toTransfurContext
import com.github.cecnull1.cecnull1lib.utils.changed.TransfurData.Companion.toTransfurDataOrNull
import com.github.cecnull1.cecnull1lib.utils.changed.TransfurData.Companion.transfurData
import com.github.cecnull1.cecnull1lib.utils.nbt.getModData
import com.github.cecnull1.cecnull1lib.utils.nbt.set
import com.github.cecnull1.cecnull1lib.utils.vector.toKVec3
import com.github.cecnull1.cecnull1lib.utils.vector.toVec3
import com.google.common.collect.Iterables
import net.ltxprogrammer.changed.data.AccessorySlots
import net.ltxprogrammer.changed.entity.SeatEntity
import net.ltxprogrammer.changed.entity.TransfurCause
import net.ltxprogrammer.changed.entity.beast.PureWhiteLatexWolf
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance
import net.ltxprogrammer.changed.init.ChangedBlocks
import net.ltxprogrammer.changed.init.ChangedSounds
import net.ltxprogrammer.changed.process.ProcessTransfur
import net.ltxprogrammer.changed.util.ItemUtil
import net.minecraft.client.Minecraft
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor
import net.minecraft.core.Holder
import net.minecraft.network.protocol.game.*
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.RelativeMovement
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.phys.Vec3
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.entity.EntityMountEvent
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.event.entity.living.LivingFallEvent
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.event.entity.living.LivingKnockBackEvent
import net.minecraftforge.event.entity.player.EntityItemPickupEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.event.level.BlockEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import kotlin.jvm.optionals.getOrNull
import kotlin.math.sqrt

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.FORGE)
object Event {
    fun onPlayerTick(event: CPlayerTickEvent) = event.process {
        val player = event.player
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
            player.foodData.foodLevel++
            player.foodData.setSaturation(player.foodData.saturationLevel+1)
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

        for (itemStack in ItemUtil.getWearingItems(player).toImmutableSafeList()) {
            if (itemStack.itemStack.item is NotCanTakeOffWetsuit && player.isInWater) {
                val fixSpeed = 20.0
                val delta = player.deltaMovement
                val rotation = player.lookAngle
                if (!player.isFallFlying) {
                    player.deltaMovement = Vec3(
                        delta.x + rotation.x / fixSpeed,
                        delta.y + rotation.y / fixSpeed,
                        delta.z + rotation.z / fixSpeed
                    )
                }
                if (!player.isUnderWater) {
                    player.deltaMovement = Vec3(
                        delta.x,
                        delta.y.coerceAtMost(0.0),
                        delta.z
                    )
                }
            }
        }

        // 检查是否在劫持状态
        if (player.hasHA) {
            // 检查劫持物品是否意外丢失
            if (player.haItem.isEmpty) {
                // 获取当前选中的快捷栏槽位
                val selectedSlot = player.inventory.selected

                // 直接访问底层物品栏（避免通过属性访问器）
                val slotItem = player.inventory.items[selectedSlot]

                // 确保槽位有可劫持物品
                if (!slotItem.isEmpty) {
                    // 执行真正的物品转移
                    player.haItem = slotItem.copy()

                    // 清空原始槽位（关键操作）
                    slotItem.count = 0

                    // 不添加额外日志 - setter 会处理变更记录
                }
            }
        }
        if (player.hasArmorHA) {
            player.haArmorItems = player.haArmorItems.mapValues<EquipmentSlot, ItemStack, ItemStack> {
                if (it.value.isEmpty) {
                    val itemStack = player.inventory.armor[it.key.index]
                    val itemStack2 = itemStack.copy()
                    itemStack.count = 0
                    if (player is ServerPlayer && itemStack2.isEmpty && !itemStack.isEmpty) {
                        player.connection.send(
                            ClientboundSoundEntityPacket(
                                Holder.direct(ChangedSounds.POISON.get()),
                                SoundSource.PLAYERS,
                                player,
                                1.0f,
                                1.0f,
                                0
                            )
                        )
                    }
                    itemStack2
                } else it.value
            }.toMutableMap()
        }

        if (event.phase == TickEvent.Phase.START && player.wuDiTime > 0) {
            player.wuDiTime--
        }

        if (player.persistentData.getBoolean(Constant.NBTKeys.BetterNeon.NORIDE2_BN_C_JH_TF)) {
            player.ifPlayerNotTransfurred {
                player.transfur(
                    TransfurData(
                        ModTransfurVariant.PURE_WHITE_LATEX_YUFENG_TRANSFUR_VARIANT.get(),
                        keepConscious = true,
                        context = TransfurCause.WHITE_LATEX.toTransfurContext()
                    )
                )
            }
            player.persistentData.remove(Constant.NBTKeys.BetterNeon.NORIDE2_BN_C_JH_TF)
        }

        fun sync() {
            if (event.phase != TickEvent.Phase.END) return
            if (player.level().isClientSide && event.player == Minecraft.getInstance().player) {
                HaStateNetworkHandler.sendToServer()
            }
        }
        sync()
    }

    fun onLivingTick(event: CLivingTickEvent) {
        (event.entity as? PureWhiteLatexWolf)?.let {
            entity ->
            when (entity.random.nextInt(20*60)) {
                0 -> {
                    val newEntity = ModEntities.PURE_WHITE_LATEX_YUFENG.get().create(entity.level())?: return
                    newEntity.setPos(entity.position())
                    entity.level().addFreshEntity(newEntity)
                }
                199 -> {
                    val newEntity = ModEntities.PURE_WHITE_LATEX_YUFENG_BY_NCDBOAT.get().create(entity.level())?: return
                    newEntity.setPos(entity.position())
                    entity.level().addFreshEntity(newEntity)
                }
                else -> {}
            }
        }
    }

    fun onMount(event: EntityMountEvent) {
        val entityMounting: Entity = event.entityMounting ?: return
        val entityBeingMounted: Entity = event.entityBeingMounted ?: return
        if (entityMounting.isAlive && entityBeingMounted.isAlive && event.isDismounting) {
            when {
                entityBeingMounted is IDismount && !entityBeingMounted.canDismount() -> event.isCanceled = true
                entityBeingMounted.persistentData.getBoolean(WFXC) -> event.isCanceled = true
                entityMounting.getModData(MODID).getBoolean(Constant.NBTKeys.NO_DISMOUNTING) -> event.isCanceled = true
            }
        }
    }

    fun onInteract(event: PlayerInteractEvent.EntityInteract) {
        val target = event.target ?: return
        if (target is IMount && target.canMount(event.entity)) {
            event.entity.startRiding(event.target ?: return)
        }
    }

    fun onEntityVariantAssigned(event: ProcessTransfur.EntityVariantAssigned.ChangedVariant) {
        val player = event.livingEntity as? Player ?: return
        val persistentData = player.persistentData
        val playerModData = player.getModData(MODID)
        if (playerModData.getBoolean(Constant.NBTKeys.BODY_WARNING)) {
            playerModData.remove(Constant.NBTKeys.BODY_WARNING)
        }
        persistentData[MODID] = playerModData

        if (event.newVariant?.`is`(ModTransfurVariant.PURE_WHITE_LATEX_YUFENG_BY_NCDBOAT_TRANSFUR_VARIANT) == true) {
            player.notCanDismountBoatAddArmor(player)
        }
    }

    fun onHurt(event: LivingHurtEvent) {
        (event.entity as? Player)?.ifPlayerTransfurred {
            if (it.changedEntity is AEntity) {
                (event.entity as? Player)?.wuDiTime?.let { it1 -> event.amount = event.amount.coerceAtMost(20f) * (it1 <= 0.0).toInt() }
                (event.entity as? Player)?.wuDiTime+=20
            }
        }
    }

    fun onLivingAttack(event: LivingAttackEvent) {
        val livingEntity = event.entity ?: return
        val attacker = event.source.entity

        (livingEntity as? Player)?.ifPlayerTransfurred {
            // 检测玩家所代表的实体存在FanJi接口
            val changedEntity = it.changedEntity
            if (changedEntity is IFanJi) {
                if (attacker != null) when (attacker) {
                    is Player -> {
                        attacker.ifPlayerTransfurred { variant ->
                            if (variant.changedEntity !is IFanJi) {
                                changedEntity.onAttackedBy(attacker)
                            }
                        }
                        attacker.ifPlayerNotTransfurred {
                            changedEntity.onAttackedBy(attacker)
                        }
                    }
                    else -> {
                        changedEntity.onAttackedBy(attacker)
                    }
                }
            }
        }
        (livingEntity as? Player)?.ifPlayerTransfurred {
            // 检测玩家所代表的生物是否是魂体
            if (it.`is`(ModTransfurVariant.SOUL_TRANSFUR_VARIANT)) {
                event.isCanceled = true
            }

        }
        (attacker as? Player)?.ifPlayerTransfurred {
            // 调用灵魂附身
            lingHunFuShen(event, it, livingEntity, attacker)
        }

        event.entity as? Player then player@ {
            if (this.level().isClientSide) return@player
            (this.vehicle as? SeatEntity) then {
                (this.level().getBlockEntity(this@then.attachedBlockPos) as? BBlockEntity) then {
                    if (seatedEntity?.id == this@player.id && !this@player.isCreative && event.source.type() != DamageTypes.FELL_OUT_OF_WORLD) event.isCanceled = true
                }
            }
        }
    }

    private fun lingHunFuShen(
        event: LivingAttackEvent,
        instance: TransfurVariantInstance<*>,
        livingEntity: LivingEntity,
        attacker: Player
    ) {
        if (instance.`is`(ModTransfurVariant.SOUL_TRANSFUR_VARIANT)) {
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
                            livingEntity.damageSources().fellOutOfWorld(),  // 使用 DamageSources 获取岩浆伤害
                            Float.POSITIVE_INFINITY
                        )
                    } else livingEntity.remove(Entity.RemovalReason.KILLED)
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

    fun onLivingKnockBack(event: LivingKnockBackEvent) {
        (event.entity as? Player)?.ifPlayerTransfurred {
            if (it.changedEntity is IFanJi) {
                event.ratioX *= -1.0f
                event.ratioZ *= -1.0f
            }
        }
    }

    fun onEntityPickup(event: EntityItemPickupEvent) {
        event.entity?.ifPlayerTransfurred {
            if (it.`is`(ModTransfurVariant.SOUL_TRANSFUR_VARIANT)) {
                event.isCanceled = true
            }
        }
    }

    fun onBlockBreak(event: BlockEvent.BreakEvent) = event process {
        val level = event.level ?: return@process
        if (!level.isClientSide) {
            when (event.state.block) {
                ChangedBlocks.WHITE_LATEX_BLOCK.get() -> {
                    if (level.random.nextInt(4) == 0 && event.player?.isCreative == false) {
                        if (level is Level) {
                            event.isCanceled = true
                            level.setBlock(
                                event.pos, ModBlocks.WHITE_LATEX_BLOCK_V2.get().defaultBlockState(),
                                Block.UPDATE_NEIGHBORS or Block.UPDATE_CLIENTS or Block.UPDATE_IMMEDIATE
                            )
                            val newNotCanDismountBoat = ModEntities.NOT_CAN_DISMOUNT_BOAT.get().create(level)
                            if (newNotCanDismountBoat != null) {
                                newNotCanDismountBoat.setPos(event.pos.x + 0.5, event.pos.y + 0.5, event.pos.z + 0.5)
                                level.addFreshEntity(newNotCanDismountBoat)
                                if (level.random.nextBoolean()) {
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
    fun onLeftClickBlock(event: PlayerInteractEvent.LeftClickBlock) {
        val level = event.level
        if (level.isClientSide) return
        (level.getBlockEntity(event.pos) as? BBlockEntity) then {
            if (seatedEntity?.id != event.entity.id) {
                Block.dropResources(level.getBlockState(event.pos), level, event.pos, null)
                level.destroyBlock(event.pos, false, event.entity)
            }
        }
    }

    fun onPlayerCloned(event: PlayerEvent.Clone) {
        val newEntity = event.entity
        if (newEntity is IPlayerExtendedData) {
            val oldEntity  = event.original
            if (oldEntity is IPlayerExtendedData) {
                newEntity.mPlayerExtendedData.haState.deserialize(oldEntity.mPlayerExtendedData.haState.serialize())
            }
        }
    }
    fun onPlayerRespawn(event: PlayerEvent.PlayerRespawnEvent) {
        val player = event.entity as? ServerPlayer ?: return
        if (player is IPlayerExtendedData) {
            if (!player.level().gameRules.getBoolean(ModGameRule.KeepHA)) {
                player.haItem = ItemStack.EMPTY
                player.hasHA = false
            }
            if (!player.level().gameRules.getBoolean(ModGameRule.KeepArmorHA)) {
                player.haArmorItems = initHaArmorItems()
                player.hasArmorHA = false
            }
            if (player.transfurData?.variant?.`is`(ModTransfurVariant.PURE_WHITE_LATEX_YUFENG_BY_NCDBOAT_TRANSFUR_VARIANT) == true) {
                player.notCanDismountBoatAddArmor(player)
            }
            HaStateNetworkHandler.sendToClient(event.entity)
        }
        player.ifPlayerNotTransfurred {
            player.setPlayerTransfurVariant(
                transfurData = TransfurData(
                    variant = ModTransfurVariant.SOUL_TRANSFUR_VARIANT.get(),
                    keepConscious = true
                ),
                progress = 1f
            )
            player.playerTransfurVariantSafe?.willSurviveTransfur = true
            player.setPos(player.lastDeathLocation.getOrNull()?.pos()?.toKVec3()?.toVec3()?:player.position())
        }
    }

    fun onDimensionChange(event: PlayerEvent.PlayerChangedDimensionEvent) {
        HaStateNetworkHandler.sendToClient(event.entity as? ServerPlayer ?: return)
    }

    fun onPlayerLogin(event: PlayerEvent.PlayerLoggedInEvent) {
        // 玩家登录时同步数据
        if (!event.entity.level().isClientSide) {
            HaStateNetworkHandler.sendToClient(event.entity)
        }
    }

    fun onAccessoryDrop(event: AccessorySlots.DropItemEvent) {
        val player = event.entity as? ServerPlayer ?: return
        if (player is IPlayerExtendedData) {
            if (player.hasArmorHA && player.level().gameRules.getBoolean(ModGameRule.KeepArmorHA)) event.keepItem()
        }
    }

    // 核心摔伤处理逻辑
    fun onLivingFall(event: LivingFallEvent) {
        val entity = event.entity
        if (entity !is Player) return

        // 使用您提供的扩展属性获取兽化状态
        val variant = entity.playerTransfurVariant ?: return

        // 检查是否为纯粹的白胶狼
        if (isPureWhiteWolf(variant)) {
            // 应用摔伤免疫规则
            handleFallImmunity(event)
        }
    }

    private fun isPureWhiteWolf(variant: TransfurVariantInstance<*>): Boolean {
        // 直接比较变体注册对象
        return variant.changedEntity is PureWhiteLatexWolf
    }

    private fun handleFallImmunity(event: LivingFallEvent) {
        // 计算原版应受伤害
        val originalDamage = calculateFallDamage(event.distance, event.damageMultiplier)

        // 应用免疫规则：最多只受1点伤害
        event.damageMultiplier = if (originalDamage > 1f) {
            1f / originalDamage
        } else {
            1f
        }
    }

    private fun calculateFallDamage(distance: Float, multiplier: Float): Float {
        // 原版摔伤计算公式 (Minecraft 1.20.1)
        return Mth.clamp(distance - 3.0f, 0.0f, 40.0f) * multiplier
    }
}

fun initHaArmorItems(): MutableMap<EquipmentSlot, ItemStack> = mutableMapOf(
    EquipmentSlot.HEAD to ItemStack.EMPTY,
    EquipmentSlot.CHEST to ItemStack.EMPTY,
    EquipmentSlot.LEGS to ItemStack.EMPTY,
    EquipmentSlot.FEET to ItemStack.EMPTY
)

fun Player.movePosToTarget(
    livingEntity: LivingEntity
) {
    setPos(livingEntity.x, livingEntity.y, livingEntity.z)
    xRot = livingEntity.xRot
    yRot = livingEntity.yRot
}

fun Player.moveItemToTarget(sourceEntity: Player) {
    // 1. 先保存源玩家的 selected 槽位（int 类型，直接赋值）
    val sourceSelected = sourceEntity.inventory.selected

    // 2. 遍历并直接替换当前玩家的 ItemStack（不修改列表结构）
    for (i in 0 until inventory.items.size) {
        inventory.items[i] = sourceEntity.inventory.items[i].copy()  // 使用 copy() 避免引用问题
    }
    inventory.selected = sourceSelected  // 直接赋值 selected

    for (i in 0 until inventory.offhand.size) {
        inventory.offhand[i] = sourceEntity.inventory.offhand[i].copy()
    }

    for (i in 0 until inventory.armor.size) {
        inventory.armor[i] = sourceEntity.inventory.armor[i].copy()
    }

    // 3. 清空源玩家的 inventory（同样仅修改，不添加/删除）
    for (i in 0 until sourceEntity.inventory.items.size) {
        sourceEntity.inventory.items[i] = ItemStack.EMPTY
    }
    sourceEntity.inventory.selected = 0

    for (i in 0 until sourceEntity.inventory.offhand.size) {
        sourceEntity.inventory.offhand[i] = ItemStack.EMPTY
    }

    for (i in 0 until sourceEntity.inventory.armor.size) {
        sourceEntity.inventory.armor[i] = ItemStack.EMPTY
    }

    inventory.armor = inventory.armor
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

// 扩展函数：专门发送头部旋转更新
fun ServerPlayer.sendHeadRotationUpdate(yHeadRot: Float) {
    // 将yHeadRot转换为包需要的字节（范围：0-256 代表 0-360 度）
    val byteYHeadRot = (yHeadRot * 256.0f / 360.0f).toInt().toByte()
    connection.send(ClientboundRotateHeadPacket(this, byteYHeadRot))
}

// 在需要更新头部旋转的地方使用：
fun syncHeadLookAt(livingEntity: LivingEntity, attacker: Entity) {
    if (livingEntity.level().isClientSide) return

    // 计算头部应该转向的位置（使用attacker的眼睛位置）
    val targetPos = attacker.getEyePosition(1.0f)
    livingEntity.lookAt(Anchor.EYES, targetPos)

    // 获取计算后的头部旋转（yHeadRot）
    val headRot = livingEntity.yHeadRot // 注意：这是头部旋转

    when (livingEntity) {
        is ServerPlayer -> {
            // 对于玩家，使用我们扩展的同步函数
            livingEntity.sendHeadRotationUpdate(headRot)
            // 重置客户端预测
            livingEntity.connection.resetPosition()
        }
        else -> {
            // 对于非玩家实体，广播给追踪这个实体的所有客户端
            (livingEntity.level() as? ServerLevel)?.chunkSource?.broadcast(livingEntity,
                ClientboundRotateHeadPacket(
                    livingEntity,
                    (headRot * 256.0f / 360.0f).toInt().toByte()
                )
            )
        }
    }
}
fun ServerPlayer.sendAbilitiesUpdate() {
    val clientBoundPlayerAbilitiesPacket = ClientboundPlayerAbilitiesPacket(abilities)
    connection.send(clientBoundPlayerAbilitiesPacket)
}

fun LivingEntity.faceEntity(attackTarget: Entity) {
    val dx = attackTarget.x - this.x
    val dz = attackTarget.z - this.z
    val dy = attackTarget.eyeY - this.eyeY

    // 计算 yaw（水平方向）
    val targetYaw = Mth.atan2(dz, dx) * (180.0f / Math.PI).toFloat() - 90.0f

    // 计算 pitch（垂直方向），注意负号！
    val distanceXZ = sqrt(dx * dx + dz * dz)
    val targetPitch = Mth.atan2(dy, distanceXZ) * (180.0f / Math.PI).toFloat()

    // 设置旋转
    this.xRot = targetPitch.toFloat()
    this.yRot = targetYaw.toFloat()

    // 同步头部朝向
    this.yHeadRot = targetYaw.toFloat()
}