package com.github.cecnull1.cecnull1_changed_plus_v2.item

import com.github.cecnull1.cecnull1_changed_plus_v2.block.ModBlocks
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1lib.utils.EnchantmentUtils.hasEnchantment
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.*
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.Level
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject


object ModItems {
    const val A_ARMOR_ID = "a_armor"

    val REGISTER: DeferredRegister<Item> = DeferredRegister.create(ForgeRegistries.ITEMS, MODID)
    val ABLOCK: RegistryObject<ABlock> = REGISTER.register(ModBlocks.A_BLOCK_ID) {
        ABlock()
    }
    val A_ARMOR: RegistryObject<AArmorItem> = REGISTER.register(A_ARMOR_ID) {
        AArmorItem()
    }
    val WHITE_LATEX_BLOCK_V2: RegistryObject<WhiteLatexBlockV2Item> = REGISTER.register(ModBlocks.WHITE_LATEX_BLOCK_V2_ID) {
        WhiteLatexBlockV2Item()
    }
    val XIN_YUE: RegistryObject<XinYueItem> = REGISTER.register("xin_yue") {
        XinYueItem()
    }
}

class ABlock : ItemNameBlockItem(ModBlocks.A_BLOCK.get(), Properties())

class AArmorItem : ArmorItem(ArmorMaterials.IRON, ArmorItem.Type.CHESTPLATE, Properties()) {
    override fun canElytraFly(stack: ItemStack?, entity: LivingEntity?): Boolean {
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onArmorTick(stack: ItemStack?, level: Level?, player: Player?) {
        super.onArmorTick(stack, level, player)
        stack?.let {
            if (!hasEnchantment(it, Enchantments.BINDING_CURSE)) {
                it.enchant(Enchantments.BINDING_CURSE, 1)
            }
        }
    }
}

class WhiteLatexBlockV2Item : ItemNameBlockItem(
    ModBlocks.WHITE_LATEX_BLOCK_V2.get(), Properties()
)

class XinYueItem : Item(Properties()) {
    override fun hurtEnemy(
        itemStack: ItemStack,
        entity: LivingEntity,
        sourceentity: LivingEntity
    ): Boolean {
        super.hurtEnemy(itemStack, entity, sourceentity)

        return false
    }
}