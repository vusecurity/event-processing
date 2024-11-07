package com.vusecurity.eventprocessing.controllers;

import com.vusecurity.EventProcessingApplicationTests;
import com.vusecurity.eventprocessing.viewmodel.request.rules.CepRuleRequest;
import com.vusecurity.eventprocessing.viewmodel.request.rules.EventStatement;
import com.vusecurity.eventprocessing.viewmodel.response.CepRuleResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class RuleEventProcessingControllerTests extends EventProcessingApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetRuleById() {
        CepRuleRequest cepRuleRequest = new CepRuleRequest();
        cepRuleRequest.setName("Test-rule");
        cepRuleRequest.setEnabled(true);
        cepRuleRequest.setIdAction(2);
        cepRuleRequest.setIdCriticalLevel(1);

        String jsonFormat = "{\"group\":null,\"groupEvent\":{\"templateName\":\"group-template-event-statement\",\"statementValue\":null,\"contextValue\":null,\"children\":[{\"templateName\":\"event-statement-template\",\"statementValue\":\"context SegmentedByUserId select sum(amount) from Transaction.win:time(4 hour) having sum(amount) >= 100000\",\"contextValue\":null,\"children\":[]},{\"templateName\":\"context-statement-template\",\"statementValue\":null,\"contextValue\":\"create context SegmentedByUserId partition by userId from Transaction\",\"children\":[]}]},\"name\":\"Test-rule\",\"score\":0,\"idChannel\":0,\"idOperationType\":0,\"idRuleType\":5,\"idAction\":2,\"action\":null,\"ruleType\":null,\"id\":0,\"order\":0,\"idProbability\":0,\"idImpact\":0,\"probability\":null,\"impact\":null,\"idCriticalLevel\":1,\"enabled\":true,\"idBusinessGroup\":0,\"idUser\":null,\"businessName\":null,\"userName\":null}";
        cepRuleRequest.setJsonFormat(jsonFormat);

        EventStatement eventStatement1 = new EventStatement();
        eventStatement1.setId(0);
        eventStatement1.setStatement("context SegmentedByUserId select sum(amount) from Transaction.win:time(4 hour) having sum(amount) >= 100000");
        eventStatement1.setContext(false);

        EventStatement eventStatement2 = new EventStatement();
        eventStatement2.setId(0);
        eventStatement2.setStatement("create context SegmentedByUserId partition by userId from Transaction");
        eventStatement2.setContext(true);

        cepRuleRequest.setEventStatements(Arrays.asList(eventStatement1, eventStatement2));

        ResponseEntity<String> saveRes = restTemplate.postForEntity("/cep/rules", cepRuleRequest, String.class);

        assertEquals(HttpStatus.CREATED, saveRes.getStatusCode());

        ResponseEntity<CepRuleResponse> response = restTemplate.getForEntity("/cep/rules/1", CepRuleResponse.class);

        assertAll("CepRuleResponse",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody(), "Response body should not be null"),
                () -> {
                    CepRuleResponse body = response.getBody();
                    if (body != null) {
                        assertAll("CepRuleResponse body",
                                () -> assertEquals(1, body.getId()),
                                () -> assertEquals("Test-rule", body.getName()),
                                () -> assertTrue(body.isEnabled(), "Rule should be enabled"),
                                () -> assertEquals(2, body.getIdAction()),
                                () -> assertEquals(1, body.getIdCriticalLevel())
                        );
                    } else {
                        fail("Response body is null");
                    }
                }
        );
    }
}