package gg.rsmod.game.model.container

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class ContainerKey(val name: String) {

    override fun equals(other: Any?): Boolean = (other as? ContainerKey)?.name == name

    override fun hashCode(): Int = name.hashCode()
}