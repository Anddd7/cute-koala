package site.koalazoo.cutekoala;

import java.io.File;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author and777
 * @date 2018/1/7
 */
public class ToolsTest {

  @Test
  public void pathTool() {
    System.out.println(PathTool.OS_NAME);
    System.out.println(PathTool.MODULE_DIR);

    String projectPath = PathTool.getProjectPath();

    System.out.println(projectPath);

    Assert.assertTrue(new File(projectPath).exists());
    Assert.assertTrue(new File(projectPath).list().length > 0);
    Assert.assertEquals(projectPath,
        PathTool.getChild(PathTool.getParent(projectPath), "koala-tool"));

    Assert.assertEquals(PathTool.getParent("D:/"), "D:/");
    Assert.assertEquals(PathTool.getParent("/home"), "/home");
  }
}
