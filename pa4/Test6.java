import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.*;

public class Test6 {
    private static Random random = new Random();

    public static void main(String[] args) {
        
        Foo foo = new Foo();
        final long startTime = System.nanoTime();
        for (int i=0;i<10;i++)
            foo.doSomething();
        final long endTime = System.nanoTime();
        System.out.println("Elapsed Time: " + (endTime - startTime)/1e6);
    }

    private static class Foo {
        private List<Bar> Bars;

        public Foo() {
            Bars = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                if (Bars != null)
                Bars.add(new Bar());
            }
        }

        public void doSomething() {
            for (Bar bar : Bars) {
                if (bar != null)
                bar.doSomethingBar();
            }
        }

        private class Bar {
            private String value;

            public Bar() {
                if (random.nextBoolean()) {
                    value = generateRandomString();
                }
            }

            public void doSomethingBar() {
                Zoo zoo = new Zoo();
                zoo.doSomethingZoo();
                if (value == null) {
                    System.out.println("Null!");
                }
                else{
                    if (value != null){
                        zoo.doSomethingZoo();
                    }
                    else{
                        zoo = null;
                    }
                }


            }

            private class Zoo {
                private List<String> values;

                public Zoo() {
                    values = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        if (values != null){
                            values.add(generateRandomString());
                        }
                    }
                }

                public void doSomethingZoo() {
                    for (String str : values) {
                        if (str != null) {
                            System.out.println(str.toLowerCase());
                        }
                    }
                }
            }
        }
    }

    private static String generateRandomString() {
        int length = random.nextInt(10) + 5;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = (char) (random.nextInt(26) + 'a');
            sb.append(c);
        }
        return sb.toString();
    }
}