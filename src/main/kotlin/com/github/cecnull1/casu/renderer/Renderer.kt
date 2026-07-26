package com.github.cecnull1.casu.renderer

import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.model.UserHumanModel
import com.github.cecnull1.cecnull1_changed_plus_v2.renderer.UserEntityRenderer
import com.github.cecnull1.cecnull1_changed_plus_v2.renderer.newrl
import net.ltxprogrammer.changed.entity.ChangedEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider

class ExpieGenericRenderer<T: ChangedEntity>(context: EntityRendererProvider.Context): UserEntityRenderer<T, UserHumanModel<T>>(
    context,
    UserHumanModel(context.bakeLayer(
        UserHumanModel.LAYER_LOCATION_ALEX
    )),
    UserHumanModel.ArmorModel.MODEL_SET, newrl(MODID, "textures/entities/lnvincible.png"),
    0.9375f*(3/4.0f)
)