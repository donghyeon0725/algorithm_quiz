package DISJOINT_SET;

public class one {
    /**
     * 이 로직을 사용해야 하는 이유,
     * 배열 2개로 구현할 수 없는 이유
     *
     * 1. 집단이 특정 된 것 이 아님.
     * 2. 입력값을 따라가다 보면 집단이 2개로 뭉치지 않을 가능성이 큼 => 몇개의 배열이 필요할지 모름 (나중에 뭉치기 어려움)
     *      * 예를 들어 ACk 0 1, ACK 2 3, ACK 4 5 이면 벌써 3개의 배열이 필요함
     *      * 따라서 연결 관계로 인한 집합처럼 다룰 수 있는 상호 배타적 집합을 사용하는 것
     *
     *
     *
     *
     *
     * 풀이
     *
     *
     * 상호 인정인 경우 => 모순이 있는지 확인 후, 병합
     * 상호 비방의 경우 => 대표 노드끼리 적인지 아닌지만 저장하고 있으면 됨
     *      1. 같은 집단을 비난했는지 확인 => 모순
     *      2. 비난 한 집단과 적대관계가 없는지 확인 => 있으면 병합 (적대 관계로는 저장하지 않음 / 이미 같은 집단에 속해버리기 때문)
     *      3. 적대 관계가 없다면, 새로운 적대 관계 추가
     *
     * (v가 u의 적일 때 w가 u를 비난하면 v 집단에 들어가고 u를 옹호하면 u 집단에 들어감)
     *
     * 모든 검사가 끝난 뒤,
     * 적대 관계가 없는 사람끼리는 일단 뭉침
     * 적대 관계가 있는 집단 끼리는 더 큰 집단을 뭉침 (사이즈 측정하고 있어야함)
     * */
    static class Set {
        int[] parent;
        int[] rank;
        int[] size;
        // 서로 다른 노드 정보
        int[] enemy;

        public Set(int n) {
            parent = new int[n];
            for (int i=0; i<n; i++) {
                parent[i] = i;
                rank[i] = 1;
                size[i] = 1;
                enemy[i] = -1;
            }
        }

        /**
         * n이 속한 집단의
         * 대표 노드 반환
         * */
        int find(int n) {
            if (n == parent[n]) return n;
            return parent[n] = find(parent[n]);
        }

        /**
         * 병합하기
         * */
        void merge(int v, int u) {
            v = find(v); u = find(u);

            if (v == u) return;

            if (rank[u] > rank[v]) {
                int temp = v;
                v = u;
                u = temp;
            }

            // v 에 더 높은 집합
            parent[u] = v;
            size[v] += size[u];

            if (rank[v] == rank[u]) rank[v]++;
        }

        /**
         * 비난 v -> u
         * */
        boolean dis(int v, int u) {
            // 집단을 대표하는 대표자들끼리 집단이 다름을 저장하고 있으면 됨
            v = find(v); u = find(u);

            // 모순
            if (v == u) return false;

            // 적대적인 집단의 적이 있으면 v 집단을 적대 관계 집단에 병합
            if (enemy[u] != -1)  {
                merge(v, enemy[u]);
                return true;
            }

            // 적대적인 집단의 동료가 있으면 그 두 집단은 이미 병합

            // 아무런 집단에 속하지 못한 경우
            enemy[v] = u;
            enemy[u] = v;
            return true;
        }

        /**
         * 옹호
         * v -> u
         * */
        boolean act(int v, int u) {
            v = find(v); u = find(u);

            if (v != u) return false;

            // 옹호 한 사람의 적은 적 => 집단으로 보기 때문에, 그냥 병합하면 됨
            merge(v, u);

            // 옹호 한 사람이 옹오한 경우는 이미 병합되어 있음
            return true;
        }

        /**
         * 사이즈가 더 큰 쪽을 리턴해줌
         * */
        int biggerSet(int v, int u) {
            v = find(v); u = find(u);

            return size[v] > size[u] ? v : u;
        }

        /**
         * 나머지 전부 합침
         * */
        void mergeRemainder() {
            for (int i=0; i<parent.length-1; i++) {
                int s1 = find(i);
                int s2 = find(i+1);

                // 대표자 관계에 적이 없는 경우 합침
                if (enemy[s1] == -1) {
                    if (enemy[s2] == -1) merge(s1, s2);

                    // s2와 s2의 적을 비교 후 더 큰쪽에 합침
                    else merge( s1,  biggerSet(s2, enemy[s2]) );
                } else {
                    // s2의 적이 없는 경우 s1의 적과 s1중 더 큰 쪽을 비교해서 합침
                    if (enemy[s2] == -1) merge( s2, biggerSet(s1, enemy[s1]) );
                    // 더 큰쪽 끼리 합침
                    else {
                        int a1 = biggerSet(s1, enemy[s1]);
                        int a2 = biggerSet(s2, enemy[s2]);
                        merge(a1, a2);
                    }
                }

            }
        }


        void run(String[] str) {
            for (int i=0; i<str.length; i++) {
                String[] list = str[i].split(" ");

                // 옹호
                if ("ACK".equals(list[0])) {
                    act(Integer.parseInt(list[1]), Integer.parseInt(list[2]));
                } else {
                    // 비난
                    dis(Integer.parseInt(list[1]), Integer.parseInt(list[2]));
                }
            }

            mergeRemainder();
        }
    }


    public static void main(String[] args) {
        String[] input1 = {"ACK 0 1", "ACK 1 2", "DIS 1 3", "ACK 2 0"};
        String[] input2 = {"ACK 0 1", "ACK 1 2", "ACK 2 3", "ACK 3 4"};
        String[] input3 = {"ACK 0 1", "ACK 1 2", "DIS 2 0"};
        String[] input4 = {"DIS 0 1", "ACK 1 2", "ACK 1 4", "DIS 4 3", "DIS 5 6", "ACK 5 7"};
    }
}
