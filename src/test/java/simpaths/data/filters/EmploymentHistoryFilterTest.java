package simpaths.data.filters;

import microsim.statistics.CrossSection;
import microsim.statistics.IDoubleSource;
import microsim.statistics.IIntSource;
import microsim.statistics.functions.MeanArrayFunction;
import microsim.statistics.functions.CountArrayFunction;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import simpaths.model.Person;
import simpaths.model.enums.Les_c4;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test filtering by employment history")
class EmploymentHistoryFilterTest {

    private static List<Person> testPopulation;

    private static Person createTestPerson(
            Les_c4 les_c4_lag1
    ) {
        Person testPerson = new Person(true);
        testPerson.setLes_c4_lag1(les_c4_lag1);

        return testPerson;
    }

    @BeforeAll
    public static void setupTestPopulation() {

        testPopulation = Arrays.asList(
                // Eight with employed history
                createTestPerson(Les_c4.EmployedOrSelfEmployed),
                createTestPerson(Les_c4.EmployedOrSelfEmployed),
                createTestPerson(Les_c4.EmployedOrSelfEmployed),
                createTestPerson(Les_c4.EmployedOrSelfEmployed),
                createTestPerson(Les_c4.EmployedOrSelfEmployed),
                createTestPerson(Les_c4.EmployedOrSelfEmployed),
                createTestPerson(Les_c4.EmployedOrSelfEmployed),
                createTestPerson(Les_c4.EmployedOrSelfEmployed),
                // Four with not employed history
                createTestPerson(Les_c4.NotEmployed),
                createTestPerson(Les_c4.NotEmployed),
                createTestPerson(Les_c4.NotEmployed),
                createTestPerson(Les_c4.NotEmployed),
                // Ignore all rest as should be filtered out
                createTestPerson(Les_c4.Student),
                createTestPerson(Les_c4.Retired),
                createTestPerson(Les_c4.Retired),
                createTestPerson(Les_c4.Student)
        );
    }

    @Test
    @DisplayName("Testing filtering previously employed")
    public void populationPreviouslyEmployed() {

        EmploymentHistoryFilter employmentHistoryEmployed = new EmploymentHistoryFilter(Les_c4.EmployedOrSelfEmployed);
        CrossSection.Integer personsPreviouslyEmployed = new CrossSection.Integer(testPopulation, Person.class, "getEmployed_Lag1", true);
        personsPreviouslyEmployed.setFilter(employmentHistoryEmployed);

        CountArrayFunction nPreviouslyEmployed = new CountArrayFunction(personsPreviouslyEmployed);
        nPreviouslyEmployed.applyFunction();

        assertEquals(8, nPreviouslyEmployed.getIntValue(IIntSource.Variables.Default));
    }

    @Test
    @DisplayName("Testing filtering previously unemployed")
    public void populationPreviouslyUnemployed() {

        EmploymentHistoryFilter employmentHistoryUnemployed = new EmploymentHistoryFilter(Les_c4.NotEmployed);
        CrossSection.Integer personsPreviouslyUnemployed = new CrossSection.Integer(testPopulation, Person.class, "getEmployed_Lag1", true);
        personsPreviouslyUnemployed.setFilter(employmentHistoryUnemployed);

        CountArrayFunction nPreviouslyUnemployed = new CountArrayFunction(personsPreviouslyUnemployed);
        nPreviouslyUnemployed.applyFunction();

        assertEquals(4, nPreviouslyUnemployed.getIntValue(IIntSource.Variables.Default));
    }

}