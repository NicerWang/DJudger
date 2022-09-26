(window.webpackJsonp=window.webpackJsonp||[]).push([[16],{290:function(t,a,s){"use strict";s.r(a);var n=s(13),e=Object(n.a)({},(function(){var t=this,a=t._self._c;return a("ContentSlotsDistributor",{attrs:{"slot-key":t.$parent.slotKey}},[a("h1",{attrs:{id:"常用配置项参考"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#常用配置项参考"}},[t._v("#")]),t._v(" 常用配置项参考")]),t._v(" "),a("h2",{attrs:{id:"config配置类"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#config配置类"}},[t._v("#")]),t._v(" Config配置类")]),t._v(" "),a("ul",[a("li",[a("p",[t._v("构造方法")]),t._v(" "),a("div",{staticClass:"language-java extra-class"},[a("pre",{pre:!0,attrs:{class:"language-java"}},[a("code",[a("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("Config")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),a("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("ModeEnum")]),t._v(" modeEnum"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(";")]),t._v("\n")])])]),a("ul",[a("li",[a("p",[t._v("目前可选：（介绍见下文）")]),t._v(" "),a("ul",[a("li",[a("p",[t._v("ModeEnum.CLASSIC - 经典模式（默认）")])]),t._v(" "),a("li",[a("p",[t._v("ModeEnum.THREAD_POOL - 线程池模式")])])])])])]),t._v(" "),a("li",[a("p",[t._v("添加配置")]),t._v(" "),a("ul",[a("li",[a("p",[a("code",[t._v("configDocker(dockerSocket)")])]),t._v(" "),a("p",[t._v("设置 Docker 的 TCP 或 Unix Socket（推荐）。")]),t._v(" "),a("p",[t._v("默认为："),a("code",[t._v("unix:///var/run/docker.sock")])])]),t._v(" "),a("li",[a("p",[a("code",[t._v("configCodePath(codePath)")])]),t._v(" "),a("p",[t._v("配置代码文件存放目录。")])]),t._v(" "),a("li",[a("p",[a("code",[t._v("addLang()")])]),t._v(" "),a("p",[t._v("参见"),a("RouterLink",{attrs:{to:"/zh/add-new-language.html"}},[t._v("添加新的编程语言")]),t._v("。")],1)]),t._v(" "),a("li",[a("p",[a("code",[t._v("addDefaultLang()")])]),t._v(" "),a("p",[t._v("添加默认的三种语言的支持，即 Java 、 Python 和 C++ 。")])]),t._v(" "),a("li",[a("p",[a("code",[t._v("enableSeccomp(seccompPath)")])]),t._v(" "),a("p",[t._v("设置 seccomp 文件的路径，对容器进行系统调用能力限制，参见"),a("RouterLink",{attrs:{to:"/zh/how-to-be-safe.html"}},[t._v("安全性是如何保证的？")]),t._v(" #2。")],1)])])])]),t._v(" "),a("h2",{attrs:{id:"allocator模式"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#allocator模式"}},[t._v("#")]),t._v(" Allocator模式")]),t._v(" "),a("h3",{attrs:{id:"_1-modeenum-classic-经典模式"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#_1-modeenum-classic-经典模式"}},[t._v("#")]),t._v(" 1.ModeEnum.CLASSIC - 经典模式")]),t._v(" "),a("p",[t._v("DJudger v0.x版本的唯一可选模式。")]),t._v(" "),a("p",[t._v("每个Docker容器对应一个线程，每种语言对应一个阻塞队列，Allocator仅负责启动容器线程，并将任务放入队列，并等待任务完成。")]),t._v(" "),a("p",[t._v("容器线程会主动从队列中取出任务，并在对应容器进行执行。")]),t._v(" "),a("p",[t._v("如果容器执行出现问题，对应的线程会被结束，如果有需要，会新建一个容器线程，对应新的容器。")]),t._v(" "),a("p",[t._v("示例如下：")]),t._v(" "),a("div",{staticClass:"language-java extra-class"},[a("pre",{pre:!0,attrs:{class:"language-java"}},[a("code",[a("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("Config")]),t._v(" config "),a("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("new")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("Config")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),t._v("\n  "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(".")]),a("span",{pre:!0,attrs:{class:"token function"}},[t._v("configCodePath")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),a("span",{pre:!0,attrs:{class:"token string"}},[t._v('"/path/to/code"')]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),t._v("\n  "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(".")]),a("span",{pre:!0,attrs:{class:"token function"}},[t._v("configDocker")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),a("span",{pre:!0,attrs:{class:"token string"}},[t._v('"unix:///var/run/docker.sock"')]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),t._v("\n  "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(".")]),a("span",{pre:!0,attrs:{class:"token function"}},[t._v("addDefaultLang")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),t._v("\n  "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(".")]),a("span",{pre:!0,attrs:{class:"token function"}},[t._v("configClassicAllocator")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),t._v("collectInterval"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("queuedTaskCnt"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("maxContainers"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(";")]),t._v("\n")])])]),a("table",[a("thead",[a("tr",[a("th",[t._v("名称")]),t._v(" "),a("th",[t._v("含义")]),t._v(" "),a("th",[t._v("默认值")])])]),t._v(" "),a("tbody",[a("tr",[a("td",[a("code",[t._v("collectInterval")])]),t._v(" "),a("td",[t._v("清理无用容器的时间间隔（秒）")]),t._v(" "),a("td",[t._v("1800")])]),t._v(" "),a("tr",[a("td",[a("code",[t._v("queuedTaskCnt")])]),t._v(" "),a("td",[t._v("每个容器的最多排队任务（超出后将会增加容器，直至达到"),a("code",[t._v("maxContainer")]),t._v("）")]),t._v(" "),a("td",[t._v("4")])]),t._v(" "),a("tr",[a("td",[a("code",[t._v("maxContainer")])]),t._v(" "),a("td",[t._v("每个语言最多的容器数目")]),t._v(" "),a("td",[t._v("2")])])])]),t._v(" "),a("h3",{attrs:{id:"_2-modeenum-thread-pool-线程池模式"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#_2-modeenum-thread-pool-线程池模式"}},[t._v("#")]),t._v(" 2.ModeEnum.THREAD_POOL - 线程池模式")]),t._v(" "),a("p",[t._v("基于 Java 的 ThreadPoolExecutor ，是 DJudger v1.x 新增的 Allocator 模式，由于配置项较多，建议仅熟悉线程池的用户使用。")]),t._v(" "),a("p",[t._v("对于每种语言，维护一个线程池，线程池内的线程和容器一一对应，如果容器出现问题，则会删除旧容器，新建容器后替换当前线程对应的容器。")]),t._v(" "),a("p",[t._v("参数即为 ThreadPoolExecutor 的构造参数（除 ThreadFactory ），"),a("code",[t._v("BlockQueue<Runable>")]),t._v("和"),a("code",[t._v("RejectedExecutionHandler")]),t._v("需要使用 Lambda 表达式提供，示例如下：")]),t._v(" "),a("div",{staticClass:"language-java extra-class"},[a("pre",{pre:!0,attrs:{class:"language-java"}},[a("code",[a("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("Config")]),t._v(" config "),a("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("new")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("Config")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),a("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("ModeEnum")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(".")]),a("span",{pre:!0,attrs:{class:"token constant"}},[t._v("THREAD_POOL")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),t._v("\n  "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(".")]),a("span",{pre:!0,attrs:{class:"token function"}},[t._v("configCodePath")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),a("span",{pre:!0,attrs:{class:"token string"}},[t._v('"/path/to/code"')]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),t._v("\n  "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(".")]),a("span",{pre:!0,attrs:{class:"token function"}},[t._v("configDocker")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),a("span",{pre:!0,attrs:{class:"token string"}},[t._v('"unix:///var/run/docker.sock"')]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),t._v("\n  "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(".")]),a("span",{pre:!0,attrs:{class:"token function"}},[t._v("addDefaultLang")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),t._v("\n  "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(".")]),a("span",{pre:!0,attrs:{class:"token function"}},[t._v("configThreadPoolAllocator")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),a("span",{pre:!0,attrs:{class:"token number"}},[t._v("2")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),a("span",{pre:!0,attrs:{class:"token number"}},[t._v("4")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),a("span",{pre:!0,attrs:{class:"token number"}},[t._v("20")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),a("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("TimeUnit")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(".")]),a("span",{pre:!0,attrs:{class:"token constant"}},[t._v("SECONDS")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),a("span",{pre:!0,attrs:{class:"token operator"}},[t._v("->")]),a("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("new")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("ArrayBlockingQueue")]),a("span",{pre:!0,attrs:{class:"token generics"}},[a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("<")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(">")])]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),a("span",{pre:!0,attrs:{class:"token number"}},[t._v("10")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),a("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("ThreadPoolExecutor"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(".")]),t._v("CallerRunsPolicy")]),a("span",{pre:!0,attrs:{class:"token operator"}},[t._v("::")]),a("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("new")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(";")]),t._v("\n")])])]),a("h2",{attrs:{id:"allocatorfactory"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#allocatorfactory"}},[t._v("#")]),t._v(" AllocatorFactory")]),t._v(" "),a("p",[t._v("构造 Allocator ：")]),t._v(" "),a("div",{staticClass:"language-java extra-class"},[a("pre",{pre:!0,attrs:{class:"language-java"}},[a("code",[a("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("Allocator")]),t._v(" allocator "),a("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("AllocatorFactory")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(".")]),a("span",{pre:!0,attrs:{class:"token function"}},[t._v("build")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),t._v("config"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(";")]),t._v("\n")])])]),a("h2",{attrs:{id:"allocator的runcode方法"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#allocator的runcode方法"}},[t._v("#")]),t._v(" Allocator的runCode方法")]),t._v(" "),a("div",{staticClass:"language-java extra-class"},[a("pre",{pre:!0,attrs:{class:"language-java"}},[a("code",[a("span",{pre:!0,attrs:{class:"token comment"}},[t._v("// 语言名称，默认Python为py、Java为java、C++为c")]),t._v("\n"),a("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("String")]),t._v(" lang "),a("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token string"}},[t._v('"py"')]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(";")]),t._v(" \n"),a("span",{pre:!0,attrs:{class:"token comment"}},[t._v("// 需要执行的命令")]),t._v("\n"),a("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("List")]),a("span",{pre:!0,attrs:{class:"token generics"}},[a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("<")]),a("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("String")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(">")])]),t._v(" commands "),a("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("Arrays")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(".")]),a("span",{pre:!0,attrs:{class:"token function"}},[t._v("asList")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),a("span",{pre:!0,attrs:{class:"token string"}},[t._v('"python $(code)"')]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(";")]),t._v("\n"),a("span",{pre:!0,attrs:{class:"token comment"}},[t._v("// 时间限制")]),t._v("\n"),a("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("Integer")]),t._v(" timeLimit "),a("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token number"}},[t._v("1000")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(";")]),t._v("\n"),a("span",{pre:!0,attrs:{class:"token comment"}},[t._v("// 时间限制的单位")]),t._v("\n"),a("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("TimeUnit")]),t._v(" timeUnit "),a("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("TimeUnit")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(".")]),a("span",{pre:!0,attrs:{class:"token constant"}},[t._v("MILLISECONDS")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(";")]),t._v("\n"),a("span",{pre:!0,attrs:{class:"token comment"}},[t._v("// 代码标识符")]),t._v("\n"),a("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("String")]),t._v(" identifier "),a("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token string"}},[t._v('"my_first_code"')]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(";")]),t._v("\n"),a("span",{pre:!0,attrs:{class:"token comment"}},[t._v("// 代码")]),t._v("\n"),a("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("String")]),t._v(" code "),a("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token string"}},[t._v('"print(\\"Hello DJudger!\\")"')]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(";")]),t._v("\n"),a("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("Task")]),t._v(" task "),a("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),t._v(" allocator"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(".")]),a("span",{pre:!0,attrs:{class:"token function"}},[t._v("runCode")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),t._v("lang"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("commands"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),a("span",{pre:!0,attrs:{class:"token number"}},[t._v("2000")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("timeUnit"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("identifier"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("code"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(";")]),t._v("\n"),a("span",{pre:!0,attrs:{class:"token comment"}},[t._v("// Task对象中含有代码的运行结果等信息")]),t._v("\n"),a("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("System")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(".")]),t._v("out"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(".")]),a("span",{pre:!0,attrs:{class:"token function"}},[t._v("print")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),t._v("task"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(".")]),a("span",{pre:!0,attrs:{class:"token function"}},[t._v("getStdout")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(";")]),t._v("\n")])])]),a("h2",{attrs:{id:"日志"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#日志"}},[t._v("#")]),t._v(" 日志")]),t._v(" "),a("p",[t._v("使用了Slf4j，您可以自行选择底层日志系统。")])])}),[],!1,null,null,null);a.default=e.exports}}]);