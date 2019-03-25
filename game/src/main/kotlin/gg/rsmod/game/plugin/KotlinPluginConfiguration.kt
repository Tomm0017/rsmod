package gg.rsmod.game.plugin

import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports

object KotlinPluginConfiguration : ScriptCompilationConfiguration({
    defaultImports(
            "gg.rsmod.game.fs.def.*",

            "gg.rsmod.game.model.*",
            "gg.rsmod.game.model.item.*",
            "gg.rsmod.game.model.entity.*",
            "gg.rsmod.game.model.container.*",
            "gg.rsmod.game.model.container.key.*",
            "gg.rsmod.game.model.queue.*",
            "gg.rsmod.game.model.attr.AttributeKey",
            "gg.rsmod.game.model.timer.TimerKey",
            "gg.rsmod.game.model.shop.ShopItem",
            "gg.rsmod.game.model.shop.PurchasePolicy",
            "gg.rsmod.game.model.shop.StockType",

            "gg.rsmod.game.plugin.Plugin",

            "gg.rsmod.plugins.api.*",
            "gg.rsmod.plugins.api.ext.*",
            "gg.rsmod.plugins.api.cfg.*",
            "gg.rsmod.plugins.api.dsl.*"
    )
})