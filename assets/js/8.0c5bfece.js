(window.webpackJsonp=window.webpackJsonp||[]).push([[8],{273:function(t,r,e){t.exports=e.p+"assets/img/icon-512.c3e13cf4.png"},288:function(t,r,e){"use strict";e.r(r);var v=e(13),_=Object(v.a)({},(function(){var t=this,r=t._self._c;return r("ContentSlotsDistributor",{attrs:{"slot-key":t.$parent.slotKey}},[r("h1",{attrs:{id:"djudger"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#djudger"}},[t._v("#")]),t._v(" DJudger")]),t._v(" "),r("img",{staticStyle:{zoom:"50%"},attrs:{src:e(273)}}),t._v(" "),r("p",[t._v("在右上角可切换中英文文档 / Switch Chinese and English in the upper right corner")]),t._v(" "),r("p",[r("strong",[t._v("如果有任何问题或建议，欢迎联系"),r("a",{attrs:{href:"https://github.com/NicerWang",target:"_blank",rel:"noopener noreferrer"}},[t._v("@NicerWang"),r("OutboundLink")],1),t._v("或提出"),r("code",[t._v("Issue")])]),t._v("。")]),t._v(" "),r("h2",{attrs:{id:"项目简介"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#项目简介"}},[t._v("#")]),t._v(" 项目简介")]),t._v(" "),r("p",[t._v("基于Docker的代码执行容器，目前"),r("strong",[t._v("仅有 Java 版本")]),t._v("。")]),t._v(" "),r("p",[t._v("通过在项目中使用 DJudger ，您可以为项目添加代码运行能力，且代码运行高效、安全，有一定的并发承载能力，可以兼容各类语言，支持多台机器作为执行机器。")]),t._v(" "),r("ul",[r("li",[r("strong",[t._v("特性：")]),t._v(" 使用简单 / 可拓展性强 / 安全性高 / 快速响应和分配")])]),t._v(" "),r("blockquote",[r("p",[t._v("如果你想使用 DJudger 作为 OJ 的代码运行器，你可以尝试使用管道符 "),r("code",[t._v("|")]),t._v(" 来解决输入问题，目前版本暂时不支持重定向 stdin 。")]),t._v(" "),r("p",[t._v("本项目暂无开发 Spring Boot Starter 的规划。")])]),t._v(" "),r("h2",{attrs:{id:"关于本项目的一些思考"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#关于本项目的一些思考"}},[t._v("#")]),t._v(" 关于本项目的一些思考")]),t._v(" "),r("p",[t._v("在操作系统中，处理死锁问题有三种思路，分别是预防（打破四个必要条件之一）、动态避免、恢复，这里用死锁问题来类比恶意代码防范问题，讨论三种恶意代码防范的方法：")]),t._v(" "),r("ul",[r("li",[r("p",[t._v("如何实现预防？")]),t._v(" "),r("p",[t._v("在代码执行之前就需要有能力检测出恶意代码，这是被抛弃的方案，因为C语言的宏定义近乎万能，可以轻松找到破解既有规则的方案，所以这里不进行讨论。但是这部分内容是有价值的，可以作为其他方案的补充。")])]),t._v(" "),r("li",[r("p",[t._v("如何实现动态避免？")]),t._v(" "),r("p",[t._v("这是现今使用最多的方案，通过 Linux 底层的一些机制，限制程序的系统调用，监控程序的CPU占用率、执行时间、内存占用等信息，对恶意行为进行动态避免。")]),t._v(" "),r("p",[t._v("这一方案性能较好，但是实现的难度较大，要求了解 Linux 系统的相关知识（ ptrace 等），且需要针对不同语言单独给出设计方案，如 C 语言需要预防编译阶段的攻击，但是 Python 语言就不需要。")])]),t._v(" "),r("li",[r("p",[t._v("如何实现恢复？")]),t._v(" "),r("p",[t._v("这是 DJudger 解决这一问题的方案。通过 Docker 容器来作为沙盒运行代码，可以完全屏蔽不同语言之间的差异，且不需要 Linux 系统相关知识，甚至可以运行在支持 Docker 的任意系统上。对于每种语言，只需要设计一个 Docker 镜像，就能得到一个安全高效的代码执行环境。")])])]),t._v(" "),r("h2",{attrs:{id:"项目文档索引"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#项目文档索引"}},[t._v("#")]),t._v(" 项目文档索引")]),t._v(" "),r("ul",[r("li",[r("p",[r("RouterLink",{attrs:{to:"/zh/quick-start.html"}},[t._v("快速入门")])],1)]),t._v(" "),r("li",[r("p",[r("RouterLink",{attrs:{to:"/zh/api-reference.html"}},[t._v("常用配置项参考")])],1)]),t._v(" "),r("li",[r("p",[r("RouterLink",{attrs:{to:"/zh/add-new-language.html"}},[t._v("添加新的编程语言")])],1)]),t._v(" "),r("li",[r("p",[r("RouterLink",{attrs:{to:"/zh/how-to-be-safe.html"}},[t._v("安全性是如何保证的？")])],1)]),t._v(" "),r("li",[r("p",[r("RouterLink",{attrs:{to:"/zh/project-structure.html"}},[t._v("项目架构要点")])],1)])]),t._v(" "),r("h2",{attrs:{id:"项目规划"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#项目规划"}},[t._v("#")]),t._v(" 项目规划")]),t._v(" "),r("ul",[r("li",[r("p",[t._v("快速入门脚本")])]),t._v(" "),r("li",[r("p",[t._v("针对常见恶意代码的测试")])]),t._v(" "),r("li",[r("p",[t._v("分布式部署模式设计")])]),t._v(" "),r("li",[r("p",[t._v("项目架构介绍")])]),t._v(" "),r("li",[r("p",[t._v("Spring 集成")])])])])}),[],!1,null,null,null);r.default=_.exports}}]);