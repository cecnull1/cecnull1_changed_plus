package com.github.cecnull1.cecnull1_changed_plus_v2.item

import com.github.cecnull1.cecnull1_changed_plus_v2.block.ModBlocks
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.ICanTakeOff
import net.ltxprogrammer.changed.data.AccessorySlotContext
import net.ltxprogrammer.changed.data.AccessorySlotType
import net.ltxprogrammer.changed.item.LabCoatItem
import net.ltxprogrammer.changed.item.WetsuitItem
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
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
    val WHITE_LATEX_BLOCK_V2: RegistryObject<WhiteLatexBlockV2Item> = REGISTER.register(ModBlocks.WHITE_LATEX_BLOCK_V2_ID) {
        WhiteLatexBlockV2Item()
    }
}

class ABlock : ItemNameBlockItem(ModBlocks.A_BLOCK.get(), Properties())

class BBlockItem : ItemNameBlockItem(ModBlocks.BBLOCK.get(), Properties())

class WhiteLatexBlockV2Item : ItemNameBlockItem(
    ModBlocks.WHITE_LATEX_BLOCK_V2.get(), Properties()
)