package com.github.moduth.ext.utils.permission;

import java.util.Map;

/**
 * Created by Abner on 16/8/1.
 * Email nimengbo@gmail.com
 * GitHub https://github.com/nimengbo
 */
public interface CheckPermissionCallBack {


    void allowed();

    /**
     * 不允许的map
     * @param disallowedMap
     */
    void disallowed(Map<String,Boolean> disallowedMap);
}
