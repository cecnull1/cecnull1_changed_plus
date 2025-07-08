package com.github.cecnull1.cecnull1_changed_plus_v2.psi

import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModTransfurVariant
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.ParamTransfurVariant
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.TransfurData
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.entityVariant
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.transfur
import net.ltxprogrammer.changed.entity.variant.TransfurVariant
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import vazkii.psi.api.spell.*
import vazkii.psi.api.spell.param.ParamEntity
import vazkii.psi.api.spell.piece.PieceOperator
import vazkii.psi.api.spell.piece.PieceTrick

open class PieceTrickTransfurLivingEntity(spell: Spell) : PieceTrick(spell) {
    private lateinit var targetParam: SpellParam<Entity>
    private lateinit var transfurVariantParam: SpellParam<TransfurVariant<*>>

    override fun initParams() {
        targetParam = ParamEntity("target", SpellParam.RED, false, false)
        transfurVariantParam = ParamTransfurVariant("transfurVariant", SpellParam.GREEN, true, false)
        addParam(targetParam)
        addParam(transfurVariantParam)
    }

    override fun addToMetadata(meta: SpellMetadata?) {
        meta?.addStat(EnumSpellStat.COST, 1)
    }

    override fun execute(context: SpellContext?): Any? {
        val target = getParamValue(context, targetParam)
        val transfurVariant = getParamValue(context, transfurVariantParam)
        if (target is LivingEntity) {
            target.transfur(TransfurData(
                variant = transfurVariant ?: ModTransfurVariant.PURE_WHITE_LATEX_YUFENG_TRANSFUR_VARIANT.get(),
                keepConscious = true
            ))
        }
        return null
    }

    override fun getEvaluationType(): Class<*>? = Void.TYPE
}

open class PieceOperatorEntityGetTransfurVariant(spell: Spell) : PieceOperator(spell) {
    private lateinit var targetParam: SpellParam<Entity>

    override fun getEvaluationType(): Class<*>? = TransfurVariant::class.java

    override fun initParams() {
        targetParam = ParamEntity("target", SpellParam.RED, false, false)
        addParam(targetParam)
    }

    override fun addToMetadata(meta: SpellMetadata?) {
        meta?.addStat(EnumSpellStat.COST, 1)
    }

    override fun execute(context: SpellContext?): Any? {
        val target = getParamValue(context, targetParam) as? LivingEntity ?: return ModTransfurVariant.PURE_WHITE_LATEX_YUFENG_TRANSFUR_VARIANT.get()
        return target.entityVariant ?: ModTransfurVariant.PURE_WHITE_LATEX_YUFENG_TRANSFUR_VARIANT.get()
    }
}