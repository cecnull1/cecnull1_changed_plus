package com.github.cecnull1.cecnull1_changed_plus_v2

import com.github.cecnull1.cecnull1_cforge.core.CForgeEventCore.post
import com.github.cecnull1.cecnull1_cforge.core.CForgeEventCore.registerFastEvents
import com.github.cecnull1.cecnull1_cforge.core.EventBus
import com.github.cecnull1.cecnull1_cforge.core.PipeCore.calc
import com.github.cecnull1.cecnull1_cforge.core.PipeCore.process
import com.github.cecnull1.cecnull1_changed_plus_v2.Events.onNullSafeAttributeCreationEvent
import com.github.cecnull1.cecnull1_changed_plus_v2.animation.Animations
import com.github.cecnull1.cecnull1_changed_plus_v2.block.ModBlocks
import com.github.cecnull1.cecnull1_changed_plus_v2.component.WFXCOwner
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.*
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.LicensedCharacters.SPECIAL
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.A_ENTITY
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.A_HORSE
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.NOT_CAN_DISMOUNT_BOAT
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.PURE_WHITE_LATEX_YUFENG
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities2.B_HORSE
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities2.MEI_XI_YUAN
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities2.MOVE_ENTITY
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities3.CMinecartType
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities3.FlyingPureWhiteLatexYufengType
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities3.LatexPinkHumanType
import com.github.cecnull1.cecnull1_changed_plus_v2.event.*
import com.github.cecnull1.cecnull1_changed_plus_v2.event.Event.onLivingTick
import com.github.cecnull1.cecnull1_changed_plus_v2.event.Event.onPlayerTick
import com.github.cecnull1.cecnull1_changed_plus_v2.event.Event.onTravelEvent
import com.github.cecnull1.cecnull1_changed_plus_v2.fluid.ModFluid
import com.github.cecnull1.cecnull1_changed_plus_v2.gamerule.ModGameRule
import com.github.cecnull1.cecnull1_changed_plus_v2.item.ModItems
import com.github.cecnull1.cecnull1_changed_plus_v2.model.UserHumanModel
import com.github.cecnull1.cecnull1_changed_plus_v2.oper.ModOper
import com.github.cecnull1.cecnull1_changed_plus_v2.packet.NetworkHandler
import com.github.cecnull1.cecnull1_changed_plus_v2.renderer.SpecialRenderer
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.TypeRegistry.register
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.value
import net.ltxprogrammer.changed.client.RegisterComplexRenderersEvent
import net.ltxprogrammer.changed.client.renderer.DarkLatexYufengRenderer
import net.ltxprogrammer.changed.client.renderer.LatexOrcaRenderer
import net.ltxprogrammer.changed.client.renderer.LatexPinkYuinDragonRenderer
import net.ltxprogrammer.changed.client.renderer.SeatEntityRenderer
import net.ltxprogrammer.changed.client.renderer.accessory.SimpleClothingRenderer
import net.ltxprogrammer.changed.client.renderer.accessory.SimpleClothingRenderer.ModelComponent
import net.ltxprogrammer.changed.client.renderer.layers.AccessoryLayer
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModels
import net.ltxprogrammer.changed.entity.ChangedEntity
import net.ltxprogrammer.changed.init.ChangedItems
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.renderer.entity.BoatRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.HorseRenderer
import net.minecraft.client.renderer.entity.MinecartRenderer
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.animal.horse.Horse
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers
import net.minecraftforge.common.ForgeMod
import net.minecraftforge.event.entity.EntityAttributeCreationEvent
import net.minecraftforge.event.entity.EntityAttributeModificationEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext

val bus = EventBus()

@Mod(MODID)
class Cecnull1ChangedPlus(context: FMLJavaModLoadingContext) {
    init {
        register<WFXCOwner>()
        LicensedCharacterInit.registerEvent()
        bus.registerFastEvents<TakeOffEvent>(::onTakeOff)
        bus.registerFastEvents<ByForgeEvent>(::onForgeEvent)
        bus.registerFastEvents<CPlayerTickEvent>(::onPlayerTick)
        bus.registerFastEvents<CLivingTickEvent>(::onLivingTick)
        bus.registerFastEvents<LivingTravelEvent>(::onTravelEvent)
        bus.registerFastEvents<NullSafeAttributeCreationEvent>(::onNullSafeAttributeCreationEvent)

        val modEventBus = context.modEventBus
        LicensedCharacterInit.registerEntities(modEventBus)
        Animations.REGISTRY.register(modEventBus)
        ModEntities.REGISTER.register(modEventBus)
        ModEntities2.REGISTER.register(modEventBus)
        ModEntities3.REGISTRY.register(modEventBus)
        ModTransfurVariant.REGISTRY.register(modEventBus)
        ModBlocks.REGISTRY_BLOCKENTITY.register(modEventBus)
        ModBlocks.REGISTRY_BLOCK.register(modEventBus)
        ModItems.REGISTER.register(modEventBus)
        ModFluid.REGISTRY.register(modEventBus)
        ModFluid.TYPE_REGISTRY.register(modEventBus)
        ModGameRule.register()
        ModOper.operRegistry.register(modEventBus)
    }
}

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
object Events {
    fun onCommonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            NetworkHandler.register()
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun registerEntityAttributes(event: EntityAttributeCreationEvent) {
        NullSafeAttributeCreationEvent(event).post(bus)
    }

    @JvmStatic
    @SubscribeEvent
    fun modEntityAttributes(event: EntityAttributeModificationEvent) {
        event.add(EntityType.PLAYER, ModOper.hasHA.attributeKey.value)
        event.add(EntityType.PLAYER, ModOper.hasArmorHA.attributeKey.value)
        event.add(EntityType.PLAYER, ModOper.hasCFlying.attributeKey.value)

        event.add(EntityType.PLAYER, ModOper.aEntityLike.attributeKey.value)
        event.add(EntityType.PLAYER, ModOper.hasNotCanTakeOffWetsuitLike.attributeKey.value)
        event.add(EntityType.PLAYER, ModOper.flyingPureWhiteLatexYufengLike.attributeKey.value)
        event.add(EntityType.PLAYER, ModOper.autoMove.attributeKey.value)
        event.add(EntityType.PLAYER, ModOper.forceSwim.attributeKey.value)
        event.add(EntityType.PLAYER, ModOper.forceSprint.attributeKey.value)
        event.add(EntityType.PLAYER, ModOper.forceInWater.attributeKey.value)
        event.add(EntityType.PLAYER, ModOper.noGravity.attributeKey.value)
        event.add(EntityType.PLAYER, ModOper.forceRide.attributeKey.value)
        event.add(EntityType.PLAYER, ModOper.forceFallFlying.attributeKey.value)
        event.add(EntityType.PLAYER, ModOper.forceClimbable.attributeKey.value)
        event.add(EntityType.PLAYER, ModOper.notOnGround.attributeKey.value)
        event.add(EntityType.PLAYER, ModOper.wocwodejiaohenhua.attributeKey.value)
        event.add(EntityType.PLAYER, ModOper.hasAutoMove2.attributeKey.value)
        event.add(EntityType.PLAYER, ModOper.meiXiYuanLike.attributeKey.value)
        event.add(EntityType.PLAYER, ModOper.noPhy.attributeKey.value)
        event.add(EntityType.PLAYER, ModOper.noInWall.attributeKey.value)

        event.add(EntityType.PLAYER, ModOper.test.attributeKey.value)
    }

    fun onNullSafeAttributeCreationEvent(event: NullSafeAttributeCreationEvent) {
        event.put(
            A_ENTITY.get(),
            createAEntityAttributes().build()
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
            PURE_WHITE_LATEX_YUFENG.get(),
            createAEntityAttributes().build()
        )
        event.put(
            MEI_XI_YUAN.get(),
            ChangedEntity.createLatexAttributes().add(ModOper.meiXiYuanLike.attributeKey.get(), 1.0).build()
        )
        event.put(
            B_HORSE.get(),
            Horse.createBaseHorseAttributes().build()
        )
        event.put(
            FlyingPureWhiteLatexYufengType.get(),
            createAEntityAttributes().add(ModOper.flyingPureWhiteLatexYufengLike.attributeKey.get(), 1.0).build()
        )
        event.put(
            LatexPinkHumanType.get(),
            createAEntityAttributes().build()
        )
    }
}

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object ClientEvents {
    @JvmStatic
    @SubscribeEvent
    fun registerComplexEntityRenderers(event: RegisterComplexRenderersEvent) {
        event.registerEntityRenderer(SPECIAL.get(), "default", SpecialRenderer.forModelSize(false))
        event.registerEntityRenderer(SPECIAL.get(), "slim", SpecialRenderer.forModelSize(true))
    }

    @JvmStatic
    @SubscribeEvent
    fun registerAccessoryRenderers(event: FMLClientSetupEvent) {
        event.enqueueWork {
            AccessoryLayer.registerRenderer(
                ModItems.LATEX_YW.get(), SimpleClothingRenderer.of(
                    ArmorModels.CLOTHING_INNER, mutableSetOf(
                        ModelComponent(ArmorModels.CLOTHING_INNER, EquipmentSlot.CHEST),
                        ModelComponent(ArmorModels.CLOTHING_INNER, EquipmentSlot.LEGS)
                    )
                )
            )
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun registerLayerDefinitions(event: RegisterLayerDefinitions): Unit = event.process {
        registerLayerDefinition(UserHumanModel.LAYER_LOCATION_STEVE, UserHumanModel.func::createSteveLayer)
        registerLayerDefinition(UserHumanModel.LAYER_LOCATION_ALEX, UserHumanModel.func::createAlexLayer)
        UserHumanModel.ArmorModel.MODEL_SET.registerDefinitions(event::registerLayerDefinition)
    } calc {}

    @JvmStatic
    @SubscribeEvent
    fun registerEntityRenderers(event: RegisterRenderers) {
        RegisterRenderers(event).post(bus)
        event.apply {
            registerEntityRenderer(A_ENTITY.get(), ::DarkLatexYufengRenderer)
            registerEntityRenderer(A_HORSE.get(), ::HorseRenderer)
            registerEntityRenderer(PURE_WHITE_LATEX_YUFENG.get(), ::DarkLatexYufengRenderer)
            registerEntityRenderer(MOVE_ENTITY.get(), ::SeatEntityRenderer)

            registerEntityRenderer(MEI_XI_YUAN.get(), ::LatexOrcaRenderer)
            registerEntityRenderer(B_HORSE.get(), ::HorseRenderer)

            registerEntityRenderer(FlyingPureWhiteLatexYufengType.get(), ::DarkLatexYufengRenderer)
            registerEntityRenderer(CMinecartType.get()) {
                MinecartRenderer(it, ModelLayers.MINECART)
            }
            registerEntityRenderer(LatexPinkHumanType.get(), ::LatexPinkYuinDragonRenderer)

            registerEntityRenderer(SPECIAL.get(), ::SpecialRenderer)

            registerEntityRenderer(
                NOT_CAN_DISMOUNT_BOAT.get()
            ) { context: EntityRendererProvider.Context ->
                BoatRenderer(context, false)
            }
        }
    }
}