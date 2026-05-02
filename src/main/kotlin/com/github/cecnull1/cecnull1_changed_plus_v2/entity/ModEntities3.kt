package com.github.cecnull1.cecnull1_changed_plus_v2.entity

import com.github.cecnull1.cecnull1_cforge.core.PipeCtrl.pipeIf
import com.github.cecnull1.cecnull1_cforge.core.PipeCtrl.then
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.*
import com.github.cecnull1.cecnull1lib.utils.MCreatorFunction.findNearestEntity
import net.ltxprogrammer.changed.entity.AttributePresets
import net.ltxprogrammer.changed.entity.ChangedEntity
import net.ltxprogrammer.changed.entity.TransfurMode
import net.ltxprogrammer.changed.entity.beast.LatexPinkYuinDragon
import net.ltxprogrammer.changed.entity.latex.LatexType
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance
import net.ltxprogrammer.changed.init.ChangedAttributes
import net.ltxprogrammer.changed.init.ChangedLatexTypes
import net.minecraft.core.BlockPos
import net.minecraft.tags.BlockTags
import net.minecraft.util.Mth
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.ai.attributes.AttributeMap
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.vehicle.Minecart
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraftforge.common.ForgeMod
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModEntities3 {
    val REGISTRY: DeferredRegister<EntityType<*>> = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID)

    val FutiEntityType: RegistryObject<EntityType<Futi>> = REGISTRY.register(rlclass<Futi>()) {
        EntityType.Builder.of(::Futi, MobCategory.MONSTER).apply {
            sized(.7f, 1.73f)
        }.build(rlclass<Futi>())
    }

    val FlyingPureWhiteLatexYufengType: RegistryObject<EntityType<FlyingPureWhiteLatexYufeng>> =
        REGISTRY.register(rlclass<FlyingPureWhiteLatexYufeng>()) {
            EntityType.Builder.of(::FlyingPureWhiteLatexYufeng, MobCategory.MONSTER)
                .sized(0.7f, 1.93f)
                .build(rlclass<FlyingPureWhiteLatexYufeng>())
        }

    val CMinecartType: RegistryObject<EntityType<CMinecart>> = REGISTRY.register(rlclass<CMinecart>()) {
        EntityType.Builder.of(::CMinecart, MobCategory.MISC)
            .sized(0.98f, 0.7f)
            .clientTrackingRange(8)
            .build(rlclass<CMinecart>())
    }

    val LatexPinkHumanType: RegistryObject<EntityType<LatexPinkHuman>> = REGISTRY.register(rlclass<LatexPinkHuman>()) {
        EntityType.Builder.of(::LatexPinkHuman, MobCategory.MONSTER)
            .clientTrackingRange(10)
            .sized(0.7F, 1.93F)
            .build(rlclass<LatexPinkHuman>())
    }
}

class Futi(type: EntityType<out ChangedEntity>, level: Level): ChangedEntity(type, level) {
    init {
        noPhysics = true
    }

    override fun getTransfurMode(): TransfurMode = TransfurMode.NONE
    override fun variantTick(level: Level) {
        super.variantTick(level)
        level.findNearestEntity(position(), 1.0, LivingEntity::class.java, maybeGetUnderlying()) then Living@ {
            maybeGetUnderlying() then WithPlayer@ {
                TransfurVariantInstance.syncEntityPosRotWithEntity(this@Living, this)
            }
        }
    }

    override fun setAttributes(attributes: AttributeMap) {
        super.setAttributes(attributes)
        AttributePresets.wolfLike(attributes)
    }

    override fun canCollideWith(entity: Entity): Boolean {
        return false
    }

    override fun canBeCollidedWith(): Boolean {
        return false
    }
}

class FlyingPureWhiteLatexYufeng(type: EntityType<out AEntity>, level: Level?) : PureWhiteLatexYufeng(type, level), ISwimming {
    override fun variantTick(level: Level?) {
        super.variantTick(level)
        maybeGetUnderlying() then {
            pipeIf(!hasEffect(MobEffects.DOLPHINS_GRACE)) then {
                addEffect(MobEffects.DOLPHINS_GRACE.new(
                    duration = MobEffectInstance.INFINITE_DURATION,
                    ambient = true,
                    visible = false
                ))
            }

            deltaMovement = deltaMovement.calcl(
                direction = if (!isFallFlying) lookAngle else yRot.toHorizontalViewVec(),
                influence = .5,
                applyToY = !isFallFlying
            )

            if (deltaMovement.lengthSqr() < 1.0 && deltaMovement.lengthSqr() != 0.toDouble()) {
                deltaMovement = deltaMovement.normalize().scale(1.0)
            }

            if (this is Player) {
                foodData.foodLevel+=20
                foodData.setExhaustion(0f)
                foodData.setSaturation(foodData.saturationLevel+20)
            }

            resetFallDistance()
        }
    }

    override fun tTick() {}

    override fun setAttributes(attributes: AttributeMap) {
        super.setAttributes(attributes)
        attributes[Attributes.ARMOR] = 100.0
        attributes[Attributes.ARMOR_TOUGHNESS] = 100.0
        attributes[Attributes.MOVEMENT_SPEED] = 3
        attributes[Attributes.MAX_HEALTH] = 100.0
        attributes[ForgeMod.SWIM_SPEED.get()] = 3
        attributes[Attributes.ATTACK_DAMAGE] = 200.0
        attributes[ChangedAttributes.TRANSFUR_DAMAGE.get()] = 200.0
    }

    override fun isFallFlying(old: Boolean): Boolean {
        return !isInWaterOrBubble
    }

    override fun isSwimming(old: Boolean): Boolean {
        return isInWaterOrBubble || old
    }
}

class CMinecart(type: EntityType<out Minecart>, level: Level?): Minecart(type, level), IOnMount {
    override fun tick() {
        super.tick()
        if (deltaMovement.lengthSqr() < .25 && deltaMovement.lengthSqr() != 0.toDouble()) {
            deltaMovement = deltaMovement.normalize().multiply(.5, .5, .5)
        }
    }

    override fun onMount(mountType: MountType): Boolean {
        return when (mountType) {
            is MountType.PlayerSelfDismount -> false
            is MountType.Dismount -> false
            else -> true
        }
    }
}

class LatexPinkHuman(type: EntityType<out LatexPinkYuinDragon>, level: Level?): LatexPinkYuinDragon(type, level), RealKeepForm, IFanJi, ISwimming, IFlying {
    override fun getLatexType(): LatexType = ChangedLatexTypes.WHITE_LATEX.get()
    override fun setAttributes(attributes: AttributeMap) {
        super.setAttributes(attributes)
        attributes[Attributes.ARMOR] = 20.0
        attributes[Attributes.ARMOR_TOUGHNESS] = 20.0
        attributes[Attributes.MOVEMENT_SPEED] = 1.5
        attributes[Attributes.MAX_HEALTH] = 40.0
        attributes[ForgeMod.SWIM_SPEED.get()] = 2.0
        attributes[Attributes.ATTACK_DAMAGE] = 40.0
        attributes[ChangedAttributes.TRANSFUR_DAMAGE.get()] = 40.0
        attributes[ForgeMod.STEP_HEIGHT_ADDITION.get()] = computeStepHeightOffset(320.0 + 64.0)
        attributes[ChangedAttributes.SNEAK_SPEED.get()] = 1.5
        attributes[ChangedAttributes.GRAB_STRUGGLE_STRENGTH.get()] = Double.MAX_VALUE
        attributes[ChangedAttributes.FALL_RESISTANCE.get()] = 1.5
    }

    override fun variantTick(level: Level?) {
        super.variantTick(level)
        aEntityTick()
        maybeGetUnderlying() as? Player then {
            pipeIf(!hasEffect(MobEffects.DOLPHINS_GRACE)) then {
                addEffect(MobEffects.DOLPHINS_GRACE.new(
                    duration = MobEffectInstance.INFINITE_DURATION,
                    ambient = true,
                    visible = false
                ))
            }
            foodData.foodLevel = 20
            foodData.setSaturation(20f)
        }
    }

    override fun isSwimming(old: Boolean): Boolean {
        return isSwimming || old
    }

    override fun isSwimming(): Boolean {
        return maybeGetUnderlying().isInWaterOrBubble
    }

    override fun isFallFlying(old: Boolean): Boolean {
        val underlying = maybeGetUnderlying()
        if (underlying?.meiyun() == true) return true

        val player = underlyingPlayer ?: return old

        // 现在时：实时检测是否接触地面
        val isOnGroundNow = isActuallyOnGround(player)

        val canFly = !player.abilities.flying && player.vehicle == null

        // 飞行条件：不接地 && 不向下 && 有能力
        return (!isOnGroundNow || player.deltaMovement.y >= 0) && !isInWater && canFly || old
    }

    fun isActuallyOnGround(player: Player): Boolean {
        val footSlice = player.boundingBox.move(0.0, -0.05, 0.0)
        val world = player.level

        // 获取脚底切片覆盖的方块坐标范围
        val minX = Mth.floor(footSlice.minX)
        val maxX = Mth.floor(footSlice.maxX - 1.0E-7)
        val minY = Mth.floor(footSlice.minY)
        val maxY = Mth.floor(footSlice.maxY - 1.0E-7)
        val minZ = Mth.floor(footSlice.minZ)
        val maxZ = Mth.floor(footSlice.maxZ - 1.0E-7)

        for (x in minX..maxX) {
            for (z in minZ..maxZ) {
                for (y in minY..maxY) {
                    val pos = BlockPos(x, y, z)
                    val state = world.getBlockState(pos)

                    // 可穿透检查
                    if (state.isAir) continue
                    if (state.liquid()) continue
                    if (state.`is`(BlockTags.CLIMBABLE)) continue

                    val shape = state.getCollisionShape(world, pos)
                    if (shape.isEmpty) continue

                    // 转换到方块坐标系
                    val localSlice = footSlice.move(-pos.x.toDouble(), -pos.y.toDouble(), -pos.z.toDouble())

                    // 遍历形状的所有 AABB 组件
                    for (box in shape.toAabbs()) {
                        if (localSlice.intersects(box)) {
                            return true
                        }
                    }
                }
            }
        }

        return false
    }
}