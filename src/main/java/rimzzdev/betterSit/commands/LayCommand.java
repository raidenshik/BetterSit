package rimzzdev.betterSit.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import rimzzdev.betterSit.SitManager;
import rimzzdev.betterSit.config.LanguageManager;

@CommandAlias("lay")
@CommandPermission("betterSit.sit")
@Description("Lie down or stand up")
@SuppressWarnings("unused")
public class LayCommand extends BaseCommand {

    private final SitManager sitManager;
    private final LanguageManager lang;

    public LayCommand(SitManager sitManager, LanguageManager lang) {
        this.sitManager = sitManager;
        this.lang = lang;
    }

    @Default
    public void onLay(Player player) {
        if (sitManager.isLaying(player)) {
            if (sitManager.unlay(player)) {
                Component msg = lang.getPrefixedMessage("stood-up");
                if (msg != Component.empty()) {
                    player.sendMessage(msg);
                }
            } else {
                player.sendMessage(Component.text("Could not stand up.").color(NamedTextColor.RED));
            }
            return;
        }

        long remaining = sitManager.getCooldownRemaining(player);
        if (remaining > 0) {
            Component msg = lang.getPrefixedMessage("cooldown", (remaining / 1000 + 1));
            if (msg != Component.empty()) {
                player.sendMessage(msg);
            }
            return;
        }

        if (sitManager.lay(player)) {
            sitManager.updateCooldown(player);
            Component msg = lang.getPrefixedMessage("now-laying");
            if (msg != Component.empty()) {
                player.sendMessage(msg);
            }
        } else {
            Component msg = lang.getPrefixedMessage("lay-failed");
            if (msg != Component.empty()) {
                player.sendMessage(msg);
            }
        }
    }
}