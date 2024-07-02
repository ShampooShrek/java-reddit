package io.github.shampooshrek.reddit.services;

import io.github.shampooshrek.reddit.services.ImageService;
import io.github.shampooshrek.reddit.exceptions.InvalidValueException;
import io.github.shampooshrek.reddit.models.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {
  private final Path fileStorageLocation;

  @Autowired
  public ImageService(@Value("${file.upload-dir}") String uploadDir) {
    this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
    try {
      Files.createDirectories(this.fileStorageLocation);
    } catch (Exception e) {
    }
  }

  public File saveImage(MultipartFile file, List<String> mimetypes)
      throws InvalidValueException, RuntimeException, IOException {

    String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
    try {
      if (!mimetypes.contains(file.getContentType())) {
        throw new RuntimeException("Mimetype não permetido!");
      }

      if (originalFileName.contains("..")) {
        throw new RuntimeException("Nome de arquivo não permitido! " + originalFileName);
      }

      String fileName = formatterFileName(originalFileName);
      String fileUrl = "http://localhost:8080/uploads/" + fileName;
      File image = new File(fileName, originalFileName, file.getSize(), fileUrl);

      Path targetLocation = this.fileStorageLocation.resolve(fileName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

      return image;
    } catch (IOException e) {
      throw new RuntimeException();
    }
  }

  private String formatterFileName(String originalFileName) {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    String formmaterDate = now.format(formatter);

    String fileName = formmaterDate + "-" + originalFileName;
    return fileName;
  }
}
