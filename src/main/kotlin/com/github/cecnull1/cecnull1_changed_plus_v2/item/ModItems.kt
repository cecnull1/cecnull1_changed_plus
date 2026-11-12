package com.github.cecnull1.cecnull1_changed_plus_v2.item

import com.github.cecnull1.cecnull1_changed_plus_v2.block.ModBlocks
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.renderer.newrl
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.ICanTakeOff
import com.github.cecnull1.cecnull1lib.utils.changed.TransfurData
import com.github.cecnull1.cecnull1lib.utils.changed.TransfurData.Companion.transfurData
import com.github.cecnull1.cecnull1lib.utils.changed.ifPlayerTransfurred
import com.github.cecnull1.cecnull1lib.utils.changed.progressTransfur
import com.github.cecnull1.cecnull1lib.utils.changed.setPlayerTransfurVariant
import com.github.cecnull1.cecnull1lib.utils.changed.transfur
import net.ltxprogrammer.changed.data.AccessorySlotContext
import net.ltxprogrammer.changed.init.ChangedRegistry
import net.ltxprogrammer.changed.init.ChangedTransfurVariants
import net.ltxprogrammer.changed.item.ClothingItem
import net.ltxprogrammer.changed.item.ClothingState
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemNameBlockItem
import net.minecraft.world.item.ItemStack
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject


object ModItems {
    val REGISTER: DeferredRegister<Item> = DeferredRegister.create(ForgeRegistries.ITEMS, MODID)
    val ABLOCK: RegistryObject<ABlock> = REGISTER.register(ModBlocks.A_BLOCK_ID) {
        ABlock()
    }
    val BBLOCK_ITME: RegistryObject<BBlockItem> = REGISTER.register("b_block") {
        BBlockItem()
    }
    val LATEX_YW: RegistryObject<LatexYW> = REGISTER.register("latex_yw") {
        LatexYW()
    }
    val WHITE_LATEX_BLOCK_V2: RegistryObject<WhiteLatexBlockV2Item> = REGISTER.register(ModBlocks.WHITE_LATEX_BLOCK_V2_ID) {
        WhiteLatexBlockV2Item()
    }
}

class ABlock : ItemNameBlockItem(ModBlocks.A_BLOCK.get(), Properties())

class BBlockItem : ItemNameBlockItem(ModBlocks.BBLOCK.get(), Properties())

class WhiteLatexBlockV2Item : ItemNameBlockItem(
    ModBlocks.WHITE_LATEX_BLOCK_V2.get(), Properties()
)

class LatexYW : ClothingItem(), ICanTakeOff {
    override fun getClothingTexture(
        stack: ItemStack,
        clothingState: ClothingState,
        wearer: Entity,
        renderSlot: EquipmentSlot,
        type: String?,
        slim: Boolean
    ): ResourceLocation {
        return newrl(MODID, "textures/entities/soul.png")
    }

    override fun accessoryTick(slotContext: AccessorySlotContext<*>) {
        super.accessoryTick(slotContext)
        val wearer = slotContext.wearer
        if (wearer is Player) {
            val variant = ChangedRegistry.TRANSFUR_VARIANT.getValue(ResourceLocation.tryParse(
                slotContext.stack.orCreateTag.getString("variant")
            ))
            variant?.let { variant ->
                wearer.progressTransfur(1.0f, TransfurData(variant))
                wearer.ifPlayerTransfurred {
                    wearer.setPlayerTransfurVariant(
                        wearer.transfurData,
                        progress = 0.9f
                    )
                    slotContext.stack.count = 0
                }
            }
        }
    }

    override fun canTakeOff(player: Player, slot: Slot, item: ItemStack): Boolean {
        return false
    }
}