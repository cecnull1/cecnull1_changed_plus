package com.github.cecnull1.cecnull1_changed_plus_v2.component

import com.github.cecnull1.cecnull1_changed_plus_v2.utils.IComponentSer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class WFXCOwner(val owner: @Contextual UUID): IComponentSer