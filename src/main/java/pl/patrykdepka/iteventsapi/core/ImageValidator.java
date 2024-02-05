package pl.patrykdepka.iteventsapi.core;

import pl.patrykdepka.iteventsapi.image.domain.dto.ImageDTO;

import javax.imageio.ImageIO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class ImageValidator implements ConstraintValidator<Image, ImageDTO> {
    private static final String[] allowedFileTypes = new String[] { "image/jpeg", "image/jpg", "image/png" };
    private int width;
    private int height;

    @Override
    public void initialize(Image constraintAnnotation) {
        this.width = constraintAnnotation.width();
        this.height = constraintAnnotation.height();
    }

    @Override
    public boolean isValid(ImageDTO imageData, ConstraintValidatorContext context) {
        List<String> errorMessages = new ArrayList<>();

        if (imageData != null) {
            String contentType = imageData.getContentType();
            boolean isContentTypeValid = Arrays.asList(allowedFileTypes).contains(contentType);

            if (!isContentTypeValid) {
                errorMessages.add("{validation.annotation.Image.invalidFileType.message}");
            } else {
                try {
                    byte[] bytes = Base64.getDecoder().decode(imageData.getContent());
                    BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));

                    if (image.getHeight() > height || image.getWidth() > width) {
                        errorMessages.add("{validation.annotation.Image.invalidImageSize.message}");
                    }

                    if (bytes.length > 2097152) {
                        errorMessages.add("{validation.annotation.Image.invalidFileSize.message}");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (!errorMessages.isEmpty()) {
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
