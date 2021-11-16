package vn.vme;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import vn.vme.repository.UserRepositoryTest;


/**
 * All test cases
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	
	// All Mappers
	UserRepositoryTest.class,
	
	})
public final class USER_TEST_REPOSITORY {}