package com.github.cecnull1.cecnull1_changed_plus.utils

import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

object MCreatorFunction {
    /**
     * 在指定的范围内查找与给定点距离最近的指定类型的实体。
     *
     * @param x 中心点的 X 坐标。
     * @param y 中心点的 Y 坐标。
     * @param z 中心点的 Z 坐标。
     * @param length 搜索范围的边长（以中心点为中心的立方体区域）。
     * @param clazz 要查找的实体类的类型。
     * @return 距离最近的实体，如果没有找到则返回 null。
     */
    inline fun <reified T : Entity> Level.findNearestEntity(
        x: Double,
        y: Double,
        z: Double,
        length: Double,
        clazz: Class<T>
    ): T? {
        return this.getEntitiesOfClass(
            clazz,
            AABB.ofSize(Vec3(x, y, z), length, length, length)
        ) { true }.minByOrNull { it.distanceToSqr(x, y, z) }
    }

    fun Level.findNearestEntity(
        x: Double,
        y: Double,
        z: Double,
        length: Double,
    ): Entity? {
        return this.findNearestEntity(x, y, z, length, Entity::class.java)
    }

    inline fun <reified T : Entity> Level.findNearestEntity(
        vec3: Vec3,
        length: Double,
        clazz: Class<T>
    ): T? {
        return this.findNearestEntity(vec3.x, vec3.y, vec3.z, length, clazz)
    }

    fun Level.findNearestEntity(
        vec3: Vec3,
        length: Double,
    ): Entity? {
        return this.findNearestEntity(vec3, length, Entity::class.java)
    }
}