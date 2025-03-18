package simpaths.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import simpaths.data.Parameters;
import simpaths.model.enums.Country;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing methods on a Person in absence of a Model")
public class PersonTest {

    Person testPerson = new Person(true);

    @BeforeAll
    static void setupParams() {
        Parameters.loadParameters(Country.UK, 100, false, false, false, false, false, false, false, 2020, 2020, 2020, 1.,1., false, false);
    }

    @Nested
    @DisplayName("Testing EQ5D calculations")
    class Eq5dTests {

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


}
