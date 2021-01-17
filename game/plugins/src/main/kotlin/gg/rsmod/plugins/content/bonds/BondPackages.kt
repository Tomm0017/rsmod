package gg.rsmod.plugins.content.bonds

import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.content.bonds.BondPackages.TRADEABLE_BOND
import gg.rsmod.plugins.content.bonds.BondPackages.UNTRADEABLE_BOND

data class BondPackage(val displayText: String, val bondsRequired: Int, val displayIcon: Int = 1129, val bonusDays: Int = 0,
          val iconSizeW: Int = 39, val iconSizeH: Int = 39, val debugText: String = "")

object BondPackages {
    val TRADEABLE_BOND = Items.OLD_SCHOOL_BOND
    val UNTRADEABLE_BOND = Items.OLD_SCHOOL_BOND_UNTRADEABLE

    val packages = mutableListOf<BondPackage>()

    val subheader = "Premier Club 2021"

    init {
        packages.add(BondPackage("1 Bond", 1, 1129))
        packages.add(BondPackage("2 Bonds", 2, 1129, 1))
        packages.add(BondPackage("3 Bonds", 3, 1129, 3))
        packages.add(BondPackage("", 0, -1, 0, 0, 0))
        packages.add(BondPackage("", 0, -1, 0, 0, 0))

        packages.add(BondPackage("", 0, -1, 0, 0, 0))
        packages.add(BondPackage("Premier Club: Bronze", 5, 1408, 14, 40, 40))
        packages.add(BondPackage("Premier Club: Silver", 10, 1409, 28, 40, 40))
        packages.add(BondPackage("Premier Club: Gold", 20, 1410, 85, 40, 40))
        packages.add(BondPackage("", 0, -1, 0, 0, 0))

        packages.add(BondPackage("", 0, -1, 0, 0, 0))
        packages.add(BondPackage("", 0, -1, 0, 0, 0))
        packages.add(BondPackage("", 0, -1, 0, 0, 0))
        packages.add(BondPackage("", 0, -1, 0, 0, 0))
        packages.add(BondPackage("", 0, -1, 0, 0, 0))
    }
}

fun Player.bondCount(): Int {
    var count = 0
    count += bonds.getItemCount(UNTRADEABLE_BOND)
    count += bonds.getItemCount(TRADEABLE_BOND)
    count += inventory.getItemCount(UNTRADEABLE_BOND)
    count += inventory.getItemCount(TRADEABLE_BOND)
    count += bank.getItemCount(UNTRADEABLE_BOND)
    count += bank.getItemCount(TRADEABLE_BOND)
    return count
}