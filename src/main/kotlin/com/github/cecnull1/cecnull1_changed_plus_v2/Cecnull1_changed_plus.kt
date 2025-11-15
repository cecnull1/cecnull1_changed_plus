package com.github.cecnull1.cecnull1_changed_plus_v2

//import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.C_PLAYER
import com.github.cecnull1.cecnull1_cforge.core.CForgeEventBus
import com.github.cecnull1.cecnull1_cforge.core.CForgeEventBus.post
import com.github.cecnull1.cecnull1_cforge.core.ComponentMap
import com.github.cecnull1.cecnull1_cforge.core.PipeCore.calc
import com.github.cecnull1.cecnull1_cforge.core.PipeCore.process
import com.github.cecnull1.cecnull1_changed_plus_v2.animation.Animations
import com.github.cecnull1.cecnull1_changed_plus_v2.block.ModBlocks
import com.github.cecnull1.cecnull1_changed_plus_v2.component.Flying
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.A_ENTITY
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.A_HORSE
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.MISC
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.NONE_ENTITY
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.NOT_CAN_DISMOUNT_BOAT
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.PURE_WHITE_LATEX_YUFENG
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.PURE_WHITE_LATEX_YUFENG_AND_ARMOR
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.SOUL
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities2
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities2.B_HORSE
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities2.MEI_XI_YUAN
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities2.MOVE_ENTITY
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModTransfurVariant
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.SWEMEntities
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.SWEMEntities.O_ENTITY
import com.github.cecnull1.cecnull1_changed_plus_v2.event.*
import com.github.cecnull1.cecnull1_changed_plus_v2.event.Event.onLivingTick
import com.github.cecnull1.cecnull1_changed_plus_v2.event.Event.onPlayerTick
import com.github.cecnull1.cecnull1_changed_plus_v2.gamerule.ModGameRule
import com.github.cecnull1.cecnull1_changed_plus_v2.item.ModItems
import com.github.cecnull1.cecnull1_changed_plus_v2.model.AEntityModel
import com.github.cecnull1.cecnull1_changed_plus_v2.model.ZombieModel
import com.github.cecnull1.cecnull1_changed_plus_v2.packet.NetworkHandler
import com.github.cecnull1.cecnull1_changed_plus_v2.psi.PieceOperatorEntityGetTransfurVariant
import com.github.cecnull1.cecnull1_changed_plus_v2.psi.PieceTrickTransfurLivingEntity
import com.github.cecnull1.cecnull1_changed_plus_v2.renderer.NoneEntityRenderer
import com.github.cecnull1.cecnull1_changed_plus_v2.renderer.SoulRenderer
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.CodecRegistry
import io.github.apace100.apoli.mixin.PlayerEntityRendererMixin
import net.ltxprogrammer.changed.client.renderer.DarkLatexYufengRenderer
import net.ltxprogrammer.changed.client.renderer.LatexOrcaRenderer
import net.ltxprogrammer.changed.client.renderer.SeatEntityRenderer
import net.ltxprogrammer.changed.client.renderer.accessory.SimpleClothingRenderer
import net.ltxprogrammer.changed.client.renderer.accessory.SimpleClothingRenderer.ModelComponent
import net.ltxprogrammer.changed.client.renderer.layers.AccessoryLayer
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModel
import net.ltxprogrammer.changed.entity.ChangedEntity
import net.ltxprogrammer.changed.entity.UseItemMode
import net.ltxprogrammer.changed.init.ChangedAttributes
import net.minecraft.client.model.HumanoidModel
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

fun String.toRL() = com.github.cecnull1.cecnull1_cforge.core.ResourceLocation(MODID, this)

fun String.toComponentRL() = com.github.cecnull1.cecnull1_cforge.core.ResourceLocation("cforge", this)

@Mod(MODID)
class Cecnull1_changed_plus(context: FMLJavaModLoadingContext) {
    init {
        CodecRegistry.registerCodec<Flying>(Flying.codec)

        val modEventBus = context.modEventBus
        Animations.REGISTRY.register(modEventBus)
        ModEntities.REGISTER.register(modEventBus)
        ModEntities2.REGISTER.register(modEventBus)
        ModTransfurVariant.REGISTRY.register(modEventBus)
        ModBlocks.REGISTRY_BLOCKENTITY.register(modEventBus)
        ModBlocks.REGISTRY_BLOCK.register(modEventBus)
        ModItems.REGISTER.register(modEventBus)
        ModGameRule.register()

        if (ModList.get().isLoaded("psi")) {
            PsiAPI.registerSpellPieceAndTexture(ResourceLocation(MODID, "transfur_living_entity"), PieceTrickTransfurLivingEntity::class.java)
            PsiAPI.registerSpellPieceAndTexture(ResourceLocation(MODID, "entity_get_transfur_variant"), PieceOperatorEntityGetTransfurVariant::class.java)
        }
        if (ModList.get().isLoaded("swem")) {
            SWEMEntities.REGISTER.register(modEventBus)
        }

        CForgeEventBus.registerEvents<TakeOffEvent>(::onTakeOff)
        CForgeEventBus.registerEvents<ByForgeEvent<*>>(::onForgeEvent)
        CForgeEventBus.registerFastEvents<CPlayerTickEvent>(::onPlayerTick)
        CForgeEventBus.registerFastEvents<CLivingTickEvent>(::onLivingTick)
        CForgeEventBus.registerFastEvents<NullSafeAttributeCreationEvent> { event ->
            event.put(
                A_ENTITY.get(),
                ChangedEntity.createLatexAttributes().build()
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
                PURE_WHITE_LATEX_YUFENG_AND_ARMOR.get(),
                ChangedEntity.createLatexAttributes().build()
            )
            event.put(
                MEI_XI_YUAN.get(),
                ChangedEntity.createLatexAttributes().build()
            )
            event.put(
                B_HORSE.get(),
                Horse.createBaseHorseAttributes().build()
            )
            HumanoidModel
            if (ModList.get().isLoaded("swem")) {
                event.put(
                    O_ENTITY.get(),
                    com.alaharranhonor.swem.entities.horse.SWEMHorseEntity.createBaseHorseAttributes().build()
                )
            }
        }
    }

    companion object {
        val entityComponentMap = ComponentMap()
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
        NullSafeAttributeCreationEvent(event).post()
    }
}

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object ClientEvents {
    @JvmStatic
    @SubscribeEvent
    fun registerAccessoryRenderers(event: FMLClientSetupEvent) {
        AccessoryLayer.registerRenderer(
            ModItems.NOT_CAN_TAKE_OFF_WETSUIT.get(), SimpleClothingRenderer.of(
                ArmorModel.CLOTHING_INNER, setOf(
                    ModelComponent(ArmorModel.CLOTHING_INNER, EquipmentSlot.CHEST),
                    ModelComponent(ArmorModel.CLOTHING_INNER, EquipmentSlot.LEGS)
                )
            )
        )
        AccessoryLayer.registerRenderer(
            ModItems.NOT_CAN_TAKE_OFF_LAB_COAT.get(), SimpleClothingRenderer.of(
                ArmorModel.CLOTHING_OUTER, setOf(
                    ModelComponent(ArmorModel.CLOTHING_OUTER, EquipmentSlot.CHEST),
                    ModelComponent(ArmorModel.CLOTHING_MIDDLE, EquipmentSlot.LEGS)
                )
            )
        )
    }

    @JvmStatic
    @SubscribeEvent
    fun registerLayerDefinitions(event: RegisterLayerDefinitions): Unit = event.process {
        registerLayerDefinition(AEntityModel.LAYER_LOCATION, AEntityModel::createBodyLayer)
        registerLayerDefinition(ZombieModel.LAYER_LOCATION, ZombieModel::createBodyLayer)
    } calc {}

    @JvmStatic
    @SubscribeEvent
    fun registerEntityRenderers(event: RegisterRenderers) {
        event.apply {
            registerEntityRenderer(
                A_ENTITY.get()
            ) { context: EntityRendererProvider.Context ->
                DarkLatexYufengRenderer(context)
            }
            registerEntityRenderer(
                A_HORSE.get()
            ) { context: EntityRendererProvider.Context ->
                HorseRenderer(context)
            }
            registerEntityRenderer(
                SOUL.get()
            ) { context: EntityRendererProvider.Context ->
                SoulRenderer(context)
            }
//          registerEntityRenderer(
//             C_PLAYER.get()
//           ) { context: EntityRendererProvider.Context ->
//            LatexHumanRenderer(context, true)
//          }
            registerEntityRenderer(
                PURE_WHITE_LATEX_YUFENG.get()
            ) { context: EntityRendererProvider.Context ->
                DarkLatexYufengRenderer(context)
            }
            registerEntityRenderer(
                NONE_ENTITY.get()
            ) { context: EntityRendererProvider.Context ->
                NoneEntityRenderer(context)
            }
            registerEntityRenderer(
                NOT_CAN_DISMOUNT_BOAT.get()
            ) { context: EntityRendererProvider.Context ->
                BoatRenderer(context, false)
            }
            registerEntityRenderer(
                MISC.get()
            ) { context: EntityRendererProvider.Context ->
                NoneEntityRenderer(context)
            }
            registerEntityRenderer(
                PURE_WHITE_LATEX_YUFENG_AND_ARMOR.get()
            ) { context: EntityRendererProvider.Context ->
                DarkLatexYufengRenderer(context)
            }
            registerEntityRenderer(
                MOVE_ENTITY.get()
            ) { context: EntityRendererProvider.Context ->
                SeatEntityRenderer(context)
            }
            registerEntityRenderer(
                MEI_XI_YUAN.get()
            ) { context: EntityRendererProvider.Context ->
                LatexOrcaRenderer(context)
            }
            registerEntityRenderer(
                B_HORSE.get()
            ) { context: EntityRendererProvider.Context ->
                HorseRenderer(context)
            }
            if (ModList.get().isLoaded("swem")) {
                registerEntityRenderer(
                    O_ENTITY.get()
                ) { context: EntityRendererProvider.Context ->
                    com.alaharranhonor.swem.client.render.SWEMHorseRenderer(context)
                }
            }
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

val SOUL_USE_ITEM_MODE = registerUseItemMode("SOUL_USE_ITEM_MODE",
    showHotbar = false,
    holdMainHand = true,
    holdOffHand = false,
    interact = false,
    breakBlocks = false
)