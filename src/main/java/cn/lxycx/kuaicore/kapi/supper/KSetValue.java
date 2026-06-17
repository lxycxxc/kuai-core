package cn.lxycx.kuaicore.kapi.supper;

import java.lang.annotation.*;

//生成一个注释
@Documented
//表示当前注解可以打在什么东西上面,此处可以放在类上与方法上
@Target({ElementType.FIELD})
//指定被修饰的Annotation将具有继承性
@Inherited
//注解声明周期
@Retention(RetentionPolicy.RUNTIME)
public @interface KSetValue {
    String key();
    SetValueType type();
    int isnull() default 0;
    String[] roles();
}
