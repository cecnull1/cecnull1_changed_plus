package com.github.cecnull1.cecnull1_changed_plus.utils

import net.ltxprogrammer.changed.entity.TransfurCause
import net.ltxprogrammer.changed.entity.TransfurContext
import net.ltxprogrammer.changed.entity.variant.TransfurVariant
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance
import net.ltxprogrammer.changed.init.ChangedGameRules
import net.ltxprogrammer.changed.process.ProcessTransfur
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import kotlin.jvm.optionals.getOrNull

interface VariantTickPlusAble {
    fun playerVariantTick(player: Player, level: Level?) {
        return
    }
}

data class TransfurData(val variant: TransfurVariant<*>, val keepConscious: Boolean = false, val context: TransfurContext = TransfurContext.hazard(
    TransfurCause.DEFAULT
)) {
    companion object {
        var Player.transfurData
            get(): TransfurData? {
                val playerVariant = this.entityVariant ?: return null
                val playerVariantInstance = this.playerTransfurVariantSafe ?: return null
                return TransfurData(
                    playerVariant,
                    playerVariantInstance.willSurviveTransfur,
                    playerVariantInstance.transfurContext
                )
            }
            set(value) {
                setPlayerTransfurVariant(
                    transfurData = value,
                    progress = this.playerTransfurVariantSafe?.transfurProgression?:0f,
                    temporaryFromSuit = this.playerTransfurVariantSafe?.isTemporaryFromSuit == true)
            }
    }
}

fun LivingEntity.transfur(transfurData: TransfurData) =
    ProcessTransfur.transfur(this, this.level, transfurData.variant, transfurData.keepConscious, transfurData.context)

fun LivingEntity.progressTransfur(amount: Float, transfurData: TransfurData) = ProcessTransfur.progressTransfur(this, amount, transfurData.variant, transfurData.context)

fun LivingEntity.changeTransfur(transfurData: TransfurData): LivingEntity? = ProcessTransfur.changeTransfur(this, transfurData.variant)

val LivingEntity.entityTransfurTolerance
    get() = ProcessTransfur.getEntityTransfurTolerance(this)

val LivingEntity.entityAttackItem
    get(): ItemStack? = ProcessTransfur.getEntityAttackItem(this)

val LivingEntity.hasVariant
    get() = ProcessTransfur.hasVariant(this)

val LivingEntity.entityVariant
    get() = ProcessTransfur.getEntityVariant(this).getOrNull()

val Player.playerTransfurVariantSafe
    get() = ProcessTransfur.getPlayerTransfurVariantSafe(this).getOrNull()

val Player.getPlayerTransfurProgress
    get() = ProcessTransfur.getPlayerTransfurProgress(this)

val Player.getPlayerTransfurVariant
    get(): TransfurVariantInstance<*>? = ProcessTransfur.getPlayerTransfurVariant(this)

val Player.isPlayerPermTransfurred
    get() = ProcessTransfur.isPlayerPermTransfurred(this)

val Player.isPlayerTransfurred
    get() = ProcessTransfur.isPlayerTransfurred(this)

fun Player.killPlayerByTransfur(livingEntity: LivingEntity) = ProcessTransfur.killPlayerByTransfur(this, livingEntity)
fun Player.killPlayerByAbsorption(livingEntity: LivingEntity) = ProcessTransfur.killPlayerByAbsorption(this, livingEntity)

fun Player.removePlayerTransfurVariant() = ProcessTransfur.removePlayerTransfurVariant(this)

fun Player.setPlayerTransfurVariant(
    transfurData: TransfurData?,
    progress: Float = if(this.level.gameRules.getBoolean(ChangedGameRules.RULE_DO_TRANSFUR_ANIMATION)) (0.0f) else (1.0f),
    temporaryFromSuit: Boolean = false): TransfurVariantInstance<*>? {
    return ProcessTransfur.setPlayerTransfurVariant(this, transfurData?.variant, transfurData?.context, progress, temporaryFromSuit)
}

inline fun Player.ifPlayerTransfurred(block: (variant: TransfurVariantInstance<*>) -> Unit): Boolean {
    val variant = ProcessTransfur.getPlayerTransfurVariant(this)
    if (variant != null) block(variant)
    return variant != null
}

inline fun Player.ifPlayerNotTransfurred(block: () -> Unit): Boolean {
    val variant = ProcessTransfur.getPlayerTransfurVariant(this)
    if (variant == null) block()
    return variant == null
}