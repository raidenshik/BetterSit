package rimzzdev.betterSit;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.plugin.java.JavaPlugin;
import rimzzdev.betterSit.commands.BsitCommand;
import rimzzdev.betterSit.commands.LayCommand;
import rimzzdev.betterSit.commands.SitCommand;
import rimzzdev.betterSit.config.BetterSitConfig;
import rimzzdev.betterSit.config.LanguageManager;
import rimzzdev.betterSit.listeners.PlayerListener;

@SuppressWarnings("unused")
public final class BetterSit extends JavaPlugin {

    private static BetterSit instance;
    private SitManager sitManager;
    private BetterSitConfig pluginConfig;
    private LanguageManager languageManager;

    @Override
    public void onEnable() {
        instance = this;

        this.pluginConfig = new BetterSitConfig(this);
        this.languageManager = new LanguageManager(this);
        this.sitManager = new SitManager(pluginConfig);

        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new SitCommand(sitManager, languageManager));
        commandManager.registerCommand(new LayCommand(sitManager, languageManager));
        commandManager.registerCommand(new BsitCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerListener(sitManager, languageManager, this), this);

        getLogger().info("BetterSit enabled.");
    }

    @Override
    public void onDisable() {
        if (sitManager != null) {
            sitManager.unsitAll();
        }
        getLogger().info("BetterSit disabled.");
    }

    public static BetterSit getInstance() { return instance; }
    public BetterSitConfig getPluginConfig() { return pluginConfig; }
    public LanguageManager getLanguageManager() { return languageManager; }
    public SitManager getSitManager() { return sitManager; }
}