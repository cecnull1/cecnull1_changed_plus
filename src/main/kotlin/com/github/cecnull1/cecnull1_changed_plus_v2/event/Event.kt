package com.github.cecnull1.cecnull1_changed_plus_v2.event

import com.github.cecnull1.adeplike.api.OperValue.Companion.getOper
import com.github.cecnull1.cecnull1_cforge.core.ComponentCore.addComponent
import com.github.cecnull1.cecnull1_cforge.core.ComponentCore.getComponent
import com.github.cecnull1.cecnull1_cforge.core.PipeCore.process
import com.github.cecnull1.cecnull1_cforge.core.PipeCtrl.then
import com.github.cecnull1.cecnull1_changed_plus_v2.block.BBlockEntity
import com.github.cecnull1.cecnull1_changed_plus_v2.block.ModBlocks
import com.github.cecnull1.cecnull1_changed_plus_v2.component.WFXCOwner
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.NBTKeys.BetterNeon.WFXC
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.*
import com.github.cecnull1.cecnull1_changed_plus_v2.gamerule.ModGameRule.isJiXianShiTF
import com.github.cecnull1.cecnull1_changed_plus_v2.oper.ModOper
import com.github.cecnull1.cecnull1_changed_plus_v2.packet.NetworkHandler
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.*
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.MPlayerExtendedData.Companion.haArmorItems
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.MPlayerExtendedData.Companion.haItem
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.MPlayerExtendedData.Companion.hasArmorHA
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.MPlayerExtendedData.Companion.hasHA
import com.github.cecnull1.cecnull1lib.utils.changed.*
import com.github.cecnull1.cecnull1lib.utils.changed.TransfurContextUtils.toTransfurContext
import com.github.cecnull1.cecnull1lib.utils.nbt.getModData
import com.github.cecnull1.cecnull1lib.utils.nbt.set
import com.github.cecnull1.cecnull1lib.utils.vector.toKVec3
import com.google.common.collect.Iterables
import net.ltxprogrammer.changed.entity.SeatEntity
import net.ltxprogrammer.changed.entity.TransfurCause
import net.ltxprogrammer.changed.entity.beast.PureWhiteLatexWolf
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance
import net.ltxprogrammer.changed.init.ChangedBlocks
import net.ltxprogrammer.changed.init.ChangedSounds
import net.ltxprogrammer.changed.process.ProcessTransfur
import net.minecraft.client.Minecraft
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor
import net.minecraft.core.Holder
import net.minecraft.network.protocol.game.*
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.world.damagesource.DamageTypes
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
import net.minecraftforge.event.entity.living.*
import net.minecraftforge.event.entity.player.EntityItemPickupEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.event.level.BlockEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.FORGE)
object Event {
    fun onPlayerTick(event: CPlayerTickEvent) = event.process {
        val player = event.player
        if (player.level.isClientSide && event.phase == TickEvent.Phase.END) return@process
        if (player.getModData(MODID).getBoolean(Constant.NBTKeys.BODY_WARNING)) {
            player.ifPlayerNotTransfurred {
                player.progressTransfur(1f, TransfurData(
                    ModTransfurVariant.PURE_WHITE_LATEX_YUFENG_TRANSFUR_VARIANT.get(),
                    context = TransfurCause.ATTACK_REPLICATE_LEFT.toTransfurContext()
                )
                )
            }
        }
        if (player.getOper(ModOper.hasCFlying).notZero()) {
            player.foodData.foodLevel++
            player.foodData.setSaturation(player.foodData.saturationLevel+1)
            if (!player.abilities.flying) {
                val abilities = player.abilities
                abilities.flying = true
                if (player is ServerPlayer) player.sendAbilitiesUpdate()
            }
        }

        if (player.health.isNaN()) {
            player.health = 0.0f
            if (player is ServerPlayer) player.sendHealthUpdate()
        }

        if (player.isInWater && player.getOper(ModOper.hasNotCanTakeOffWetsuitLike).notZero()) {
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
            if (!player.isInWaterOrBubble) {
                player.deltaMovement = Vec3(
                    delta.x,
                    delta.y.coerceAtMost(0.0),
                    delta.z
                )
            }
        }

        if (player.hasHA) {
            if (player.haItem.isEmpty) {
                val selectedSlot = player.inventory.selected

                val slotItem = player.inventory.items[selectedSlot]

                if (!slotItem.isEmpty) {
                    player.haItem = slotItem.copy()
                    slotItem.count = 0
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
                                Holder.direct(ChangedSounds.TRANSFUR_BY_LATEX.get()),
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

        if (player.persistentData.getBoolean(Constant.NBTKeys.BetterNeon.NORIDE2_BN_C_JH_TF)) {
            player.ifPlayerNotTransfurred {
                player.transfur(
                    TransfurData(
                        ModTransfurVariant.LATEX_PINK_HUMAN_VARIANT.get(),
                        keepConscious = true,
                        context = TransfurCause.WHITE_LATEX.toTransfurContext()
                    )
                )
            }
            player.persistentData.remove(Constant.NBTKeys.BetterNeon.NORIDE2_BN_C_JH_TF)
        }

        fun sync() {
            if (player.level.isClientSide && event.player == Minecraft.getInstance().player) {
                NetworkHandler.haStateSendToServer()
            }
        }
        sync()
    }

    @JvmStatic
    @SubscribeEvent
    fun onDead(event: LivingDeathEvent) {
        event.entity as? ServerPlayer then {

            val pos = (this.respawnPosition ?: this.level.sharedSpawnPos).toKVec3()
            (vehicle as? LivingEntity)?.let {
                Thread.startVirtualThread {
                    level.getPlayerByUUID((it as EntityExtendedComponent).components.getComponent<WFXCOwner>("WFXCOwner".toRL())?.owner?: return@startVirtualThread)?: return@startVirtualThread
                    it.changeDimension(server.getLevel(this.respawnDimension))
                    it.teleportTo(pos.x, pos.y, pos.z)
                    it.health = Float.POSITIVE_INFINITY
                }
            }
        }
    }

    fun onLivingTick(event: CLivingTickEvent) {


        if (event.phase == TickEvent.Phase.START) {
            val (at, en, ef) = event.entity.getOper(ModOper.autoMove)
            val v1 = at?.value?:0.0
            val v2 = en.sumOf { it.second }
            val v3 = ef?.amplifier?:0
            event.entity.newAutoMove(v1+v2/48.0+v3/48.0)

            val aEntityLikeValue = event.entity.getOper(ModOper.aEntityLike)
            if (aEntityLikeValue.notZero()) {
                event.entity.aEntityTick()
            }

            if (event.entity.getOper(ModOper.flyingPureWhiteLatexYufengLike).notZero()) {
                event.entity.flyingPureWhiteLatexYufengLike()
            }

            if (event.entity.getOper(ModOper.meiXiYuanLike).notZero()) {
                event.entity.meiXiYuanLikeTick()
            }
        }

        if (event.entity.level.isClientSide) return

        (event.entity as EntityExtendedComponent).components.apply {
            getComponent<WFXCOwner>("WFXCOwner".toRL()) then {
                event.entity.level.getPlayerByUUID(this.owner) as? ServerPlayer then {
                    if (this.isAlive) {
                        startRiding(event.entity)
                    }
                }
            }
        }
    }

    fun onTravelEvent(event: LivingTravelEvent) {
//        (event.entity as EntityExtendedComponent).components.getComponent<AutoMove>("AutoMove".toRL()) then {
//            if (this.isEnabled) {
//                event.entity.autoMove(this.speed)
//            }
//        }
    }

    fun onMount(event: EntityMountEvent) {
        val entityMounting: Entity = event.entityMounting ?: return
        val entityBeingMounted: Entity = event.entityBeingMounted ?: return
        if (entityMounting.isAlive && entityBeingMounted.isAlive && event.isDismounting) {
            when {
                entityBeingMounted.persistentData.getBoolean(WFXC) -> {
                    Thread.startVirtualThread {
                        (entityBeingMounted as EntityExtendedComponent).components.addComponent("WFXCOwner".toRL(), WFXCOwner(
                            entityMounting.uuid
                        ))
                    }
                    event.isCanceled = true
                }
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
    }

    fun onHurt(event: LivingHurtEvent) {
        (event.entity as? Player)?.ifPlayerTransfurred {
        }
    }

    fun onLivingAttack(event: LivingAttackEvent) {
        ModEntities.PURE_WHITE_LATEX_YUFENG.get()
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
            // 禁止被攻击
            if (it.changedEntity is LatexPinkHuman) {
                event.isCanceled = true
            }
        }

        event.entity as? Player then player@ {
            if (this.level.isClientSide) return@player
            (this.vehicle as? SeatEntity) then {
                (this.level.getBlockEntity(this@then.attachedBlockPos) as? BBlockEntity) then {
                    if (seatedEntity?.id == this@player.id && !this@player.isCreative && event.source.type() != DamageTypes.FELL_OUT_OF_WORLD) event.isCanceled = true
                }
            }
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
//            if (!player.level.gameRules.getBoolean(ModGameRule.KeepHA)) {
//                player.haItem = ItemStack.EMPTY
//                player.getAttribute(ModOper.hasHA.attributeKey.get())?.addPermanentModifier(
//                    AttributeModifier("The HA Item", 1.0, AttributeModifier.Operation.ADDITION)
//                )
//            }
//            if (!player.level.gameRules.getBoolean(ModGameRule.KeepArmorHA)) {
//                player.haArmorItems = initHaArmorItems()
//                player.getAttribute(ModOper.hasArmorHA.attributeKey.get())?.addPermanentModifier(
//                    AttributeModifier("The ArmorHA Item", 1.0, AttributeModifier.Operation.ADDITION)
//                )
//            }
            NetworkHandler.haStateSendToClient(event.entity)
        }

        if (player.level.gameRules.getBoolean(isJiXianShiTF)) {
            player.transfur(TransfurData(
                ModTransfurVariant.LATEX_PINK_HUMAN_VARIANT.get(),
                keepConscious = true
            ))
        }
    }

    fun onDimensionChange(event: PlayerEvent.PlayerChangedDimensionEvent) {
        NetworkHandler.haStateSendToClient(event.entity as? ServerPlayer ?: return)
    }

    fun onPlayerLogin(event: PlayerEvent.PlayerLoggedInEvent) {
        // 玩家登录时同步数据
        if (!event.entity.level.isClientSide) {
            NetworkHandler.haStateSendToClient(event.entity)
        }
    }

    fun onLivingFall(event: LivingFallEvent) {
        val entity = event.entity
        if (entity !is Player) return

        val variant = entity.playerTransfurVariant ?: return

        if (isPureWhiteWolf(variant)) {
            handleFallImmunity(event)
        }
    }

    private fun isPureWhiteWolf(variant: TransfurVariantInstance<*>): Boolean {
        return variant.changedEntity is PureWhiteLatexWolf
    }

    private fun handleFallImmunity(event: LivingFallEvent) {
        val originalDamage = calculateFallDamage(event.distance, event.damageMultiplier)

        event.damageMultiplier = if (originalDamage > 1f) {
            1f / originalDamage
        } else {
            1f
        }
    }

    private fun calculateFallDamage(distance: Float, multiplier: Float): Float {
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
    for (i in inventory.items.indices) {
        inventory.items[i] = sourceEntity.inventory.items[i].copy()  // 使用 copy() 避免引用问题
    }
    inventory.selected = sourceSelected  // 直接赋值 selected

    for (i in inventory.offhand.indices) {
        inventory.offhand[i] = sourceEntity.inventory.offhand[i].copy()
    }

    for (i in inventory.armor.indices) {
        inventory.armor[i] = sourceEntity.inventory.armor[i].copy()
    }

    // 3. 清空源玩家的 inventory（同样仅修改，不添加/删除）
    for (i in sourceEntity.inventory.items.indices) {
        sourceEntity.inventory.items[i] = ItemStack.EMPTY
    }
    sourceEntity.inventory.selected = 0

    for (i in sourceEntity.inventory.offhand.indices) {
        sourceEntity.inventory.offhand[i] = ItemStack.EMPTY
    }

    for (i in sourceEntity.inventory.armor.indices) {
        sourceEntity.inventory.armor[i] = ItemStack.EMPTY
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

fun ServerPlayer.sendHeadRotationUpdate(yHeadRot: Float) {
    val byteYHeadRot = (yHeadRot * 256.0f / 360.0f).toInt().toByte()
    connection.send(ClientboundRotateHeadPacket(this, byteYHeadRot))
}

fun syncHeadLookAt(livingEntity: LivingEntity, attacker: Entity) {
    if (livingEntity.level.isClientSide) return

    // 计算头部应该转向的位置（使用attacker的眼睛位置）
    val targetPos = attacker.getEyePosition(1.0f)
    livingEntity.lookAt(Anchor.EYES, targetPos)

    // 获取计算后的头部旋转（yHeadRot）
    val headRot = livingEntity.yHeadRot // 注意：这是头部旋转

    when (livingEntity) {
        is ServerPlayer -> {
            livingEntity.sendHeadRotationUpdate(headRot)
            livingEntity.connection.resetPosition()
        }
        else -> {
            (livingEntity.level as? ServerLevel)?.chunkSource?.broadcast(livingEntity,
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