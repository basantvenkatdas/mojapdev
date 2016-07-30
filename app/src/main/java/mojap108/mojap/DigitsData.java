package mojap108.mojap;

/**
 * Created by gollaba on 7/22/16.
 */
public class DigitsData {

    public String phoneNo = null;
    public String authId = null;

    public DigitsData() {

    }

    public DigitsData(String phoneNo, String authId) {
        this.phoneNo = phoneNo;
        this.authId = authId;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getAuthId() {
        return authId;
    }
}
