import java.util.*;

public class Test6 {
    private static Random random = new Random(2024);

    public static void main(String[] args) {

        Foo foo = new Foo();
        final long startTime = System.nanoTime();
        for (int i = 0; i < 10; i++) {
            foo.doSomething();
        }
        final long endTime = System.nanoTime();
        System.out.println("Elapsed Time: " + (endTime - startTime)/1e6);
    }

    private static class Foo {
        private List<Bar> Bars;

        public Foo() {
            final int total = 10;
            Bar[] LocalBars = new Bar[total];
            int i = 0;
            while(i < total) {
                if (LocalBars != null) {
                    LocalBars[i] = new Bar();
                }
                if (LocalBars == null) {
                    LocalBars = new Bar[total];
                }
                i++;
            }
            Bars = Arrays.asList(LocalBars);
        }

        public void doSomething() {
            for (Bar bar : Bars) {
                if(bar == null) {
                    bar = new Bar();
                }
                if (bar != null) {
                    bar.doSomethingBar();
                }
            }
        }

        private class Bar {
            private String value;
            int counter = 0;

            public Bar() {
                if (random.nextBoolean()) {
                    value = "value" + counter;
                    counter++;
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
                    final int total = 20;
                    String[] LocalValues = new String[total];
                    int i = 0;
                    while(i < total) {
                        if (LocalValues != null) {
                            LocalValues[i] = "localStr";
                        }
                        if (LocalValues == null) {
                            LocalValues = new String[total];
                        }
                        i++;
                    }
                    values = Arrays.asList(LocalValues);
                }

                public void doSomethingZoo() {
                    for (String str : values) {
                        if (str == null) {
                            str = "str";
                        }
                        if (str != null) {
                            System.out.println(str.toLowerCase());
                        }
                    }
                }
            }
        }
    }
}

/*
pa4$ bash run.sh c . Test6 Test6\$Foo Test6\$Foo\$Bar Test6\$Foo\$Bar\$Zoo
11600
5890
*/
