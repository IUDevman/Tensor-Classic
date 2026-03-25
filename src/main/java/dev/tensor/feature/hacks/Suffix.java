package dev.tensor.feature.hacks;

import dev.tensor.TensorClassic;
import dev.tensor.backend.events.ChatSentEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.event.imp.Priority;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.EnumSetting;

/**
 * @author Hoosiers
 * @since 03-24-2026
 */

@Hack.Info(name = "Suffix", category = Category.Misc)
public final class Suffix extends Hack {

    public final EnumSetting separator = new EnumSetting("Separator", Separator.Right_Arrow);

    @SuppressWarnings("unused")
    @EventTarget(value = Priority.LOWEST)
    public void onChatSentEvent(ChatSentEvent event) {

        String message = event.ARGS;

        if (!message.startsWith("/") && !message.startsWith(this.getHackManager().getHack(Commands.class).prefix.getValue())) {

            String messageSuffix = message + " " + this.getSuffix(TensorClassic.MOD_NAME_SHORT);;

            if (messageSuffix.length() >= 256) {
                return;
            }

            event.ARGS = messageSuffix;
        }
    }

    private String getSuffix(String message) {
        String separator = "";

        switch ((Separator) this.separator.getValue()) {
            case Left_Arrow:
                separator = "<<";
                break;
            case Right_Arrow:
                separator = ">>";
                break;
            case Line:
                separator = "||";
                break;
            case Brackets:
                separator = "[";
                break;
            case None:
                separator = "";
                break;
        }

        return separator + message + (this.separator.getValue().equals(Separator.Brackets) ? "]" : "");
    }

    public enum Separator {
        Left_Arrow,
        Right_Arrow,
        Line,
        Brackets,
        None
    }
}
