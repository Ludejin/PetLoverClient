/*
 * The GPL License (GPL)
 *
 * Copyright (c) 2016 Moduth (https://github.com/moduth)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.moduth.petlover.domain.model;

import com.google.gson.annotations.SerializedName;

/**
 * 全局响应格式
 */
public class PlResponse {

    private static final Object NULL_DATA = new Object();

    @SerializedName("status_no")
    private int statusCode;

    @SerializedName("status_msg")
    private String statusMessage;

    @SerializedName("time")
    private long time;

    @SerializedName("elapsed")
    public double elapsed;

    public Object getData(){
        return NULL_DATA;
    }

    public boolean hasMore() {
        return true;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void setTime(long time) {
        this.time = time;
    }

    /**
     * 状态码  	详细描述
     * -1	    普通异常，详见 status_msg 字段描述
     * -1000	用户登录凭证不合法，请先登录或重新登录
     * -1010	请前往完善用户信息（昵称、头像）
     * -1020	第三方账号登陆后，请前往绑定官方账号（详见文档）
     * -9990	APPID 不合法
     * -9991	APPID 对应的应用信息不存在，请联系管理员
     * -9992	APPVER 不合法
     * -9999	应用传输数据解密失败，请联系管理员
     * -999	    未知的其他异常
     * -404	    指定目标不存在或已删除
     * -2001	请输入正确的图形验证码
     *
     * @return 响应代码
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * @return 响应文本
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * @return 当前时间戳
     */
    public long getTime() {
        return time;
    }
}
