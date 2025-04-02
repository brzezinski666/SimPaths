package simpaths.data.filters;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import simpaths.model.Person;
import simpaths.model.enums.Les_c4;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test filtering by employment history")
class EmploymentHistoryFilterTest {

    private static Person createTestPerson(
            Les_c4 les_c4_lag1
    ) {
        Person testPerson = new Person(true);
        testPerson.setLes_c4_lag1(les_c4_lag1);

        return testPerson;
    }

    public static class PersonTestData {
        final Les_c4 les_c4_lag1;
        final boolean isFilteredEmployed;
        final boolean isFilteredUnemployed;

        PersonTestData(Les_c4 les_c4_lag1, boolean isFilteredEmployed, boolean isFilteredUnemployed) {
            this.les_c4_lag1 = les_c4_lag1;
            this.isFilteredEmployed = isFilteredEmployed;
            this.isFilteredUnemployed = isFilteredUnemployed;
        }
    }

    private static final List<PersonTestData> TEST_SCENARIOS = Arrays.asList(
                // Eight with employed history
                new PersonTestData(Les_c4.EmployedOrSelfEmployed, true, false),
                new PersonTestData(Les_c4.EmployedOrSelfEmployed, true, false),
                new PersonTestData(Les_c4.EmployedOrSelfEmployed, true, false),
                new PersonTestData(Les_c4.EmployedOrSelfEmployed, true, false),
                new PersonTestData(Les_c4.EmployedOrSelfEmployed, true, false),
                new PersonTestData(Les_c4.EmployedOrSelfEmployed, true, false),
                new PersonTestData(Les_c4.EmployedOrSelfEmployed, true, false),
                new PersonTestData(Les_c4.EmployedOrSelfEmployed, true, false),
                // Four with not employed history
                new PersonTestData(Les_c4.NotEmployed, false, true),
                new PersonTestData(Les_c4.NotEmployed, false, true),
                new PersonTestData(Les_c4.NotEmployed, false, true),
                new PersonTestData(Les_c4.NotEmployed, false, true),
                // Ignore all rest as should be filtered out
                new PersonTestData(Les_c4.Student, false, false),
                new PersonTestData(Les_c4.Retired, false, false),
                new PersonTestData(Les_c4.Retired, false, false),
                new PersonTestData(Les_c4.Student, false, false)
    );


    @ParameterizedTest
    @MethodSource("getPreviousEmploymentScenarios")
    @DisplayName("Testing filtering previously employed")
    public void populationPreviouslyEmployed(PersonTestData testData) {

        Person testPerson = createTestPerson(testData.les_c4_lag1);

        EmploymentHistoryFilter employmentHistoryUnemployed = new EmploymentHistoryFilter(Les_c4.EmployedOrSelfEmployed);

        assertEquals(testData.isFilteredEmployed, employmentHistoryUnemployed.isFiltered(testPerson));

    }

    @ParameterizedTest
    @MethodSource("getPreviousEmploymentScenarios")
    @DisplayName("Testing filtering previously unemployed")
    public void populationPreviouslyUnemployed(PersonTestData testData) {

        Person testPerson = createTestPerson(testData.les_c4_lag1);

        EmploymentHistoryFilter employmentHistoryUnemployed = new EmploymentHistoryFilter(Les_c4.NotEmployed);

        assertEquals(testData.isFilteredUnemployed, employmentHistoryUnemployed.isFiltered(testPerson));
    }

    private static Stream<Arguments> getPreviousEmploymentScenarios() {
        return TEST_SCENARIOS.stream()
                .map(Arguments::of);
    }

}