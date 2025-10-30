package likelion13th.shop.S3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor

public class S3Service {

    private final AmazonS3 amazonS3;
    private final S3Properties s3properties;

    public String uploadFile(MultipartFile file) {
        String bucketName = s3properties.getBucket();

        String fileName=java.util.UUID.randomUUID()+"_"+file.getOriginalFilename();

        try{
            ObjectMetadata metadata=new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3.putObject(bucketName,fileName,file.getInputStream(),metadata);

            return amazonS3.getUrl(bucketName,fileName).toString();
        }catch(Exception e){
            throw new RuntimeException("S3 파일 업로드 실패",e);
        }

    }
}
