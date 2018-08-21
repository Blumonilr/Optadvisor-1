package utf8.optadvisor.domain;

public class RegisterInfo {
    private String userName=null;
    private String telephone=null;
    private String birthday=null;
    private String name=null;
    private String gender=null;

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getUserName() {
        return userName;
    }
    public boolean isInfoOk(){
        return true;
    }
}
