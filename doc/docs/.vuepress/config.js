module.exports = {
    title: 'DJudger文档',
    description: '基于 Docker 的代码执行容器',
    base: "/DJudger/",
    head: [
      ['link', { rel: 'manifest', href: '/manifest.webmanifest' }],
      ['meta', { name: 'theme-color', content: '#fff' }],
      ['link', { rel: 'icon', type: 'image/png', sizes: '196x196', href: '/favicon.png' }],
      ['link', { rel: 'apple-touch-icon', href: '/apple-icon-180.png' }]
    ],
    locales: {
      '/': {
        lang: 'en-US',
        title: 'DJudger Docs',
        description: 'Docker container as sandbox for running codes'
      },
      '/zh/': {
        lang: 'zh-CN',
        title: 'DJudger文档',
        description: '基于 Docker 的代码执行容器'
      }
    },
    themeConfig: {
      logo: '/icon-512.png',
      repo: "https://github.com/NicerWang/DJudger",
      lastUpdated: '上次更新',
      locales: {
        '/': {
          label: 'English',
          selectText: '选择语言',
          ariaLabel: '选择语言',
          lastUpdated: 'Last Updated',
          nav: [
            { text: 'Quick Start', link: '/quick-start' },
            { text: 'API Reference', link: '/api-reference' },
          ]
        },
        '/zh/': {
          label: '简体中文',
          selectText: 'Languages',
          ariaLabel: 'Select language',
          lastUpdated: '上次更新',
          nav: [
            { text: '快速入门', link: '/zh/quick-start' },
            { text: '常用配置项参考', link: '/zh/api-reference' },
          ]
        },
      }
    },
    plugins: [
        '@vuepress/medium-zoom',
        '@vuepress/back-to-top',
        '@vuepress/plugin-search',
        ["vuepress-plugin-auto-sidebar", {
          title: {
            mode: "titlecase",
          },
          sidebarDepth: 2,
        }],
        ["@vuepress/last-updated", {
          transformer: (timestamp, lang) => {
            const moment = require('moment');
            moment.locale(lang)
            return moment(timestamp).utcOffset(480).format('LLLL') + " GMT+8"
          }
        }]
      ],
  }
