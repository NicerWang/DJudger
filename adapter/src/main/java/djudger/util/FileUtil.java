package djudger.util;

import djudger.entity.LangEnum;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;


public class FileUtil {
    public static String[] writeCode(LangEnum lang, String fileName, String code) {
        File file = null;
        String containerPath = null;
        String filePath = null;
        try {
            File directory = new File(new File(PropertyUtil.codePath, lang.getFileSymbol()), fileName);
            containerPath = "/code/" + lang.getFileSymbol() + "/" + fileName;
            if (!directory.mkdir()) {
                PropertyUtil.logger.log(Level.ERROR, "[FILE]Father directory already exists, check same identifier");
            }
            if (lang == LangEnum.CPP) {
                file = new File(directory, "main.cpp");
                filePath = containerPath + "/main.cpp";
            } else if (lang == LangEnum.Java) {
                file = new File(directory, "Main.java");
                filePath = containerPath + "/Main.java";
            } else if (lang == LangEnum.Python) {
                file = new File(directory, "main.py");
                filePath = containerPath + "/main.py";
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(code.getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (Exception e) {
            PropertyUtil.logger.log(Level.ERROR, "[FILE]File write error");
            e.printStackTrace();
            return null;
        }
        return new String[]{containerPath, filePath};
    }
}
