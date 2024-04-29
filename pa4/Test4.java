class Node4 {
	Node4 f;
	static Node4 g;
	void foo() {
		this.f = null;
	}
	void test_this() {
		if(this == null) {
			g = null;
		}
	}
}

public class Test4 {
	public static void main(String[] args) {
		test_new();

		Node4 x = new Node4();
		test_this(x);
		test_fieldref(x);
		test_invokeexpr(x);
		test_nullbranch(x);

		Node4 y = null;
		test_copy(x, y);

		Node4[] arr = new Node4[3];
		test_arrayref(arr);
	}

	static void test_new() {
		Node4 x = new Node4();
		// x known to be nonnull
		if(x == null) {
			x = new Node4();
		}

		// also works for string constant, array expr, etc.
	}

	static void test_this(Node4 x) {
		x.test_this();
	}

	static void test_fieldref(Node4 x) {
		x.f = null;
		// x known to be nonnull
		if(x == null) {
			x = new Node4();
		}
	}

	static void test_invokeexpr(Node4 x) {
		x.foo();
		// x known to be nonnull
		if(x == null) {
			x = new Node4();
		}
	}

	static void test_nullbranch(Node4 x) {
		if(x == null) {
			// x known to be null
			if(x != null) {
				x = null;
			}
			if(x == null) {
				x = new Node4();
			}
		}
		else {
			// x known to be nonnull
			if(x != null) {
				x = null;
			}
			if(x == null) {
				x = new Node4();
			}
		}
	}

	static void test_copy(Node4 x, Node4 y) {
		x = new Node4();
		// x known to be nonnull
		y = x;
		// y known to be nonnull
		if(y == null) {
			y = new Node4();
		}
	}

	static void test_arrayref(Node4[] x) {
		x[0] = null;
		// x known to be nonnull
		if(x == null) {
			x = new Node4[10];
		}
	}
}
