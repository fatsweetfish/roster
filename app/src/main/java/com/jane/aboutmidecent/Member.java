package com.jane.aboutmidecent;

/**
 * Created by Jane on 2017/5/4.
 */

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

//import static com.jane.aboutmidecent.Member.creatSPFMembers;


public class Member {
    public int _id;
    private String name;
    private String email;
    private String desc;
    private String image;

    //构造函数
    public Member(String name, String image, String email, String desc) {
        this.name = name;
        this.image = image;
        this.email = email;
        this.desc = desc;
    }


    public static List<Member> getAllMembers(){
        List<Member> members = new ArrayList<Member>();
        //members.add(new Member("Jane",R.drawable.jane,"janezheng@mid-cent.com","Jane, famle, born on 1982.9.28, graduated from NanJing University of Information Science & Technology,Embedded Software Engineer, work in Mid-Cent "));
        //members.add(new Member("David",R.drawable.david,"davidzhu@sercomm.com","David, male, born on 1981.11.26, graduated from Anhui Jianzhu University, Software Engineer, work in Sercomm"));
        //members.add(new Member("Milo",R.drawable.milo,"milozhu@gmail.com","Milo, baby boy, born on 2014.2.17, will be go to kindergarten this September"));
        return members;
    }

    // 以下都是访问内部属性的getter和setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
