package vn.vme.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@Configuration
public class StorageService {

	private static final Logger log = LoggerFactory.getLogger(StorageService.class);

	@Value("${fileUploadPath}")
	private String fileUploadPath;

	@PostConstruct
	public void init() {
		try {
			Files.createDirectories(Paths.get(fileUploadPath));
		} catch (IOException e) {
			log.error("Could not initialize storage location" + e);
		}
	}

	public String store(MultipartFile file, String filename) throws Exception {
		if (file.isEmpty()) {
			throw new IOException("Failed to store empty file " + filename);
		}
		if (filename.contains("..")) {
			// This is a security check
			throw new IOException("Cannot store file " + filename);
		}
		try (InputStream inputStream = file.getInputStream()) {
			Files.copy(inputStream, Paths.get(fileUploadPath).resolve(filename), StandardCopyOption.REPLACE_EXISTING);
		}
		return filename;
	}
	
	public String store(MultipartFile file, String filename,String folder) throws Exception {
		if (file.isEmpty()) {
			throw new IOException("Failed to store empty file " + filename);
		}
		if (filename.contains("..")) {
			// This is a security check
			throw new IOException("Cannot store file " + filename);
		}
		File filePath = new File(fileUploadPath + folder + "/" + filename);
		boolean dirCreated = false;
		if(!filePath.exists()) {
			dirCreated = filePath.mkdirs();
		}else {
			dirCreated = true;
		}
		
		if(dirCreated) {
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, filePath.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		}
		return filename;
	}

	
	public Stream<Path> loadAll() {
		try {
			Path rootLocation = Paths.get(fileUploadPath);
			Stream<Path> pathStream = Files.walk(rootLocation, 1)
					.filter(path -> !path.equals(rootLocation))
					.map(rootLocation::relativize);

			return pathStream;
		} catch (IOException e) {
			log.error("Failed to read stored files", e);
			return null;
		}

	}

	public Path load(String filename) {
		Path rootLocation = Paths.get(fileUploadPath);
		return rootLocation.resolve(filename);
	}

	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new FileNotFoundException("Could not read file: " + filename);
			}
		} catch (Exception e) {
			log.error("Could not read file: " + filename, e);
			return null;
		}
	}

	public void deleteAll() {
		try {
			Path path = Paths.get(fileUploadPath);
			FileSystemUtils.deleteRecursively(path.toFile());
		} catch (Exception e) {
			log.error("Could not delete file: " + fileUploadPath , e);
		}
	}

	public void deleteFile(String fileName) {
		try {
			Path path = Paths.get(fileUploadPath + File.separator + fileName);
			FileSystemUtils.deleteRecursively(path.toFile());
		} catch (Exception e) {
			log.error("Could not delete file: " + fileName , e);
		}
	}

}
