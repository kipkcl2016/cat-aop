# fw-starter-cat
通过 aspectj和spring-aop对sc项目进行埋点配置(接入cat)
### quick start
1. (Optional)将resources/lib目录下的jar部署到私服，并更新pom相关配置
1. 应用项目依赖加入cat-aop
1. 启动参数加入-javaagent:D:\repository\org\aspectj\aspectjweaver\1.8.13\aspectjweaver-1.8.13.jar(替换为你本地仓库位置)
### 配置相关
- fw.cat.enable(default true)用来开启关闭cat所有打点处理，可自行细化类别开关（例如mysql、http等）
- 其他配置，参考 [CatProperties](src/main/java/com/yq/starter/cat/config/CatProperties.java)  