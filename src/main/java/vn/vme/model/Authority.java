package vn.vme.model;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * @m-tech
 */
public class Authority implements GrantedAuthority {

    Long id;

    String name;

    public Authority(String name) {
    	 this.name = name;
	}

	public Authority() {
	}

	@Override
    public String getAuthority() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public String getName() {
        return name;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
