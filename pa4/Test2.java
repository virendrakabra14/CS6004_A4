class Node2 {
	Node2 f;
	Node2 g;
}

public class Test2 {
	public static void main(String[] args) {
		foo(null);
	}
	static void foo(Node2 x) {
		x = new Node2();
		x.f = null;
		if(x.f != null) {
			System.out.println(x.f);
		}
	}
}
