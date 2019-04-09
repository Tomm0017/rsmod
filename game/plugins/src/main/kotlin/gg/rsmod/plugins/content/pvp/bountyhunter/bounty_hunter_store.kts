package gg.rsmod.plugins.content.pvp.bountyhunter

import java.text.DecimalFormat

val STORE_INTERFACE_ID = 178
val BOUNTY_POINTS_VARP = 1132

// kills(?) varp = 1137
// death(?) varp = 1138

val ITEM_PRICES = hashMapOf(
        Items.DRAGON_SCIMITAR to 300_000,
        Items.DRAGON_LONGSWORD to 300_000,
        Items.DRAGON_DAGGER to 90_000,
        Items.DRAGON_BATTLEAXE to 600_000,
        Items.DRAGON_MACE to 150_000,
        Items.DRAGON_HALBERD to 900_000,
        Items.HELM_OF_NEITIZNOT to 150_000,
        Items.BERSERKER_HELM to 234_000,
        Items.WARRIOR_HELM to 234_000,
        Items.ARCHER_HELM to 234_000,
        Items.FARSEER_HELM to 234_000,
        Items.MYSTIC_ROBE_TOP to 360_000,
        Items.MYSTIC_ROBE_BOTTOM to 240_000,
        Items.MYSTIC_HAT to 45_000,
        Items.MYSTIC_GLOVES to 30_000,
        Items.MYSTIC_BOOTS to 30_000,
        Items.RUNE_PLATEBODY to 255_000,
        Items.RUNE_PLATELEGS to 192_000,
        Items.RUNE_PLATESKIRT to 192_000,
        Items.CRYSTAL_SEED to 540_000,
        Items.GREEN_DARK_BOW_PAINT to 500_000,
        Items.YELLOW_DARK_BOW_PAINT to 500_000,
        Items.WHITE_DARK_BOW_PAINT to 500_000,
        Items.BLUE_DARK_BOW_PAINT to 500_000,
        Items.PADDEWWA_TELEPORT to 10_000,
        Items.SENNTISTEN_TELEPORT to 10_000,
        Items.ANNAKARL_TELEPORT to 10_000,
        Items.CARRALLANGAR_TELEPORT to 10_000,
        Items.DAREEYAK_TELEPORT to 10_000,
        Items.GHORROCK_TELEPORT to 10_000,
        Items.KHARYRLL_TELEPORT to 10_000,
        Items.LASSAR_TELEPORT to 10_000,
        Items.VOLCANIC_WHIP_MIX to 500_000,
        Items.FROZEN_WHIP_MIX to 500_000,
        Items.STEAM_STAFF_UPGRADE_KIT to 250_000,
        Items.LAVA_STAFF_UPGRADE_KIT to 250_000,
        Items.DRAGON_PICKAXE_UPGRADE_KIT to 300_000,
        Items.WARD_UPGRADE_KIT to 350_000,
        Items.RING_OF_WEALTH_SCROLL to 50_000,
        Items.MAGIC_SHORTBOW_SCROLL to 100_000,
        Items.RUNE_POUCH to 1_200_000,
        Items.LOOTING_BAG to 10_000,
        Items.BOLT_RACK to 360,
        Items.RUNE_ARROW to 600,
        Items.ADAMANT_ARROW to 240,
        Items.CLUE_BOX to 100_000,
        Items.CLIMBING_BOOTS to 5400,
        Items.GRANITE_CLAMP to 250_000,
        Items.BOUNTY_TELEPORT_SCROLL to 8_000_000,
        Items.HUNTERS_HONOUR to 2_500_000
)

on_interface_open(interfaceId = STORE_INTERFACE_ID) {
    player.setInterfaceEvents(interfaceId = STORE_INTERFACE_ID, component = 2, range = 0..50, setting = 1086)
    player.runClientScript(23, 11665410, 11665411, 878)
}

on_button(interfaceId = STORE_INTERFACE_ID, component = 2) {
    val item = player.getInteractingItemId()
    val opt = player.getInteractingOption()

    val price = ITEM_PRICES[item] ?: return@on_button

    when (opt) {
        1 -> player.message("${Item(item).getName(world.definitions)} costs ${DecimalFormat().format(price)} points.")
        2 -> buy_item(player, item, 1)
        3 -> buy_item(player, item, 5)
        4 -> buy_item(player, item, 10)
        5 -> buy_item(player, item, 50)
        10 -> world.sendExamine(player, item, ExamineEntityType.ITEM)
        else -> return@on_button
    }
}

fun buy_item(p: Player, item: Int, amt: Int) {
    val price = ITEM_PRICES[item] ?: return
    val name = Item(item).getName(world.definitions)

    var amount = amt
    while (amount-- > 0) {
        val points = p.getVarp(BOUNTY_POINTS_VARP)

        if (points < price) {
            p.message("You need ${DecimalFormat().format(price)} points to buy the $name.")
            break
        }

        val add = p.inventory.add(item = item, amount = 1)

        if (add.hasSucceeded()) {
            p.setVarp(BOUNTY_POINTS_VARP, points - price)
        } else {
            if (amt == 1) {
                p.message("You don't have enough free inventory space to buy that.")
            } else {
                p.message("You don't have enough free inventory space to buy that many.")
            }
            break
        }
    }
}