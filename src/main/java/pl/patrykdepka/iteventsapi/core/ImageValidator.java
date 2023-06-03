package pl.patrykdepka.iteventsapi.core;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageValidator implements ConstraintValidator<Image, MultipartFile> {
    private int width;
    private int height;

    @Override
    public void initialize(Image constraintAnnotation) {
        this.width = constraintAnnotation.width();
        this.height = constraintAnnotation.height();
    }

    @Override
    public boolean isValid(MultipartFile image, ConstraintValidatorContext context) {
        List<String> errorMessages = new ArrayList<>();
        String message;

        if (image != null && !image.isEmpty()) {
            String[] allowedFileTypes = new String[] { "image/jpeg", "image/jpg", "image/png" };
            String fileType = image.getContentType();
            boolean isFileTypeValid = Arrays.stream(allowedFileTypes).anyMatch(fileType::equals);

            if (!isFileTypeValid) {
                message = "{form.field.image.error.invalidFileType.message}";
                errorMessages.add(message);
            }

            if (isFileTypeValid) {
                try {
                    ImageInputStream imageInputStream = ImageIO.createImageInputStream(image.getInputStream());
                    BufferedImage bufferedImage = ImageIO.read(imageInputStream);
                    if (bufferedImage.getWidth() > width || bufferedImage.getHeight() > height) {
                        message = "{form.field.image.error.invalidImageSize.message}";
                        errorMessages.add(message);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (image.getSize() > 2097152) {
                message = "{form.field.image.error.invalidFileSize.message}";
                errorMessages.add(message);
            }

            if (errorMessages.size() > 0) {
                context.disableDefaultConstraintViolation();
                for (String errorMessage : errorMessages) {
                    context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
                }

                return false;
            }
        }

        return true;
    }
}
