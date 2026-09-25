package dev.tensor.feature.hacks;

import dev.tensor.TensorClassic;
import dev.tensor.backend.events.variable.ChatSentEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.EventPriority;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.EnumSetting;

/**
 * @author Hoosiers
 * @since 03-24-2026
 */

@Hack.Info(name = "Suffix", category = Category.Misc, description = "Puts the cool Tensor suffix at the end of send chat messages.")
public final class Suffix extends Hack {

    public final EnumSetting separator = new EnumSetting("Separator", Separator.Right_Arrow, new String[]{
            "This is what will encapsulate the suffix text.",
            "Left Arrow® <<Tensor.",
            "Right Arrow® >>Tensor.",
            "Line® ||Tensor||.",
            "Brackets® [Tensor].",
            "None® No separator."
    });
    public final EnumSetting clientName = new EnumSetting("Client", Clients.Tensor, new String[]{
            "This is what client name will be put on the suffix."
    });

    @SuppressWarnings("unused")
    private final EventAction<ChatSentEvent> chatSentEventAction = this.addHackEventAction(new EventAction<>(ChatSentEvent.class, EventPriority.Lowest, event -> {
        if (event.isCancelled()) {
            return;
        }

        String message = event.ARGS;

        if (!message.startsWith("/") && !message.startsWith(this.getHackManager().getHack(Commands.class).prefix.getValue())) {

            String messageSuffix = message + " " + this.getSuffix(this.getClientName());

            if (messageSuffix.length() > 100) {
                return;
            }

            event.ARGS = messageSuffix;
        }
    }));

    private String getClientName() {
        if (this.clientName.getValue().equals(Clients.Tensor)) {
            return TensorClassic.MOD_NAME_SHORT;
        }

        return this.clientName.getValue().name();
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

    public enum Clients {
        Tensor,
        Relique,
        $now,
        Velocity,
        Space,
        Rose,
        Hephaestus,
        End,
        Radium,
        Infinity,
        GameSense,
        MaybeAClient,
        FreeCliff
    }
}
