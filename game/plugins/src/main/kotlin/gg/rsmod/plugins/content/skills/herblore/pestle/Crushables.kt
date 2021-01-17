package gg.rsmod.plugins.content.skills.herblore.pestle

import gg.rsmod.game.model.container.ItemContainer
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.ext.autoReplace
import gg.rsmod.plugins.api.ext.message
import gg.rsmod.plugins.api.ext.playSound
import kotlin.random.Random

/**
 * @param itemIn - the 'Supplies' [Item.id] to be crushed
 * @param itemOut - the crushed/dust [Item.id] to be produced
 */
enum class Crushables(val itemIn: Int, val itemOut: Int, val message: String = ""){
    ANCHOVIES(Items.ANCHOVIES, Items.ANCHOVY_PASTE, "You grind the anchovies into a fishy, sticky paste."),
    UNICORN_HORN(Items.UNICORN_HORN, Items.UNICORN_HORN_DUST, "You grind the unicorn horn to dust."),
    CHOCOLATE(Items.CHOCOLATE_BAR, Items.CHOCOLATE_DUST, "You grind the chocolate to dust."),
    GARLIC(Items.GARLIC, Items.GARLIC_POWDER, "You crush the garlic into a fine powder."),
    KEBBIT_TEETH(Items.KEBBIT_TEETH, Items.KEBBIT_TEETH_DUST, "You grind the kebbit teeth to dust."),
    BIRD_NEST(Items.BIRD_NEST, Items.CRUSHED_NEST, "You grind the bird's nest down."),
    GOAT_HORN(Items.DESERT_GOAT_HORN, Items.GOAT_HORN_DUST, "You grind the goat's horn into dust."),
    CHARCOAL(Items.CHARCOAL, Items.GROUND_CHARCOAL, "You grind the charcoal to a powder."),
    ASHES(Items.ASHES, Items.GROUND_ASHES, "You grind down the ashes."),
    SUPERIOR_DBONES(Items.SUPERIOR_DRAGON_BONES, Items.CRUSHED_SUPERIOR_DRAGON_BONES, "You grind down the superior dragon bones."),

    KARAMBWAN_RAW(Items.RAW_KARAMBWAN, Items.KARAMBWAN_PASTE, "You grind the raw Karambwan to form a sticky paste."),
    KARAMBWAN_POISONED(Items.POISON_KARAMBWAN, Items.KARAMBWAN_PASTE_3153),
    KARAMBWAN_COOKED_POISONED(Items.COOKED_KARAMBWAN_3147, Items.KARAMBWAN_PASTE_3153),
    KARAMBWAN_COOKED(Items.COOKED_KARAMBWAN, Items.KARAMBWAN_PASTE_3154, "You grind the cooked Karambwan to form a sticky paste."),
    KARAMBWAN_COOKED2(Items.COOKED_KARAMBWAN_23533, Items.KARAMBWAN_PASTE_3154),

    // unknown texts
    GORAK_CLAW(Items.GORAK_CLAWS, Items.GORAK_CLAW_POWDER),
    RUNE(Items.RUNE_SHARDS, Items.RUNE_DUST),
    KARAMBWANJI_RAW(Items.RAW_KARAMBWANJI, Items.KARAMBWANJI_PASTE),
    KARAMBWANJI_COOKED(Items.KARAMBWANJI, Items.KARAMBWANJI_PASTE_3156),
    BLACK_SHROOMS(Items.BLACK_MUSHROOM, Items.BLACK_MUSHROOM_INK),

    LAVA(Items.LAVA_SCALE, Items.LAVA_SCALE_SHARD); // special case creates multiple amount

    fun crush(player: Player){
        if(!player.hasPM()) {
            player.message("You do not have the pestle and mortar required.")
            return
        }
        player.autoReplace(itemIn, itemOut, perform = {
            player.animate(id = 364, delay = 0)
            player.playSound(2608, 1, 0)
        }, success = {
            if(this == LAVA){
                val shards = Random.nextInt(3,5)
                player.inventory.add(itemOut, shards)
                player.message("You grind the lava dragon scale into $shards shards.")
            } else if(message != "") {
                player.message(message)
            }
        })
    }

    companion object {
        val PaM = Items.PESTLE_AND_MORTAR

        fun Player.hasPM(container: ItemContainer = inventory): Boolean {
            return container.contains(PaM)
        }
    }
}