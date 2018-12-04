package com.android.newprojectdemo.utils.alipay;

/**
 * 用户信息
 */
public class UserInfo {

    private String id;//用户ID
    private String username;//账号
    private String nickName;//昵称
    private String sex;//性别（0:女，1:男，2:保密）
    private String avatar;//头像URl
    private String accessToken;//用户Token

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
