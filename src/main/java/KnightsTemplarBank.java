import java.util.Arrays;

public class KnightsTemplarBank {


    /**
     * 返回圣殿骑士团银行职员可有的兑换方法总数以及
     * 最少使用多少枚不同面值钱币即可完成兑换
     *
     * @param amount  钱币数额
     * @param options 可以使用的面值，最小值未必是1，无特定排列顺序
     */
    public int[] resolve(int amount, int[] options) {
        Arrays.sort(options);

        int[][] dp = new int[amount + 1][options.length];
        for (int i = options[0]; i <= amount; i++) {
            dp[i][0] = i % options[0] == 0 ? 1 : 0;
        }
        for (int i = 0; i < options.length; i++) {
            dp[0][i] = 1;
        }

        for (int i = options[0]; i <= amount; i++) {
            for (int j = 1; j < options.length; j++) {
                for (int k = i; k >= 0; k -= options[j]) {
                    dp[i][j] += dp[k][j - 1];
                }
            }
        }


        int all = dp[amount][options.length - 1];
        int minOfMethod = -1;
        if (all > 0) {

        if(amount<1) return null;
        int[] dp2 = new int[amount+1];
        int sum = 0;

        while(++sum<=amount) {
            int min = -1;
            for(int coin : options) {
                if(sum >= coin && dp2[sum-coin]!=-1) {
                    int temp = dp2[sum-coin]+1;
                    min = min<0 ? temp : (temp < min ? temp : min);
                }
            }
            dp2[sum] = min;
        }
            minOfMethod = dp2[amount];
        } else {
            all = -1;
        }

        System.out.println(all + "  " + minOfMethod);
        return new int[]{all, minOfMethod};
    }


    public static void main(String[] args) {
        KnightsTemplarBank ktBank = new KnightsTemplarBank();
        int[] options = {1, 2, 3, 4};
        ktBank.resolve(4, options);
    }

}
