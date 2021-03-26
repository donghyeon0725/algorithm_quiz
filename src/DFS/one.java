package DFS;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

public class one {
    /**
     * 주어진 문자열 배열을 가지고
     * 알파벳의 순서를 알아내는 알고리즘
     *
     * 알파벳을 각각 0 ~ 26의 숫자로 바라보고 (알파벳 - 'a')
     * 순서를 간선으로 변환하는 로직이다.
     *
     * 예를 들어 (1, 21) 배열의 값이 1 일 경우
     * b와 u 사이에는 b -> u 로의 순서가 있다.
     *
     * */
    static int[][] graph = new int[26][26];
    static Queue queue = new LinkedList();;
    static boolean[] visited = new boolean[26];

    /**
     * 인접 행렬을 이용해서, 알파벳과 순서 사이에 관계를 그래프로 나타내는 방법
     *
     * 이 것을 위상 정렬하면, 순서를 알아낼 수 있다.
     * */
    static void makeGraph(String[] words) {
        for (int j=1; j<words.length; j++) {
            int i = j - 1;
            /* 제일 짧은 단어의 크기 만큼 반복 */
            int len = words[i].length() > words[j].length() ? words[j].length() : words[i].length();

            for (int k=0; k<len; k++) {
                if (words[i].charAt(k) != words[j].charAt(k)) {
                    int a = words[i].charAt(k) - 'a';
                    int b = words[j].charAt(k) - 'a';

                    graph[a][b] = 1;
                    break;
                }
            }
        }
    }

    /**
     *  깊이 우선 탐색 실행
     *  */
    static void search(int here) {
        visited[here] = true;

        for (int there=0; there<graph[here].length; there++) {
            /* 탐색된 순서(간선)가 있는지 */
            boolean isThereEdge = graph[here][there] == 1;

            /* i에서 j 방향으로의 탐색이 되어 있지 않고, 간선이 있다면 값을 넣어줌 */
            if (isThereEdge && !visited[there]) {
                search(there);
            }
        }
        queue.add(here);
    }
}
