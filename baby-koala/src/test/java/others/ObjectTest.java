package others;


import java.util.List;
import java.util.stream.Stream;
import org.junit.Test;

public class ObjectTest {

  @Test
  public void objectTest() {
    //Stream.of(Actor.class.getDeclaredFields())
        //.forEach(field -> System.out.println(field.getName()));
  }

  @Test
  public void classTest() {
    Class<List> c = List.class;

    System.out.println(c.equals(List.class));
  }
}
