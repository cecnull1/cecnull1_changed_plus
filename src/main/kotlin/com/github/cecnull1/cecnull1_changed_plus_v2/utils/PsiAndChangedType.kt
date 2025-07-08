package com.github.cecnull1.cecnull1_changed_plus_v2.utils

import net.ltxprogrammer.changed.entity.variant.TransfurVariant
import vazkii.psi.api.spell.param.ParamSpecific

class ParamTransfurVariant(
    name: String,
    color: Int,
    canDisable:
    Boolean,
    constant: Boolean
) : ParamSpecific<TransfurVariant<*>>(
    name,
    color,
    canDisable,
    constant
) {
    override fun getRequiredType(): Class<TransfurVariant<*>> {
        return TransfurVariant::class.java
    }
}

class ParamTransfurData(
    name: String,
    color: Int,
    canDisable:
    Boolean,
    constant: Boolean
) : ParamSpecific<TransfurData>(
    name,
    color,
    canDisable,
    constant
) {
    override fun getRequiredType(): Class<TransfurData> {
        return TransfurData::class.java
    }
}