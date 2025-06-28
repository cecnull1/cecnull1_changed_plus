package com.github.cecnull1.cecnull1_changed_plus.block

import com.github.cecnull1.cecnull1_changed_plus.constant.Constant.Lang.BODY_WARNING
import com.github.cecnull1.cecnull1_changed_plus.constant.Constant.Lang.MESSAGE
import com.github.cecnull1.cecnull1_changed_plus.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus.constant.Constant.NBTKeys
import com.github.cecnull1.cecnull1_changed_plus.entity.ModEntities
import com.github.cecnull1.cecnull1_changed_plus.entity.ModTransfurVariant
import com.github.cecnull1.cecnull1_changed_plus.entity.PureWhiteLatexYufeng
import com.github.cecnull1.cecnull1lib.utils.nbt.getModData
import com.github.cecnull1.cecnull1lib.utils.nbt.set
import net.ltxprogrammer.changed.block.ChangedBlock
import net.ltxprogrammer.changed.block.WhiteLatexBlock
import net.ltxprogrammer.changed.block.WhiteLatexTransportInterface
import net.ltxprogrammer.changed.entity.TransfurCause
import net.ltxprogrammer.changed.entity.TransfurContext
import net.ltxprogrammer.changed.init.ChangedBlocks
import net.ltxprogrammer.changed.process.ProcessTransfur
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Material
import net.minecraft.world.level.material.MaterialColor
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModBlocks {
    const val A_BLOCK_ID = "a_block"
    const val WHITE_LATEX_BLOCK_V2_ID = "white_latex_block_v2"

    val REGISTER: DeferredRegister<Block> = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID)
    val A_BLOCK: RegistryObject<ABlock> = REGISTER.register(A_BLOCK_ID) { ABlock() }
    val WHITE_LATEX_BLOCK_V2: RegistryObject<WhiteLatexBlockV2> = REGISTER.register(WHITE_LATEX_BLOCK_V2_ID) {
        WhiteLatexBlockV2(
            Properties.copy(ChangedBlocks.WHITE_LATEX_BLOCK.get()).color(MaterialColor.QUARTZ).noOcclusion()
        )
    }
}

class ABlock : ChangedBlock(Properties.of(Material.WATER).jumpFactor(0f)) {
    override fun stepOn(p_152431_: Level, p_152432_: BlockPos, p_152433_: BlockState, p_152434_: Entity) {
        super.stepOn(p_152431_, p_152432_, p_152433_, p_152434_)
        entityInside(p_152433_, p_152431_, p_152432_, p_152434_)
    }

    @Deprecated("Deprecated in Java")
    override fun entityInside(p_60495_: BlockState, p_60496_: Level, p_60497_: BlockPos, p_60498_: Entity) {
        super.entityInside(p_60495_, p_60496_, p_60497_, p_60498_)
        val livingEntity = p_60498_ as? LivingEntity?: return
        ProcessTransfur.progressTransfur(
            livingEntity,
            4f,
            ModTransfurVariant.A_ENTITY_TRANSFUR_VARIANT.get(),
            TransfurContext.hazard(TransfurCause.LATEX_SYRINGE_FLOOR)
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
                player.displayClientMessage(TranslatableComponent(MODID + MESSAGE + BODY_WARNING), true)

                ProcessTransfur.ifPlayerTransfurred(player, {
                    if (it.parent.canGlide) {
                        playerModData[NBTKeys.FLYING] = true
                    } else {
                        player.vehicle ?: run {
                            ModEntities.A_HORSE.get().create(player.level)?.apply {
                                // 仅在 horse 非空时执行
                                setPos(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
                                persistentData[NBTKeys.BetterNeon.WFXC] = true
                                player.level.addFreshEntity(this)
                                player.startRiding(this)
                            }
                        }
                        playerModData[NBTKeys.NO_DISMOUNTING] = true
                    }
                }) {
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
                ModEntities.PURE_WHITE_LATEX_YUFENG.get(),
                level
            )
            pureWhiteLatexYufeng.setPos(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble())
            level.addFreshEntity(pureWhiteLatexYufeng)
        }
    }
}