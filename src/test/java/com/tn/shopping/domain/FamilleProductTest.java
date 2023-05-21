package com.tn.shopping.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tn.shopping.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FamilleProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FamilleProduct.class);
        FamilleProduct familleProduct1 = new FamilleProduct();
        familleProduct1.setId(1L);
        FamilleProduct familleProduct2 = new FamilleProduct();
        familleProduct2.setId(familleProduct1.getId());
        assertThat(familleProduct1).isEqualTo(familleProduct2);
        familleProduct2.setId(2L);
        assertThat(familleProduct1).isNotEqualTo(familleProduct2);
        familleProduct1.setId(null);
        assertThat(familleProduct1).isNotEqualTo(familleProduct2);
    }
}
