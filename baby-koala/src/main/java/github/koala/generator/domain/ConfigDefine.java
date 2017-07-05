package github.koala.generator.domain;

import java.util.List;
import lombok.Data;

/**
 * @author edliao on 2017/6/23.
 * @description 自动生成代码时对配置的描述pojo
 */
@Data
public class ConfigDefine {

  List<ModuleDefine> modules;
}
