package gg.rsmod.plugins.content.bonds

import gg.rsmod.game.model.attr.NEW_ACCOUNT_ATTR
import gg.rsmod.game.model.attr.FREE_BOND_CLAIMED_ATTR
import gg.rsmod.game.model.attr.MEMBERS_EXPIRES_ATTR
import gg.rsmod.game.model.timer.TimeConstants.DAY
import gg.rsmod.game.model.timer.TimeConstants.WEEK
import gg.rsmod.game.model.timer.TimeConstants.getBriefTimeContext
import gg.rsmod.game.model.timer.TimeConstants.getTimeContext
import gg.rsmod.plugins.content.bonds.BondPackages.TRADEABLE_BOND
import gg.rsmod.plugins.content.bonds.BondPackages.UNTRADEABLE_BOND
import gg.rsmod.plugins.content.inter.options.OptionsTab
import gg.rsmod.plugins.content.inter.options.OptionsTab.BONDS_INTERFACE_ID
import gg.rsmod.plugins.content.inter.options.OptionsTab.REDEEM_BONDS_INTERFACE_ID
import mu.KLogging
import java.util.*

val TAB_SELECTED_VARBIT = 9544

val FREE_BOND_WAITING_TIME = WEEK * 2

/**
 * One free tradeable and untradeable bond for new players
 * and a free tradeable bond can be claimed every 2 weeks
 */
on_login {
    val newAccount = player.attr[NEW_ACCOUNT_ATTR] ?: return@on_login
    if(newAccount){
        player.bonds.add(TRADEABLE_BOND)
        player.bonds.add(UNTRADEABLE_BOND)
        player.attr.put(FREE_BOND_CLAIMED_ATTR, System.currentTimeMillis().toString())
    }
}

fun updateBondsInterface(player: Player, memberRedemption: Int = 0, nameChangeRedemption: Int = 0) {
    val tradeableBankCount = player.bank.getItemCount(TRADEABLE_BOND)
    val tradeableInvCount = player.inventory.getItemCount(TRADEABLE_BOND)
    val tradeablePouchCount = player.bonds.getItemCount(TRADEABLE_BOND)

    val untradeableBankCount = player.bank.getItemCount(UNTRADEABLE_BOND)
    val untradeableInvCount = player.inventory.getItemCount(UNTRADEABLE_BOND)
    val untradeablePouchCount = player.bonds.getItemCount(UNTRADEABLE_BOND)

    player.runClientScript(733,
            tradeableBankCount, tradeableInvCount, tradeablePouchCount, memberRedemption,
            untradeableBankCount, untradeableInvCount, untradeablePouchCount, nameChangeRedemption)
}

fun openBondsInterface(player: Player, tab: Int) {
    if (!player.lock.canInterfaceInteract()) { return }
    player.setInterfaceUnderlay(color = -1, transparency = -1)
    player.openInterface(interfaceId = BONDS_INTERFACE_ID, dest = InterfaceDestination.MAIN_SCREEN)
    player.runClientScript(3650, 2, tab)

    /**
     * open interface initially not redeeming anything
     */
    updateBondsInterface(player)
}

on_button(interfaceId = OptionsTab.OPTIONS_INTERFACE_ID, component = 30) {
    openBondsInterface(player, player.getVarbit(TAB_SELECTED_VARBIT))
}

/**
 * Tab selection.
 */
on_button(interfaceId = BONDS_INTERFACE_ID, component = 103) {
    player.setVarbit(TAB_SELECTED_VARBIT, 0)
}
on_button(interfaceId = BONDS_INTERFACE_ID, component = 104) {
    player.setVarbit(TAB_SELECTED_VARBIT, 1)
}

/**
 * Buy membership button.
 */
on_button(interfaceId = BONDS_INTERFACE_ID, component = 105) {
    player.openUrl("https://rsmodders.club/membership.php")
}

/**
 * Buy bonds button. -> FREE OSRS BONDS every two weeks!
 */
on_button(interfaceId = BONDS_INTERFACE_ID, component = 99) {
    val now = System.currentTimeMillis()
    val last_claimed = player.attr.getOrDefault(FREE_BOND_CLAIMED_ATTR, now.toString()).toLong()
    player.message("Last bond claimed: ${Date(last_claimed)}")

    /**
     * if [last] defaulted to [now] then they never claimed one before(??)
     * this condition should never occur as it is set on first login; or if
     * the time that has passed is greater than [FREE_BOND_WAITING_TIME] (2 weeks)
     * then we give the player another tradeable bond here
     *   Note| 1000ms = 1s -> 2 weeks = 1000(ms) x 60(s) x 60(min) x 24(hours) x 14(days) = 1209600000ms
      */
    val time_lapsed = (now - last_claimed)
    player.message("time lapsed since last claim: $time_lapsed")
    if(last_claimed == now || time_lapsed > FREE_BOND_WAITING_TIME){
        player.inventory.add(Items.OLD_SCHOOL_BOND)
        player.attr[FREE_BOND_CLAIMED_ATTR] = now.toString()
    } else{
        val remaining = FREE_BOND_WAITING_TIME - time_lapsed
        player.message("time remaining: $remaining")
        player.message("You still have ${getBriefTimeContext(remaining)} until you can claim another bond.")
    }
}

/**
 * Bond pouch controls
 */
// tradeable_to_pouch_button = 113
on_button(interfaceId = BONDS_INTERFACE_ID, component = 113) {
    if(!player.inventory.contains(TRADEABLE_BOND))
        player.message("Your inventory doesn't contain any of those bonds.")
    else {
        if(player.bonds.hasSpace && player.inventory.remove(TRADEABLE_BOND).hasSucceeded()){
            player.bonds.add(TRADEABLE_BOND)
            updateBondsInterface(player)
        } else {
            player.message("Your pouch cannot hold any more bonds.")
        }
    }
}
// tradeable_from_pouch_button = 109
on_button(interfaceId = BONDS_INTERFACE_ID, component = 109) {
    if(!player.bonds.contains(TRADEABLE_BOND))
        player.message("Your pouch doesn't contain any of those bonds.")
    else {
        if(player.inventory.hasSpace && player.bonds.remove(TRADEABLE_BOND).hasSucceeded()){
            player.inventory.add(TRADEABLE_BOND)
            updateBondsInterface(player)
        } else {
            player.message("There is no space left in your inventory.")
        }
    }
}

// untradeable_to_pouch_button = 117
on_button(interfaceId = BONDS_INTERFACE_ID, component = 117) {
    if(!player.inventory.contains(UNTRADEABLE_BOND))
        player.message("Your inventory doesn't contain any of those bonds.")
    else {
        if(player.bonds.hasSpace && player.inventory.remove(UNTRADEABLE_BOND).hasSucceeded()){
            player.bonds.add(UNTRADEABLE_BOND)
            updateBondsInterface(player)
        } else {
            player.message("Your pouch cannot hold any more bonds.")
        }
    }
}
// untradeable_from_pouch_button = 124
on_button(interfaceId = BONDS_INTERFACE_ID, component = 124) {
    if(!player.bonds.contains(UNTRADEABLE_BOND))
        player.message("Your pouch doesn't contain any of those bonds.")
    else {
        if(player.inventory.hasSpace && player.bonds.remove(UNTRADEABLE_BOND).hasSucceeded()){
            player.inventory.add(UNTRADEABLE_BOND)
            updateBondsInterface(player)
        } else {
            player.message("There is no space left in your inventory.")
        }
    }
}

on_command("lastbond") {
    player.message("last bond claimed: ${Date(player.attr[FREE_BOND_CLAIMED_ATTR]!!.toLong())}")
}

//// cs2278[ package#, displayText, numberOfBonds, iconId, iconWidth, iconHeight, awardText, debugText ]
////   Note| if debugText is set then the package button is DISABLED and logs in the chat debugText
//player.runClientScript(2278, 1, "1 Bond", 1, 1129, 39, 39, "14 days", "")
//player.runClientScript(2278, 2, "2 Bonds", 2, 1129, 39, 39, "29 days", "")
//player.runClientScript(2278, 3, "3 Bonds", 3, 1129, 39, 39, "45 days", "")
//player.runClientScript(2277, "Premier Club 2021") // insert subheader at first break in BondPackage listings
//player.runClientScript(2278, 4, "",  0, -1, 0, 0, "",  "")
//player.runClientScript(2278, 5, "",  0, -1, 0, 0, "",  "")
//player.runClientScript(2278, 6, "",  0, -1, 0, 0, "",  "")
//player.runClientScript(2278, 7, "Premier Club: Bronze", 5, 1408, 40, 40, "3 months", "")
//player.runClientScript(2278, 8, "Premier Club: Silver", 10, 1409, 40, 40, "6 months", "")
//player.runClientScript(2278, 9, "Premier Club: Gold", 20, 1410, 40, 40, "12 months", "")
//player.runClientScript(2278, 10, "",  0, -1, 0, 0, "",  "")
//player.runClientScript(2278, 11, "",  0, -1, 0, 0, "",  "")
//player.runClientScript(2278, 12, "",  0, -1, 0, 0, "",  "")
//player.runClientScript(2278, 13, "",  0, -1, 0, 0, "",  "")
//player.runClientScript(2278, 14, "",  0, -1, 0, 0, "",  "")
//player.runClientScript(2278, 15, "",  0, -1, 0, 0, "",  "")
fun openBondRedeemInterface(player: Player){
    player.playSound(2266, 1, 0)
    player.runClientScript(2524, -1, -1)
    player.setVarbit(10149, 1)
    player.openInterface(interfaceId = REDEEM_BONDS_INTERFACE_ID, dest = InterfaceDestination.MAIN_SCREEN)

    var sep = false
    BondPackages.packages.forEachIndexed { dex, pkg ->
        if(pkg.displayText == "" && pkg.displayIcon == -1 && !sep){
            player.runClientScript(2277, BondPackages.subheader)
            sep = true
        }
        val pkgDays = 14 * pkg.bondsRequired + pkg.bonusDays
        player.runClientScript(2278, dex+1, pkg.displayText, pkg.bondsRequired, pkg.displayIcon,
                pkg.iconSizeW, pkg.iconSizeH, getTimeContext(pkgDays * DAY.toLong()), pkg.debugText)
    }

    player.runClientScript(2284)
    player.setVarp(1204, 0)
}

/**
 * Redeem membership button.
 */
on_button(interfaceId = BONDS_INTERFACE_ID, component = 79) {
    openBondRedeemInterface(player)
}

/**
 * Redeem option on bond items.
 */
arrayOf(TRADEABLE_BOND, UNTRADEABLE_BOND).forEach { item ->
    on_item_option(item, "Redeem") {
        openBondRedeemInterface(player)
    }
    on_item_option(item, "Deposit") {
        openBondsInterface(player, 1) // opens directly to the "My Bonds" tab
    }
}

/**
 * Redeem bonds interface.
 */
arrayOf(7,8,9,13,14,15).forEach { button ->
    on_button(interfaceId = REDEEM_BONDS_INTERFACE_ID, component = button) {
        player.setVarp(1204, button - 6)
    }
}

/**
 * Confirm redeem button
 */
on_button(interfaceId = REDEEM_BONDS_INTERFACE_ID, component = 24) {
    player.closeInterface(REDEEM_BONDS_INTERFACE_ID)
    player.queue(TaskPriority.STRONG) {
        player.lock()
        messageBox("Sending redemption request...", continues = true)
        updateBondsInterface(player, 1)

        val pack = BondPackages.packages[player.varps.getState(1204) - 1]
        val addingDays = (14 * pack.bondsRequired) + pack.bonusDays

        if(player.bondCount() < pack.bondsRequired){
            player.unlock()
            messageBox("Sorry, you do not have enough Old School Bonds for the selected<br>package.")
            return@queue
        }

        var claimed = 0
        while(claimed < pack.bondsRequired){
            when {
                player.bonds.remove(UNTRADEABLE_BOND).hasSucceeded() -> claimed++
                player.inventory.remove(UNTRADEABLE_BOND).hasSucceeded() -> claimed++
                player.bank.remove(UNTRADEABLE_BOND).hasSucceeded() -> claimed++
                player.bonds.remove(TRADEABLE_BOND).hasSucceeded() -> claimed++
                player.inventory.remove(TRADEABLE_BOND).hasSucceeded() -> claimed++
                player.bank.remove(TRADEABLE_BOND).hasSucceeded() -> claimed++
            }
        }

        // log a failure but since we likely claimed any number of bonds we'll continue as a courtesy
        if(claimed != pack.bondsRequired) KLogging().logger.error("improper bond package claim occurred for ${player.username} on ${Date(System.currentTimeMillis())}")

        updateBondsInterface(player)

        val now = System.currentTimeMillis()
        val current = player.attr.getOrDefault(MEMBERS_EXPIRES_ATTR, now.toString()).toLong()

        val expires = if(now > current){
            now + (addingDays * DAY)
        } else {
            current + (addingDays * DAY)
        }
        player.attr[MEMBERS_EXPIRES_ATTR] = expires.toString()

        wait(2)
        messageBox("You've gained $addingDays days of membership.<br>Please log out before attempting to log into members' worlds.")
        player.unlock()
    }
}
