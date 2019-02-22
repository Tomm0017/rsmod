package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.UpdateAppearanceMessage
import gg.rsmod.game.model.Gender
import gg.rsmod.game.model.entity.Client
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
class UpdateAppearanceHandler : MessageHandler<UpdateAppearanceMessage> {

    override fun handle(client: Client, message: UpdateAppearanceMessage) {
        val gender = if (message.gender == 1) Gender.FEMALE else Gender.MALE
        val appearance = message.appearance
        val colors = message.colors

        log(client, "Update appearance: gender=%s, appearance=%s, colors=%s", gender.toString(), Arrays.toString(appearance), Arrays.toString(colors))

        appearance.forEachIndexed { index, app -> client.looks[index] = app }
        colors.forEachIndexed { index, color -> client.lookColors[index] = color }
    }
}