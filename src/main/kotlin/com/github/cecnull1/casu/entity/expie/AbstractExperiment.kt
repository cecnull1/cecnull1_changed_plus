package com.github.cecnull1.casu.entity.expie

import com.github.cecnull1.cecnull1_changed_plus_v2.entity.get
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.set
import net.ltxprogrammer.changed.entity.AttributePresets
import net.ltxprogrammer.changed.entity.ChangedEntity
import net.ltxprogrammer.changed.entity.TransfurMode
import net.ltxprogrammer.changed.init.ChangedAttributes
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeMap
import net.minecraft.world.level.Level

/**
 * # Table:
 * - Aim Speed -> [net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE]
 * - Reload Speed -> [net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_SPEED]
 * - Move Speed -> [net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED]
 * - Health -> [net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH]
 * - Lung Capacity -> [net.ltxprogrammer.changed.init.ChangedAttributes.AIR_CAPACITY]
 * - Noticeability -> Empty
 * - Jump Force -> [net.ltxprogrammer.changed.init.ChangedAttributes.JUMP_STRENGTH]
 * - Kick Damage -> [net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE] + [net.ltxprogrammer.changed.init.ChangedAttributes.MINING_SPEED]
 * - Swim Speed -> [net.minecraftforge.common.ForgeMod.SWIM_SPEED]
 * - Run Speed -> [net.ltxprogrammer.changed.init.ChangedAttributes.SPRINT_SPEED]
 *
 * # VariantInstance:
 * - Base: [Experiment]
 * - [com.github.cecnull1.casu.entity.expie.variants.Albino]
 * - [com.github.cecnull1.casu.entity.expie.variants.Baron]
 * - [com.github.cecnull1.casu.entity.expie.variants.Chik]
 * - [com.github.cecnull1.casu.entity.expie.variants.Crystal]
 * - [com.github.cecnull1.casu.entity.expie.variants.Dune]
 * - [com.github.cecnull1.casu.entity.expie.variants.Icke]
 * - [com.github.cecnull1.casu.entity.expie.variants.Leapy]
 * - [com.github.cecnull1.casu.entity.expie.variants.Milky]
 * - [com.github.cecnull1.casu.entity.expie.variants.Orange]
 * - [com.github.cecnull1.casu.entity.expie.variants.Roza]
 * - [com.github.cecnull1.casu.entity.expie.variants.Shelly]
 * - [com.github.cecnull1.casu.entity.expie.variants.Velvet]
 * - [com.github.cecnull1.casu.entity.expie.variants.Voyager]
 * - TODO: Chompy: ??? (Wait Unlock)
 *
 * # Gameplay (gunsaw):
 * > The base for almost all other species.
 * */
abstract class AbstractExperiment(type: EntityType<out AbstractExperiment>, level: Level) : ChangedEntity(type, level) {
    override fun getTransfurMode(): TransfurMode = TransfurMode.NONE
    override fun setAttributes(attributes: AttributeMap) {
        super.setAttributes(attributes)
        AttributePresets.playerLike(attributes)
        attributes[ChangedAttributes.JUMP_STRENGTH.get()]*=7/5
        attributes[ChangedAttributes.FALL_RESISTANCE.get()]*=7/5
        Result
    }
}