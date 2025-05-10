package com.github.cecnull1.cecnull1_changed_plus.block

import com.github.cecnull1.cecnull1_changed_plus.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus.entity.ModTransfurVariant
import net.ltxprogrammer.changed.block.ChangedBlock
import net.ltxprogrammer.changed.entity.TransfurCause
import net.ltxprogrammer.changed.entity.TransfurContext
import net.ltxprogrammer.changed.process.ProcessTransfur
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModBlocks {
    const val A_BLOCK_ID = "a_block"

    val REGISTER: DeferredRegister<Block> = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID)
    val A_BLOCK: RegistryObject<ABlock> = REGISTER.register(A_BLOCK_ID) { ABlock() }
}

class ABlock : ChangedBlock(Properties.of(Material.WATER)) {
    override fun stepOn(p_152431_: Level, p_152432_: BlockPos, p_152433_: BlockState, p_152434_: Entity) {
        super.stepOn(p_152431_, p_152432_, p_152433_, p_152434_)
        entityInside(p_152433_, p_152431_, p_152432_, p_152434_)
    }

    @Deprecated("Deprecated in Java")
    override fun entityInside(p_60495_: BlockState, p_60496_: Level, p_60497_: BlockPos, p_60498_: Entity) {
        super.entityInside(p_60495_, p_60496_, p_60497_, p_60498_)
        ProcessTransfur.transfur(
            p_60498_ as? LivingEntity ?: return,
            p_60496_,
            ModTransfurVariant.A_ENTITY_TRANSFUR_VARIANT.get(),
            true,
            TransfurContext.hazard(TransfurCause.WHITE_LATEX))
    }
}