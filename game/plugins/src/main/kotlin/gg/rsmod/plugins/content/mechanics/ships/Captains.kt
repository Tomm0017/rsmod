package gg.rsmod.plugins.content.mechanics.ships

import gg.rsmod.plugins.api.cfg.Npcs


object Captains {
    val CAPTAINS = setOf(
            Captain(captainId = Npcs.CAPTAIN_TOBIAS, tripType = arrayOf(TripType.PORTSARIM_KARAMAJA) ),
            Captain(captainId = Npcs.SEAMAN_LORRIS, tripType = arrayOf(TripType.PORTSARIM_KARAMAJA)),
            Captain(captainId = Npcs.SEAMAN_THRESNOR, tripType = arrayOf(TripType.PORTSARIM_KARAMAJA)),
            Captain(captainId = Npcs.CUSTOMS_OFFICER, tripType = arrayOf(TripType.KARMAJA_PORTSARIM,TripType.BRIMHAVEN_ARDOUGNE,TripType.SHIPYARD_KHAZARD)),
            Captain(captainId = Npcs.CAPTAIN_BARNABY, tripType = arrayOf(TripType.ARDOUGNE_BRIMHAVEN)),
            Captain(captainId = Npcs.MONK_OF_ENTRANA, tripType = arrayOf(TripType.PORTSARIM_ENTRANA,TripType.ENTRANA_PORTSARIM)),
            Captain(captainId = Npcs.MONK_OF_ENTRANA_1166, tripType = arrayOf(TripType.PORTSARIM_ENTRANA,TripType.ENTRANA_PORTSARIM)),
            Captain(captainId = Npcs.MONK_OF_ENTRANA_1167, tripType = arrayOf(TripType.PORTSARIM_ENTRANA,TripType.ENTRANA_PORTSARIM))
    )

    data class Captain(val captainId : Int, val tripType : Array<TripType>) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Captain

            if (captainId != other.captainId) return false
            if (!tripType.contentEquals(other.tripType)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = captainId
            result = 31 * result + tripType.contentHashCode()
            return result
        }
    }
}