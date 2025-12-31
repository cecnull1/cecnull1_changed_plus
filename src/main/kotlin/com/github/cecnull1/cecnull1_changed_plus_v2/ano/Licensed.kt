package com.github.cecnull1.cecnull1_changed_plus_v2.ano

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Licensed(val name: String, val author: String, val comments: String = "", val id: Long, val idEx: Long = 0L)