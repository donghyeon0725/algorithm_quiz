package BFS;

import java.util.*;
import java.util.stream.Collectors;

public class two {

    // 배열을 해시화 해서 키값으로 사용,
    static Map<Integer, Integer> toSort = new HashMap<>();

    /**
     * 미리 계산해두기
     *
     * 이 방법은, 정렬이 되어 있지 않은 배열을 뒤집어 가면서 정렬 된 배열을 찾아가는 과정이 아닌,
     *
     * 미리 정렬 된 배열을 뒤집어 계산해두고
     * 뒤집어진 배열을 키값으로 뒤집은 회수를 찾아오는 방법이다.
     *
     * ********************************* 핵심 *******************************
     * 그리고
     * {1,2,4,3}을 뒤집나 {10, 20, 40, 30}을 뒤집나 똑같은 회수로 뒤집어야 하기 때문에
     * 받은 배열을 미리, 상대크기(순열)로 채워진 배열 {0,1,2,3,4}를 생성한다. (크기가 5인 경우)
     *
     * 이 배열을 뒤집어 가면서, deep를 미리 계산 해놓는다. => 1000건의 케이스를 테스트 하는 실제 문제에서 static으로 결과를 미리 만들어 놓기 때문에 계산이 빠르다.
     * 그리고 배열을 키 값으로 deep를 찾아온다.
     * **************************************************************
     * */
    static void precalc(int n) {
        int[] perm = new int[n];
        for (int i=0; i<n; i++) {
            // 정렬된 배열 생성
            perm[i] = i;
        }

        Queue<int[]> queue = new LinkedList<>();

        // 큐에 탐색할 대상을 넣어둔다.
        queue.add(perm);
        // 탐색 깊이
        toSort.put(hash(perm), 0);

        // 탐색 시작
        while (!queue.isEmpty()) {
            int[] here = queue.poll();

            int cost = toSort.get(hash(here));

            /*for (int i=0; i<n; i++) {
                for (int j=i+2; j<=n; j++) {
                    reverse(here, i, j);

                    int there = hash(here);
                    if(!toSort.containsKey(there)) {
                        // 뒤집은 배열을 넣음
                        toSort.put(there, cost+1);
                        queue.add(here);
                    }
                    // 2번 뒤집으면, 새로운 배열을 만들 필요가 없음
                    reverse(here, i, j);
                }
            }*/

            for (int i=0; i<n; i++) {
                // 시작 부분
                for (int j=2+i; j<=n; j++) {

                    // 뒤집은 here 배열
                    reverse(here, i, j);

                    int there = hash(here);
                    // 새로운 베열이 발견 되지 않았으면
                    if (!toSort.containsKey(there)) {
                        // 이 때 복사본을 넣어야함
                        queue.add(here);
                        toSort.put(there, cost + 1 );
                    }

                    // 복사본을 사용할 것이 아니라면, 한번 뒤집은 결과는 다시 뒤집어 놓아야, 다음 연산에 제대로 된 결과가 나옴
                    reverse(here, i, j);
                }
            }

        }
    }

    static Integer solve(int[] perm) {
        // perm 을 [0, ...,n-1] 의 순열로 변환한다.
        int n = perm.length;
        int[] fixed = new int[n];

        // 배열 전체 반복
        for (int i=0; i<n; i++) {
            int smaller = 0;

            // 기준이 되는 값보다 작은 값이 많을 수록 상대값이 +1 된다.
            for (int j=0; j<n; j++) {
                if (perm[j] < perm[i])
                    ++smaller;
            }

            fixed[i] = smaller;
        }

        return toSort.get(hash(fixed));
    }

    static Integer hash(int[] arr) {
        return Arrays.deepHashCode(Arrays.stream(arr).boxed().toArray(Integer[]::new));
    }

    static void reverse(int[] arr, int s, int e) {
        int[] copy = Arrays.copyOfRange(arr, s, e);

        int len = copy.length;
        for (int i=0; i<len; i++) {
            arr[s+i] = copy[len - (i+1)];
        }
    }

    public static void main(String[] args) {
        int[] arr = {0,1,2,3,7,6,5,4};
        int[] arr1 = {1,2,3,4,8,7,6,5};


        int[] input1 ={1,2,3,4,8,7,5,6};
        //int[] input2 ={1,2,3,4,8,7,6,5};
        int[] input2 ={1,2,3,4,8,7,6,5};
        int[] input3 ={3, 9999, 1, 2};
        int[] input4 ={1000, 2000, 3000};

        precalc(input2.length);

        //int a1 = solve(input1);
        int a2 = solve(input2);

        //System.out.println(a1);
        System.out.println(a2);
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
}
