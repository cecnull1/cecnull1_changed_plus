package com.github.cecnull1.cecnull1_changed_plus.damage

import com.github.cecnull1.cecnull1_changed_plus.damage.DamageSource.SOUL_ATTACK_ID
import net.minecraft.world.damagesource.DamageSource

object DamageSource {
    const val SOUL_ATTACK_ID = "soul_attack"

    val SOUL_ATTACK: DamageSource = SoulAttack().bypassMagic().bypassArmor().bypassInvul()
}

class SoulAttack : DamageSource(SOUL_ATTACK_ID) {
}