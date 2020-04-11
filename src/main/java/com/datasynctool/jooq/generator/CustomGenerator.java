package com.datasynctool.jooq.generator;

import org.jooq.codegen.JavaWriter;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.TableDefinition;
import org.jooq.meta.TypedElementDefinition;
import org.jooq.tools.StringUtils;

import java.time.LocalDateTime;

public class CustomGenerator extends io.github.jklingsporn.vertx.jooq.generate.rx.RXAsyncGuiceVertxGenerator {
  @Override
  protected void writeDAOImports(JavaWriter out){
    super.writeDAOImports(out);
    out.println("import io.vertx.reactivex.core.MultiMap;");
  }

  @Override
  protected void writeDAOConstructor(JavaWriter out, String className, String tableIdentifier, String rType, String pType, String tType, String schema) {
    super.writeDAOConstructor(out, className, tableIdentifier, rType, pType, tType, schema);
    out.println();
    out.tab(1).println("public %s() {", className);
    out.tab(2).println("this(com.datasynctool.database.DBUtilsKt.getJooqConfig(), com.datasynctool.MainVerticle.Companion.getDbClient());");
    out.tab(1).println("}");
    //out.println("/* CustomGenerator writeDAOConstructor() %s %s %s %s %s %s*/", className, tableIdentifier, rType, pType, tType, schema);
  }

  @Override
  protected boolean handleCustomTypeFromJson(TypedElementDefinition<?> column, String setter, String columnType, String javaMemberName, JavaWriter out) {
    if(isType(columnType, LocalDateTime.class)){
      out.tab(2).println("%s(json.getString(\"%s\")==null?null:LocalDateTime.parse(json.getString(\"%s\")));", setter, javaMemberName, javaMemberName);
      return true;
    }
    return super.handleCustomTypeFromJson(column, setter, columnType, javaMemberName, out);
  }

  // Converts a LocalDateTime from/into a String during JSON-conversion.

  @Override
  protected boolean handleCustomTypeToJson(TypedElementDefinition<?> column, String getter, String columnType, String javaMemberName, JavaWriter out) {
    if(isType(columnType, LocalDateTime.class)){
      out.tab(2).println("json.put(\"%s\",%s()==null?null:%s().toString());", getJsonKeyName(column),getter,getter);
      return true;
    }
    return super.handleCustomTypeToJson(column, getter, columnType, javaMemberName, out);
  }

  @Override
  protected void generatePojoClassFooter(TableDefinition table, JavaWriter out) {
    super.generatePojoClassFooter(table, out);

    out.println();
    out.tab(1).println("public " + StringUtils.toCamelCase(table.getName()) + "(io.vertx.reactivex.core.MultiMap multiMap) {" );
    for (ColumnDefinition column : table.getColumns()) {
      String javaType = getJavaType(column.getType());
      String fieldName = StringUtils.toCamelCaseLC(column.getName());
      if (javaType == "java.lang.Integer") {
        out.tab(2).println("if (multiMap.contains(\"" + fieldName + "\")) {");
        out.tab(3).println("try {");
        out.tab(4).println(fieldName + " = Integer.parseInt(multiMap.get(\"" + fieldName + "\"));");
        out.tab(3).println("}");
        out.tab(3).println("catch (NumberFormatException e) {");
        out.tab(3).println("}");
        out.tab(2).println("}");

        /* Example output:
        if (multiMap.contains("usrId")) {
          try {
            usrId = Integer.parseInt(multiMap.get("usrId"));
          }
          catch (NumberFormatException e) {
          }
        }
        */
      }
      else if (javaType == "java.lang.String") {
        out.tab(2).println("if (multiMap.contains(\"" + fieldName + "\")) {");
        out.tab(3).println(fieldName + " = multiMap.get(\"" + fieldName + "\");");
        out.tab(2).println("}");
        /* Example output:
        if (multiMap.contains("userEmail")) {
          usrEmail = multiMap.get("usrEmail");
        }
        */
      }
    }
    out.tab(1).println("}");
  }
}
