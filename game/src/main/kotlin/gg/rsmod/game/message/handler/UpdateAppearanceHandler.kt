package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.UpdateAppearanceMessage
import gg.rsmod.game.model.Appearance
import gg.rsmod.game.model.Gender
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Client
import java.util.Arrays

/**
 * @author Tom <rspsmods@gmail.com>
 */
class UpdateAppearanceHandler : MessageHandler<UpdateAppearanceMessage> {

    override fun handle(client: Client, world: World, message: UpdateAppearanceMessage) {
        val gender = if (message.gender == 1) Gender.FEMALE else Gender.MALE
        val looks = message.appearance
        val colors = message.colors

        log(client, "Update appearance: gender=%s, appearance=%s, colors=%s", gender.toString(), Arrays.toString(looks), Arrays.toString(colors))
        client.queues.submitReturnValue(Appearance(looks, colors, gender))
    }
}