---
title: 添加新的编程语言
---

# 添加新的编程语言

1. 编写测试代码文件，选择合适的 Docker 镜像，定制 Dockerfile ，而后进行构建，其中至少包含以下内容：

   > 为什么需要测试代码？参阅[安全性是如何实现的？](how-to-be-safe.md) #3

   ```dockerfile
   FROM xx:version # 基础镜像
   
   # 以下三步是必须的
   WORKDIR /code
   COPY test.xx .
   RUN mkdir 语言名称 # 如py/java/c
   
   CMD ["bash"]
   ```

2. 在构建配置文件时，进行如下修改：

   ```java
   Config config = new Config(ModeEnum.CLASSIC)
     .addLang(languageName,languageFileName,imageName,testCommand,testResult)
   	...
   ```

   其中：

   | 名称             | 含义                                                     |
   | ---------------- | -------------------------------------------------------- |
   | languageName     | 第一步中的语言名称，为 runCode方法中需要提供的第一个参数 |
   | languageFileName | 创建的代码文件名称，对于 Java 语言需要特殊注意主类名问题 |
   | imageName        | 第一步中构建的镜像名称                                   |
   | testCommand      | 测试命令                                                 |
   | testResult       | 测试正确执行的预期结果                                   |

### 示例：DJudger 默认的 C++ 配置

https://github.com/NicerWang/DJudger/tree/master/containers/c

1. 文件准备：

   * Dockerfile

     ```dockerfile
     FROM gcc:9.4
     
     WORKDIR /code
     COPY test.cpp .
     RUN mkdir c # 文件夹名称必须和 languageName 一致
     
     CMD ["bash"]
     ```

   * 测试代码

     ```cpp
     #include<iostream>
     using namespace std;
     int main(){
     	cout<<"Pass"<<endl;
     }
     ```

2. 构建容器：

   ```shell
   cd containers/c
   sudo docker build -t judger_c .
   ```

3. 在构建配置对象时引入：

   ```java
   Config config = new Config(ModeEnum.CLASSIC)
     .addLang("c","main.cpp","judger_c","g++ test.cpp&&./a.out","Pass")
   	...
   ```

   

