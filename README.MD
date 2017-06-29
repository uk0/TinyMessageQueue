# Tiny Message Queue
TMQ是一个小型分布式消息队列，它实现了自动持久化，由纯JAVA实现，几行代码即可创建消息队列服务。

## 消息格式
当前版本对消息格式做了严格的限制，以后可能会考虑自定义消息

`[任意字符作为开头,惯例使用@][消息ID][消息内容长度][消息内容][(校验码，TMQ会自动生成)]`

## 使用
````java
MasterWorker master = new MasterWorker(5120);	//创建TMQ
master.configureWorker();						//配置TMQ
master.handleRequest();							//处理请求
````