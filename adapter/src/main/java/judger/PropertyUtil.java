package judger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Properties;

public class PropertyUtil {
    static final Logger logger = LogManager.getLogger("judger");
    static String dockerSocket;
    static String codePath;
    static Integer timeLimit;
    static String seccompFile;
    static Integer queuedTaskCnt;
    static Integer maxContainer;

    public static void init(){
        Properties properties = new Properties();
        InputStream inputStream = DockerAdapter.class.getClassLoader().getResourceAsStream("adapter.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("[CONF]adapter.properties not found.");
            e.printStackTrace();
        }
        dockerSocket = properties.getProperty("docker.socket","unix:///var/run/docker.sock");
        codePath = properties.getProperty("docker.code","~/codes");
        timeLimit = Integer.parseInt(properties.getProperty("time_limit","10"));
        queuedTaskCnt = Integer.parseInt(properties.getProperty("queued_task_cnt","4"));
        maxContainer = Integer.parseInt(properties.getProperty("max_container","2"));
        String seccompPath = properties.getProperty("docker.seccomp","~/seccomp/default.json");
        try {
            String str;
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(seccompPath));
            while ((str = br.readLine()) != null) {
                stringBuilder.append(str);
            }
            seccompFile = stringBuilder.toString();
//            seccompFile = "{}";
        } catch (FileNotFoundException e) {
            logger.error("[CONF]seccomp file not found.");
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("[CONF]seccomp file read error.");
            e.printStackTrace();
        }
        initLang(properties);
        logger.info("[CONF]configuration imported.");
    }

    private static void initLang(Properties properties) {
        for(LangEnum langType: LangEnum.values()){
            String prefix = langType.getFileSymbol() + ".";
            if(!Boolean.parseBoolean(properties.getProperty(prefix + "support"))){
                continue;
            }
            Lang newLang = new Lang(langType);
            for(String command:properties.getProperty(prefix + "command").split(";"))
                newLang.getCommands().add(command);
            newLang.setImageName(properties.getProperty(prefix + "image_name"));
            newLang.setTestCommand(properties.getProperty(prefix + "test_command"));
            newLang.setTestResult(properties.getProperty(prefix + "test_result"));
            Allocator.lang.put(langType,newLang);
        }
    }
}
