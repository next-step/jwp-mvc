package util;

import core.util.ReflectionUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReflectionUtilsTest {

    @Test
    void newInstance() {
        Car speedCar = ReflectionUtils.newInstance(Car.class, "jun", 80);
        Car specialCar = ReflectionUtils.newInstance(Car.class, "jun", "special");
        Car superCar = ReflectionUtils.newInstance(Car.class, "super");

        assertThat(speedCar.runNameSpeed()).isEqualTo("jun 80");
        assertThat(specialCar.runNameGear()).isEqualTo("jun special");
        assertThat(superCar.runPower()).isEqualTo("super");
    }

    @Test
    void convertStringValue() {
        assertThat(ReflectionUtils.convertStringValue("word", String.class)).isEqualTo("word");
        assertThat(ReflectionUtils.convertStringValue("12", Integer.class)).isEqualTo(12);
        assertThat(ReflectionUtils.convertStringValue("5000", Long.class)).isEqualTo(5000L);
        assertThat(ReflectionUtils.convertStringValue("1.1", Float.class)).isEqualTo(1.1F);
        assertThat(ReflectionUtils.convertStringValue("5.5", Double.class)).isEqualTo(5.5D);
    }

    @Test
    void hasFieldMethod() {
        assertTrue(ReflectionUtils.hasFieldMethod(Car.class, "setName", String.class));
        assertFalse(ReflectionUtils.hasFieldMethod(Car.class, "setName1", String.class));
        assertFalse(ReflectionUtils.hasFieldMethod(Car.class, "setName", Integer.class));
    }

    public static class Car {

        String name;

        Integer speed;

        String gear;

        String power;

        public Car(String name, Integer speed) {
            this.name = name;
            this.speed = speed;
        }

        public Car(String name, String gear) {
            this.name = name;
            this.gear = gear;
        }

        public Car(String power) {
            this.power = power;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setSpeed(Integer speed) {
            this.speed = speed;
        }

        public String runNameSpeed() {
            return name + " " + speed;
        }

        public String runNameGear() {
            return name + " " + gear;
        }

        public String runPower() {
            return power;
        }
    }

}
