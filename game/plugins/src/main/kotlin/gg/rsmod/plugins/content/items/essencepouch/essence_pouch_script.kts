package gg.rsmod.plugins.content.items.essencepouch

/**
 * The set of essence pouch definitions
 */
private val pouches = setOf(
        EssencePouch(id = Items.SMALL_POUCH,    levelReq = 1,   capacity = 3),
        EssencePouch(id = Items.MEDIUM_POUCH,   levelReq = 25,  capacity = 6),
        EssencePouch(id = Items.LARGE_POUCH,    levelReq = 50,  capacity = 9),
        EssencePouch(id = Items.GIANT_POUCH,    levelReq = 75,  capacity = 12)
)

/**
 * Bind item option events for the various essence pouches
 */
pouches.forEach { pouch ->
    on_item_option(item = pouch.id, option = "fill") { fillPouch(player, pouch) }
    on_item_option(item = pouch.id, option = "empty") { emptyPouch(player) }
    on_item_option(item = pouch.id, option = "check") { checkPouch(player) }
}

/**
 * Handles the filling of an essence pouch. If a pouch is empty, it should attempt to fill the pouch with Pure Essence
 * first, but if none are found then it should default to Rune Essence. If the pouch already contains essence, then it should
 * attempt to fill the pouch with the same type.
 *
 * @param player    The player attempting to fill the pouch
 * @param pouch     The essence pouch definition
 */
fun fillPouch(player: Player, pouch: EssencePouch) {

    if (player.getSkills().getMaxLevel(Skills.RUNECRAFTING) < pouch.levelReq) {
        player.message("This pouch requires level ${pouch.levelReq} ${Skills.getSkillName(world, Skills.RUNECRAFTING)} to use.")
        return
    }

    val item = player.getInteractingItem()
    val containedItem = item.getAttr(ItemAttribute.ATTACHED_ITEM_ID) ?: -1
    val amount = Math.max(item.getAttr(ItemAttribute.ATTACHED_ITEM_COUNT) ?: 0, 0)

    val def = world.definitions.getNullable(ItemDef::class.java, containedItem)
    val inventory = player.inventory
    val freeSpace = pouch.capacity - amount

    if (freeSpace <= 0) {
        player.message("You cannot add any more essence to the pouch.")
        return
    }

    /**
     * Deposits essence from a container, into a pouch
     *
     * @param pouch     The essence pouch receiving the essence
     * @param container The item container to deposit the essence from
     * @param essence   The essence item id
     * @param def       The definition of the essence pouch
     */
    fun deposit(pouch: Item, container: ItemContainer, essence: Int, def: EssencePouch) {
        val fillAmount = Math.min(container.getItemCount(essence), def.capacity)
        val transaction = container.remove(item = essence, amount = fillAmount)
        val amountRemoved = transaction.items.size

        item.putAttr(ItemAttribute.ATTACHED_ITEM_ID, essence)
        pouch.putAttr(ItemAttribute.ATTACHED_ITEM_COUNT, amount + amountRemoved)
    }

    if (def != null) {

        if (!inventory.contains(def.id)) {
            player.message("You can only put ${def.name.toLowerCase()} in the pouch.")
            return
        }

        deposit(pouch = item, container = player.inventory, essence = def.id, def = pouch)
        return
    }

    if (!inventory.containsAny(Items.PURE_ESSENCE, Items.RUNE_ESSENCE)) {
        player.message("You do not have any essence to fill your pouch with.")
        return
    }

    val essence = if (inventory.contains(Items.PURE_ESSENCE)) Items.PURE_ESSENCE else Items.RUNE_ESSENCE
    deposit(pouch = item, container = player.inventory, essence = essence, def = pouch)
}

/**
 * Empties the essence contained within the pouch.
 *
 * @param player    The player attempting to empty the pouch contents
 */
fun emptyPouch(player: Player) {
    val pouch = player.getInteractingItem()

    val item = pouch.getAttr(ItemAttribute.ATTACHED_ITEM_ID) ?: -1
    val count = pouch.getAttr(ItemAttribute.ATTACHED_ITEM_COUNT) ?: 0
    val inventory = player.inventory

    if (item != Items.RUNE_ESSENCE && item != Items.PURE_ESSENCE || count <= 0) {
        player.message("There are no essences in this pouch.")
        return
    }

    val removeCount = Math.min(inventory.freeSlotCount, count)

    if (removeCount <= 0) {
        player.message("You do not have any free space in your inventory.")
        return
    }

    val transaction = inventory.add(item, removeCount)
    val remainder = count - transaction.items.size

    if (remainder == 0) {
        pouch.putAttr(ItemAttribute.ATTACHED_ITEM_ID, -1)
    }

    pouch.putAttr(ItemAttribute.ATTACHED_ITEM_COUNT, remainder)
}

/**
 * Checks the amount of essence in the selected pouch
 *
 * @param player    The player that is checking the pouch contents
 */
fun checkPouch(player: Player) {
    val pouch = player.getInteractingItem()

    val item = pouch.getAttr(ItemAttribute.ATTACHED_ITEM_ID) ?: -1
    val count = pouch.getAttr(ItemAttribute.ATTACHED_ITEM_COUNT) ?: 0

    if (item != Items.RUNE_ESSENCE && item != Items.PURE_ESSENCE || count <= 0) {
        player.message("There are no essences in this pouch.")
        return
    }

    val name = world.definitions.get(ItemDef::class.java, item).name.toLowerCase()
    val multiple = count > 1

    player.message("There ${count.toLiteral()?.pluralPrefix(count)} ${name.pluralSuffix(count)} in this pouch.")
}