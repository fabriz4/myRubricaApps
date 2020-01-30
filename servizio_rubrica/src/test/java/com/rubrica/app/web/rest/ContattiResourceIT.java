package com.rubrica.app.web.rest;

import com.rubrica.app.ServiziorubricaApp;
import com.rubrica.app.domain.Contatti;
import com.rubrica.app.repository.ContattiRepository;
import com.rubrica.app.web.rest.errors.ExceptionTranslator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.rubrica.app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ContattiResource} REST controller.
 */
@SpringBootTest(classes = ServiziorubricaApp.class)
public class ContattiResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    @Autowired
    private ContattiRepository contattiRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restContattiMockMvc;

    private Contatti contatti;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContattiResource contattiResource = new ContattiResource(contattiRepository);
        this.restContattiMockMvc = MockMvcBuilders.standaloneSetup(contattiResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contatti createEntity(EntityManager em) {
        Contatti contatti = new Contatti()
            .name(DEFAULT_NAME)
            .surname(DEFAULT_SURNAME)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL)
            .owner(DEFAULT_OWNER);
        return contatti;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contatti createUpdatedEntity(EntityManager em) {
        Contatti contatti = new Contatti()
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .owner(UPDATED_OWNER);
        return contatti;
    }

    @BeforeEach
    public void initTest() {
        contatti = createEntity(em);
    }

    @Test
    @Transactional
    public void createContatti() throws Exception {
        int databaseSizeBeforeCreate = contattiRepository.findAll().size();

        // Create the Contatti
        restContattiMockMvc.perform(post("/api/contattis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contatti)))
            .andExpect(status().isCreated());

        // Validate the Contatti in the database
        List<Contatti> contattiList = contattiRepository.findAll();
        assertThat(contattiList).hasSize(databaseSizeBeforeCreate + 1);
        Contatti testContatti = contattiList.get(contattiList.size() - 1);
        assertThat(testContatti.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContatti.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testContatti.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testContatti.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testContatti.getOwner()).isEqualTo(DEFAULT_OWNER);
    }

    @Test
    @Transactional
    public void createContattiWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contattiRepository.findAll().size();

        // Create the Contatti with an existing ID
        contatti.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContattiMockMvc.perform(post("/api/contattis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contatti)))
            .andExpect(status().isBadRequest());

        // Validate the Contatti in the database
        List<Contatti> contattiList = contattiRepository.findAll();
        assertThat(contattiList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllContattis() throws Exception {
        // Initialize the database
        contattiRepository.saveAndFlush(contatti);

        // Get all the contattiList
        restContattiMockMvc.perform(get("/api/contattis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contatti.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)));
    }

    @Test
    @Transactional
    public void getContatti() throws Exception {
        // Initialize the database
        contattiRepository.saveAndFlush(contatti);

        // Get the contatti
        restContattiMockMvc.perform(get("/api/contattis/{id}", contatti.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contatti.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER));
    }

    @Test
    @Transactional
    public void getNonExistingContatti() throws Exception {
        // Get the contatti
        restContattiMockMvc.perform(get("/api/contattis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContatti() throws Exception {
        // Initialize the database
        contattiRepository.saveAndFlush(contatti);

        int databaseSizeBeforeUpdate = contattiRepository.findAll().size();

        // Update the contatti
        Contatti updatedContatti = contattiRepository.findById(contatti.getId()).get();
        // Disconnect from session so that the updates on updatedContatti are not directly saved in db
        em.detach(updatedContatti);
        updatedContatti
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .owner(UPDATED_OWNER);

        restContattiMockMvc.perform(put("/api/contattis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedContatti)))
            .andExpect(status().isOk());

        // Validate the Contatti in the database
        List<Contatti> contattiList = contattiRepository.findAll();
        assertThat(contattiList).hasSize(databaseSizeBeforeUpdate);
        Contatti testContatti = contattiList.get(contattiList.size() - 1);
        assertThat(testContatti.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContatti.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testContatti.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testContatti.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testContatti.getOwner()).isEqualTo(UPDATED_OWNER);
    }

    @Test
    @Transactional
    public void updateNonExistingContatti() throws Exception {
        int databaseSizeBeforeUpdate = contattiRepository.findAll().size();

        // Create the Contatti

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContattiMockMvc.perform(put("/api/contattis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contatti)))
            .andExpect(status().isBadRequest());

        // Validate the Contatti in the database
        List<Contatti> contattiList = contattiRepository.findAll();
        assertThat(contattiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteContatti() throws Exception {
        // Initialize the database
        contattiRepository.saveAndFlush(contatti);

        int databaseSizeBeforeDelete = contattiRepository.findAll().size();

        // Delete the contatti
        restContattiMockMvc.perform(delete("/api/contattis/{id}", contatti.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contatti> contattiList = contattiRepository.findAll();
        assertThat(contattiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
