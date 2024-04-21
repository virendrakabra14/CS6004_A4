class Node3 {
	Node3 f;
	Node3 g;
}

public class Test3 {
	public static void main(String[] args) {
		Node3 x = new Node3();
		// x.f = new Node3(); // swapping happens with and without this
		if(x != null) {
			System.out.println("not null");
		}
	}
}
