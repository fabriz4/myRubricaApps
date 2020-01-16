package com.rubrica.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.rubrica.app.web.rest.TestUtil;

public class ContattiTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contatti.class);
        Contatti contatti1 = new Contatti();
        contatti1.setId(1L);
        Contatti contatti2 = new Contatti();
        contatti2.setId(contatti1.getId());
        assertThat(contatti1).isEqualTo(contatti2);
        contatti2.setId(2L);
        assertThat(contatti1).isNotEqualTo(contatti2);
        contatti1.setId(null);
        assertThat(contatti1).isNotEqualTo(contatti2);
    }
}
