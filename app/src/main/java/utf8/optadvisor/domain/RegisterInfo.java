package utf8.optadvisor.domain;

public class RegisterInfo {
    private String year=null;
    private String month=null;
    private String userName=null;
    private String telephone=null;
    private String birthday=null;
    private String name=null;
    private String gender=null;
    private String day=null;

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setMonth(String month) {
        this.month = month;
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

    public void setYear(String year) {
        this.year = year;
    }

    public void setDay(String day) {
        this.day = day;
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

    public String getDay() {
        return day;
    }

    public String getName() {
        return name;
    }

    public String getMonth() {
        return month;
    }

    public String getUserName() {
        return userName;
    }

    public String getYear() {
        return year;
    }
    public boolean isInfoOk(){
        return true;
    }
}
