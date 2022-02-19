package djudger.util;

import djudger.Allocator;
import djudger.entity.Lang;
import djudger.entity.LangEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Properties;

public class PropertyUtil {
    public static final Logger logger = LogManager.getLogger("DJudger");
    public static String dockerSocket;
    public static String codePath;
    public static Integer timeLimit;
    public static String seccompFile;
    public static Integer queuedTaskCnt;
    public static Integer maxContainer;
    public static Integer collectTime;

    public static void init() {
        Properties properties = new Properties();
        InputStream inputStream = DockerAdapter.class.getClassLoader().getResourceAsStream("adapter.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("[CONF]adapter.properties not found.");
            e.printStackTrace();
        }
        dockerSocket = properties.getProperty("docker.socket", "unix:///var/run/docker.sock");
        codePath = properties.getProperty("docker.code", "/root/codes");
        timeLimit = Integer.parseInt(properties.getProperty("time_limit", "10"));
        queuedTaskCnt = Integer.parseInt(properties.getProperty("queued_task_cnt", "4"));
        maxContainer = Integer.parseInt(properties.getProperty("max_container", "2"));
        collectTime = Integer.parseInt(properties.getProperty("collect_time", "1800"));
        String seccompPath = properties.getProperty("docker.seccomp", "/root/seccomp/default.json");
        try {
            String str;
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(seccompPath));
            while ((str = br.readLine()) != null) {
                stringBuilder.append(str);
            }
            seccompFile = stringBuilder.toString();
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
        for (LangEnum langType : LangEnum.values()) {
            String prefix = langType.getFileSymbol() + ".";
            if (!Boolean.parseBoolean(properties.getProperty(prefix + "support"))) {
                continue;
            }
            Lang newLang = new Lang(langType);
            newLang.setImageName(properties.getProperty(prefix + "image_name"));
            newLang.setTestCommand(properties.getProperty(prefix + "test_command"));
            newLang.setTestResult(properties.getProperty(prefix + "test_result"));
            Allocator.lang.put(langType, newLang);
        }
    }
}
