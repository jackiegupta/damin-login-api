package vn.vme.io.user;

import lombok.Data;

@Data
public class ListPackageRequest {
    private String username;
    private String pswd;
    private String request_id;
}
