package dev.ivanravasi.piggy.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.ivanravasi.piggy.api.piggy.bodies.entities.AnnotationExclusionStrategy
import dev.ivanravasi.piggy.api.piggy.bodies.entities.BudgetDeserializer
import dev.ivanravasi.piggy.api.piggy.bodies.entities.BudgetSerializer
import dev.ivanravasi.piggy.api.piggy.bodies.entities.CategoryBudget

object GsonProvider {
    fun getSerializer(exclude: Boolean = false): Gson {
        return GsonBuilder()
            .registerTypeAdapter(CategoryBudget::class.java, BudgetSerializer())
            .apply {
                if (exclude)
                    setExclusionStrategies(AnnotationExclusionStrategy())
            }
            .create()
    }

    fun getDeserializer(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(CategoryBudget::class.java, BudgetDeserializer())
            .create()
    }
}