package com.github.cecnull1.cecnull1_changed_plus_v2

//import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.C_PLAYER
import com.github.cecnull1.cecnull1_changed_plus_v2.block.ModBlocks
import com.github.cecnull1.cecnull1_changed_plus_v2.cforge.event.CForgeEvent
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.A_ENTITY
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.A_HORSE
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.CEXOSKELETON
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.MISC
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.NONE_ENTITY
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.NOT_CAN_DISMOUNT_BOAT
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.PURE_WHITE_LATEX_YUFENG
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.PURE_WHITE_LATEX_YUFENG_BY_NCDBOAT
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.SOUL
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModTransfurVariant
import com.github.cecnull1.cecnull1_changed_plus_v2.event.ByForgeEvent
import com.github.cecnull1.cecnull1_changed_plus_v2.event.TakeOffEvent
import com.github.cecnull1.cecnull1_changed_plus_v2.event.onForgeEvent
import com.github.cecnull1.cecnull1_changed_plus_v2.event.onTakeOff
import com.github.cecnull1.cecnull1_changed_plus_v2.gamerule.ModGameRule
import com.github.cecnull1.cecnull1_changed_plus_v2.item.ModItems
import com.github.cecnull1.cecnull1_changed_plus_v2.model.AEntityModel
import com.github.cecnull1.cecnull1_changed_plus_v2.model.ZombieModel
import com.github.cecnull1.cecnull1_changed_plus_v2.packet.HaStateNetworkHandler
import com.github.cecnull1.cecnull1_changed_plus_v2.psi.PieceOperatorEntityGetTransfurVariant
import com.github.cecnull1.cecnull1_changed_plus_v2.psi.PieceTrickTransfurLivingEntity
import com.github.cecnull1.cecnull1_changed_plus_v2.renderer.NoneTransfurVariantRenderer
import com.github.cecnull1.cecnull1_changed_plus_v2.renderer.SoulRenderer
import net.ltxprogrammer.changed.client.renderer.DarkLatexYufengRenderer
import net.ltxprogrammer.changed.client.renderer.ExoskeletonRenderer
import net.ltxprogrammer.changed.client.renderer.accessory.SimpleClothingRenderer
import net.ltxprogrammer.changed.client.renderer.accessory.SimpleClothingRenderer.ModelComponent
import net.ltxprogrammer.changed.client.renderer.layers.AccessoryLayer
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModel
import net.ltxprogrammer.changed.entity.ChangedEntity
import net.ltxprogrammer.changed.entity.UseItemMode
import net.ltxprogrammer.changed.entity.robot.Exoskeleton
import net.ltxprogrammer.changed.init.ChangedAttributes
import net.minecraft.client.renderer.entity.BoatRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.HorseRenderer
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.animal.horse.Horse
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers
import net.minecraftforge.common.ForgeMod
import net.minecraftforge.event.entity.EntityAttributeCreationEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import vazkii.psi.api.PsiAPI


@Mod(MODID)
class Cecnull1_changed_plus(context: FMLJavaModLoadingContext) {
    init {
        CForgeEvent.init()

        val modEventBus = context.modEventBus
        // 其他初始化代码...
        ModEntities.REGISTER.register(modEventBus)
        ModTransfurVariant.REGISTRY.register(modEventBus)
        ModBlocks.REGISTER.register(modEventBus)
        ModItems.REGISTER.register(modEventBus)
        ModGameRule.register()

        if (ModList.get().isLoaded("psi")) {
            PsiAPI.registerSpellPieceAndTexture(ResourceLocation(MODID, "transfur_living_entity"), PieceTrickTransfurLivingEntity::class.java)
            PsiAPI.registerSpellPieceAndTexture(ResourceLocation(MODID, "entity_get_transfur_variant"), PieceOperatorEntityGetTransfurVariant::class.java)
        }

        CForgeEvent.registerEvents<TakeOffEvent>(::onTakeOff)
        CForgeEvent.registerEvents<ByForgeEvent<*>>(::onForgeEvent)
    }
}
@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
object Events {
    @JvmStatic
    @SubscribeEvent
    fun onCommonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            HaStateNetworkHandler.register()
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun registerEntityAttributes(event: EntityAttributeCreationEvent) {
        event.put(
            A_ENTITY.get(),
            ChangedEntity.createLatexAttributes().build()
        )
        event.put(
            CEXOSKELETON.get(),
            Exoskeleton.createAttributes().build()
        )
        event.put(
            A_HORSE.get(),
            Horse.createBaseHorseAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 3.0)
                .add(ForgeMod.SWIM_SPEED.get(), 2.0)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .build()
        )
        event.put(
            SOUL.get(),
            ChangedEntity.createLatexAttributes()
                .add(Attributes.MAX_HEALTH, 1.0)
                .add(Attributes.MOVEMENT_SPEED, 0.0)
                .add(ForgeMod.SWIM_SPEED.get(), 0.0)
                .add(Attributes.ATTACK_DAMAGE, 0.01)
                .add(Attributes.JUMP_STRENGTH, 0.0)
                .add(Attributes.FLYING_SPEED)
                .build()
        )
        event.put(
            PURE_WHITE_LATEX_YUFENG.get(),
            ChangedEntity.createLatexAttributes().build()
        )
        event.put(
            NONE_ENTITY.get(),
            ChangedEntity.createLatexAttributes().build()
        )
        event.put(
            MISC.get(),
            ChangedEntity.createLatexAttributes()
                .add(Attributes.MAX_HEALTH, 24.0)
                .add(Attributes.MOVEMENT_SPEED, 4.0)
                .add(ForgeMod.SWIM_SPEED.get(), 2.0)
                .add(Attributes.ATTACK_DAMAGE, 20.0)
                .add(ChangedAttributes.TRANSFUR_DAMAGE.get(), 20.0)
                .build()
        )
        event.put(
            PURE_WHITE_LATEX_YUFENG_BY_NCDBOAT.get(),
            ChangedEntity.createLatexAttributes().build()
        )

    }
}

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object ClientEvents {
    @JvmStatic
    @SubscribeEvent
    fun registerAccessoryRenderers(event: FMLClientSetupEvent) {
        AccessoryLayer.registerRenderer(
            ModItems.NOT_CAN_TAKE_OFF_WETSUIT.get(), SimpleClothingRenderer.of(
                ArmorModel.CLOTHING_INNER, setOf<ModelComponent>(
                    ModelComponent(ArmorModel.CLOTHING_INNER, EquipmentSlot.CHEST),
                    ModelComponent(ArmorModel.CLOTHING_INNER, EquipmentSlot.LEGS)
                )
            )
        )
        AccessoryLayer.registerRenderer(
            ModItems.NOT_CAN_TAKE_OFF_LAB_COAT.get(), SimpleClothingRenderer.of(
                ArmorModel.CLOTHING_OUTER, setOf<ModelComponent>(
                    ModelComponent(ArmorModel.CLOTHING_OUTER, EquipmentSlot.CHEST),
                    ModelComponent(ArmorModel.CLOTHING_MIDDLE, EquipmentSlot.LEGS)
                )
            )
        )
    }

    @JvmStatic
    @SubscribeEvent
    fun registerLayerDefinitions(event: RegisterLayerDefinitions) {
        event.registerLayerDefinition(AEntityModel.LAYER_LOCATION, AEntityModel::createBodyLayer)
        event.registerLayerDefinition(ZombieModel.LAYER_LOCATION, ZombieModel::createBodyLayer)
    }

    @JvmStatic
    @SubscribeEvent
    fun registerEntityRenderers(event: RegisterRenderers) {
        event.registerEntityRenderer(
            A_ENTITY.get()
        ) { context: EntityRendererProvider.Context ->
            DarkLatexYufengRenderer(context)
        }
        event.registerEntityRenderer(
            CEXOSKELETON.get()
        ) { context: EntityRendererProvider.Context ->
            ExoskeletonRenderer(context)
        }
        event.registerEntityRenderer(
            A_HORSE.get()
        ) { context: EntityRendererProvider.Context ->
            HorseRenderer(context)
        }
        event.registerEntityRenderer(
            SOUL.get()
        ) { context: EntityRendererProvider.Context ->
            SoulRenderer(context)
        }
//        event.registerEntityRenderer(
//            C_PLAYER.get()
//        ) { context: EntityRendererProvider.Context ->
//            LatexHumanRenderer(context, true)
//        }
        event.registerEntityRenderer(
            PURE_WHITE_LATEX_YUFENG.get()
        ) { context: EntityRendererProvider.Context ->
            DarkLatexYufengRenderer(context)
        }
        event.registerEntityRenderer(
            NONE_ENTITY.get()
        ) { context: EntityRendererProvider.Context ->
            NoneTransfurVariantRenderer(context)
        }
        event.registerEntityRenderer(
            NOT_CAN_DISMOUNT_BOAT.get()
        ) { context: EntityRendererProvider.Context ->
            BoatRenderer(context, false)
        }
        event.registerEntityRenderer(
            MISC.get()
        ) { context: EntityRendererProvider.Context ->
            NoneTransfurVariantRenderer(context)
        }
        event.registerEntityRenderer(
            PURE_WHITE_LATEX_YUFENG_BY_NCDBOAT.get()
        ) { context: EntityRendererProvider.Context ->
            DarkLatexYufengRenderer(context)
        }
    }
}

private fun registerUseItemMode(
    name: String, showHotbar: Boolean, holdMainHand: Boolean,
    holdOffHand: Boolean, interact: Boolean, breakBlocks: Boolean
): UseItemMode {
    return UseItemMode.create(
        name.uppercase(),  // 名称必须大写
        showHotbar,
        holdMainHand,
        holdOffHand,
        interact,
        breakBlocks
    )
}

val SOUL_USE_ITEM_MODE = registerUseItemMode("SOUL_USE_ITEM_MODE", false, true, false, false, false)