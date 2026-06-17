---
title: 快速开始
date: 2025-01-10 10:00
---

## 引用jar包
::: info 提示
面向数据编程框架。
:::

**maven**
```xml
<dependency>
    <groupId>cn.lxycx</groupId>
    <artifactId>kuai-core</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## 创建一个Kapi接口
:::info 提示
写一个表的增删查改操作，无需再创建 Controller，service，mapper。只需创建一个 AbsKApi的实现类，用@KApi 注解声明即可。
:::
```java
@KApi(alias = "PureAdminUser",tables = "PURE_ADMIN_USER",roles = "查询,添加,修改,删除")
public class PureAdminUserApi extends AbsKApi {

    @TableName("PURE_ADMIN_USER")
    @Data @Accessors(chain=true)
    public static class PureAdminUser {

        @KField(col="ID",query = true,where = true,edit = false)
        private String id;

        /**头像*/
        @KField(col="AVATAR",query = true,where = true,edit = false)
        private String avatar;

        /**菜单排序*/
        @KField(col="rank",iskey = true,query = true,where = true,edit = true)
        private Long rank;
    }

    @Override
    public void setting(RegisterApi reg) {
        reg.setPojo(PureAdminUser.class); //绑定实体类
        //reg.yesCx(); //允许无条件查询
        //reg.yesSc(); //允许无条件删除
        //reg.yesXg(); //允许无条件修改
    }

}
```

##  启动配置（必要）
启动时添加扫描项: `cn.lxycx.kuaicore`
```java
@ComponentScan(value = "cn.lxycx.kuaicore")
@MapperScan("cn.lxycx.kuaicore")
@SpringBootApplication
public class KuaiWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(KuaiWebApplication.class, args);
    }
}
```


## 调用查询接口
Kapi 生成的接口地址为 `http://ip/项目名称/middle/别名/接口类型` ,如 `http://127.0.0.1/xxxx/middle/PureAdminUser/getList`, 其中别名对应@KApi 注解中的alias字段,接口类型则指定增删查改操作，详细参考 [接口调用规范](./apiparam.md)

### 请求实例
```javascript
var settings = {
   "url": "http://127.0.0.1/xxxx/middle/PureAdminUser/getList",
   "method": "POST",
   "headers": {
      "Content-Type": "application/json",
   },
   "data": JSON.stringify({
      "id": {
         "=": "ca2a44c61ecb47feb45ae5da5d2cbb84"
      },
      "ORDER_BY": {
         "rank": "ASC"
      }
   }),
};

$.ajax(settings).done(function (response) {
   console.log(response);
});
```

### 响应实例
```json
{
    "success": true,
    "message": "查询成功",
    "data": [
        {
            "id": "ca2a44c61ecb47feb45ae5da5d2cbb84",
            ...
        }
    ]
}

```

## 一些相关的约定
1. 有关联关系的表字段不要重名，如果重名，在写关联查询语句的时候必须要声明别名来区分，否则无法识别。
2. /middle/** ,/interface/**  被框架占用，扩展接口时避免使用
3. 配置文件中kuai.** 配置被框架占用，避免使用