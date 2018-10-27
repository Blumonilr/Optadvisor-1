package utf8.optadvisor.domain;

import java.io.Serializable;

public class RegisterInfo implements Serializable{
    private String email=null;
    private String username =null;
    private String telephone=null;
    private String birthday=null;
    private String name=null;
    private String gender=null;
    private String password=null;
    private int w1=0;
    private int w2=0;


    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }


    public String getName() {
        return name;
    }


    public String getUsername() {
        return username;
    }

    public int getW1() {
        return w1;
    }

    public int getW2() {
        return w2;
    }
    public void setW1(int w1) {
        this.w1 = w1;
    }

    public void setW2(int w2) {
        this.w2 = w2;
    }
}
