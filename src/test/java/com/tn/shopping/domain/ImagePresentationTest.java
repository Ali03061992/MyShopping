package com.tn.shopping.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tn.shopping.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImagePresentationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImagePresentation.class);
        ImagePresentation imagePresentation1 = new ImagePresentation();
        imagePresentation1.setId(1L);
        ImagePresentation imagePresentation2 = new ImagePresentation();
        imagePresentation2.setId(imagePresentation1.getId());
        assertThat(imagePresentation1).isEqualTo(imagePresentation2);
        imagePresentation2.setId(2L);
        assertThat(imagePresentation1).isNotEqualTo(imagePresentation2);
        imagePresentation1.setId(null);
        assertThat(imagePresentation1).isNotEqualTo(imagePresentation2);
    }
}
