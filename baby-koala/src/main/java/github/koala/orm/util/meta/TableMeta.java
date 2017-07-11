package github.koala.orm.util.meta;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * @author edliao on 2017/7/7.
 * @description TODO
 */
@Data
public class TableMeta {

  String schemaName;
  String tableName;
  List<ColumnMeta> primaryKeys = new ArrayList<>();
  List<ColumnMeta> columns = new ArrayList<>();//last_name,12,java.lang.String
  List<ColumnMeta> notKeyColumns = new ArrayList<>();

  public void addNotKeyColumns(ColumnMeta columnMeta) {
    notKeyColumns.add(columnMeta);
  }

  public void addColumn(ColumnMeta columnMeta) {
    columns.add(columnMeta);
  }

  public void addPrimaryKey(ColumnMeta columnMeta) {
    primaryKeys.add(columnMeta);
  }

}
