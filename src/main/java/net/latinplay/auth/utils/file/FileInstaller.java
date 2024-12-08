package net.latinplay.auth.utils.file;

import me.blueslime.bungeemeteor.libs.utilitiesapi.utils.consumer.PluginConsumer;

import java.io.File;
import java.nio.file.Files;

public class FileInstaller {

    /**
     * Verify if a file is empty
     *
     * @param file to check.
     * @return true if file don't have content or only has comments.
     */
    @SuppressWarnings("resource")
    public static boolean isFileEmpty(File file) {
        if (!file.exists() || !file.isFile()) {
            return true;
        }

        if (file.length() == 0) {
            return true;
        }

        return PluginConsumer.ofUnchecked(
            () -> Files.lines(file.toPath()).allMatch(line -> line.trim().isEmpty() || line.trim().startsWith("#")),
            e -> {},
            () -> false
        );
    }

}
