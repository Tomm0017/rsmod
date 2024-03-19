package gg.rsmod.game.model.appearance

/**
 * Represents the valid genders in the game.
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class Gender(val id: Int) {
    MALE(id = 0),
    FEMALE(id = 1);

    companion object {
        val values = enumValues<Gender>()
    }
}