package com.github.cecnull1.cecnull1_changed_plus.utils

import net.ltxprogrammer.changed.entity.ChangedEntity
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

/**
 * 封装与Transfur有关的数据，可直接传递给特定方法
 *
 * @param variant 已注册的TransfurVariant对象
 * @param keepConscious 是否保持意识
 * @param context 兽化的来源
 * */
data class TransfurData(val variant: TransfurVariant<*>, val keepConscious: Boolean = false, val context: TransfurContext = TransfurContext.hazard(
    TransfurCause.DEFAULT
)) {
    companion object {
        /**
         * 提供了对Player进行对变体数据的获取与修改
         * */
        var Player.transfurData
            inline get(): TransfurData? {
                val playerVariant = this.entityVariant ?: return null
                val playerVariantInstance = this.playerTransfurVariantSafe ?: return null
                return TransfurData(
                    playerVariant,
                    playerVariantInstance.willSurviveTransfur,
                    playerVariantInstance.transfurContext
                )
            }
            inline set(value) {
                setPlayerTransfurVariant(
                    transfurData = value,
                    progress = this.playerTransfurVariantSafe?.transfurProgression?:0f,
                    temporaryFromSuit = this.playerTransfurVariantSafe?.isTemporaryFromSuit == true)
            }

        /**
         * 将 TransfurVariant 转换为 TransfurData
         *
         * 如果 TransfurVariant 为 空的，则返回 null
         * */
        fun TransfurVariant<*>?.toTransfurDataOrNull(
            keepConscious: Boolean = false,
            context: TransfurContext = TransfurContext.hazard(TransfurCause.DEFAULT)
        ): TransfurData? {
            return TransfurData(this ?: return null, keepConscious, context)
        }

        /**
         * 将 TransfurVariant 转换为 TransfurData
         * */
        fun TransfurVariant<*>.toTransfurData(
            keepConscious: Boolean = false,
            context: TransfurContext = TransfurContext.hazard(TransfurCause.DEFAULT)
        ): TransfurData {
            return TransfurData(this, keepConscious, context)
        }
    }
}

fun LivingEntity.transfur(transfurData: TransfurData) =
    ProcessTransfur.transfur(this, this.level, transfurData.variant, transfurData.keepConscious, transfurData.context)

fun LivingEntity.progressTransfur(amount: Float, transfurData: TransfurData) = ProcessTransfur.progressTransfur(this, amount, transfurData.variant, transfurData.context)

fun LivingEntity.changeTransfur(transfurData: TransfurData): LivingEntity? = ProcessTransfur.changeTransfur(this, transfurData.variant)

inline val LivingEntity.entityTransfurTolerance
    get() = ProcessTransfur.getEntityTransfurTolerance(this)

inline val LivingEntity.entityAttackItem
    get(): ItemStack? = ProcessTransfur.getEntityAttackItem(this)

inline val LivingEntity.hasVariant
    get() = ProcessTransfur.hasVariant(this)

inline val LivingEntity.entityVariant
    get() = ProcessTransfur.getEntityVariant(this).getOrNull()

inline val Player.playerTransfurVariantSafe
    get() = ProcessTransfur.getPlayerTransfurVariantSafe(this).getOrNull()

inline val Player.playerTransfurProgress
    get() = ProcessTransfur.getPlayerTransfurProgress(this)

inline val Player.playerTransfurVariant
    get(): TransfurVariantInstance<*>? = ProcessTransfur.getPlayerTransfurVariant(this)

inline val Player.isPlayerPermTransfurred
    get() = ProcessTransfur.isPlayerPermTransfurred(this)

inline val Player.isPlayerTransfurred
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
    val variant = this.playerTransfurVariant
    if (variant != null) block(variant)
    return variant != null
}

inline fun Player.ifPlayerNotTransfurred(block: () -> Unit): Boolean {
    val variant = this.playerTransfurVariant
    if (variant == null) block()
    return variant == null
}

inline fun <R> Player.withTransfurred(
    block: TransfurVariantInstance<*>.() -> R
): R? = this.playerTransfurVariant?.block()

object TransfurContextUtils {
    sealed class TransfurContextType {
        object Hazard : TransfurContextType()
        data class PlayerLatexHazard(val player: Player) : TransfurContextType()
        data class NpcLatexHazard(val npc: ChangedEntity) : TransfurContextType()
    }

    fun TransfurCause.toTransfurContext(type: TransfurContextType = TransfurContextType.Hazard): TransfurContext = when (type) {
        is TransfurContextType.Hazard -> TransfurContext.hazard(this)
        is TransfurContextType.PlayerLatexHazard -> TransfurContext.playerLatexHazard(type.player, this)
        is TransfurContextType.NpcLatexHazard -> TransfurContext.npcLatexHazard(type.npc, this)
    }
}

interface VariantTickPlusAble {
    /**
     * 为 ChangedEntity 的类提供扩展的 VariantTick
     * 以解决原始的 VariantTick 无法处理对于玩家的情况的问题
     *
     * @param player 将要被处理的玩家对象
     * @param level 当前世界对象
     * */
    fun playerVariantTick(player: Player, level: Level?) {
        return
    }
}