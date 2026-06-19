package cards.api.fintech.e2e;

import cards.api.fintech.client.AccountClient;
import cards.api.fintech.client.AccountModel;
import cards.api.fintech.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CardControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CardRepository cardRepository;

    @MockitoBean
    private AccountClient accountClient;

    @BeforeEach
    void setUp() {
        cardRepository.deleteAll();

        var account = new AccountModel(
                1L,
                1L,
                "Juan",
                "1001-001-ARS",
                "ARS",
                new BigDecimal("50000.00"),
                BigDecimal.ZERO,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(accountClient.getAccountById(anyLong())).thenReturn(account);
    }

    @Test
    void shouldCreateCard() throws Exception {
        String request = """
                {
                  "accountId": 1,
                  "cardType": "CREDIT",
                  "cardBrand": "VISA",
                  "creditLimit": 500000.00,
                  "availableBalance": 350000.00,
                  "usedBalance": 150000.00,
                  "expirationDate": "2029-06-18",
                  "issueDate": "2026-06-18"
                }
                """;

        mockMvc.perform(post("/api/v1/cards")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.cardNumber").exists())
                .andExpect(jsonPath("$.cardType").value("CREDIT"))
                .andExpect(jsonPath("$.cardStatus").value("ACTIVE"))
                .andExpect(jsonPath("$.cardBrand").value("VISA"));
    }

    @Test
    void shouldGetAllCards() throws Exception {
        createCard();

        mockMvc.perform(get("/api/v1/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void shouldGetCardById() throws Exception {
        Long cardId = createCard();

        mockMvc.perform(get("/api/v1/cards/{id}", cardId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cardId))
                .andExpect(jsonPath("$.cardStatus").value("ACTIVE"));
    }

    @Test
    void shouldGetCardsByAccount() throws Exception {
        createCard();

        mockMvc.perform(get("/api/v1/cards/account/{accountId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].accountId").value(1));
    }

    @Test
    void shouldGetCardsByStatus() throws Exception {
        createCard();

        mockMvc.perform(get("/api/v1/cards/status/{status}", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].cardStatus").value("ACTIVE"));
    }

    @Test
    void shouldUpdateCardStatus() throws Exception {
        Long cardId = createCard();

        mockMvc.perform(patch("/api/v1/cards/{id}/status/{newStatus}", cardId, "BLOCKED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cardId))
                .andExpect(jsonPath("$.cardStatus").value("BLOCKED"));
    }

    @Test
    void shouldDeleteCard() throws Exception {
        Long cardId = createCard();

        mockMvc.perform(delete("/api/v1/cards/{id}", cardId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    private Long createCard() throws Exception {
        String request = """
                {
                  "accountId": 1,
                  "cardType": "CREDIT",
                  "cardBrand": "VISA",
                  "creditLimit": 500000.00,
                  "availableBalance": 350000.00,
                  "usedBalance": 150000.00,
                  "expirationDate": "2029-06-18",
                  "issueDate": "2026-06-18"
                }
                """;

        String response = mockMvc.perform(post("/api/v1/cards")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return Long.valueOf(response.replaceAll(".*\"id\":(\\d+).*", "$1"));
    }
}