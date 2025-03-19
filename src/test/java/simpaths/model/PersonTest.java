package simpaths.model;

import org.junit.jupiter.api.*;
import simpaths.data.Parameters;
import simpaths.model.enums.Country;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Person class")
public class PersonTest {

    static Person testPerson;

    @BeforeAll
    static void setup() {
        testPerson = new Person(true);
        Parameters.loadParameters(Country.UK, 100, false, false, false, false, false, false, false, 2020, 2020, 2020, 1.,1., false, false);
    }

    @Nested
    @DisplayName("EQ5D process")
    @TestClassOrder(ClassOrderer.DisplayName.class)
    class Eq5dTests {

        @Nested
        @DisplayName("1. With default parameters")
        class WithDefaultParameters {

            @Test
            @DisplayName("Calculates low score correctly using Lawrence and Fleishman coefficients")
            public void calculatesLowScoreCorrectly() {


                testPerson.setDhe_mcs(1.);
                testPerson.setDhe_pcs(1.);

                testPerson.onEvent(Person.Processes.HealthEQ5D);

                assertEquals(-0.594, testPerson.getHe_eq5d());

            }

            @Test
            @DisplayName("Calculates high score correctly using Lawrence and Fleishman coefficients")
            public void calculatesHighScoreCorrectly()  {


                testPerson.setDhe_mcs(100.);
                testPerson.setDhe_pcs(100.);

                testPerson.onEvent(Person.Processes.HealthEQ5D);

                assertEquals(1, testPerson.getHe_eq5d());

            }

        }


        @Nested
        @DisplayName("2. With eq5dConversionParameters set to 'franks'")
        class WithFranksParameters {

            @BeforeAll
            public static void setupFranksCoefficients() {

                Parameters.eq5dConversionParameters = "franks";

                Parameters.loadParameters(Country.UK, 100, false, false, false, false, false, false, false, 2020, 2020, 2020, 1.,1., false, false);

            }

            @Test
            @DisplayName("Calculates low score correctly using Franks coefficients")
            public void calculatesLowScoreCorrectly() {

                testPerson.setDhe_mcs(1.);
                testPerson.setDhe_pcs(1.);

                testPerson.onEvent(Person.Processes.HealthEQ5D);

                assertEquals(-0.594, testPerson.getHe_eq5d());

            }

            @Test
            @DisplayName("Calculates high score correctly using Franks coefficients")
            public void calculatesHighScoreCorrectly(){

                testPerson.setDhe_mcs(100.);
                testPerson.setDhe_pcs(100.);

                testPerson.onEvent(Person.Processes.HealthEQ5D);

                // The maximum possible value given by the Franks coefficients
                assertEquals(0.9035601, testPerson.getHe_eq5d());

            }

        }
    }


}
