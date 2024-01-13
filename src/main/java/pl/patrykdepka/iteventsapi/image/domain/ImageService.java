package pl.patrykdepka.iteventsapi.image.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.patrykdepka.iteventsapi.image.domain.exception.DefaultImageNotFoundException;
import pl.patrykdepka.iteventsapi.image.domain.exception.ImageNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {
    public static final String DEFAULT_PROFILE_IMAGE_NAME = "default_profile_image.png";
    public static final String DEFAULT_EVENT_IMAGE_NAME = "default_profile_image.png";
    private final ImageRepository imageRepository;

    public Image createDefaultImage(String filename, ImageType type) {
        ClassPathResource resource = new ClassPathResource("static/images/" + filename);

        try (InputStream is = resource.getInputStream()) {
            Image image = new Image();
            image.setType(type);
            image.setFileName(resource.getFilename());
            image.setContentType("image/png");
            image.setFileData(is.readAllBytes());
            return imageRepository.save(image);
        } catch (IOException e) {
            throw new DefaultImageNotFoundException("Default file image " + resource.getPath() + " not found");
        }
    }

    public void updateImage(Long id, MultipartFile file) {
        Optional<Image> userProfileImageOpt = imageRepository.findById(id);

        try {
            if (userProfileImageOpt.isPresent()) {
                Image image = userProfileImageOpt.get();
                image.setFileName(file.getOriginalFilename());
                image.setContentType(file.getContentType());
                image.setFileData(file.getBytes());
            }
        } catch (IOException e) {
            throw new ImageNotFoundException("File not found");
        }
    }
}
