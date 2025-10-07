package com.github.cecnull1.cecnull1_changed_plus_v2.block

import com.github.cecnull1.cecnull1_cforge.core.PipeCtrl.then
import com.github.cecnull1.cecnull1_changed_plus_v2.animation.Animations
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.Lang.BODY_WARNING
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.Lang.MESSAGE
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.NBTKeys
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.*
import com.github.cecnull1.cecnull1lib.utils.changed.*
import com.github.cecnull1.cecnull1lib.utils.nbt.getModData
import com.github.cecnull1.cecnull1lib.utils.nbt.set
import com.github.cecnull1.cecnull1lib.utils.vector.toKVec3
import com.github.cecnull1.cecnull1lib.utils.vector.toVec3
import net.ltxprogrammer.changed.block.ChangedBlock
import net.ltxprogrammer.changed.block.SeatableBlock
import net.ltxprogrammer.changed.block.WhiteLatexBlock
import net.ltxprogrammer.changed.block.WhiteLatexTransportInterface
import net.ltxprogrammer.changed.block.entity.SeatableBlockEntity
import net.ltxprogrammer.changed.entity.SeatEntity
import net.ltxprogrammer.changed.entity.animation.AnimationCategory
import net.ltxprogrammer.changed.init.ChangedAnimationEvents
import net.ltxprogrammer.changed.init.ChangedTransfurVariants
import net.ltxprogrammer.changed.process.ProcessTransfur
import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.datafix.fixes.References
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import com.mojang.datafixers.types.Type as MType

object ModBlocks {
    const val A_BLOCK_ID = "a_block"
    const val WHITE_LATEX_BLOCK_V2_ID = "white_latex_block_v2"

    val REGISTRY_BLOCK: DeferredRegister<Block> = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID)
    val A_BLOCK: RegistryObject<ABlock> = REGISTRY_BLOCK.register(A_BLOCK_ID) { ABlock() }
    val WHITE_LATEX_BLOCK_V2: RegistryObject<WhiteLatexBlockV2> = REGISTRY_BLOCK.register(WHITE_LATEX_BLOCK_V2_ID) {
        WhiteLatexBlockV2(
            Properties.of().apply {
                noOcclusion()
                dynamicShape()
                isSuffocating { _,_, _ -> true }
            }
        )
    }
    val BBLOCK: RegistryObject<BBlock> = REGISTRY_BLOCK.register("b_block") { BBlock() }

    val REGISTRY_BLOCKENTITY: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID)

    val BBLOCK_BLOCKENTITY: RegistryObject<BlockEntityType<BBlockEntity>> =
        REGISTRY_BLOCKENTITY.register("b_block") {
            val id = "b_block"
            val type: MType<*>? = Util.fetchChoiceType(References.BLOCK_ENTITY, id)

            BlockEntityType.Builder.of(
                { pos, state -> BBlockEntity(BBLOCK_BLOCKENTITY.get(), pos, state) },
                BBLOCK.get()
            ).build(null)
        }
}

class ABlock : ChangedBlock(Properties.of().jumpFactor(0f)) {
    override fun stepOn(level: Level, pos: BlockPos, state: BlockState, entity: Entity) {
        super.stepOn(level, pos, state, entity)
        entityInside(state, level, pos, entity)
    }

    @Deprecated("Deprecated in Java")
    override fun entityInside(state: BlockState, level: Level, pos: BlockPos, entity: Entity) {
        super.entityInside(state, level, pos, entity)
        val livingEntity = entity as? LivingEntity?: return
        livingEntity.progressTransfur(
            1f,
            TransfurData(
                ModTransfurVariant.A_ENTITY_TRANSFUR_VARIANT.get()
            )
        )
    }

    override fun onDestroyedByPlayer(
        state: BlockState?,
        level: Level?,
        pos: BlockPos,
        player: Player?,
        willHarvest: Boolean,
        fluid: FluidState?
    ): Boolean {
        if (player != null) {
            val persistentData = player.persistentData
            val playerModData = player.getModData(MODID)
            if (!playerModData.getBoolean(NBTKeys.BODY_WARNING)) {
                player.displayClientMessage(Component.translatable(MODID + MESSAGE + BODY_WARNING), true)

                player.ifPlayerTransfurred {
                    if (it.parent.canGlide && it.changedEntity !is PureWhiteLatexYufengByNCDBoat) {
                        playerModData[NBTKeys.FLYING] = true
                    } else {
                        player.vehicle ?: run {
                            ModEntities.A_HORSE.get().create(player.level())?.apply {
                                // 仅在 horse 非空时执行
                                setPos(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
                                persistentData[NBTKeys.BetterNeon.WFXC] = true
                                player.level().addFreshEntity(this)
                                player.startRiding(this)
                            }
                        }
                        playerModData[NBTKeys.NO_DISMOUNTING] = true
                    }
                }
                player.ifPlayerNotTransfurred {
                    playerModData[NBTKeys.BODY_WARNING] = true
                }
                persistentData[MODID] = playerModData
            }
        }

        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid)
    }
}

open class WhiteLatexBlockV2(properties: Properties) : WhiteLatexBlock(properties), WhiteLatexTransportInterface {
    override fun fallOn(
        level: Level,
        blockState: BlockState,
        blockPos: BlockPos,
        entity: Entity,
        distance: Float
    ) {
        super.fallOn(level, blockState, blockPos, entity, distance)
        ProcessTransfur.ifPlayerTransfurred(entity as? Player ?: return, {}) {
            val pureWhiteLatexYufeng = PureWhiteLatexYufeng(
                ModEntities.PURE_WHITE_LATEX_YUFENG_BY_NCDBOAT.get(),
                level
            )
            pureWhiteLatexYufeng.setPos(blockPos.x.toDouble()+0.5, blockPos.y.toDouble()+0.5, blockPos.z.toDouble()+0.5)
            level.addFreshEntity(pureWhiteLatexYufeng)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun getCollisionShape(
        state: BlockState,
        getter: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        return Shapes.empty()
    }

    @Deprecated("Deprecated in Java")
    override fun entityInside(state: BlockState, level: Level, pos: BlockPos, entity: Entity) {
        super.entityInside(state, level, pos, entity)
        if (entity is Player) {
            entity.ifPlayerTransfurred {
                if (it.`is`(ChangedTransfurVariants.PURE_WHITE_LATEX_WOLF.get())) {
                    entity.setPlayerTransfurVariant(
                        TransfurData(
                            ModTransfurVariant.PURE_WHITE_LATEX_YUFENG_TRANSFUR_VARIANT.get(),
                            true
                        ),
                        progress = it.transfurProgression
                    )
                }
            }
        }
    }
}

open class BBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : BlockEntity(type, pos,
    state
), SeatableBlockEntity {
    private var entityHolder: SeatEntity? = null

    override fun getEntityHolder(): SeatEntity? {
        return entityHolder
    }

    override fun setEntityHolder(p0: SeatEntity?) {
        entityHolder = p0 as? MoveEntity ?: p0?.toMoveEntity() ?: p0
    }

    fun startRiding(
        level: Level,
        state: BlockState,
        pos: BlockPos,
        entity: LivingEntity
    ) {
        if (entityHolder == null || entityHolder!!.isRemoved) entityHolder = SeatEntity.createFor(level, state, pos, false, false, false).toMoveEntity()
        if (entity.vehicle != entityHolder && entityHolder is MoveEntity && !level.isClientSide && !entityHolder!!.isRemoved) {
            if (entity.startRiding(entityHolder!!)) {
                entityHolder!!.setPos(pos.toKVec3().toVec3())
                entityHolder!!.hasImpulse = true
            }
            if (entity is ServerPlayer) {
                entity.connection.send(
                    ClientboundSetPassengersPacket(
                        entityHolder!!
                    )
                )
            }
        }
        seatedEntity then {
            ChangedAnimationEvents.broadcastEntityAnimation(
                this,
                Animations.CP_STASIS_IDLE.get(),
                AnimationCategory.IDLE,
                com.github.cecnull1.cecnull1_changed_plus_v2.animation.StasisAnimationParameters
            )
        }
    }

    override fun setRemoved() {
        super.setRemoved()
        entityHolder?.discard()
        entityHolder = null
    }
}

open class BBlock(): Block(Properties.of().destroyTime(-1.0f)), EntityBlock, SeatableBlock {
    override fun getSitOffset(p0: BlockGetter, p1: BlockState, p2: BlockPos): Vec3 {
        return Vec3(0.0, 1.0, 0.0)
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return BBlockEntity(ModBlocks.BBLOCK_BLOCKENTITY.get(), pos, state)
    }

    override fun stepOn(level: Level, pos: BlockPos, state: BlockState, entity: Entity) {
        level.getBlockEntity(pos) then {
            if (this is BBlockEntity && entity is LivingEntity) {
                this.startRiding(level, state, pos, entity)
            }
        }
    }
}