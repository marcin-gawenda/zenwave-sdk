package io.zenwave360.generator.generators;

import io.zenwave360.generator.utils.JSONPath;
import io.zenwave360.generator.utils.Maps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JDLEntitiesToAvroConverter {

    public String idType = "string";
    public String namespace = "com.example.please.update";

    public JDLEntitiesToAvroConverter withIdType(String idType) {
        this.idType = idType;
        return this;
    }

    public JDLEntitiesToAvroConverter withNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    private static final List blobTypes = List.of("Blob", "AnyBlob", "ImageBlob");

    public Map<String, Object> convertToAvro(Map<String, Object> entityOrEnum) {
        boolean isEnum = entityOrEnum.get("values") != null;
        return isEnum? convertEnumToAvro(entityOrEnum) : convertEntityToAvro(entityOrEnum);
    }
    public Map<String, Object> convertEnumToAvro(Map<String, Object> enumValue) {
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "enum");
        schema.put("name", enumValue.get("name"));
        schema.put("namespace", namespace);
        if(enumValue.get("comment") != null) {
            schema.put("doc", enumValue.get("comment"));
        }
        List<String> values = JSONPath.get(enumValue, "$.values..name");
        schema.put("symbols", values);
        return schema;
    }

    public Map<String, Object> convertEntityToAvro(Map<String, Object> entity) {
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "record");
        schema.put("name", entity.get("name"));
        schema.put("namespace", namespace);
        if(entity.get("comment") != null) {
            schema.put("doc", entity.get("comment"));
        }
        List<Map<String, Object>> fields = new ArrayList<>();
        schema.put("fields", fields);

        if(!JSONPath.get(entity, "options.embedded", false)) {
            fields.add(Maps.of("name","id","type", idType));
        }

        List<Map<String, Object>> entityFields = (List) JSONPath.get(entity, "$.fields[*]");
        for (Map<String, Object> entityField : entityFields) {
            boolean isRequired = JSONPath.get(entityField, "$.validations.required.value") != null;
            Map<String, Object> field = new LinkedHashMap<>();
            field.put("name", entityField.get("name"));

            if("String".equals(entityField.get("type")) || "TextBlob".equals(entityField.get("type"))) {
                field.put("type", typeList("string", isRequired));
            }
            else if("Enum".equals(entityField.get("type"))) {
                field.put("type", typeList("string", isRequired));
            }
            else if("LocalDate".equals(entityField.get("type"))) {
                field.put("type", typeList("string", isRequired));
//                field.put("format", "date");
            }
            else if("ZonedDateTime".equals(entityField.get("type"))) {
                field.put("type", typeList("string", isRequired));
//                field.put("format", "date-time");
            }
            else if("Instant".equals(entityField.get("type"))) {
                field.put("type", typeList("string", isRequired));
//                field.put("format", "date-time");
            }
            else if("Duration".equals(entityField.get("type"))) {
                field.put("type", typeList("string", isRequired));
                //                property.put("format", "date-time");
            }
            else if("Integer".equals(entityField.get("type"))) {
                field.put("type", typeList("integer", isRequired));
//                field.put("format", "int32");
            }
            else if("Long".equals(entityField.get("type"))) {
                field.put("type", typeList("long", isRequired));
//                field.put("format", "int64");
            }
            else if("Float".equals(entityField.get("type"))) {
                field.put("type", typeList("float", isRequired));
//                field.put("format", "float");
            }
            else if("Double".equals(entityField.get("type")) || "BigDecimal".equals(entityField.get("type"))) {
                field.put("type", typeList("double", isRequired));
//                field.put("format", "double");
            }
            else if("Boolean".equals(entityField.get("type"))) {
                field.put("type", typeList("boolean", isRequired));
            }
            else if("UUID".equals(entityField.get("type"))) {
                field.put("type", typeList("string", isRequired));
//                field.put("pattern", "^[a-f\\d]{4}(?:[a-f\\d]{4}-){4}[a-f\\d]{12}$");
            }
            else if(blobTypes.contains(entityField.get("type"))) {
                field.put("type", typeList("bytes", isRequired));
//                field.put("format", "binary");
            } else {
                field.put("type", entityField.get("type")); // TODO consider embedding
            }

//            String minlength = JSONPath.get(entityField, "$.validations.minlength.value");
//            if(minlength != null) {
//                field.put("minLength", asNumber(minlength));
//            }
//            String maxlength = JSONPath.get(entityField, "$.validations.maxlength.value");
//            if(maxlength != null) {
//                field.put("maxLength", asNumber(maxlength));
//            }
//            String pattern = JSONPath.get(entityField, "$.validations.pattern.value");
//            if(pattern != null) {
//                field.put("pattern", pattern);
//            }
            if(entityField.get("comment") != null){
                field.put("doc", entityField.get("comment"));
            }

            if(entityField.get("isArray") == Boolean.TRUE) {
                field = Maps.of("type", "array", "items", field.get("type"), "java-class", "java.util.List");
            }

            fields.add(field);
        }

        return schema;
    }

    private Object typeList(String type, boolean isRequired) {
        return isRequired? type : Arrays.asList(null, type);
    }

    private static Object asNumber(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return number;
        }
    }
}
