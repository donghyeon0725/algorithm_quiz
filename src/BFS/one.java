package BFS;

import java.util.*;
import java.util.stream.Collectors;

public class one {
    /**
     * 정수의 배열이 주어질 때 연속된 부분 구간의 순서를 뒤집는 것을 뒤집기 연산이라고 부릅시다.
     *
     * 예를 들어 배열 {3,4,1,2}에서 부분 구간 {4,1,2}를 뒤집으면 {3,2,1,4}가 됩니다.
     *
     * 중복이 없는 정수 배열을 뒤집기 연산을 이용해서 정렬하려고 합니다.
     * 필요한 최소한의 연산수를 계산하는 프로그램을 작성하세요.
     *
     * 예를 들어 배열 {3,4,1,2}는 {3,4}와 {1,2}를 각각 뒤집고 전체를 뒤집으면
     * 세 번의 연산으로도 정렬할 수 있지만 {4,1,2}를 먼저 뒤집고 {3,2,1}을 뒤집으면 두 번의 연산만으로 정렬 할 수 있습니다.
     * */

    public static void main(String[] args) {
        int[] input1 ={1,2,3,4,8,7,5,6};
        int[] input2 ={1,2,3,4,8,7,6,5};
        int[] input3 ={3, 9999, 1, 2};
        int[] input4 ={1000, 2000, 3000};

        System.out.println(searchPath(input1)); // 2
        System.out.println(searchPath(input2)); // 1
        System.out.println(searchPath(input3)); // 2
        System.out.println(searchPath(input4)); // 0
    }

    /**
     * 배열 자체를 정점으로 보고,
     * 뒤집기 연산으로 만들 수 있는 배열을 다른 정점으로 바라볼 때,
     * 최종으로 정렬된 정점까지의 거리를 구하면 되는 문제
     *
     * 이 때 핵심은 배열을 키 값으로 사용한다는 점이다.
     *
     * 배열을 그대로 사용할 경우, 주소 복사가 일어나서 배열의 값이 같아도 다른 객체로 인식할 수 있기 때문에 주의한다.
     * 이때, Arrays 클래스에서 제공하는 deepHashCode 를 사용하면, 쉽게 같은 배열인지 구별할 수 있다.
     *
     * 단, int 배열을 사용할 수는 없고 래퍼클래스인 Integer[] 를 이용해야한다.
     */
    static int searchPath(int[] input) {
        // 배열을 복사해둔다. => 정렬할 배열을 별도로 만들어둔다. 이것은 비교 대상이 된다.
        int[] sorted = input.clone();
        // 기존의 배열을 정렬한다.
        Arrays.sort(sorted);

        // 배열의 해시코드를 key 값으로 사용하고, value는 해당 정점(뒤집은 배열)까지의 깊이를 의미한다.
        Map<Integer, Integer> discovered = new HashMap<>();
        // 방문 예정인 정점 (발견한 배열)을 저장하는 저장 공간
        // 너비 우선 탐색을 가능하게 해주는 자료형은 FIFO 구조를 가진 Queue를 사용해야한다.
        Queue<int[]> queue = new LinkedList<>();

        // 시작 시점을 집어 넣는다.
        queue.add(input);
        discovered.put(hash(input), 0);

        // 너비 우선 탐색 시작
        // 방문할 정점이 있으면
        while (!queue.isEmpty()) {
            // 방문할 정점(배열)을 꺼낸다.
            int[] here = queue.poll();
            // 발견한 배열의 해시코드 값
            int key_here = hash(here);

            // 정렬된 배열과 동일한 배열을 찾으면 반복을 멈춘다.
            if (Arrays.equals(here, sorted)) {
                return discovered.get(key_here);
            }

            // 가능한 모든 부분을 뒤집어 본다.
            for (int i=0; i<here.length; i++) {
                // 크기 2 이상부터 시작
                for (int t=i+2; t<=here.length; t++) {
                    // 배열을 뒤집기 전에 복사본을 만들어 놓음
                    int[] copy = here.clone();

                    // 뒤집은 copy 배열 => 새로 만든 배열
                    reverse(copy, i, t);
                    // 새로 발견한 배열의 키값
                    int key_there = hash(copy);

                    // 새로운 베열 copy가 기존에 없던 배열이면
                    if (!discovered.containsKey(key_there)) {
                        // 깊이
                        int deep = discovered.get(key_here);
                        // 발견한 배열
                        queue.add(copy);
                        // here 배열(copy 아님) 탐색 완료
                        discovered.put(key_there, deep + 1 );
                    }
                }
            }
        }

        return -1;
    }

    /**
     * 배열을 디버깅 하기 위해 사용합니다.
     * */
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

    /**
     * 배열의 해시코드 값을 반환합니다.
     * 같은 배열의 경우, 같은 해시 코드값을 반환합니다.
     * */
    static int hash(int[] arr) {
        return Arrays.deepHashCode(Arrays.stream(arr).boxed().toArray(Integer[]::new));
    }

    /**
     * 배열의 일부분 또는 전체를 뒤집는 방법
     *
     * reverse(arr, 0, 3) 은
     * 배열 arr의 0번 부터 2번(3번 앞) 까지의 배열을 뒤집습니다.
     * */
    static void reverse(int[] arr, int s, int e) {
        int[] copy = Arrays.copyOfRange(arr, s, e);

        int len = copy.length;
        for (int i=0; i<len; i++) {
            arr[s+i] = copy[len - (i+1)];
        }
    }

}
