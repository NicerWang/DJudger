---
title: 首页
sidebar: auto
---

# DJudger

<img src="https://pictures-nicerwang-1256891306.cos.ap-beijing.myqcloud.com//imgicon-512.png" alt="icon-512" style="zoom:50%;" />

在右上角可切换中英文文档 / Switch Chinese and English in the upper right corner

**如果有任何问题或建议，欢迎联系[@NicerWang](https://github.com/NicerWang)或提出`Issue`**。

## 项目简介

基于Docker的代码执行容器，目前**仅有 Java 版本**。

通过在项目中使用 DJudger ，您可以为项目添加代码运行能力，且代码运行高效、安全，有一定的并发承载能力，可以兼容各类语言，支持多台机器作为执行机器。

* **特性：** 使用简单 / 可拓展性强 / 安全性高 / 快速响应和分配

> 如果你想使用 DJudger 作为 OJ 的代码运行器，你可以尝试使用管道符 `|` 来解决输入问题，目前版本暂时不支持重定向 stdin 。
>
> 本项目暂无开发 Spring Boot Starter 的规划。

## 项目文档索引

* [快速入门](quick-start.md)

* [常用配置项参考](api-reference.md)

* [添加新的编程语言](add-new-language.md)

* [安全性是如何保证的？](how-to-be-safe.md)

* [项目架构要点](project-structure.md)

## 项目规划

* 快速入门脚本

* 针对常见恶意代码的测试

* 分布式部署模式设计

* 项目架构介绍

* Spring 集成
