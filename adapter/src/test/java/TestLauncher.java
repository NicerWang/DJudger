import djudger.Allocator;
import djudger.entity.LangEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestLauncher {

    static class Test extends Thread{
        String content;
        List<String> commands;
        String id;
        LangEnum type;
        @Override
        public void run() {
            super.run();
            System.out.print(Allocator.runCode(type,commands,id,content));
        }

        public Test(String content, List<String> commands, String id, LangEnum type) {
            this.content = content;
            this.commands = commands;
            this.id = id;
            this.type = type;
        }
    }

    public static void main(String[] args) throws Exception {
        Allocator.init();
        List<String> pyCommands = new ArrayList<>();
        pyCommands.add("python3 $(code)");
        List<String> javaCommands = new ArrayList<>();
        javaCommands.add("cd $(directory)");
        javaCommands.add("javac $(code)");
        javaCommands.add("java Main");
        List<String> cCommands = new ArrayList<>();
        cCommands.add("cd $(directory)");
        cCommands.add("g++ $(code)");
        cCommands.add("./a.out");
        String pyCode = "print(\"Good-PY\")";
        String javaCode = "class Main{\n" +
                "       public static void main(String[] args){\n" +
                "        System.out.println(\"Good-JAVA\");\n" +
                "    }\n" +
                "}";
        String cCode = "#include<iostream>\n" +
                "using namespace std;\n" +
                "int main(){\n" +
                "\tcout<<\"Good-C\"<<endl;\n" +
                "}\n";
        Random r = new Random();
        int idx = 0;
        while(true){
            Integer j = r.nextInt() % 3;
            if(j == 1){
                for(int i = 0; i < 10; i++){
                    new Test(javaCode,javaCommands,"" + r.nextInt(), LangEnum.Java).start();
                }
            }
            else if(j == 2){
                for(int i = 0; i < 10; i++){
                    new Test(cCode,cCommands,"" + r.nextInt(), LangEnum.CPP).start();
                }
            }
            else {
                for(int i = 0; i < 10; i++){
                    new Test(pyCode,pyCommands,"" + r.nextInt(), LangEnum.Python).start();
                }
            }
            Thread.sleep(1000);
            idx++;

            if(idx % 10 == 0){
                Thread.sleep(20 * 1000);
            }
            if(idx % 100 == 0) break;
        }
    }
}
