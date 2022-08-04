public class Sorting {
	static Sorting op = new Sorting();

	public static void main(String[] args) {
		try {
			op.bubbleSort();
			op.binarySearch();
			op.sequentialSearch();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void bubbleSort() {
		int[] input = { 44, 14, 22, 7 };
		int count = input.length;
		for (int i = 0; i < count - 1; i++) {
			for (int j = 0; j < count - 1 - 1; j++) {
				if (input[j] > input[j + 1]) {
					int temp = input[j];
					input[j] = input[j + 1];
					input[j + 1] = temp;
				}
			}
		}
		for (int i = 0; i < count; ++i)
			System.out.print(input[i] + " ");
		System.out.println();
	}

	public void binarySearch() {
		int[] input = { 44, 14, 22, 7 };
		boolean swap = true;
		int start = 0;

		while (swap) {
			swap = false;
			for (int i = start; i < input.length - 1; ++i) {
				if (input[i] > input[i + 1]) {
					int temp = input[i];
					input[i] = input[i + 1];
					input[i + 1] = temp;
					swap = true;
				}
			}
			if (!swap) {
				break;
			}
			swap = false;
			for (int i = input.length - 2; i >= start; i--) {
				if (input[i] > input[i + 1]) {
					int temp = input[i];
					input[i] = input[i + 1];
					input[i + 1] = temp;
					swap = true;
				}
			}
			start = start + 1;
		}
		for (int i = 0; i < input.length; i++)
			System.out.print(input[i] + " ");
		System.out.println();
	}

	public void sequentialSearch() {
		int[] input = { 44, 14, 22, 7 };
		int number = 22;
		for (int i = 0; i < input.length - 1; i++) {
			if (input[i] == number) {
				System.out.print("Number is present at index " + i);
			}
		}
		System.out.print("Number is not present in array");
	}
}
