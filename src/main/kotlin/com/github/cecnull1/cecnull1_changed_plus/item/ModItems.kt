package com.github.cecnull1.cecnull1_changed_plus.item

import com.github.cecnull1.cecnull1_changed_plus.block.ModBlocks
import com.github.cecnull1.cecnull1_changed_plus.constant.Constant.MODID
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemNameBlockItem
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject


object ModItems {
    const val ABLOCK_ID = "a_block"
    const val AITEM_ID = "a_item"
    const val A_BUCKET_ID = "a_bukket"

    val REGISTER: DeferredRegister<Item> = DeferredRegister.create(ForgeRegistries.ITEMS, MODID)
    val ABLOCK: RegistryObject<ABlock> = REGISTER.register(ABLOCK_ID) {
        ABlock()
    }
//    val aBucketItem = REGISTER.register(A_BUCKET_ID) {
//        ABucketItem()
//    }
}

class ABlock : ItemNameBlockItem(ModBlocks.A_BLOCK.get(), Properties().tab(CreativeModeTab.TAB_MISC))

class ABucketItem : Item(Properties().tab(CreativeModeTab.TAB_MISC))