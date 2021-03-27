package BFS;

import java.util.*;
import java.util.stream.Collectors;

public class two {

    // 배열을 해시화 해서 키값으로 사용,
    static Map<Integer, Integer> discovered = new HashMap<>();

    /**
     * 미리 계산해두기
     *
     * 정렬이 되어 있지 않은 배열을 뒤집어 가면서 정렬 된 배열을 찾아가는 과정이 아니라
     * 미리 정렬 된 배열을 뒤집어 계산해두고
     * 받은 배열을 키로 변환해서 계산된 값에서 꺼내오는 방법이다.
     *
     * ********************************* 핵심 *******************************
     * {1,2,4,3}을 뒤집은 것은 {10, 20, 40, 30}을 뒤집은 것과 똑같은 회수로 뒤집어야 하기 때문에
     * 받은 배열을 상대크기(순열)로 채워진 배열 {1,2,4,3}를 사용해도 된다.
     * 이는 {0,1,3,2} 배열과 뒤집는 회수가 동일하므로 이것으로 계산을 시작할 것이다.
     * 
     * 그리고 이렇게 하는 이유는 배열을 해시코드로 사용할 때, 
     * {1,2,3}과 {10,20,30}의 해시코드 값은 서로 다르기 때문에, 동일하게 맞추기 위함이다.
     *
     * 이 배열을 뒤집어 가면서, deep(깊이=뒤집은 회수)을 미리 계산 해놓는다. 
     * => 1000건의 케이스를 테스트 하는 실제 알고리즘 테스트에서 static으로 결과를 미리 만들어 놓고
     * 키(배열) 값으로 deep(깊이)을 찾아온다.
     * **************************************************************
     * */
    static void preCalc(int n) {
        int[] perm = new int[n];
        for (int i=0; i<n; i++) {
            // 정렬된 배열 생성
            perm[i] = i;
        }

        // 방문할 정점(배열)을 저장할 공간 => 재귀 호출이 아닌, Queue 를 사용해서 저장하는 것이 핵심
        Queue<int[]> queue = new LinkedList<>();

        // 큐에 탐색할 대상을 넣어둔다.
        queue.add(perm);
        discovered.put(hash(perm), 0);

        // 탐색 시작
        while (!queue.isEmpty()) {
            // 배열을 꺼내옴
            int[] here = queue.poll();
            // 배열을 뒤집기 전에 미리, 해시값으로 변환하여, discovered에 저장된 깊이를 찾아옴
            int cost = discovered.get(hash(here));

            for (int i=0; i<n; i++) {
                // 2 크기 이상 부터 시작
                for (int j=2+i; j<=n; j++) {
                    int[] copy = here.clone();

                    // 뒤집은 copy 배열
                    reverse(copy, i, j);

                    int there = hash(copy);
                    // 새로운 베열이 발견 되지 않았으면
                    if (!discovered.containsKey(there)) {
                        // 이 때 복사본을 넣어야함
                        queue.add(copy);
                        discovered.put(there, cost + 1);
                    }

                    // reverse(here, i, j);
                }
            }

        }
    }

    /**
     * 받은 배열 값을 상대값으로 변환된 배열을 통해,
     *
     * 탐색한 값을 찾아온다.
     * */
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

        return discovered.containsKey(hash(fixed)) ? discovered.get(hash(fixed)) : -1;
    }

    /**
     * 해시코드로 변환
     * 값이 같은 배열은 같은 해시코드를 반환
     * */
    static Integer hash(int[] arr) {
        return Arrays.deepHashCode(Arrays.stream(arr).boxed().toArray(Integer[]::new));
    }

    /**
     * 배열의 일부분 또는 전체를 뒤집는 로직
     * */
    static void reverse(int[] arr, int s, int e) {
        int[] copy = Arrays.copyOfRange(arr, s, e);

        int len = copy.length;
        for (int i=0; i<len; i++) {
            arr[s+i] = copy[len - (i+1)];
        }
    }

    /**
     * 배열을 디버깅 할 때 사용
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

    public static void main(String[] args) {
        // 두 배열을 자세히 보면 좀 다릅니다
        int[] input1 ={1,2,3,4,8,7,5,6};
        int[] input2 ={1,2,3,4,8,7,6,5};

        // 미리 계산
        preCalc(input2.length);

        int value1 = solve(input2);
        int value2 = solve(input1);

        System.out.println(value1); // 1
        System.out.println(value2); // 2
    }


}
