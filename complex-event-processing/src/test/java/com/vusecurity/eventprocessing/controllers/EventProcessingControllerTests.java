package com.vusecurity.eventprocessing.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vusecurity.EventProcessingApplicationTests;
import com.vusecurity.eventprocessing.viewmodel.request.events.Location;
import com.vusecurity.eventprocessing.viewmodel.request.rules.CepRuleRequest;
import com.vusecurity.eventprocessing.viewmodel.request.rules.EventStatement;
import com.vusecurity.eventprocessing.viewmodel.request.events.AnalyzeTransaction;
import com.vusecurity.eventprocessing.viewmodel.response.AnalyzeResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class EventProcessingControllerTests extends EventProcessingApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @BeforeEach
    void setUp() throws Exception {
        // Set up the rule
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

        // Save the rule
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(cepRuleRequest), headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/cep/rules", request, String.class);

        assertAll("HTTP Response",
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(201, response.getStatusCode().value())
        );
    }

    @Test
    void testRuleTriggering() throws Exception {
        AnalyzeTransaction transaction = createSampleTransaction();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Perform 9 transactions that don't trigger the rule
        for (int i = 0; i < 9; i++) {
            HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(transaction), headers);
            ResponseEntity<AnalyzeResult> response = restTemplate.postForEntity("/cep/transaction", request, AnalyzeResult.class);

            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
            AnalyzeResult analyzeResult = response.getBody();
            assertNull(analyzeResult.getTriggeredRule(), "Rule should not trigger for transaction " + (i + 1));
        }

        // Perform the 10th transaction that should trigger the rule
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(transaction), headers);
        ResponseEntity<AnalyzeResult> response = restTemplate.postForEntity("/cep/transaction", request, AnalyzeResult.class);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        AnalyzeResult analyzeResult = response.getBody();
        assertNotNull(analyzeResult.getTriggeredRule(), "Rule should trigger for the 11th transaction");
        assertEquals(2, analyzeResult.getTriggeredRule().getActionId(), "Triggered rule action ID should be 2");
    }

    private AnalyzeTransaction createSampleTransaction() throws ParseException {
        AnalyzeTransaction transaction = new AnalyzeTransaction();
        transaction.setDeviceLocation(new Location(0, 0));
        transaction.setEventDate(dateFormat.parse("2021-03-18T10:51:49.758Z"));
        transaction.setIdChannel(8);
        transaction.setIdOperation(1);
        transaction.setUserId("user-test-1223");
        transaction.setAmount(10000);
        transaction.setDebitAccount("12312312312321");
        transaction.setCreditAccount("21312321312");

        Map<String, String> additionalParameters = new HashMap<>();
        additionalParameters.put("ATM", "1");
        transaction.setAditionalParameters(additionalParameters);

        return transaction;
    }
}