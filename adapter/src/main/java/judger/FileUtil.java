package judger;

import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;


public class FileUtil {
    public static String writeCode(LangEnum lang, String fileName, String code){
        File file = null;
        try{
            File directory = new File(new File(PropertyUtil.codePath, lang.getFileSymbol()),fileName);
            if(!directory.mkdir()){
                PropertyUtil.logger.log(Level.ERROR,"[FILE]Father directory not exists");
            }
            if(lang == LangEnum.CPP){
                file = new File(directory,"main.cpp");
            }
            else if(lang == LangEnum.Java){
                file = new File(directory,"Main.java");
            }
            else if(lang == LangEnum.Python){
                file = new File(directory, "main.py");
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(code.getBytes(StandardCharsets.UTF_8));
            fos.close();
        }catch (Exception e){
            PropertyUtil.logger.log(Level.ERROR,"[FILE]File write error");
            e.printStackTrace();
            return null;
        }
        return file.getAbsolutePath();
    }
}
