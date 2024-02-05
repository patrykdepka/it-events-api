package pl.patrykdepka.iteventsapi.image.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.patrykdepka.iteventsapi.core.BaseIT;
import pl.patrykdepka.iteventsapi.creator.ImageCreator;
import pl.patrykdepka.iteventsapi.image.domain.exception.DefaultImageNotFoundException;
import pl.patrykdepka.iteventsapi.image.domain.exception.ImageNotFoundException;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static pl.patrykdepka.iteventsapi.image.domain.ImageService.DEFAULT_EVENT_IMAGE_NAME;
import static pl.patrykdepka.iteventsapi.image.domain.ImageService.DEFAULT_PROFILE_IMAGE_NAME;
import static pl.patrykdepka.iteventsapi.image.domain.ImageType.EVENT_IMAGE;
import static pl.patrykdepka.iteventsapi.image.domain.ImageType.PROFILE_IMAGE;

class ImageServiceIT extends BaseIT {

    @Autowired
    private ImageService imageService;

    @Test
    void shouldCreateAndReturnDefaultProfileImage() {
        // when
        var defaultProfileImage = imageService.createDefaultImage(DEFAULT_PROFILE_IMAGE_NAME, PROFILE_IMAGE);
        // then
        assertThat(defaultProfileImage).isNotNull();
        assertThat(defaultProfileImage.getId()).isNotNull();
        assertThat(defaultProfileImage.getType()).isEqualTo(PROFILE_IMAGE);
        assertThat(defaultProfileImage.getFileName()).isEqualTo(DEFAULT_PROFILE_IMAGE_NAME);
        assertThat(defaultProfileImage.getContentType()).isEqualTo("image/png");
        assertThat(defaultProfileImage.getFileData()).isNotEmpty();
    }

    @Test
    void shouldCreateAndReturnDefaultEventImage() {
        // when
        var defaultEventImage = imageService.createDefaultImage(DEFAULT_EVENT_IMAGE_NAME, EVENT_IMAGE);
        // then
        assertThat(defaultEventImage).isNotNull();
        assertThat(defaultEventImage.getId()).isNotNull();
        assertThat(defaultEventImage.getType()).isEqualTo(EVENT_IMAGE);
        assertThat(defaultEventImage.getFileName()).isEqualTo(DEFAULT_EVENT_IMAGE_NAME);
        assertThat(defaultEventImage.getContentType()).isEqualTo("image/png");
        assertThat(defaultEventImage.getFileData()).isNotEmpty();
    }

    @Test
    void shouldThrowExceptionWhenImageWithGivenNameDoesNotExist() {
        assertThatThrownBy(() -> imageService.createDefaultImage("profile_image.png", PROFILE_IMAGE))
                .isInstanceOf(DefaultImageNotFoundException.class);
    }

    @Test
    void shouldUpdateImage() {
        // given
        var defaultProfileImage = imageService.createDefaultImage(DEFAULT_PROFILE_IMAGE_NAME, PROFILE_IMAGE);
        var newImageData = ImageCreator.createCustomProfileImageData();
        // when
        imageService.updateImage(defaultProfileImage.getId(), newImageData);
        // then
        var updatedProfileImage = imageRepository.findById(defaultProfileImage.getId()).get();
        assertThat(updatedProfileImage.getId()).isEqualTo(defaultProfileImage.getId());
        assertThat(updatedProfileImage.getType()).isEqualTo(PROFILE_IMAGE);
        assertThat(updatedProfileImage.getFileName()).isEqualTo(newImageData.getFilename());
        assertThat(updatedProfileImage.getContentType()).isEqualTo(newImageData.getContentType());
        assertThat(Base64.getEncoder().encodeToString(updatedProfileImage.getFileData())).isEqualTo(newImageData.getContent());
    }

    @Test
    void shouldThrowExceptionWhenImageWithGivenIdDoesNotExist() {
        // given
        var newImageData = ImageCreator.createCustomProfileImageData();
        // then
        assertThatThrownBy(() -> imageService.updateImage(0L, newImageData))
                .isInstanceOf(ImageNotFoundException.class);
    }
}
