package simpaths.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {

    Person testPerson = new Person(true);

    @Test
    public void agePerson() {
        testPerson.setDag(25);
        testPerson.aging();
        assertEquals(26, testPerson.getDag());
    }

    @Test
    public void calculateEQ5D() {

        testPerson.setDhe_mcs(1.);
        testPerson.setDhe_pcs(1.);

        testPerson.qolEQ5D();

        assertEquals(1.0, testPerson.getDeq5d());

    }

}
