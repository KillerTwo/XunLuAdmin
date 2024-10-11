package org.wm.generator.domain;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 功能描述：<功能描述>
 * 数据传输实体
 *
 * @author dove
 * @date 2023/07/24 21:40
 * @since 1.0
 **/
@Slf4j
public class TransferDataMap extends HashMap<String, Object> {

    public String getString(String key) {
        var value = get(key);
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }

    public Integer getInteger(String key) {
        var value = get(key);
        if (value == null) {
            return null;
        }
        return Integer.parseInt(String.valueOf(value));
    }

    public Long getLong(String key) {
        var value = get(key);
        if (value == null) {
            return null;
        }
        return Long.parseLong(String.valueOf(value));
    }

    public Boolean getBoolean(String key) {
        var value = get(key);
        if (value == null) {
            return null;
        }
        return Boolean.getBoolean(String.valueOf(value));
    }

    public Double getDouble(String key) {
        var value = get(key);
        if (value == null) {
            return null;
        }
        return Double.valueOf(String.valueOf(value));
    }

    public Float getFloat(String key) {
        var value = get(key);
        if (value == null) {
            return null;
        }
        return Float.valueOf(String.valueOf(value));
    }

    public Short getShort(String key) {
        var value = get(key);
        if (value == null) {
            return null;
        }
        return Short.valueOf(String.valueOf(value));
    }

    public List<?> getList(String key) {
        var value = get(key);
        if (value == null) {
            return null;
        }

        return castList(value, Object.class);
    }

    public Set<?> getSet(String key) {
        var value = get(key);
        if (value == null) {
            return null;
        }
        return castSet(value, Object.class);
    }

    public Set<String> getSetString(String key) {
        var value = get(key);
        if (value == null) {
            return null;
        }
        return castSet(value, String.class);
    }

    public Set<Integer> getSetInteger(String key) {
        var value = get(key);
        if (value == null) {
            return null;
        }
        return castSet(value, Integer.class);
    }

    public Set<Long> getSetLong(String key) {
        var value = get(key);
        if (value == null) {
            return null;
        }
        return castSet(value, Long.class);
    }


    public Set<Double> getSetDouble(String key) {
        var value = get(key);
        if (value == null) {
            return null;
        }
        return castSet(value, Double.class);
    }

    public Set<Float> getSetFloat(String key) {
        var value = get(key);
        if (value == null) {
            return null;
        }
        return castSet(value, Float.class);
    }

    public String[] getStringArray(String key) {
        var value = get(key);
        if (value == null) {
            return null;
        }
        return (String[]) value;
    }

    public Long[] getLongArray(String key) {
        var value = get(key);
        if (value == null) {
            return null;
        }
        return (Long[]) value;
    }

    public Integer[] getIntegerArray(String key) {
        var value = get(key);
        if (value == null) {
            return null;
        }
        return (Integer[]) value;
    }


    public Double[] getDoubleArray(String key) {
        var value = get(key);
        if (value == null) {
            return null;
        }
        return (Double[]) value;
    }

    public Float[] getFloatArray(String key) {
        var value = get(key);
        if (value == null) {
            return null;
        }
        return (Float[]) value;
    }

    public Object getObject(String key) {
        return get(key);
    }

    public static TransferDataMap instance(Object object) {
        var data = new TransferDataMap();
        var fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                data.put(field.getName(), field.get(object));
            } catch (IllegalAccessException e) {
                log.error("数据转换出错： {}", field.getName(), e);
                e.printStackTrace();
            }
        }
        return data;
    }

    public static TransferDataMap instance(Map<String, Object> map) {
        var data = new TransferDataMap();
        data.putAll(map);
        return data;
    }

    private static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

    private static <T> Set<T> castSet(Object obj, Class<T> clazz) {
        Set<T> result = new HashSet<>();
        if (obj instanceof Set<?>) {
            for (Object o : (Set<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        } else if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }


    public static void main(String[] args) {

    }


}
