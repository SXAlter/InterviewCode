package com.example.helloworld.Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.TypeUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    public enum SFeature {

        /**
         * 无 SerializerFeature
         */
        EMPTY,
        /**
         * 只有 SerializerFeature.BrowserCompatible 避免乱码
         */
        BROWSER_COMPATIBLE_ONLY(SerializerFeature.BrowserCompatible),
        /**
         * 如果字段值为 null，保留字段名，值为 null
         */
        WRITE_NULL_VALUE(SerializerFeature.WriteMapNullValue),
        /**
         * 格式化输出
         */
        PRETTY_FORMAT(SerializerFeature.PrettyFormat),
        /**
         * 如果字段为空，用默认值代替
         *
         * @see SFeature#WRITE_NULL_VALUE
         * @deprecated null 和 "" 的含义是不一样，该序列化方式会改变默认含义，使用 WRITE_NULL_VALUE 代替
         */
        WRITE_NULL_AS_DEFAULT_VALUE_AND_BROWSER_COMPATIBLE(
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.BrowserCompatible);

        private final SerializerFeature[] features;

        SFeature(SerializerFeature... features) {
            this.features = features;
        }

        private SerializerFeature[] getFeatures() {
            return features;
        }
    }

    /**
     * Java 对象转换成 Json 字符串
     *
     * @param object   对象
     * @return json 字符串
     */
    public static String toJson(Object object) {
        return toJson(object, SFeature.EMPTY);
    }

    /**
     * Java 对象转换成 Json 字符串
     *
     * @param object   对象
     * @param features 序列化方式
     * @return json 字符串
     */
    public static String toJson(Object object, SFeature... features) {
        SerializerFeature[] serializerFeatures = SFeature.EMPTY.getFeatures();
        // 只传了一个参数
        if (null != features && features.length == 1) {
            serializerFeatures = features[0].getFeatures();
        }
        // 传了多个参数
        if (null != features && features.length > 1) {
            // 计算总个数
            int featureCount = 0;
            for (SFeature feature : features) {
                featureCount += feature.features.length;
            }
            // 创建总长度数组
            final SerializerFeature[] newFeatures = new SerializerFeature[featureCount];
            // copy 指针
            int point = 0;
            // copy feature 到 newFeatures
            for (SFeature feature : features) {
                System.arraycopy(feature.features, 0, newFeatures, point, feature.features.length);
                point += feature.features.length;
            }
            serializerFeatures = newFeatures;
        }
        return JSON.toJSONString(object, serializerFeatures);
    }

    /**
     * json 转换成 Java 对象
     *
     * @param json  json字符串
     * @param clazz Java 对象
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        return parseObject(json, (Type) clazz);
    }

    /**
     * json 转换成 Java 对象（可嵌套）
     *
     * @param json          json字符串
     * @param jsonReference new JsonReference< Map< String, Integer >>() {}; 等方式；new 一个 JsonReference
     *                      的匿名子类来记录泛型类型
     * @param <T>           泛型类型
     * @return Java 对象
     */
    public static <T> T parseObject(String json, TypeReference<T> jsonReference) {
        return parseObject(json, jsonReference.getType());
    }

    public static <T> T parseObject(String json, Type type) {
        try {
            return JSON.parseObject(json, type);
        } catch (JSONException ex) {
            throw new JSONException(json + " >> error parse to >> " + type.getTypeName(), ex);
        }
    }

    public static JSONObject parseObject(String json) {
        return JSON.parseObject(json);
    }

    /**
     * 一个Java对象转换成另一个Java对象
     *
     * @param obj   Java 对象
     * @param clazz 转换后的 Java 对象类型
     * @return 转换后的 Java 对象
     */
    public static <T> T caseObject(Object obj, Class<T> clazz) {
        return TypeUtils.cast(obj, clazz, ParserConfig.getGlobalInstance());
    }

    /**
     * 一个Java对象转换成另一个Java对象
     *
     * @param obj   Java 对象
     * @param type 转换后的 Java 对象类型
     * @return 转换后的 Java 对象
     */
    public static <T> T caseObject(Object obj, Type type) {
        return TypeUtils.cast(obj, type, ParserConfig.getGlobalInstance());
    }

    public static <T> T caseObject(Object obj, TypeReference<T> jsonReference) {
        return TypeUtils.cast(obj, jsonReference.getType(), ParserConfig.getGlobalInstance());
    }

    /**
     * 转换list
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> parseJsonList(String json, Class<T> clazz) {
        return (List<T>) JSON.parseArray(json, clazz);
    }

    /**
     * 转换arrayList
     * @param json
     * @param clazz
     * @return
     */
    public static <T> ArrayList<T> parseJsonArrayList(String json, Class<T> clazz) {
        return (ArrayList<T>) JSON.parseArray(json, clazz);
    }

}
