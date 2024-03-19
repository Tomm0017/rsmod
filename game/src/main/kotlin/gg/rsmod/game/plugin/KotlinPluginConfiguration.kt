package gg.rsmod.game.plugin

import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports

object KotlinPluginConfiguration : ScriptCompilationConfiguration({
    defaultImports(
            "org.alter.game.fs.def.*",
            "org.alter.game.model.*",
            "org.alter.game.model.item.*",
            "org.alter.game.model.entity.*",
            "org.alter.game.model.container.*",
            "org.alter.game.model.container.key.*",
            "org.alter.game.model.queue.*",
            "org.alter.game.model.attr.AttributeKey",
            "org.alter.game.model.timer.TimerKey",
            "org.alter.game.model.shop.ShopItem",
            "org.alter.game.model.shop.PurchasePolicy",
            "org.alter.game.model.shop.StockType",
            "org.alter.game.plugin.Plugin",
            "org.alter.game.**",
            "org.alter.api.*",
            "org.alter.api.ext.*",
            "org.alter.api.cfg.*",
            "org.alter.api.dsl.*"
    )
})