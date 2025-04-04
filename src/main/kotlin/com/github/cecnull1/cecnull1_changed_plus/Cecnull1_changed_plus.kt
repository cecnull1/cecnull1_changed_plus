package com.github.cecnull1.cecnull1_changed_plus

import com.github.cecnull1.cecnull1_changed_plus.entity.Inits
import com.github.cecnull1.cecnull1_changed_plus.entity.modEventBus
import net.minecraftforge.fml.common.Mod


const val MODID = "cecnull1_changed_plus"

@Mod(MODID)
class Cecnull1_changed_plus {
    init {
        Inits.ENTITIES.register(modEventBus)
        Inits.TRANSFUR_VARIANTS.register(modEventBus)
    }
}