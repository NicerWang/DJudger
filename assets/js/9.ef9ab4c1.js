(window.webpackJsonp=window.webpackJsonp||[]).push([[9],{274:function(t,r,s){t.exports=s.p+"assets/img/structure.4e60fbfa.png"},292:function(t,r,s){"use strict";s.r(r);var a=s(13),e=Object(a.a)({},(function(){var t=this,r=t._self._c;return r("ContentSlotsDistributor",{attrs:{"slot-key":t.$parent.slotKey}},[r("h1",{attrs:{id:"项目架构要点"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#项目架构要点"}},[t._v("#")]),t._v(" 项目架构要点")]),t._v(" "),r("h2",{attrs:{id:"代码是如何被执行的"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#代码是如何被执行的"}},[t._v("#")]),t._v(" 代码是如何被执行的？")]),t._v(" "),r("p",[r("img",{attrs:{src:s(274),alt:""}})]),t._v(" "),r("p",[t._v("代码实际上是在 Docker 容器中被执行，这些容器来源于定制的镜像，由这些镜像生成的容器除了（编译）执行代码之外，不负责其他任何任务。")]),t._v(" "),r("p",[t._v("DJudger 实际上处理了容器的创建、删除、分配等工作。")]),t._v(" "),r("h2",{attrs:{id:"为什么需要定制-docker-镜像"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#为什么需要定制-docker-镜像"}},[t._v("#")]),t._v(" 为什么需要定制 Docker 镜像？")]),t._v(" "),r("ol",[r("li",[t._v("需要为每种语言设计对应的（编译）执行环境，这一环境就由 Docker 镜像体现。")]),t._v(" "),r("li",[t._v("代码执行并不总是顺利的，需要编写测试代码，在每次运行结束后，对容器进行测试，以保证容器能够顺利执行后续任务。")])])])}),[],!1,null,null,null);r.default=e.exports}}]);