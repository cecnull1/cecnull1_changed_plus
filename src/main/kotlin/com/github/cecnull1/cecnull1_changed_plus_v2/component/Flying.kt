package com.github.cecnull1.cecnull1_changed_plus_v2.component

import com.github.cecnull1.cecnull1_changed_plus_v2.utils.IPersistent
import com.mojang.serialization.Codec

object Flying: IPersistent {
    override val codec: Codec<Flying> = Codec.unit(Flying)
}