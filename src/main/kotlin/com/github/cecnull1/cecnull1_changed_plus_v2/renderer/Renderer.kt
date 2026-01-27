package com.github.cecnull1.cecnull1_changed_plus_v2.renderer

import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.FrezoMS
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.Lnvincible
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.Special
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.TianLing
import com.github.cecnull1.cecnull1_changed_plus_v2.model.UserHumanModel
import com.mojang.blaze3d.vertex.PoseStack
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer
import net.ltxprogrammer.changed.client.renderer.layers.DarkLatexMaskLayer
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModelSet
import net.ltxprogrammer.changed.client.renderer.model.armor.LatexHumanoidArmorModel
import net.ltxprogrammer.changed.entity.BasicPlayerInfo
import net.ltxprogrammer.changed.entity.ChangedEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity

//class SoulRenderer(context: EntityRendererProvider.Context): AdvancedHumanoidRenderer<Soul, SoulModel, ArmorHumanModel<Soul>>(
//    context,
//    SoulModel(context.bakeLayer(SoulModel.LAYER_LOCATION)),
//    ArmorHumanModel.MODEL_SET, 0.5f
//) {
//    override fun getTextureLocation(entity: Soul): ResourceLocation {
//        return TEXTURE
//    }
//
//    companion object {
//        private val TEXTURE = newrl(MODID, "textures/entities/soul.png")
//    }
//}

class NoneEntityRenderer<T: Entity>(context: EntityRendererProvider.Context)
    : EntityRenderer<T>(context) {

    // 不渲染任何内容
    override fun render(
        entity: T,
        yaw: Float,
        partial: Float,
        stack: PoseStack,
        buffers: MultiBufferSource,
        light: Int
    ) {
        // 完全空实现
    }

    // 返回null避免默认名称渲染
    override fun getTextureLocation(entity: T): ResourceLocation {
        return newrl(MODID, "")
    }
}

abstract class UserEntityRenderer<
        T : ChangedEntity,
        R : AdvancedHumanoidModel<T>,
        >(
    context: EntityRendererProvider.Context,
    main: R,
    modelSet: ArmorModelSet<in T, out LatexHumanoidArmorModel<in T, *>>,
    open val location: ResourceLocation,
    val f: Float = 0.9375f,
) : AdvancedHumanoidRenderer<T, R>(
    context,
    main,
    modelSet,
    .5f
) {
    init {
        this.addLayer(LatexParticlesLayer(this, getModel()))
        this.addLayer(TransfurCapeLayer.normalCape(this, context.modelSet))
        this.addLayer(DarkLatexMaskLayer(this, context.modelSet))
        this.addLayer(GasMaskLayer(this, context.modelSet))
    }

    override fun scale(entity: T, pose: PoseStack, partialTick: Float) {
        pose.scale(f, f, f)
    }

    override fun scaleForBPI(entity: T, bpi: BasicPlayerInfo?, poseStack: PoseStack?) {
    }

    override fun getTextureLocation(t: T): ResourceLocation {
        return location
    }

    override fun getModel(): R {
        return super.getModel()
    }
}

abstract class UserSAHumanEntityRenderer<T : ChangedEntity>(
    context: EntityRendererProvider.Context,
    location: ResourceLocation,
    isAlex: Boolean
) : UserEntityRenderer<T, UserHumanModel<T>>(
    context,
    UserHumanModel<T>(context.bakeLayer(
        if (isAlex) UserHumanModel.LAYER_LOCATION_ALEX else UserHumanModel.LAYER_LOCATION_STEVE
    )),
    UserHumanModel.ArmorModel.MODEL_SET, location
)

class FrezoMSRenderer(context: EntityRendererProvider.Context): UserSAHumanEntityRenderer<FrezoMS>(
    context,
    location = newrl(MODID, "textures/entities/frezo_ms.png"),
    isAlex = false
)

class TianLingRenderer(context: EntityRendererProvider.Context): UserSAHumanEntityRenderer<TianLing>(
    context,
    location = newrl(MODID, "textures/entities/tian_ling.png"),
    isAlex = true
)

class LnvincibleRenderer(context: EntityRendererProvider.Context): UserSAHumanEntityRenderer<Lnvincible>(
    context,
    location = newrl(MODID, "textures/entities/lnvincible.png"),
    isAlex = true
)

class GenericRenderer<T: ChangedEntity>(context: EntityRendererProvider.Context): UserSAHumanEntityRenderer<T>(
    context,
    location = newrl(MODID, "textures/entities/frezo_ms.png"),
    isAlex = false
)

class SpecialRenderer(
    context: EntityRendererProvider.Context,
    location: ResourceLocation = newrl("minecraft", ""),
    isAlex: Boolean = false
): UserSAHumanEntityRenderer<Special>(context, location, isAlex) {

    val modelO: UserHumanModel<Special> = model
    private val alex = UserHumanModel<Special>(context.bakeLayer(UserHumanModel.LAYER_LOCATION_ALEX))
    private val steve = UserHumanModel<Special>(context.bakeLayer(UserHumanModel.LAYER_LOCATION_STEVE))

    override fun getTextureLocation(t: Special): ResourceLocation {
        return (if (t.isPlayerSkin) t.getSkinTextureLocation() else ResourceLocation.tryParse(t.location)) ?: location
    }

    override fun render(
        latex: Special,
        yRot: Float,
        p_115457_: Float,
        p_115458_: PoseStack,
        bufferSource: MultiBufferSource,
        p_115460_: Int
    ) {
        if (!latex.isPlayerSkin) this.model = if (latex.isAlex) alex else steve
        else this.model = this.modelO
        super.render(latex, yRot, p_115457_, p_115458_, bufferSource, p_115460_)
    }

    companion object {
        fun forModelSize(slim: Boolean): EntityRendererProvider<Special> {
            return EntityRendererProvider { context: EntityRendererProvider.Context -> SpecialRenderer(context, isAlex = slim) }
        }
    }
}

fun newrl(namespace: String, path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, path)