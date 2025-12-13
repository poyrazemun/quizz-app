package com.poyraz.steps;

import com.poyraz.util.CucumberUtil;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GetQuestionSteps {
    @LocalServerPort
    private int port;
    private String urlEndpoint;
    private HttpMethod httpMethod;
    private Response response;

    @Given("the REST API service {string}")
    public void the_rest_api_service(String endpoint) {
        urlEndpoint = endpoint;
    }

    @Given("the HTTP method is {word}")
    public void the_http_method_is(String method) {
        httpMethod = HttpMethod.valueOf(method);
    }

    @When("make the REST API call without body")
    public void make_the_rest_api_call_without_body() {
        response = CucumberUtil.executeApiCall(port, urlEndpoint, httpMethod, null);
    }

    @Then("the REST API response has status as {int}")
    public void the_rest_api_response_has_status_as(Integer expectedStatusCode) {
        assertNotNull(response, "Response is null");
        assertEquals(expectedStatusCode, response.getStatusCode(), "Mismatch in status code of response");
    }

    @Then("the REST API response contains errorMessage as {string}")
    public void the_rest_api_response_contains_error_message_as(String errorMessage) {
        assertNotNull(response, "Response is null");
        JsonPath jsonPath = response.jsonPath();
        String actualError = jsonPath.getString("message");
        assertEquals(errorMessage, actualError, "Mismatch in error message");
    }

    @Then("the REST API response contains question data")
    public void the_rest_api_response_contains_question_data(DataTable dataTable) {
        assertNotNull(response, "Response is null");
        JsonPath jsonPath = response.jsonPath();
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            assertEquals(row.get("id"), jsonPath.getString("id"), "Mismatch in id of question.");
            assertEquals(row.get("question_text"), jsonPath.getString("questionText"), "Mismatch in questionText of question.");
            assertEquals(row.get("option1"), jsonPath.getString("option1"), "Mismatch in option1 of question.");
            assertEquals(row.get("option2"), jsonPath.getString("option2"), "Mismatch in option2 of question.");
            assertEquals(row.get("option3"), jsonPath.getString("option3"), "Mismatch in option3 of question.");
            assertEquals(row.get("option4"), jsonPath.getString("option4"), "Mismatch in option4 of question.");
            assertEquals(row.get("right_answer"), jsonPath.getString("rightAnswer"), "Mismatch in rightAnswer of question.");
            assertEquals(row.get("difficulty_level"), jsonPath.getString("difficultyLevel"), "Mismatch in difficultyLevel of question.");
            assertEquals(row.get("category"), jsonPath.getString("category"), "Mismatch in category of question.");
        }
    }


}
