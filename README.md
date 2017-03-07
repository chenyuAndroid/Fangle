# Fangle
仿知乎日报的应用

完整实现了知乎日报的功能，包括日报文章的获取、日报文章的详细内容的获取、数据的持久化（即离线阅读已加载文章）、收藏文章等功能。
代码整体采用MVP架构，分为Model、Presenter、View三层。其中Model为数据层，负责从网络加载数据、将数据保存到本地数据库；Presenter为中间层，
负责数据到View层的传递；View层即为显示层，一般为Activity、Fragment等。该项目还使用了Retrofit2、EventBus、Picasso等主流开源库，
遵从Material Design规范。除此之外，笔者还使用到了自己写的另外一个库BannerViewPager，实现顶部的轮播图。

具体可以查下以下的图片：

![pic01](https://github.com/chenyuAndroid/Fangle/blob/master/Fangle/pic/pic01.png)
![pic02](https://github.com/chenyuAndroid/Fangle/blob/master/Fangle/pic/pic02.png)
