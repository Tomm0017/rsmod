/**
 * Gets the 'interface hash' of a given interface id and
 * child component. This value is commonly used in ClientScripts when referring
 * to a child component in the game. An 'interface hash' is in the format of (parent >> 16) | child
 *
 * Example: 335.getInterfaceHash(25) would return 21954585, which is the 'absolute' id of the component
 *
 * @param child     The child component
 */
fun Int.getInterfaceHash(child: Int = -1) : Int {
    val value = (this shl 16)
    if (child != -1) return value or child
    return value
}