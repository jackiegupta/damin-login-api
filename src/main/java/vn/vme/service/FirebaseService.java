package vn.vme.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;

import vn.vme.common.JConstants;

@Component
public class FirebaseService {
	private static final Logger log = LoggerFactory.getLogger(CommentService.class);

    FirebaseDatabase db;

    public FirebaseService() throws IOException {
    	
    	log.info("Firebase init:");
    	log.info("FIREBASE_URL:" + JConstants.FIREBASE_URL);
    	
    	FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(
						new ClassPathResource(JConstants.FIREBASE_ACCOUNT).getInputStream()))
				.setDatabaseUrl(JConstants.FIREBASE_URL)
				.build();

    	
    	log.info("DatabaseUrl:" + options.getDatabaseUrl());
    	log.info("ProjectId:" + options.getProjectId());
    	log.info("ServiceAccountId:" + options.getServiceAccountId());
        if (FirebaseApp.getApps().isEmpty()) {
			FirebaseApp.initializeApp(options);
			log.info("Firebase application has been initialized");
		}

        db = FirebaseDatabase.getInstance(JConstants.FIREBASE_URL);
    }

    public FirebaseDatabase getDb() {
        return db;
    }

}
