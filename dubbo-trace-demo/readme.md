# 基于dubbo的分布式系统调用跟踪demo

Quick Start：

+ 确保[zipkin server](http://121.40.31.185:9411/)已经正常启动；
+ 确保[zookeeper](zookeeper://120.55.85.150:2181?backup=120.55.85.150:2182,120.55.85.150:2183)已经正常启动：
+ 分别运行以下命令：

	+ 克隆项目：
	
		```bash
		git clone https://gitlab.codiruifu.com/codi-product/config.git
		git clone https://gitlab.codiruifu.com/codi-product/dubbo-trace.git
		```
	    config使用默认配置
	    dubbo-trace-demo中dubbo-web，user-web,order-web需要配置CODI_HOME配置
	    
	+ 安装项目：

		```bash
		mvn clean install -DskipTests
		```
	
	+ 运行user服务:

		```bash
		./run_user.sh
		```
	
	+ 运行order服务:

		```bash
		./run_order.sh
		```
	
	+ 运行web服务：

		```bash
		./run_web.sh
		```
	
	+ 在浏览器请求：

		```
		http://localhost:10000/api/orders/create 正常场景
		http://localhost:10000/api/orders/delete 异常场景
		```
	
	+ 即可zipkin-server中查看跟踪记录。
