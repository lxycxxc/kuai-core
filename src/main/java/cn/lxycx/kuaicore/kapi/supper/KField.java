package cn.lxycx.kuaicore.kapi.supper;

import cn.lxycx.kuaicore.kapi.KRegex;

import java.lang.annotation.*;

//生成一个注释
@Documented
//表示当前注解可以打在什么东西上面,此处可以放在类上与方法上
@Target({ElementType.FIELD})
//指定被修饰的Annotation将具有继承性
@Inherited
//注解声明周期
@Retention(RetentionPolicy.RUNTIME)
public @interface KField {
    String col();


    /**
     * 描述
     * @return
     */

    String desc() default "";
    /**
     * 是否为数据库关键字
     * @return
     */
    boolean iskey() default false;

    /**
     * 是否作为查询字段
     * @return
     */
    boolean query() default true;

    /**
     * 是否作为查询条件
     * @return
     */
    boolean where() default false;

    /**
     * 是否允许被修改
     * @return
     */
    boolean edit() default false;

    /**
     * 设置校验
     * @return
     */
    String verify() default KRegex.NONE;

    /**
     * 设置取值，从请求头或者cookie等位置取值
     * @return
     */
    KSetValue value() default @KSetValue(key = "", type = SetValueType.Attr, roles = "");
}
