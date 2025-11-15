package com.github.cecnull1.cecnull1_changed_plus_v2.utils

import com.github.cecnull1.cecnull1_cforge.core.IComponent
import com.mojang.serialization.Codec

interface IPersistent : IComponent {
    val codec: Codec<out IPersistent>
}