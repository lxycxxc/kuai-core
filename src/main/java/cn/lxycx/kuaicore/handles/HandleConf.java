package cn.lxycx.kuaicore.handles;

import java.lang.annotation.*;

//生成一个注释
@Documented
//表示当前注解可以打在什么东西上面,此处可以放在类上与方法上
@Target({ElementType.TYPE,ElementType.METHOD})
//指定被修饰的Annotation将具有继承性
@Inherited
//注解声明周期
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleConf {
    String name();
    String type() default "select,insert,update,delete";
    String remark() default "";
}
