package com.github.cecnull1.cecnull1_changed_plus_v2.item

import com.github.cecnull1.cecnull1_changed_plus_v2.block.ModBlocks
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1lib.utils.EnchantmentUtils.hasEnchantment
import com.github.cecnull1.cecnull1lib.utils.changed.removePlayerTransfurVariant
import net.ltxprogrammer.changed.data.AccessorySlotContext
import net.ltxprogrammer.changed.data.AccessorySlotType
import net.ltxprogrammer.changed.item.BloodSyringe
import net.ltxprogrammer.changed.item.LabCoatItem
import net.ltxprogrammer.changed.item.WetsuitItem
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
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
    val NOT_CAN_TAKE_OFF_WETSUIT: RegistryObject<NotCanTakeOffWetsuit> = REGISTER.register("not_can_take_off_wetsuit") {
        NotCanTakeOffWetsuit()
    }
    val UN_TRANSFUR_SYRINGE: RegistryObject<UnTransfurSyringe> = REGISTER.register("un_transfur_syringe") {
        UnTransfurSyringe(Item.Properties())
    }
    val NOT_CAN_TAKE_OFF_LAB_COAT: RegistryObject<NotCanTakeOffLabCoat> = REGISTER.register("not_can_take_off_lab_coat") {
        NotCanTakeOffLabCoat()
    }
}

class ABlock : ItemNameBlockItem(ModBlocks.A_BLOCK.get(), Properties())

class AArmorItem : ArmorItem(ArmorMaterials.IRON, Type.CHESTPLATE, Properties()) {
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

class NotCanTakeOffWetsuit : WetsuitItem() {

    // 性能优化：避免每tick检查
    override fun accessoryTick(slotContext: AccessorySlotContext<*>) {
        super.accessoryTick(slotContext)
        val itemStack = slotContext.stack ?: return

        // 仅当物品第一次被穿戴时添加诅咒
        if (itemStack.enchantmentTags.isEmpty) {
            itemStack.enchant(Enchantments.BINDING_CURSE, 1)
        }
    }

    // 完全保留您原始纹理实现
    override fun getArmorTexture(stack: ItemStack?, entity: Entity?, slot: EquipmentSlot?, type: String?): String? {
        return "changed:textures/models/wetsuit.png"
    }
}

class NotCanTakeOffLabCoat: LabCoatItem() {
    override fun allowedInSlot(itemStack: ItemStack?, wearer: LivingEntity?, slot: AccessorySlotType?): Boolean {
        return true
    }

    override fun shouldDisableSlot(slotContext: AccessorySlotContext<*>, otherSlot: AccessorySlotType): Boolean {
        return false
    }

    override fun accessoryInteract(slotContext: AccessorySlotContext<*>) {
        val state = this.getClothingState(slotContext.stack())
        if (!state.getValue<Boolean>(CLOSED)) {
            this.setClothingState(slotContext.stack(), this.getClothingState(slotContext.stack()).cycle<Boolean?>(CLOSED))
            val changeSound = this.getEquipSound(slotContext.stack())
            if (changeSound != null) slotContext.wearer().playSound(changeSound, 1f, 1f)
        } else {
            slotContext.wearer?.let {
                if (it is ServerPlayer) {
                    it.displayClientMessage(Component.literal(
                        "你试图解开这套实验室服，但失败了。" + Constant.NI_BU_YING_GAI_CHUAN_DAI_ZHE_GE_WU_PIN_DE
                    ), true)
                }
            }
        }
    }

    override fun accessoryTick(slotContext: AccessorySlotContext<*>) {
        super.accessoryTick(slotContext)
        val itemStack = slotContext.stack ?: return
        if (this.getClothingState(itemStack).getValue<Boolean>(CLOSED) && !hasEnchantment(itemStack, Enchantments.BINDING_CURSE)) {
            itemStack.enchant(Enchantments.BINDING_CURSE, 1)
        }
    }
}

class UnTransfurSyringe(p_41383_: Properties) : BloodSyringe(p_41383_) {
    override fun m_5922_(stack: ItemStack, level: Level, entity: LivingEntity): ItemStack {
        if (entity is Player) {
            entity.removePlayerTransfurVariant()
        }
        return super.m_5922_(stack, level, entity)
    }
}