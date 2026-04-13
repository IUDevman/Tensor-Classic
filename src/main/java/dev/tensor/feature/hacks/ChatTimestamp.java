package dev.tensor.feature.hacks;

import dev.tensor.backend.events.ChatReceivedEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.event.imp.Priority;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Hoosiers
 * @since 04-11-2026
 */

@Hack.Info(name = "ChatTimestamp", category = Category.Misc)
public final class ChatTimestamp extends Hack {

    public final EnumSetting format = new EnumSetting("Format", Format.Hour24$Min$Sec);
    public final EnumSetting separator = new EnumSetting("Separator", Separator.Brackets);
    public final BooleanSetting space = new BooleanSetting("Spaced", true);
    public final EnumSetting dateColor = new EnumSetting("Date Color", ChatColor.RED);
    public final EnumSetting separatorColor = new EnumSetting("Sep Color", ChatColor.LIGHT_GRAY);

    private String messageCutPrev = "";

    @Override
    public void onDisable(boolean isNull) {
        this.messageCutPrev = "";
    }

    @SuppressWarnings("unused")
    @EventTarget(value = Priority.LOWEST)
    public void onChatReceivedEvent(ChatReceivedEvent event) {
        if (this.messageCutPrev != "") {

            if (event.message.startsWith(">")) {
                String first = event.message.substring(0, 1);
                String rest = event.message.substring(1);
                event.message = first + this.messageCutPrev + rest;

            } else if (event.message.startsWith("§") && event.message.length() > 1) {
                String first = event.message.substring(0, 2);
                String rest = event.message.substring(2);
                event.message = first + this.messageCutPrev + rest;

            } else {
                event.message = this.messageCutPrev + event.message;
            }

            this.messageCutPrev = "";
        }

        String leftSeparator = this.getSeparator(true);

        String dateFormat = this.getDateFormat();
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        String timeStamp = format.format(new Date());

        String rightSeparator = this.getSeparator(false);

        event.message = this.getColor((ChatColor) this.separatorColor.getValue())
                + leftSeparator + this.getColor((ChatColor) this.dateColor.getValue())
                + timeStamp + this.getColor((ChatColor) this.separatorColor.getValue())
                + rightSeparator + (this.space.getValue() ? " " : "") + this.getColor(ChatColor.DEFAULT)
                + event.message;

        if (this.getFontRenderer().getStringWidth(event.message) > 320) {

            int firstEnd = 1;

            while (firstEnd < event.message.length() && this.getFontRenderer().getStringWidth(event.message.substring(0, firstEnd + 1)) <= 320) {
                firstEnd++;
            }

            String cutMessage = event.message.substring(0, firstEnd);
            this.messageCutPrev = event.message.substring(firstEnd);
            event.message = cutMessage;
        } else {
            this.messageCutPrev = "";
        }

        event.cancel();
    }

    private String getDateFormat() {
        switch ((Format) this.format.getValue()) {
            case Hour24$Min:
                return "K:mm";
            case Hour24$Min$Sec:
                return "K:mm:ss";
            case Hour12$Min:
                return "h:mm";
            case Hour12$Min$Sec:
                return "h:mm:ss";
            case Hour12$Min$XX:
                return "h:mm a";
            default:
                return "h:mm:ss a";
        }
    }

    private String getSeparator(boolean left) {
        switch ((Separator) this.separator.getValue()) {
            case Line:
                return "|";
            case Left_Arrow:
                if (left) return ">";
                else return "<";
            case Right_Arrow:
                if (left) return "<";
                else return ">";
            case Brackets:
                if (left) return "[";
                else return "]";
            case Curly_Brackets:
                if (left) return "{";
                else return "}";
            default:
                return "";
        }
    }

    private enum Format {
        Hour24$Min,
        Hour24$Min$Sec,
        Hour12$Min,
        Hour12$Min$Sec,
        Hour12$Min$XX,
        Hour12$Min$Sec$XX
    }

    private enum Separator {
        Left_Arrow,
        Right_Arrow,
        Line,
        Brackets,
        Curly_Brackets,
        None
    }
}
