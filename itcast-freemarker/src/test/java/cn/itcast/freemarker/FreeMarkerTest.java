package cn.itcast.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.FileWriter;
import java.util.*;

/*第一步：创建一个 Configuration 对象，直接 new 一个对象。构造方法的参数就
是 freemarker 的版本号
第二步：设置模板文件所在的路径
第三步：设置模板文件使用的字符集；一般为 utf-8
第四步：获取模板
第五步：创建一个模板使用的数据集，可以是 pojo 也可以是 map；一般是 Map
第六步：创建一个 Writer 对象，一般创建 FileWriter 对象，指定生成的文件名
第七步：调用模板对象的 process 方法输出文件
第八步：关闭流
* */
public class FreeMarkerTest {
    @Test
    public void test() throws Exception {
        //创建配置对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //设置默认生成文件编码
        configuration.setDefaultEncoding("utf-8");
        //设置模板路径
        configuration.setClassForTemplateLoading(FreeMarkerTest.class,"/ftl");
        //获取模板
        Template template = configuration.getTemplate("test.ftl");

        //加载数据
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("name","传智播客");
        dataModel.put("message","欢迎使用Freemarker.");

        List<Map<String,Object>> goodsList = new ArrayList<>();
        Map<String,Object> map1 = new HashMap<>();
        map1.put("name","苹果");
        map1.put("price",4.5);
        goodsList.add(map1);
        Map<String,Object> map2 = new HashMap<>();
        map2.put("name","香蕉");
        map2.put("price",2.5);
        goodsList.add(map2);
        dataModel.put("goodsList",goodsList);

        dataModel.put("today",new Date());

        dataModel.put("number",123456789L);


        //创建输出对象和输出路径
        FileWriter  fileWriter  =  new FileWriter("C:\\Users\\Administrator\\Desktop\\test.html");
        System.out.println(dataModel.toString());
        //渲染模板和数据
        template.process(dataModel,fileWriter);

        //关闭输出
        fileWriter.close();




    }
}
