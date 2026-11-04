package com.github.cecnull1.adeplike.api

import net.minecraft.nbt.CompoundTag
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeInstance
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentCategory
import net.minecraft.world.item.enchantment.EnchantmentInstance
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

class ADEPAttribute(string: String, number: Double): Attribute(string, number)
class ADEPEnchant: Enchantment(Rarity.COMMON, ADEPEnchantmentCategory.ADEP_COMMON,
    EquipmentSlot.entries.toTypedArray()
)

class ADEPMobEffect: MobEffect(MobEffectCategory.NEUTRAL, 0x7fdf7f)

data class OperValue(
    val attribute: AttributeInstance?,
    val enchantMap: List<Pair<EquipmentSlot, Int>>,
    val effect: MobEffectInstance?
) {
    companion object {
        fun LivingEntity.getOper(operKey: OperKey): OperValue =
            OperValue(
                this.getAttribute(operKey.attributeKey.get()),
                this.allSlots.map {
                    LivingEntity.getEquipmentSlotForItem(it) to it.getEnchantmentLevel(operKey.enchantKey.get())
                },
                this.getEffect(operKey.effectKey.get())
            )

        fun LivingEntity.getOperNoItem(operKey: OperKey): OperValue =
            OperValue(
                this.getAttribute(operKey.attributeKey.get()),
                listOf(),
                this.getEffect(operKey.effectKey.get())
            )

        fun LivingEntity.itemEnchantLevelSendToNBT(compoundTag: CompoundTag, vararg operKey: OperKey) {
            this.allSlots.forEach {
                compoundTag.put(LivingEntity.getEquipmentSlotForItem(it).name, CompoundTag().apply {
                    operKey.forEach { key ->
                        putInt(key.enchantKey.id.toString(), it.getEnchantmentLevel(key.enchantKey.get()))
                    }
                })
            }
        }
    }
}

data class OperKey(
    val attributeKey: RegistryObject<Attribute>,
    val enchantKey: RegistryObject<Enchantment>,
    val effectKey: RegistryObject<MobEffect>
)

class OperRegistry(
    val attributeRegistry: DeferredRegister<Attribute>,
    val enchantRegistry: DeferredRegister<Enchantment>,
    val effectRegistry: DeferredRegister<MobEffect>,
    val modid: String
) {
    fun register(bus: IEventBus) {
        attributeRegistry.register(bus)
        enchantRegistry.register(bus)
        effectRegistry.register(bus)
    }

    fun register(path: String): OperKey = OperKey(
        attributeRegistry.register(path) { ADEPAttribute("attribute.$modid.$path", 0.0).setSyncable(true) },
        enchantRegistry.register(path, ::ADEPEnchant),
        effectRegistry.register(path, ::ADEPMobEffect)
    )

    companion object {
        fun create(modid: String): OperRegistry = OperRegistry(
            DeferredRegister.create(ForgeRegistries.ATTRIBUTES, modid),
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, modid),
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, modid),
            modid = modid
        )
    }
}

object ADEPEnchantmentCategory {
    val ADEP_COMMON: EnchantmentCategory = EnchantmentCategory.create("ADEP_COMMON", fun(_) = true)
}