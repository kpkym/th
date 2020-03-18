打算写一个爬虫项目实现商品价格对比，方便看有哪些便宜的东西可以淘

* [ ] [煤炉(mercari)](https://www.mercari.com/jp/)
* [ ] [骏河屋~~垃圾屋~~(suruga-ya)](https://www.suruga-ya.jp/)
* [ ] \***********

# 项目思路
1. 抓取所有感兴趣的商品，保存在数据库中。(使用[webmagic](https://github.com/code4craft/webmagic)爬虫框架) **需要代理**
2. 隔一段时间再一次抓取，对比上一次的差异后进行干涉。(还没想好)
3. 用Vue或React写一个前端，好操作点。


# 问题解决
1. [解决WebMagic抓HTTPS时出现SSLException](http://nullpointer.pw/%E8%A7%A3%E5%86%B3WebMagic%E6%8A%93HTTPS%E6%97%B6%E5%87%BA%E7%8E%B0SSLException.html)
