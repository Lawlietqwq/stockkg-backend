# stockkg-backend

股权关系的知识图谱、股票板块排名推荐系统

* dependencies: 依赖版本管理
* framworks: 组件库
  * cache: Redis组件
  * common: 公共数据管理——工具类 & 异常类 & 异常处理切面 & 配置类 & 响应体 & 上下文等
  * database: 数据库模版类 & 分布式id
  * patterns: 职责链模版，负责数据校验、缓存加载
  * web: 防重复提交（待修改） & 日志配置类
* pyjobs: 部分python脚本，neo4j数据库建库以及tushare接口数据更新
* services: 应用程序
  * kg: 知识图谱系统
    * controller: 服务接口
      * ModelController: 调用远程服务器执行深度学习代码
      * HolderController: 股东节点相关操作
      * StockController: 股票节点接口
    * service: service层
    * filters: 职责链
      * node: 股票/股东校验链实现类
    * constant: 系统常量 & Redis-Key
    * dao: Neo4J数据库查询接口
    * mapper: MySQL数据库查询接口
    * remote: 远程接口
    * vo、dto、entity: 再封装
* model: 远程服务器提供调用接口，提供参数更新服务

