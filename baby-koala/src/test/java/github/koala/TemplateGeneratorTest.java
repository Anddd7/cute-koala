package github.koala;

import com.alibaba.fastjson.JSON;
import com.koala.orm.dao.ActorDao;
import com.koala.orm.dao.impl.ActorDaoImpl;
import com.koala.orm.domian.Actor;
import com.koala.normal.services.UserService;
import github.eddy.common.FileSystemTool;
import github.eddy.common.YAMLScanner;
import github.koala.core.factory.KoalaFactory;
import github.koala.generator.KoalaGenerator;
import github.koala.generator.domain.ConfigDefine;
import github.koala.orm.DataBasePool;
import github.koala.orm.conn.DBConnection;
import github.koala.orm.conn.MysqlConnection;
import github.koala.orm.util.Generator;
import java.io.FileNotFoundException;
import java.util.List;
import org.junit.Test;

/**
 * @author edliao on 2017/6/26.
 * @description 模板生成
 */
public class TemplateGeneratorTest {

  @Test
  public void testYaml() throws FileNotFoundException {
    new KoalaGenerator()
        .generate(FileSystemTool.getProjectPath() + "/generator-test/",
            YAMLScanner.getConfigInClassPath("template/modules-define.yaml", ConfigDefine.class));
  }

  @Test
  public void testWithBeanFactory() throws FileNotFoundException {
    new KoalaGenerator()
        .generate(FileSystemTool.getProjectPath() + "/src/test/java/",
            YAMLScanner.getConfigInClassPath("template/modules-define.yaml", ConfigDefine.class));
  }

  @Test
  public void test() throws ClassNotFoundException {
    KoalaFactory beanFactory = KoalaFactory.of(Class.forName("com.koala.UserHttpModule"));
    beanFactory.getBean(UserService.class).welcome();
  }

  @Test
  public void testORMGenerate() {
    DBConnection connection = new MysqlConnection("localhost:3306", "sakila", "root", "root");
    Generator generator = new Generator(connection);
    generator.generate("src/test/java", "com.koala.orm", "actor");
  }


  @Test
  public void testORMCall() {
    DBConnection connection = new MysqlConnection("localhost:3306", "sakila", "root", "root");
    DataBasePool.addDBConnection(connection);
    ActorDao dao = new ActorDaoImpl();
    Actor example = new Actor();
    example.setActorId(195);
    List<Actor> actors = dao.selectByExample(example,0,10);
    actors.forEach(actor -> System.out.println(JSON.toJSONString(actor)));
  }
}
