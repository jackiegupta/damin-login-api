package vn.vme.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.vme.repository.RoleRepository;


@Service
public class RoleService {	 
	
	@Autowired
	RoleRepository roleRepository;

	static Logger log = LoggerFactory.getLogger(RoleService.class.getName());

	public List<String> getRoleByUserId(Long userId) {
		List<String> roles = new ArrayList<String>();

		// for testing only
		roles.add("ADMIN");

		return roles;
	}
}
