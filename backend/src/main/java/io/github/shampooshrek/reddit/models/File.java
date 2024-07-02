package io.github.shampooshrek.reddit.models;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class File {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String name;

  private String originalName;

  @Column(name = "file_size")
  private long fileSize;

  @Column(name = "file_url")
  private String fileUrl;

  @Column(name = "mime_type")
  private String mimeType;

  public File() {
  }

  public File(String name, String originalName, long fileSize, String fileUrl) {
    this.name = name;
    this.originalName = originalName;
    this.fileSize = fileSize;
    this.fileUrl = fileUrl;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOriginalName() {
    return originalName;
  }

  public void setOriginalName(String originalName) {
    this.originalName = originalName;
  }

  public long getFileSize() {
    return fileSize;
  }

  public void setImageSize(long fileSize) {
    this.fileSize = fileSize;
  }

  public String getFileUrl() {
    return fileUrl;
  }

  public void setFileUrl(String fileUrl) {
    this.fileUrl = fileUrl;
  }

}
