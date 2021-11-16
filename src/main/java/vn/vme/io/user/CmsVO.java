package vn.vme.io.user;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CmsVO implements Serializable{
	protected Long userCount;	
	
	public CmsVO() {
	}

	public CmsVO(long userCount) {
		this.userCount = userCount;
	}
		
}
