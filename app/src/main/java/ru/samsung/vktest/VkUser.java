package ru.samsung.vktest;

public class VkUser {
    @Override
    public String toString() {
        return "VkUser{" +
                "id='" + id + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", screen_name='" + screen_name + '\'' +
                ", sex='" + sex + '\'' +
                ", bdate='" + bdate + '\'' +
                ", photo_big='" + photo_big + '\'' +
                '}';
    }

    public String id;
    public String first_name;
    public String last_name;
    public String screen_name;
    public String sex;
    public String bdate;
    public String photo_big;
}
