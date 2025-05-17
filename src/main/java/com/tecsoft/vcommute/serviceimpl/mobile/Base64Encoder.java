package com.tecsoft.vcommute.serviceimpl.mobile;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;

public class Base64Encoder {
    @Value("${file.upload-dir}")
	private static String UPLOAD_DIR;

	public static String uploadDirectory = System.getProperty("user.dir") + "/uploads";

	public static String encodeImageToBase64(File imageFile) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(imageFile);
		byte[] bytes = new byte[(int) imageFile.length()];
		fileInputStream.read(bytes);
		fileInputStream.close();
		String encodedImage = Base64.getEncoder().encodeToString(bytes);
		return encodedImage;
	}

	public static void decodeBase64ToImage(String encodedImage, String imagePath) throws IOException {
		byte[] decodedBytes = Base64.getDecoder().decode(encodedImage);
		// File uploadDir = new File("c:/upload");
		File uploadDir = new File(uploadDirectory);
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}

		String uploadPath = uploadDir + "/" + imagePath;

		Path path = Paths.get(uploadPath);

		Files.write(path, decodedBytes);
	}
}
