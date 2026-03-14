package rimzzdev.betterSit.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;

@SuppressWarnings("deprecation")
public class ModrinthUpdateChecker {
    private static final String MODRINTH_API = "https://api.modrinth.com/v2/project/bettersit/version";
    private static final String MODRINTH_URL = "https://modrinth.com/plugin/bettersit/version/";
    public static void checkForUpdate(Player player, JavaPlugin plugin) {
        if (!player.hasPermission("betterSit.admin")) return;

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                URI uri = new URI(MODRINTH_API);
                HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestProperty("User-Agent", "BetterSit-UpdateChecker");

                if (conn.getResponseCode() != 200) return;

                try (InputStreamReader reader = new InputStreamReader(conn.getInputStream())) {
                    JsonArray versions = JsonParser.parseReader(reader).getAsJsonArray();
                    if (versions.isEmpty()) return;

                    JsonObject latest = versions.get(0).getAsJsonObject();
                    String latestVersion = latest.get("version_number").getAsString();
                    String currentVersion = plugin.getDescription().getVersion();

                    if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                        sendUpdateMessage(player, latestVersion);
                    }
                }
            } catch (Exception ignored) {}
        });
    }

    private static void sendUpdateMessage(Player player, String latestVersion) {
        Component message = Component.text()
                .append(Component.text("[BetterSit] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.text("New version available: ", NamedTextColor.GREEN))
                .append(Component.text("v" + latestVersion, NamedTextColor.YELLOW))
                .append(Component.text(". ", NamedTextColor.GRAY))
                .append(Component.text("Click here", NamedTextColor.AQUA, TextDecoration.UNDERLINED)
                        .clickEvent(ClickEvent.openUrl(MODRINTH_URL + latestVersion)))
                .append(Component.text(" to download.", NamedTextColor.GRAY))
                .build();

        player.sendMessage(message);
    }
}