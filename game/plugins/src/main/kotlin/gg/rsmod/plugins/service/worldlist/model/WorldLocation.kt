package gg.rsmod.plugins.service.worldlist.model

/**
 * @author Triston Plummer ("Dread")
 *
 * Represents the various world locations
 *
 * @param id    The flag id
 */
enum class WorldLocation(val id: Int) {
    UNITED_STATES(0),
    UNITED_KINGDOM(1),
    CANADA(2),
    AUSTRALIA(3),
    NETHERLANDS(4),
    SWEDEN(5),
    FINLAND(6),
    GERMANY(7)
}