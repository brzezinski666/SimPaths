package simpaths.data.filters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import simpaths.model.Person;
import simpaths.model.enums.Les_c4;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test filtering by employment history")
class EmploymentHistoryFilterTest {

    private static Person createTestPerson(
            Les_c4 les_c4_lag1,
            Les_c4 les_c4
    ) {
        Person testPerson = new Person(true);
        testPerson.setLes_c4_lag1(les_c4_lag1);
        testPerson.setLes_c4(les_c4);

        return testPerson;
    }

    private static class PersonTestData {
        final Les_c4 les_c4_lag1;
        final Les_c4 les_c4;

        PersonTestData(Les_c4 les_c4_lag1, Les_c4 les_c4) {
            this.les_c4_lag1 = les_c4_lag1;
            this.les_c4 = les_c4;
        }
    }

    private static final List<PersonTestData> TEST_SCENARIOS = Arrays.asList(
            // Eight with employed history
            new PersonTestData(Les_c4.EmployedOrSelfEmployed, Les_c4.NotEmployed),
            new PersonTestData(Les_c4.EmployedOrSelfEmployed, Les_c4.NotEmployed),
            new PersonTestData(Les_c4.EmployedOrSelfEmployed, Les_c4.EmployedOrSelfEmployed),
            new PersonTestData(Les_c4.EmployedOrSelfEmployed, Les_c4.EmployedOrSelfEmployed),
            new PersonTestData(Les_c4.EmployedOrSelfEmployed, Les_c4.EmployedOrSelfEmployed),
            new PersonTestData(Les_c4.EmployedOrSelfEmployed, Les_c4.EmployedOrSelfEmployed),
            new PersonTestData(Les_c4.EmployedOrSelfEmployed, Les_c4.Retired),
            new PersonTestData(Les_c4.EmployedOrSelfEmployed, Les_c4.Student),
            // Four with not employed history
            new PersonTestData(Les_c4.NotEmployed, Les_c4.EmployedOrSelfEmployed),
            new PersonTestData(Les_c4.NotEmployed, Les_c4.EmployedOrSelfEmployed),
            new PersonTestData(Les_c4.NotEmployed, Les_c4.Retired),
            new PersonTestData(Les_c4.NotEmployed, Les_c4.EmployedOrSelfEmployed),
            // Ignore all rest as should be filtered out
            new PersonTestData(Les_c4.Student, Les_c4.EmployedOrSelfEmployed),
            new PersonTestData(Les_c4.Retired, Les_c4.EmployedOrSelfEmployed),
            new PersonTestData(Les_c4.Retired, Les_c4.NotEmployed),
            new PersonTestData(Les_c4.Student, Les_c4.Retired)
    );

    @ParameterizedTest
    @DisplayName("Testing proportion entering employment")
    public void proportionEnteringEmployment(PersonTestData testData) {
        Person testPerson = createTestPerson(testData.les_c4_lag1, testData.les_c4);
    }

    private static Stream<Arguments> getTestScenarios() {
        return TEST_SCENARIOS.stream()
                .map(Arguments::of);
    }

}