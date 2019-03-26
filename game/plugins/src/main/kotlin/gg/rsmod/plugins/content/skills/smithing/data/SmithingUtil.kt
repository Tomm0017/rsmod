package gg.rsmod.plugins.content.skills.smithing.data

/**
 * The map of Shayzien child ids to their names
 */
internal val shayzien: Map<Int, String> by lazy {
    val values = HashMap<Int, String>()

    for (i in 2 until 27 step 5) {
        val level = (i / 5) + 1
        values[i] = "shayzien supply gloves ($level)"
        values[i + 1] = "shayzien supply boots ($level)"
        values[i + 2] = "shayzien supply helm ($level)"
        values[i + 3] = "shayzien supply greaves ($level)"
        values[i + 4] = "shayzien supply platebody ($level)"
    }
    values
}

/**
 * Gets the bar id for a specified item name. I wasn't able to find any reference
 * to bars in the cache enums or item definitions, so I figured this would at least
 * achieve the desired affect.
 *
 * @param name  The name of the item
 * @return      The bar id
 */
internal fun getBar(name: String) : Bar? {
    return Bar.values.firstOrNull { name.startsWith(it.prefix) } ?: when {
        name.endsWith("lantern frame") -> Bar.IRON
        name.endsWith("lantern (unf)") -> Bar.STEEL
        name.endsWith("grapple tip") -> Bar.MITHRIL
        else -> null
    }
}

/**
 * Gets the item 'type' for a specified child id
 *
 * @param child The child id
 * @param bar   The bar type
 * @return      The type, as a string
 */
internal fun typeForChild(child: Int, bar: Bar) : String? = when {
    bar == Bar.LOVAKITE -> shayzien.getValue(child)
    child == 2 -> "${bar.prefix} dagger"
    child == 3 -> "${bar.prefix} sword"
    child == 4 -> "${bar.prefix} scimitar"
    child == 5 -> "${bar.prefix} longsword"
    child == 6 -> "${bar.prefix} 2h sword"
    child == 7 -> "${bar.prefix} axe"
    child == 8 -> "${bar.prefix} mace"
    child == 9 -> "${bar.prefix} warhammer"
    child == 10 -> "${bar.prefix} battleaxe"
    child == 11 -> "${bar.prefix} claws"
    child == 12 -> "${bar.prefix} chainbody"
    child == 13 -> "${bar.prefix} platelegs"
    child == 14 -> "${bar.prefix} plateskirt"
    child == 15 -> "${bar.prefix} platebody"
    child == 16 -> "${bar.prefix} nails"
    child == 17 -> "${bar.prefix} med helm"
    child == 18 -> "${bar.prefix} full helm"
    child == 19 -> "${bar.prefix} sq shield"
    child == 20 -> "${bar.prefix} kiteshield"
    child == 21 -> when (bar) {
        Bar.IRON -> "lantern frame"
        Bar.STEEL -> "lantern (unf)"
        else -> null
    }
    child == 23 -> "${bar.prefix} dart tip"
    child == 24 -> "${bar.prefix} arrowtips"
    child == 25 -> "${bar.prefix} knife"
    child == 26 -> when (bar) {
        Bar.BRONZE -> "wire"
        Bar.IRON -> "spit"
        Bar.STEEL -> "studs"
        Bar.MITHRIL -> "grapple tip"
        else -> null
    }
    child == 28 -> "${bar.prefix} bolts (unf)"
    child == 29 -> "${bar.prefix} limbs"
    child == 30 -> "${bar.prefix} javelin heads"
    else -> null
}