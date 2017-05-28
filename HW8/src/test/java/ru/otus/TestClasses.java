package ru.otus;

import java.util.*;

class TestClasses {

    class ClassWithPrimitives {
        private int intField = 1;
        private String stringField = "abc";
        private double doubleField = 3;
        private boolean boolField = false;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ClassWithPrimitives that = (ClassWithPrimitives) o;

            if (intField != that.intField) return false;
            if (doubleField != that.doubleField) return false;
            if (boolField != that.boolField) return false;
            return stringField != null ? stringField.equals(that.stringField) : that.stringField == null;
        }

        @Override
        public String toString() {
            return "ClassWithPrimitives{" +
                    "intField=" + intField +
                    ", stringField='" + stringField + '\'' +
                    ", doubleField=" + doubleField +
                    ", boolField=" + boolField +
                    '}';
        }
    }

    class ClassWithPrimitiveArray {
        private int intField = 2;
        private double[] arrayDouble = {1.0, 2.0, 3.5, 2.1};

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ClassWithPrimitiveArray that = (ClassWithPrimitiveArray) o;

            if (intField != that.intField) return false;
            return Arrays.equals(arrayDouble, that.arrayDouble);
        }

        @Override
        public String toString() {
            return "ClassWithPrimitiveArray{" +
                    "intField=" + intField +
                    ", arrayDouble=" + Arrays.toString(arrayDouble) +
                    '}';
        }
    }

    class ClassWithListOfPrimitives {
        private int intField = 3;
        private List<Integer> integerList = new ArrayList<>();

        public ClassWithListOfPrimitives() {
            integerList.add(1);
            integerList.add(2);
            integerList.add(7);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ClassWithListOfPrimitives that = (ClassWithListOfPrimitives) o;

            if (intField != that.intField) return false;
            return integerList != null ? integerList.equals(that.integerList) : that.integerList == null;
        }

        @Override
        public String toString() {
            return "ClassWithListOfPrimitives{" +
                    "intField=" + intField +
                    ", integerList=" + integerList +
                    '}';
        }
    }

    class ClassWithSetOfPrimitives {
        private int intField = 4;
        private Set<Integer> integerSet = new HashSet<>();

        public ClassWithSetOfPrimitives() {
            integerSet.add(1);
            integerSet.add(2);
            integerSet.add(7);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ClassWithSetOfPrimitives that = (ClassWithSetOfPrimitives) o;

            if (intField != that.intField) return false;
            return integerSet != null ? integerSet.equals(that.integerSet) : that.integerSet == null;
        }

        @Override
        public String toString() {
            return "ClassWithSetOfPrimitives{" +
                    "intField=" + intField +
                    ", integerSet=" + integerSet +
                    '}';
        }
    }

    class ClassWithObject {
        private int intField = 5;
        private ClassWithPrimitiveArray objectWithPrimitiveArray;

        public ClassWithObject() {
            this.objectWithPrimitiveArray = new TestClasses().new ClassWithPrimitiveArray();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ClassWithObject that = (ClassWithObject) o;

            if (intField != that.intField) return false;
            return objectWithPrimitiveArray != null ? objectWithPrimitiveArray.equals(that.objectWithPrimitiveArray) : that.objectWithPrimitiveArray == null;
        }

        @Override
        public String toString() {
            return "ClassWithObject{" +
                    "intField=" + intField +
                    ", objectWithPrimitiveArray=" + objectWithPrimitiveArray +
                    '}';
        }
    }

    class ClassWithListOfObjects {
        private int intField = 6;
        private List<ClassWithPrimitiveArray> listWithObjects = new ArrayList<>();

        public ClassWithListOfObjects() {
            listWithObjects.add(new TestClasses().new ClassWithPrimitiveArray());
            listWithObjects.add(new TestClasses().new ClassWithPrimitiveArray());
            listWithObjects.add(new TestClasses().new ClassWithPrimitiveArray());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ClassWithListOfObjects that = (ClassWithListOfObjects) o;

            if (intField != that.intField) return false;
            return listWithObjects != null ? listWithObjects.equals(that.listWithObjects) : that.listWithObjects == null;
        }

        @Override
        public String toString() {
            return "ClassWithListOfObjects{" +
                    "intField=" + intField +
                    ", listWithObjects=" + listWithObjects +
                    '}';
        }
    }
}























