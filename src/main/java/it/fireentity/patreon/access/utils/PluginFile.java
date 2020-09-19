package it.fireentity.patreon.access.utils;

import com.google.common.io.ByteStreams;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

@Getter
public class PluginFile {
    private final String name;
    private final JavaPlugin plugin;
    private final File file;
    private final YamlConfiguration config;

    public PluginFile(String name, JavaPlugin plugin) {
        this.name = name;
        this.plugin = plugin;

        file = new File(plugin.getDataFolder(), name + ".yml");
        config = YamlConfiguration.loadConfiguration(file);
    }

    public File getFolder() {
        return plugin.getDataFolder();
    }

    public void createFolder() {
        File folder = getFolder();
        if (!folder.exists()) folder.mkdir();
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDefault() {
        if (file.exists()) return;

        createFolder();
        try {
            file.createNewFile();
            InputStream in = plugin.getResource(name + ".yml");
            OutputStream out = new FileOutputStream(file);
            ByteStreams.copy(in, out);

            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}