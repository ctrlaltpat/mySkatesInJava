package com.ctrlaltpat.MySkates.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RollerSkatesPairTest {

    @Test
    void defaultConstructor_ShouldCreateEmptyObject() {
        RollerSkatesPair pair = new RollerSkatesPair();

        assertThat(pair.getId()).isNull();
        assertThat(pair.getName()).isNull();
        assertThat(pair.getBrand()).isNull();
        assertThat(pair.getPlates()).isNull();
        assertThat(pair.getWheels()).isNull();
    }

    @Test
    void parameterizedConstructor_ShouldSetAllFields() {
        String name = "OG 172";
        String brand = "Riedell";
        String plates = "Chaya Ophira";
        String wheels = "RollerBones Teams (white)";

        RollerSkatesPair pair = new RollerSkatesPair(name, brand, plates, wheels);

        assertThat(pair.getName()).isEqualTo(name);
        assertThat(pair.getBrand()).isEqualTo(brand);
        assertThat(pair.getPlates()).isEqualTo(plates);
        assertThat(pair.getWheels()).isEqualTo(wheels);
        assertThat(pair.getId()).isNull();
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        RollerSkatesPair pair = new RollerSkatesPair();

        pair.setId(1L);
        pair.setName("Test Name");
        pair.setBrand("Test Brand");
        pair.setPlates("Test Plates");
        pair.setWheels("Test Wheels");

        assertThat(pair.getId()).isEqualTo(1L);
        assertThat(pair.getName()).isEqualTo("Test Name");
        assertThat(pair.getBrand()).isEqualTo("Test Brand");
        assertThat(pair.getPlates()).isEqualTo("Test Plates");
        assertThat(pair.getWheels()).isEqualTo("Test Wheels");
    }

    @Test
    void equals_ShouldReturnTrue_WhenObjectsAreIdentical() {
        RollerSkatesPair pair = new RollerSkatesPair("OG 172", "Riedell", "Chaya Ophira", "RollerBones Teams (white)");

        assertThat(pair.equals(pair)).isTrue();
    }

    @Test
    void equals_ShouldReturnTrue_WhenObjectsHaveSameValues() {
        RollerSkatesPair pair1 = new RollerSkatesPair("OG 172", "Riedell", "Chaya Ophira", "RollerBones Teams (white)");
        pair1.setId(1L);

        RollerSkatesPair pair2 = new RollerSkatesPair("OG 172", "Riedell", "Chaya Ophira", "RollerBones Teams (white)");
        pair2.setId(1L);

        assertThat(pair1).isEqualTo(pair2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenObjectsHaveDifferentValues() {
        RollerSkatesPair pair1 = new RollerSkatesPair("OG 172", "Riedell", "Chaya Ophira", "RollerBones Teams (white)");
        pair1.setId(1L);

        RollerSkatesPair pair2 = new RollerSkatesPair("3200", "Riedell", "Arius", "Varsity Plus (black)");
        pair2.setId(2L);

        assertThat(pair1).isNotEqualTo(pair2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenComparedToNull() {
        RollerSkatesPair pair = new RollerSkatesPair("OG 172", "Riedell", "Chaya Ophira", "RollerBones Teams (white)");

        assertThat(pair.equals(null)).isFalse();
    }

    @Test
    void equals_ShouldReturnFalse_WhenComparedToDifferentClass() {
        RollerSkatesPair pair = new RollerSkatesPair("OG 172", "Riedell", "Chaya Ophira", "RollerBones Teams (white)");
        String notAPair = "Not a RollerSkatesPair";

        assertThat(pair.equals(notAPair)).isFalse();
    }

    @Test
    void equals_ShouldHandleNullFields() {
        RollerSkatesPair pair1 = new RollerSkatesPair();
        RollerSkatesPair pair2 = new RollerSkatesPair();

        assertThat(pair1).isEqualTo(pair2);

        pair1.setName("Test");
        assertThat(pair1).isNotEqualTo(pair2);

        pair2.setName("Test");
        assertThat(pair1).isEqualTo(pair2);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        RollerSkatesPair pair = new RollerSkatesPair("OG 172", "Riedell", "Chaya Ophira", "RollerBones Teams (white)");
        pair.setId(1L);

        int firstHashCode = pair.hashCode();
        int secondHashCode = pair.hashCode();

        assertThat(firstHashCode).isEqualTo(secondHashCode);
    }

    @Test
    void hashCode_ShouldBeEqualForEqualObjects() {
        RollerSkatesPair pair1 = new RollerSkatesPair("OG 172", "Riedell", "Chaya Ophira", "RollerBones Teams (white)");
        pair1.setId(1L);

        RollerSkatesPair pair2 = new RollerSkatesPair("OG 172", "Riedell", "Chaya Ophira", "RollerBones Teams (white)");
        pair2.setId(1L);

        assertThat(pair1.hashCode()).isEqualTo(pair2.hashCode());
    }

    @Test
    void hashCode_ShouldHandleNullFields() {
        RollerSkatesPair pair1 = new RollerSkatesPair();
        RollerSkatesPair pair2 = new RollerSkatesPair();

        assertThat(pair1.hashCode()).isEqualTo(pair2.hashCode());
    }

    @Test
    void settersWithNullValues_ShouldAcceptNull() {
        RollerSkatesPair pair = new RollerSkatesPair("Test", "Test", "Test", "Test");

        pair.setId(null);
        pair.setName(null);
        pair.setBrand(null);
        pair.setPlates(null);
        pair.setWheels(null);

        assertThat(pair.getId()).isNull();
        assertThat(pair.getName()).isNull();
        assertThat(pair.getBrand()).isNull();
        assertThat(pair.getPlates()).isNull();
        assertThat(pair.getWheels()).isNull();
    }
}