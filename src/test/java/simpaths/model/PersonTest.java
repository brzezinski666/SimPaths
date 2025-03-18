package simpaths.model;

import org.junit.jupiter.api.*;
import simpaths.data.Parameters;
import simpaths.model.enums.Country;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Person methods in absence of a Model")
public class PersonTest {

    Person testPerson = new Person(true);

    @BeforeAll
    static void setupParams() {
        Parameters.loadParameters(Country.UK, 100, false, false, false, false, false, false, false, 2020, 2020, 2020, 1.,1., false, false);
    }

    @Nested
    @DisplayName("EQ5D calculations")
    @TestClassOrder(ClassOrderer.DisplayName.class)
    class Eq5dTests {

        @Nested
        @DisplayName("1. Lawrence and Fleishman coefficients")
        class LawrenceCoefficients {

            @Test
            public void calculateEQ5Dlow() {


                testPerson.setDhe_mcs(1.);
                testPerson.setDhe_pcs(1.);

                testPerson.qolEQ5D();

                assertEquals(-0.594, testPerson.getDeq5d());

            }

            @Test
            public void calculateEQ5Dhigh() {


                testPerson.setDhe_mcs(100.);
                testPerson.setDhe_pcs(100.);

                testPerson.qolEQ5D();

                assertEquals(1, testPerson.getDeq5d());

            }

        }


        @Nested
        @DisplayName("2. Franks coefficients")
        class FranksCoefficients {

            @BeforeAll
            public static void setupFranksCoefficients() {

                Parameters.eq5dConversionParameters = "franks";

                Parameters.loadParameters(Country.UK, 100, false, false, false, false, false, false, false, 2020, 2020, 2020, 1.,1., false, false);

            }

            @Test
            public void calculateEQ5Dlow(){

                testPerson.setDhe_mcs(1.);
                testPerson.setDhe_pcs(1.);

                testPerson.qolEQ5D();

                assertEquals(-0.594, testPerson.getDeq5d());

            }

            @Test
            public void calculateEQ5Dhigh(){

                testPerson.setDhe_mcs(100.);
                testPerson.setDhe_pcs(100.);

                testPerson.qolEQ5D();

                // The maximum possible value given by the Franks coefficients
                assertEquals(0.9035601, testPerson.getDeq5d());

            }

        }
    }


}
