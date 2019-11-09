package gg.rsmod.plugins.api.ext

import gg.rsmod.game.model.attr.*
import gg.rsmod.game.model.entity.Player

val Player.isNewAccount by NEW_ACCOUNT_ATTR
val Player.isNoclip by NO_CLIP_ATTR.defaultValue(false)
val Player.protectItemActive by PROTECT_ITEM_ATTR.defaultValue(false)
val Player.displayMode by DISPLAY_MODE_CHANGE_ATTR
val Player.currentShop by CURRENT_SHOP_ATTR
val Player.antifirePotCharges by ANTIFIRE_POTION_CHARGES_ATTR.defaultValue(0)
val Player.command by COMMAND_ATTR
val Player.commandArgs by COMMAND_ARGS_ATTR
val Player.interactingOption by INTERACTING_OPT_ATTR.defaultValue(0)
val Player.interactingSlot by INTERACTING_SLOT_ATTR
val Player.interactingGroundItem by INTERACTING_GROUNDITEM_ATTR
val Player.itemPickupTransaction by GROUNDITEM_PICKUP_TRANSACTION
val Player.interactingObj by INTERACTING_OBJ_ATTR
val Player.interactingNpc by INTERACTING_NPC_ATTR
val Player.interactingPlayer by INTERACTING_PLAYER_ATTR
val Player.interactingItemSlot by INTERACTING_ITEM_SLOT
val Player.interactingItemId by INTERACTING_ITEM_ID
val Player.interactingItem by INTERACTING_ITEM
val Player.otherItemSlot by OTHER_ITEM_SLOT_ATTR
val Player.otherItemId by OTHER_ITEM_ID_ATTR
val Player.otherItem by OTHER_ITEM_ATTR
val Player.interactingParent by INTERACTING_COMPONENT_PARENT
val Player.interactingChild by INTERACTING_COMPONENT_CHILD
val Player.levelUpSkillId by LEVEL_UP_SKILL_ID
val Player.levelUpIncrement by LEVEL_UP_INCREMENT
val Player.levelUpOldExp by LEVEL_UP_OLD_XP