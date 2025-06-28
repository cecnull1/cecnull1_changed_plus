package com.github.cecnull1.cecnull1_changed_plus.psi

import com.github.cecnull1.cecnull1_changed_plus.entity.ModTransfurVariant
import net.ltxprogrammer.changed.entity.TransfurCause
import net.ltxprogrammer.changed.entity.TransfurContext
import net.ltxprogrammer.changed.process.ProcessTransfur
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import vazkii.psi.api.spell.*
import vazkii.psi.api.spell.param.ParamAny
import vazkii.psi.api.spell.param.ParamEntity
import vazkii.psi.api.spell.piece.PieceTrick

open class E(spell: Spell) : PieceTrick(spell) {
    private lateinit var targetParam: SpellParam<Entity>

    override fun initParams() {
        targetParam = ParamEntity("target", SpellParam.RED, false, false)
        addParam(targetParam)
    }

    override fun addToMetadata(meta: SpellMetadata?) {
        meta?.addStat(EnumSpellStat.COST, 1)
    }

    override fun execute(context: SpellContext?): Any? {
        super.execute(context)
        val target = getParamValue(context, targetParam)
        if (target is LivingEntity) {
            ProcessTransfur.transfur(target, target.level, ModTransfurVariant.PURE_WHITE_LATEX_YUFENG_TRANSFUR_VARIANT.get(), true,
                TransfurContext.hazard(TransfurCause.DEFAULT))
        }
        return null
    }

    override fun getEvaluationType(): Class<*>? = Void.TYPE
}