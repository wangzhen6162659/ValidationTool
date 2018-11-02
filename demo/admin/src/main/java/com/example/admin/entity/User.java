package com.example.admin.entity;

import java.util.Date;

public class User {
    private Long id;

    private String account;

    private String password;

    /**
     * 姓名
     *
     * @mbggenerated
     */
    private String name;

    private String nickname;

    /**
     * 性别
     *
     * @mbggenerated
     */
    private String sex;

    /**
     * 生日
     *
     * @mbggenerated
     */
    private Date birthday;

    /**
     * 民族
     *
     * @mbggenerated
     */
    private String nation;

    /**
     * 照片
     *
     * @mbggenerated
     */
    private String photo;

    /**
     * 标签
     *
     * @mbggenerated
     */
    private String selfLaber;

    /**
     * 手机
     *
     * @mbggenerated
     */
    private String phone;

    /**
     * 心情
     *
     * @mbggenerated
     */
    private String autograph;

    /**
     * 最后登录
     *
     * @mbggenerated
     */
    private Date lastLogin;

    /**
     * 创建人id
     *
     * @mbggenerated
     */
    private Long createUser;

    /**
     * 创建时间
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * 更新人id
     *
     * @mbggenerated
     */
    private Long updateUser;

    /**
     * 更新时间
     *
     * @mbggenerated
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 姓名
     *
     * @mbggenerated
     */
    public String getName() {
        return name;
    }

    /**
     * 姓名
     *
     * @mbggenerated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    /**
     * 性别
     *
     * @mbggenerated
     */
    public String getSex() {
        return sex;
    }

    /**
     * 性别
     *
     * @mbggenerated
     */
    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    /**
     * 生日
     *
     * @mbggenerated
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * 生日
     *
     * @mbggenerated
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * 民族
     *
     * @mbggenerated
     */
    public String getNation() {
        return nation;
    }

    /**
     * 民族
     *
     * @mbggenerated
     */
    public void setNation(String nation) {
        this.nation = nation == null ? null : nation.trim();
    }

    /**
     * 照片
     *
     * @mbggenerated
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * 照片
     *
     * @mbggenerated
     */
    public void setPhoto(String photo) {
        this.photo = photo == null ? null : photo.trim();
    }

    /**
     * 标签
     *
     * @mbggenerated
     */
    public String getSelfLaber() {
        return selfLaber;
    }

    /**
     * 标签
     *
     * @mbggenerated
     */
    public void setSelfLaber(String selfLaber) {
        this.selfLaber = selfLaber == null ? null : selfLaber.trim();
    }

    /**
     * 手机
     *
     * @mbggenerated
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 手机
     *
     * @mbggenerated
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 心情
     *
     * @mbggenerated
     */
    public String getAutograph() {
        return autograph;
    }

    /**
     * 心情
     *
     * @mbggenerated
     */
    public void setAutograph(String autograph) {
        this.autograph = autograph == null ? null : autograph.trim();
    }

    /**
     * 最后登录
     *
     * @mbggenerated
     */
    public Date getLastLogin() {
        return lastLogin;
    }

    /**
     * 最后登录
     *
     * @mbggenerated
     */
    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * 创建人id
     *
     * @mbggenerated
     */
    public Long getCreateUser() {
        return createUser;
    }

    /**
     * 创建人id
     *
     * @mbggenerated
     */
    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    /**
     * 创建时间
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 更新人id
     *
     * @mbggenerated
     */
    public Long getUpdateUser() {
        return updateUser;
    }

    /**
     * 更新人id
     *
     * @mbggenerated
     */
    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * 更新时间
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 更新时间
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}