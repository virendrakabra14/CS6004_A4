import java.util.*;

class Node5 {
	Node5 f;
	static Node5 g;
	void foo() {
		this.f = null;
	}
	void test_this() {
		if(this == null) {
			g = null;
		}
	}
}

public class Test5 {
	public static void main(String[] args) {
		Node5 x = new Node5();
		Node5 y = null;
		Node5[] arr = new Node5[3];

		final int total_test_functions = 7;
		final int total_iterations = 1000000;
		int[] fn_call_numbers = new int[total_iterations];
		Random rand = new Random(2024);
		for(int i = 0; i < total_iterations; i++) {
			fn_call_numbers[i] = rand.nextInt(total_test_functions);
		}

		final long startTime = System.nanoTime();
		for(int i = 0; i < total_iterations; i++) {
			switch (fn_call_numbers[i]) {
				case 0:
					test_new();
					break;
				case 1:
					test_this(x);
					break;
				case 2:
					test_fieldref(x);
					break;
				case 3:
					test_invokeexpr(x);
					break;
				case 4:
					test_nullbranch(x);
					break;
				case 5:
					test_copy(x, y);
					break;
				case 6:
					test_arrayref(arr);
					break;
				default:
					break;
			}
		}
		final long endTime = System.nanoTime();
		System.out.println("Elapsed Time: " + (endTime - startTime)/1e6);
	}

	static void test_new() {
		Node5 x = new Node5();
		// x known to be nonnull
		if(x == null) {
			x = new Node5();
		}

		// also works for string constant, array expr, etc.
	}

	static void test_this(Node5 x) {
		x.test_this();
	}

	static void test_fieldref(Node5 x) {
		x.f = null;
		// x known to be nonnull
		if(x == null) {
			x = new Node5();
		}
	}

	static void test_invokeexpr(Node5 x) {
		x.foo();
		// x known to be nonnull
		if(x == null) {
			x = new Node5();
		}
	}

	static void test_nullbranch(Node5 x) {
		if(x == null) {
			// x known to be null
			if(x != null) {
				x = null;
			}
			if(x == null) {
				x = new Node5();
			}
		}
		else {
			// x known to be nonnull
			if(x != null) {
				x = null;
			}
			if(x == null) {
				x = new Node5();
			}
		}
	}

	static void test_copy(Node5 x, Node5 y) {
		x = new Node5();
		// x known to be nonnull
		y = x;
		// y known to be nonnull
		if(y == null) {
			y = new Node5();
		}
	}

	static void test_arrayref(Node5[] x) {
		x[0] = null;
		// x known to be nonnull
		if(x == null) {
			x = new Node5[10];
		}
	}
}

/*
pa4$ bash run.sh t . Test5 Node5
Elapsed Time: 39.821656
Elapsed Time: 38.664183
pa4$ bash run.sh t . Test5 Node5
Elapsed Time: 40.107508
Elapsed Time: 38.218334
pa4$ bash run.sh t . Test5 Node5
Elapsed Time: 40.191722
Elapsed Time: 38.666965
pa4$ bash run.sh t . Test5 Node5
Elapsed Time: 40.065901
Elapsed Time: 38.19287
pa4$ bash run.sh t . Test5 Node5
Elapsed Time: 40.998617
Elapsed Time: 38.446659

Averages
40.2370808
38.4378022

Percent improvement
(40.2370808-38.4378022)/40.2370808 ~ 4.47%
*/
