package simpaths.data.statistics;

import microsim.statistics.CrossSection;
import microsim.statistics.IDoubleSource;
import microsim.statistics.functions.MeanArrayFunction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import simpaths.data.filters.EmploymentHistoryFilter;
import simpaths.model.Person;
import simpaths.model.enums.Les_c4;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Calculating employment statistics")
class EmploymentStatisticsTest {

    private static List<Person> testPopulation;

    private static Person createTestPerson(
            int id,
            Les_c4 les_c4_lag1,
            Les_c4 les_c4
    ) {
        Person testPerson = new Person(true, id, 100);
        testPerson.setLes_c4_lag1(les_c4_lag1);
        testPerson.setLes_c4(les_c4);

        return testPerson;
    }

    @BeforeAll
    public static void setupTestPopulation() {

        testPopulation = Arrays.asList(
                // 25% move from employment into unemployment
                createTestPerson(1, Les_c4.EmployedOrSelfEmployed, Les_c4.NotEmployed),
                createTestPerson(2, Les_c4.EmployedOrSelfEmployed, Les_c4.Student),
                createTestPerson(3, Les_c4.EmployedOrSelfEmployed, Les_c4.EmployedOrSelfEmployed),
                createTestPerson(4, Les_c4.EmployedOrSelfEmployed, Les_c4.EmployedOrSelfEmployed),
                createTestPerson(5, Les_c4.EmployedOrSelfEmployed, Les_c4.NotEmployed),
                createTestPerson(6, Les_c4.EmployedOrSelfEmployed, Les_c4.EmployedOrSelfEmployed),
                createTestPerson(7, Les_c4.EmployedOrSelfEmployed, Les_c4.EmployedOrSelfEmployed),
                createTestPerson(8, Les_c4.EmployedOrSelfEmployed, Les_c4.Retired),
                // 50% from unemployment into employment
                createTestPerson(9, Les_c4.NotEmployed, Les_c4.EmployedOrSelfEmployed),
                createTestPerson(10, Les_c4.NotEmployed, Les_c4.EmployedOrSelfEmployed),
                createTestPerson(11, Les_c4.NotEmployed, Les_c4.NotEmployed),
                createTestPerson(12, Les_c4.NotEmployed, Les_c4.Retired),
                // Ignore all rest as should be filtered out
                createTestPerson(13, Les_c4.Student, Les_c4.Student),
                createTestPerson(14, Les_c4.Retired, Les_c4.EmployedOrSelfEmployed),
                createTestPerson(15, Les_c4.Retired, Les_c4.NotEmployed),
                createTestPerson(16, Les_c4.Student, Les_c4.EmployedOrSelfEmployed)
        );
    }

    @Test
    @DisplayName("Proportion becoming unemployed")
    public void proportionEmpToNotEmp() {


        // Entering unemployment prevalence
        EmploymentHistoryFilter employmentHistoryEmployed = new EmploymentHistoryFilter(Les_c4.EmployedOrSelfEmployed);
        CrossSection.Integer personsEmpToNotEmp = new CrossSection.Integer(testPopulation, Person.class, "getNonwork", true);
        personsEmpToNotEmp.setFilter(employmentHistoryEmployed);


        MeanArrayFunction isEmpToNotEmp = new MeanArrayFunction(personsEmpToNotEmp);
        isEmpToNotEmp.applyFunction();
        assertEquals(0.25, isEmpToNotEmp.getDoubleValue(IDoubleSource.Variables.Default));

    }

    @Test
    @DisplayName("Proportion becoming employed")
    public void proportionNotEmpToEmp() {

        // Entering employment prevalence
        EmploymentHistoryFilter employmentHistoryUnemployed = new EmploymentHistoryFilter(Les_c4.NotEmployed);
        CrossSection.Integer personsNotEmpToEmp = new CrossSection.Integer(testPopulation, Person.class, "getEmployed", true);
        personsNotEmpToEmp.setFilter(employmentHistoryUnemployed);

        MeanArrayFunction isNotEmpToEmp = new MeanArrayFunction(personsNotEmpToEmp);
        isNotEmpToEmp.applyFunction();
        assertEquals(0.5, isNotEmpToEmp.getDoubleValue(IDoubleSource.Variables.Default));


    }

}
