---
title: Add New Language
sidebar: false
---

# Add New Language

1. Choose an appropriate Docker image, write test code files, and build a custom Dockerfile that contains at least the following:

   > Why Code for test needed？Refer to [How To Be Safe?](how-to-be-safe.md) #3

   ```dockerfile
   FROM xx:version # Base Image
   
   # Follow 3 steps are necessary
   WORKDIR /code
   COPY test.xx .
   RUN mkdir languageName # eg.py/java/c
   
   CMD ["bash"]
   ```

2. Configuration:

   ```java
   Config config = new Config(ModeEnum.CLASSIC)
     .addLang(languageName,languageFileName,imageName,testCommand,testResult)
   	...
   ```

   Among them:

   | Parameter Name   | Meaning                                                      |
   | ---------------- | ------------------------------------------------------------ |
   | languageName     | languageName in 1st step, as the first parameter of `runCode` method. |
   | languageFileName | The code file name created, requires special attention to the main class name problem for `Java` language. |
   | imageName        | Image Name built in 1st step.                                |
   | testCommand      | Commands for test.                                           |
   | testResult       | Expected results of the test for normal execution.           |

### Example：DJudger Default C++ Configuration

https://github.com/NicerWang/DJudger/tree/master/containers/c

1. File Preparation：

   * Dockerfile

     ```dockerfile
     FROM gcc:9.4
     
     WORKDIR /code
     COPY test.cpp .
     RUN mkdir c # must be identical with languageName
     
     CMD ["bash"]
     ```

   * Code for test

     ```cpp
     #include<iostream>
     using namespace std;
     int main(){
     	cout<<"Pass"<<endl;
     }
     ```

2. Build Image：

   ```shell
   cd containers/c
   sudo docker build -t judger_c .
   ```

3. Configuration：

   ```java
   Config config = new Config(ModeEnum.CLASSIC)
     .addLang("c","main.cpp","judger_c","g++ test.cpp&&./a.out","Pass")
   	...
   ```

   

