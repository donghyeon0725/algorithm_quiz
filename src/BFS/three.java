package BFS;

import java.util.*;

public class three {

    /**
     * 필요한 사탕의 수를 x라고 했을 때 최소 개수 F(x)는
     *
     * F(x) = n * x + m  => n 명 중 m 명에게만 1개씩 더
     *
     * 너비 우선 탐색으로 조합된 숫자로 이루어진 값을 a 라고 했을 때,
     * a % F(x) = 0 인지 확인하는 문제이다.
     *
     * 이때, F(x) < "전체 사탕의 개수" 를 만족해야 한다.
     *
     * 깊이 우선 탐색이 아닌, 너비 우선 탐색으로 숫자를 조합해보아야 하는 이유는,
     * "최소값"을 찾아야 하기 때문이다.
     * */
    static long search(long[] num, long n, long m) {
        // 탐색된 수가 들어갈 공간
        Map<Long, Long> searched = new TreeMap<>();
        // 탐색할 수가 들어갈 공간
        Queue<Long> candidate = new LinkedList<>();

        /* num을 먼저 정렬하도록 한다. */
        Arrays.sort(num);

        for (int i=0; i<num.length; i++) {
            // 트리 맵에 1자리 숫자로 이루어진 수를 넣는다.
            searched.put(num[i], (long)0);
            candidate.add(num[i]);
        }


        while(!candidate.isEmpty()) {
            long here = candidate.poll();
            long cost = searched.get(here);

            for (long x=1, smallest = 0; smallest < here; x++) {
                smallest = n*x+m;

                // 조건에 맞는 숫자를 찾으면 멈춘다.
                if (here % smallest == 0) {
                    return here;
                }
            }


            // 배열을 돌려가면서 하나씩 뒤에 숫자를 붙여본다. => 이때 num은 정렬되어 있으므로 작은 숫자 순서로 정렬한다.
            for (int i=0; i<num.length; i++) {
                // 다음 숫자를 뒤에 붙여본다.
                long there = here * 10 + num[i];

                if (!searched.containsKey(there)) {
                    // here 탐색 완료 => there의 깊이 세팅
                    searched.put(there, cost+1);
                    // 탐색할 목록에 there 을 넣는다.
                    candidate.add(there);
                }

            }
        }
        return -1;
    }

    public static void main(String[] args) {
        long[] input1 = {1};
        long[] input2 = {1};
        long[] input3 = {0};
        long[] input4 = {3,4,5};
        long[] input5 = {3,5};

        long result1 = search(input1, 10, 1);
        long result2 = search(input2, 7, 0);
        long result3 = search(input3, 7, 3);
        long result4 = search(input4, 9997, 3333);
        long result5 = search(input5, 9, 8);

        System.out.println(result1);
        System.out.println(result2);
        System.out.println(result3);
        System.out.println(result4);
        System.out.println(result5);
    }
}
