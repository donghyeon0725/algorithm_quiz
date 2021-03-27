package BFS;

import java.util.*;
import java.util.stream.Collectors;

public class one {
    /**
     * 정수의 배열이 주어질 때 연속된 부분 구간의 순서를 뒤집는 것을 뒤집기 연산이라고 부릅시다.
     *
     * 예를 들어 배열 {3,4,1,2}에서 부분 구간 {4,1,2}}를 뒤집으면 {3,2,1,4}가 됩니다.
     *
     * 중복이 없는 정수 배열을 뒤집기 연산을 이용해서 정렬하려고 합니다.
     * 필요한 최소한의 연산수를 계산하는 프로그램을 작성하세요.
     *
     * 예를 들어 배열 {3,4,1,2}는 {3,4}와 {1,2}를 각각 뒤집고 전체를 뒤집으면
     * 세 번의 연산으로도 정렬할 수 있지만 {4,1,2}를 먼저 뒤집고 {3,2,1}을 뒤집으면 두 번의 연산만으로 정렬 할 수 있습니다.
     * */

    public static void main(String[] args) {
        int size = 8;
        int[] input1 ={1,2,3,4,8,7,5,6};
        int[] input2 ={1,2,3,4,8,7,6,5};
        int[] input3 ={3, 9999, 1, 2};
        int[] input4 ={1000, 2000, 3000};
        System.out.println(searchPath(input1));
        System.out.println(searchPath(input2));
        System.out.println(searchPath(input3));
        System.out.println(searchPath(input4));


    }



    static int[] solve(int[] arr) {
        // perm 을 [0, ...,n-1] 의 순열로 변환한다.
        int size = arr.length;
        int[] result = new int[size];

        // 배열 전체 반복
        for (int i=0; i<size; i++) {
            int smaller = 0;

            // 기준이 되는 값보다 작은 값이 많을 수록 상대값이 +1 된다.
            for (int j=0; j<size; j++) {
                if (arr[j] < arr[i])
                    ++smaller;
            }

            result[i] = smaller;
        }

        return result;
    }


    /**
     * 배열 자체를 정점으로 보고, 뒤집기 연산으로 도착할 수 있는 지점을 다른 정점으로 바라볼 때,
     * 최종으로 정렬된 정점까지의 거리를 구하면 되는 문제
     *
     * 이 때 핵심은 배열을 키 값으로 사용한다는 점이다.
     *
     * 단, 배열을 그대로 사용할 경우, 주소 복사가 일어나서 값은 같아도 같은 키값으로 인식하지 않는다.
     * 이때, Arrays 클래스에서 제공하는 deepHashCode 를 사용하면, 쉽게 같은 배열인지 구별할 수 있게 되는데,
     *
     * int 배열을 사용할 수는 없고 래퍼클래스인 Integer[] 를 이용해야한다.
     */
    static int searchPath(int[] input) {
        int[] sorted = input.clone();
        // Arrays.sort 는 기존의 배열을 정렬하기는 하나, 새로운 배열을 return 하는 것은 아니라는 사실을 주의한다.
        Arrays.sort(sorted);

        Map<Integer, Integer> discovered_distance = new HashMap<>();
        Queue<int[]> queue = new LinkedList<>();

        queue.add(input);
        discovered_distance.put(hash(input), 0);

        while (!queue.isEmpty()) {
            // 랜덤하게 정렬해야함
            int[] newArr = queue.poll();

            if (Arrays.equals(newArr, sorted)) {
                return discovered_distance.get(hash(newArr));
            }

            // 가능한 모든 부분을 뒤집어 본다.
            // 두께
            for (int i=0; i<newArr.length; i++) {
                // 시작 부분
                for (int t=i+2; t<=newArr.length; t++) {
                    int[] copy = newArr.clone();

                    // 뒤집은 copy 배열
                    reverse(copy, i, t);

                    // 새로운 베열이 발견 되지 않았으면
                    if (discovered_distance.get(hash(copy)) == null) {
                        queue.add(copy);
                        discovered_distance.put(hash(copy), discovered_distance.get(hash(newArr))+ 1 );
                    }
                }
            }
        }

        return -1;
    }

    static void toString(int[] arr) {
        System.out.print("{");
        for (int i=0; i<arr.length; i++) {
            System.out.print(arr[i]);
            if (i + 1 < arr.length) {
                System.out.print(",");
            }
        }
        System.out.println("}");
    }

    static int hash(int[] arr) {
        return Arrays.deepHashCode(Arrays.stream(arr).boxed().toArray(Integer[]::new));
    }

    static void reverse(int[] arr, int s, int e) {
        int[] copy = Arrays.copyOfRange(arr, s, e);

        int len = copy.length;
        for (int i=0; i<len; i++) {
            arr[s+i] = copy[len - (i+1)];
        }
    }

}
