package github.koala.orm.util.meta;

import com.mysql.cj.core.result.Field;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/7/7.
 * @description TODO
 */
@Slf4j
@Data
public class ColumnMeta {
    String columnName;
    String columnType;
    Class columnClass;
}
