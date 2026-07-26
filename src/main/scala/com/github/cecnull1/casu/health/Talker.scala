package com.github.cecnull1.casu.health

object Talker:
  def talk(
            string: String,
            canTalk: Boolean = true,
            impairedSpeech: Boolean = false,
            brainDamage: Boolean = false,
            inWater: Boolean = false
          ): String =
    if !canTalk then return ""
    var result = string
    if impairedSpeech then result = impairSpeech(result)
    if brainDamage    then result = applyBrainDamage(result)
    if inWater        then result = applyUnderwaterEffect(result)
    result
  end talk

  def impairSpeech(text: String): String = text
  def applyBrainDamage(text: String): String = text
  def applyUnderwaterEffect(text: String): String = text
end Talker
