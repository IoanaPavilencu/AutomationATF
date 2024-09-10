package requestObject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestUser extends GeneralObject{
    @JsonProperty("userName")
    private String userName;

    @JsonProperty("password")
    private String password;


    public RequestUser(String filePath) {
        super(filePath);
        password += "***lallaala...";
        userName += System.currentTimeMillis();
    }


}
