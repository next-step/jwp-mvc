package util;

import core.util.ReflectionUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
