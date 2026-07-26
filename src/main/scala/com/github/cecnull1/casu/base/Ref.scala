package com.github.cecnull1.casu.base

case class Ref[T](@volatile var value: T)