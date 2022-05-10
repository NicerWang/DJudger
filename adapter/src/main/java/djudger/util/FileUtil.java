package djudger.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileUtil {
    public static void writeCode(String directory, String fileName, String code) throws IOException {
        if (!new File(directory).mkdir()) {
            LogUtil.logger.error("[FILE]Directory Already Exists(Possibly Caused by Same Identifier)");
        }
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.write(code.getBytes(StandardCharsets.UTF_8));
        fos.close();
    }
}
