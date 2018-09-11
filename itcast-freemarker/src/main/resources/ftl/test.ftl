<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Freemarker 测试</title>
</head>
<body>
<#-- 这是 freemarker 注释，不会输出到文件中 -->
<h1>${name}；${message}</h1>
<br/>
<#--assign-->
<#--简单类型-->
<#--此指令用于在页面上定义一个变量。语法：<#assign name=value>-->
<#assign linkman="黑马"/>
联系人:${linkman}
<br/>

<#--对象-->
<#assign info={"mobile":"138000000","address":"广州天河吉山"}/>
联系电话:${info.mobile},联系地址:${info.address}
<br>

<#--include-->
<#include "header.ftl">
<br>

<#--if-->
<#assign bool=true>
<#if bool>
    bool的值为true
<#else>
    bool的值为false
</#if>
<br>

<#list goodsList as goods>
    ${goods_index},名称为: ${goods.name};价格为:${goods.price}<br>
</#list>
<hr>
<#--获取集合总记录数-->
总共${goodsList?size}条记录
<br>

<#--将字符串转成为json对象-->
<#assign str="{'id':123,'text':'itcast'}"/>
<#assign  jsonObj = str?eval/>
id为:${jsonObj.id};text为:${jsonObj.text}
<br>

<#--日期格式化-->
当前日期:${today?date}<br>
当前时间:${today?time}<br>
当前日期+时间:${today?datetime}<br>
格式化显示当前日期时间:${today?string('yyyy年MM月dd日 HH:mm:ss')}<br>
<br>

<#--数值显示处理-->
number=${number};number?c=${number?c}
<br>

<#--空值的处理-->
${str!"str空值的默认显示值"}
<br>

<#--判断变量是否存在-->
<#if 1 gt 2>
    str变量存在.
<#else>
    str变量不存在
</#if>
<hr>
</body>
</html>