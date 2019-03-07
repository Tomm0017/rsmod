package gg.rsmod.game.plugin

import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports

object KotlinPluginConfiguration : ScriptCompilationConfiguration({
    defaultImports(
            "gg.rsmod.game.model.World",
            "gg.rsmod.game.plugin.Plugin",
            "gg.rsmod.plugins.api.*",
            "gg.rsmod.plugins.api.ext.*",
            "gg.rsmod.game.model.entity.*",
            "gg.rsmod.plugins.api.cfg.*",
            "gg.rsmod.game.model.attr.AttributeKey",
            "gg.rsmod.game.model.timer.TimerKey",
            "gg.rsmod.game.model.Direction",
            "gg.rsmod.game.model.shop.ShopItem",
            "gg.rsmod.game.model.shop.PurchasePolicy",
            "gg.rsmod.game.model.shop.StockType"
    )
})