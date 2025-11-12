package com.example.communityserver.utils.common;


import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Map工具类
 * 提供Map相关的常用操作和转换方法
 * <p>
 * 使用{@link lombok.experimental.UtilityClass}注解，使该类成为工具类：
 * - 自动将类设置为final
 * - 自动将构造函数设置为private并抛出异常，防止实例化
 * - 所有方法必须是静态的
 *
 * @author: DongGuo
 * @create: 2025-11-12
 */
@UtilityClass
public class MapUtils {

    /**
     * 安全的获取Map值，避免空指针异常
     *
     * @param map Map对象
     * @param key 键
     * @param <K> 键类型
     * @param <V> 值类型
     * @return 值，如果Map为null或key不存在返回null
     */
    public static <K, V> V getSafe(Map<K, V> map, K key) {
        if (map == null || key == null) {
            return null;
        }
        return map.get(key);
    }

    /**
     * 安全的获取Map值，提供默认值
     *
     * @param map          Map对象
     * @param key          键
     * @param defaultValue 默认值
     * @param <K>          键类型
     * @param <V>          值类型
     * @return 值或默认值
     */
    public static <K, V> V getSafe(Map<K, V> map, K key, V defaultValue) {
        V value = getSafe(map, key);
        return value != null ? value : defaultValue;
    }

    /**
     * 检查Map是否为空
     *
     * @param map Map对象
     * @return 是否为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 检查Map是否不为空
     *
     * @param map Map对象
     * @return 是否不为空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 安全的Map合并，避免空指针
     *
     * @param map1 第一个Map
     * @param map2 第二个Map
     * @param <K>  键类型
     * @param <V>  值类型
     * @return 合并后的新Map
     */
    public static <K, V> Map<K, V> merge(Map<K, V> map1, Map<K, V> map2) {
        Map<K, V> result = new HashMap<>();
        if (isNotEmpty(map1)) {
            result.putAll(map1);
        }
        if (isNotEmpty(map2)) {
            result.putAll(map2);
        }
        return result;
    }

    /**
     * 将List转换为Map
     *
     * @param list      列表
     * @param keyMapper 键映射函数
     * @param <K>       键类型
     * @param <V>       值类型
     * @return Map
     */
    public static <K, V> Map<K, V> toMap(List<V> list, Function<V, K> keyMapper) {
        if (CollectionUtils.isEmpty(list)) {
            return new HashMap<>();
        }
        return list.stream().collect(Collectors.toMap(keyMapper, Function.identity()));
    }

    /**
     * 将List转换为Map，可处理重复键
     *
     * @param list          列表
     * @param keyMapper     键映射函数
     * @param mergeFunction 合并函数（处理重复键）
     * @param <K>           键类型
     * @param <V>           值类型
     * @return Map
     */
    public static <K, V> Map<K, V> toMap(List<V> list, Function<V, K> keyMapper,
                                         BinaryOperator<V> mergeFunction) {
        if (CollectionUtils.isEmpty(list)) {
            return new HashMap<>();
        }
        return list.stream().collect(Collectors.toMap(keyMapper, Function.identity(), mergeFunction));
    }

    /**
     * 过滤Map
     *
     * @param map    原始Map
     * @param filter 过滤条件
     * @param <K>    键类型
     * @param <V>    值类型
     * @return 过滤后的Map
     */
    public static <K, V> Map<K, V> filter(Map<K, V> map, java.util.function.Predicate<Map.Entry<K, V>> filter) {
        if (isEmpty(map)) {
            return new HashMap<>();
        }
        return map.entrySet().stream()
                .filter(filter)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * 转换Map的值
     *
     * @param map         原始Map
     * @param valueMapper 值转换函数
     * @param <K>         键类型
     * @param <V>         原始值类型
     * @param <R>         转换后值类型
     * @return 转换后的Map
     */
    public static <K, V, R> Map<K, R> transformValues(Map<K, V> map, Function<V, R> valueMapper) {
        if (isEmpty(map)) {
            return new HashMap<>();
        }
        return map.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> valueMapper.apply(entry.getValue())));
    }

    /**
     * 获取Map的键集合，安全版本
     *
     * @param map Map对象
     * @param <K> 键类型
     * @return 键集合
     */
    public static <K> Set<K> keySetSafe(Map<K, ?> map) {
        if (isEmpty(map)) {
            return new HashSet<>();
        }
        return map.keySet();
    }

    /**
     * 获取Map的值集合，安全版本
     *
     * @param map Map对象
     * @param <V> 值类型
     * @return 值集合
     */
    public static <V> Collection<V> valuesSafe(Map<?, V> map) {
        if (isEmpty(map)) {
            return new ArrayList<>();
        }
        return map.values();
    }

    /**
     * 创建不可变Map
     *
     * @param map 原始Map
     * @param <K> 键类型
     * @param <V> 值类型
     * @return 不可变Map
     */
    public static <K, V> Map<K, V> unmodifiableMap(Map<K, V> map) {
        if (isEmpty(map)) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(map);
    }

    /**
     * 按值排序Map
     *
     * @param map        原始Map
     * @param comparator 比较器
     * @param <K>        键类型
     * @param <V>        值类型
     * @return 排序后的LinkedHashMap
     */
    public static <K, V> Map<K, V> sortByValue(Map<K, V> map, Comparator<? super V> comparator) {
        if (isEmpty(map)) {
            return new LinkedHashMap<>();
        }
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(comparator))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    /**
     * 按键排序Map
     *
     * @param map        原始Map
     * @param comparator 比较器
     * @param <K>        键类型
     * @param <V>        值类型
     * @return 排序后的LinkedHashMap
     */
    public static <K, V> Map<K, V> sortByKey(Map<K, V> map, Comparator<? super K> comparator) {
        if (isEmpty(map)) {
            return new LinkedHashMap<>();
        }
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(comparator))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}
