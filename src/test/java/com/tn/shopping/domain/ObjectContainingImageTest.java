package com.tn.shopping.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tn.shopping.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ObjectContainingImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ObjectContainingImage.class);
        ObjectContainingImage objectContainingImage1 = new ObjectContainingImage();
        objectContainingImage1.setId(1L);
        ObjectContainingImage objectContainingImage2 = new ObjectContainingImage();
        objectContainingImage2.setId(objectContainingImage1.getId());
        assertThat(objectContainingImage1).isEqualTo(objectContainingImage2);
        objectContainingImage2.setId(2L);
        assertThat(objectContainingImage1).isNotEqualTo(objectContainingImage2);
        objectContainingImage1.setId(null);
        assertThat(objectContainingImage1).isNotEqualTo(objectContainingImage2);
    }
}
