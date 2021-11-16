package vn.vme.service;

import org.springframework.stereotype.Service;

import vn.vme.context.UserContextHolder;

@Service
public class BaseService {

	public boolean isAdmin() {
		if (UserContextHolder.getContext().getCurrentUser() != null) {
			return UserContextHolder.getContext().getCurrentUser().isAdmin();
		} else {
			return false;
		}
	}
}
