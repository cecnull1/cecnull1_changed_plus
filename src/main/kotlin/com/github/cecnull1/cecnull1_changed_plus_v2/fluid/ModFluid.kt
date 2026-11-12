package com.github.cecnull1.cecnull1_changed_plus_v2.fluid

import com.github.cecnull1.casu.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.block.ModBlocks
import com.github.cecnull1.cecnull1_changed_plus_v2.renderer.newrl
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.rlclass
import net.ltxprogrammer.changed.fluid.WolfGas
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.FluidState
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions
import net.minecraftforge.fluids.FluidType
import net.minecraftforge.fluids.ForgeFlowingFluid
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import java.util.function.Consumer

object ModFluid {
    val REGISTRY: DeferredRegister<Fluid> = DeferredRegister.create(ForgeRegistries.FLUIDS, MODID)
    val TYPE_REGISTRY: DeferredRegister<FluidType> = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, MODID)

    val A_FLUID: RegistryObject<AFluid.Source> = REGISTRY.register(rlclass<AFluid>()) {
        AFluid.Source()
    }

    val A_FLUID_FLOWING: RegistryObject<AFluid.Flowing> = REGISTRY.register(rlclass<AFluid>()+"_flowing") {
        AFluid.Flowing()
    }

    val A_FLUID_TYPE: RegistryObject<FluidType> = TYPE_REGISTRY.register(rlclass<AFluid>()) {
        object : FluidType(Properties.create().canSwim(true).canPushEntity(false).lightLevel(15).motionScale(2.0)) {
            override fun initializeClient(consumer: Consumer<IClientFluidTypeExtensions>) {
                consumer.accept(object : IClientFluidTypeExtensions {
                    override fun getStillTexture(): ResourceLocation =
                        newrl(MODID, "textures/entities/soul")

                    override fun getFlowingTexture(): ResourceLocation =
                        newrl(MODID, "textures/entities/soul")

                    override fun getTintColor(): Int = 0xFFFFFFFF.toInt()
                })
            }
        }
    }
}

abstract class AFluid: ForgeFlowingFluid(Properties) {
    companion object {
        val Properties: Properties = Properties(ModFluid.A_FLUID_TYPE, ModFluid.A_FLUID, ModFluid.A_FLUID_FLOWING)
            .tickRate(5)
            .block(ModBlocks.AFluidBlock)
            .explosionResistance(100f)
            .levelDecreasePerBlock(1)
    }

    class Flowing : AFluid() {
        init {
            registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7))
        }

        override fun createFluidStateDefinition(builder: StateDefinition.Builder<Fluid?, FluidState?>) {
            super.createFluidStateDefinition(builder)
            builder.add(LEVEL)
        }

        override fun getAmount(state: FluidState): Int {
            return state.getValue(LEVEL)
        }

        override fun isSource(state: FluidState): Boolean {
            return false
        }
    }

    class Source : AFluid() {
        override fun getAmount(state: FluidState): Int {
            return 8
        }

        override fun isSource(state: FluidState): Boolean {
            return true
        }
    }
}