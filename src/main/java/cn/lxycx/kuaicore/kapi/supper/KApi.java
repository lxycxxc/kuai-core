package cn.lxycx.kuaicore.kapi.supper;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

//生成一个注释
@Documented
//表示当前注解可以打在什么东西上面,此处可以放在类上与方法上
@Target({ElementType.TYPE,ElementType.METHOD})
//指定被修饰的Annotation将具有继承性
@Inherited
//注解声明周期
@Retention(RetentionPolicy.RUNTIME)

@Component
public @interface KApi {
    String pkey() default "id";
    String alias();
    String remark() default "";
    String type() default "table";
    String tables();
    String queryTable() default "";
    String roles() default "查询";
}
