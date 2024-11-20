package univ.yesummit.global.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 이미지를 S3에 업로드하고 URL을 반환
     * @param multipartFile
     * @param folder
     * @return
     */
    public String uploadImage(MultipartFile multipartFile, String folder) {
        String fileName = createFileName(multipartFile.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket + "/" + folder + "/image", fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return amazonS3.getUrl(bucket + "/" + folder + "/image", fileName).toString();
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 고유한 파일 이름을 생성
     * @param originalFileName
     * @return
     */
    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    /**
     * 파일 확장자를 검증
     * @param fileName
     * @return
     */
    private String getFileExtension(String fileName) {
        if (fileName.length() == 0) {
            throw new RuntimeException("파일 이름이 유효하지 않습니다.");
        }
        String extension = fileName.substring(fileName.lastIndexOf("."));
        List<String> validExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".JPG", ".JPEG", ".PNG", ".GIF");
        if (!validExtensions.contains(extension)) {
            throw new RuntimeException("지원하지 않는 파일 형식입니다.");
        }
        return extension;
    }

    /**
     * S3에서 이미지를 삭제합니다.
     * @param imageUrl
     */
    public void deleteImage(String imageUrl) {
        // 버킷의 url을 가져옴
        String bucketUrl = amazonS3.getUrl(bucket, "").toString();
        // 이미지 url에서 버킷 url을 제거하여 이미지 키를 얻음
        String imageKey = imageUrl.replace(bucketUrl, "");

        // 이미지 키가 '/'로 시작하면 제거
        if (imageKey.startsWith("/")) {
            imageKey = imageKey.substring(1);
        }

        // S3에서 객체를 삭제
        amazonS3.deleteObject(bucket, imageKey);
    }
}
