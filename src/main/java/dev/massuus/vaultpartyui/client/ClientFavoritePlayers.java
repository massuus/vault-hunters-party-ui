package dev.massuus.vaultpartyui.client;

import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class ClientFavoritePlayers {
    private static final String FILE_NAME = "vaultpartyui-favorites.txt";
    private static final Set<UUID> FAVORITES = new LinkedHashSet<>();

    private static boolean loaded;

    private ClientFavoritePlayers() {
    }

    public static boolean isFavorite(UUID playerId) {
        ensureLoaded();
        return playerId != null && FAVORITES.contains(playerId);
    }

    public static void toggleFavorite(UUID playerId) {
        if (playerId == null) {
            return;
        }
        setFavorite(playerId, !isFavorite(playerId));
    }

    public static void setFavorite(UUID playerId, boolean favorite) {
        if (playerId == null) {
            return;
        }

        ensureLoaded();
        if (favorite) {
            FAVORITES.add(playerId);
        } else {
            FAVORITES.remove(playerId);
        }
        save();
    }

    private static void ensureLoaded() {
        if (loaded) {
            return;
        }
        loaded = true;

        Path file = favoritesFile();
        if (file == null || !Files.exists(file)) {
            return;
        }

        try {
            List<String> lines = Files.readAllLines(file);
            for (String line : lines) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                try {
                    FAVORITES.add(UUID.fromString(trimmed));
                } catch (IllegalArgumentException ignored) {
                }
            }
        } catch (IOException ignored) {
        }
    }

    private static void save() {
        Path file = favoritesFile();
        if (file == null) {
            return;
        }

        try {
            Path parent = file.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.write(file, FAVORITES.stream().map(UUID::toString).toList());
        } catch (IOException ignored) {
        }
    }

    private static Path favoritesFile() {
        return FMLPaths.CONFIGDIR.get().resolve(FILE_NAME);
    }
}