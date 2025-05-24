package com.github.cecnull1.cecnull1_changed_plus.item

import com.github.cecnull1.cecnull1_changed_plus.block.ModBlocks
import com.github.cecnull1.cecnull1_changed_plus.constant.Constant.MODID
import com.github.cecnull1.cecnull1lib.utils.enchantment.EnchantmentUtils.hasEnchantment
import net.ltxprogrammer.changed.init.ChangedAccessorySlots
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterials
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemNameBlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.Level
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject


object ModItems {
    const val ABLOCK_ID = "a_block"
    const val AITEM_ID = "a_item"
    const val A_BUCKET_ID = "a_bukket"
    const val A_ARMOR_ID = "a_armor"

    val REGISTER: DeferredRegister<Item> = DeferredRegister.create(ForgeRegistries.ITEMS, MODID)
    val ABLOCK: RegistryObject<ABlock> = REGISTER.register(ABLOCK_ID) {
        ABlock()
    }
    val A_ARMOR: RegistryObject<AArmorItem> = REGISTER.register(A_ARMOR_ID) {
        AArmorItem()
    }
//    val aBucketItem = REGISTER.register(A_BUCKET_ID) {
//        ABucketItem()
//    }
}

class ABlock : ItemNameBlockItem(ModBlocks.A_BLOCK.get(), Properties().tab(CreativeModeTab.TAB_MISC))

class ABucketItem : Item(Properties().tab(CreativeModeTab.TAB_MISC))

class AArmorItem : ArmorItem(ArmorMaterials.IRON, EquipmentSlot.CHEST, Properties().tab(CreativeModeTab.TAB_MISC)) {
    override fun canElytraFly(stack: ItemStack?, entity: LivingEntity?): Boolean {
        return true
    }

    override fun onArmorTick(stack: ItemStack?, level: Level?, player: Player?) {
        super.onArmorTick(stack, level, player)
        stack?.let {
            if (!hasEnchantment(it, Enchantments.BINDING_CURSE)) {
                it.enchant(Enchantments.BINDING_CURSE, 1)
            }
        }
    }
}