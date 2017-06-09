package next.service;

import static io.restassured.RestAssured.given;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class QnaEndToEndTest {
	int qustionId = 9;
	int answerId = 6;

	@Before
	public void setup() {
		RestAssured.port = 8080; // default가 8080
		// RestAssured 안에 보면 디폴트 값 다 나와 있음
	}

	@Test
	public void deleteQuestion() {
		// postQuestion();
		// postAnswer();
		given().auth().preemptive().basic("admin", "password")
				.contentType(ContentType.HTML).when().delete("/questions/9")
				.then().statusCode(HttpStatus.FOUND.value());
	}

	@Test
	public void deleteAnswer() {
		// postQuestion();
		// postAnswer();
		given().auth().preemptive().basic("admin", "password")
				.contentType(ContentType.HTML).when()
				.delete("/api/questions/" + qustionId + "/answers/" + answerId)
				.then().statusCode(HttpStatus.OK.value());
	}

	public void postQuestion() {
		given().auth().preemptive().basic("admin", "password")
				.queryParam("title", "blabla title")
				.queryParam("contents", "blabla contents")
				.contentType(ContentType.URLENC).when().post("/questions")
				.then().statusCode(HttpStatus.FOUND.value());
	}

	public void postAnswer() {
		given().auth().preemptive().basic("admin", "password")
				.queryParam("contents", "blabla contents")
				.contentType(ContentType.URLENC).when()
				.post("/api/questions/" + qustionId + "/answers").then()
				.statusCode(HttpStatus.OK.value());
	}
}
