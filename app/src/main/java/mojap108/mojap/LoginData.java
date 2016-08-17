
package mojap108.mojap;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "__v",
    "authId",
    "phoneNo",
    "_id",
    "totalBeeds"
})
public class LoginData {

    @JsonProperty("__v")
    private Integer v;
    @JsonProperty("authId")
    private String authId;
    @JsonProperty("phoneNo")
    private String phoneNo;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("totalBeeds")
    private int totalBeeds;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();



    /**
     * 
     * @return
     *     The v
     */
    @JsonProperty("__v")
    public Integer getV() {
        return v;
    }

    /**
     * 
     * @param v
     *     The __v
     */
    @JsonProperty("__v")
    public void setV(Integer v) {
        this.v = v;
    }

    /**
     * 
     * @return
     *     The authId
     */
    @JsonProperty("authId")
    public String getAuthId() {
        return authId;
    }

    /**
     * 
     * @param authId
     *     The authId
     */
    @JsonProperty("authId")
    public void setAuthId(String authId) {
        this.authId = authId;
    }

    /**
     * 
     * @return
     *     The phoneNo
     */
    @JsonProperty("phoneNo")
    public String getPhoneNo() {
        return phoneNo;
    }

    /**
     * 
     * @param phoneNo
     *     The phoneNo
     */
    @JsonProperty("phoneNo")
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    /**
     *
     * @return
     *     The Total BeedCount
     */
    @JsonProperty("totalBeeds")
    public int getTotalBeads() {
        return totalBeeds;
    }

    /**
     *
     * @param beads
     *     The totalBeeds
     */
    @JsonProperty("totalBeeds")
    public void setTotalBeads(int beads) {
        this.totalBeeds = beads;
    }


    /**
     * 
     * @return
     *     The id
     */
    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The _id
     */
    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
