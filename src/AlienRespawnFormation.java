public class AlienRespawnFormation {
    public static final int[][] formation_1 = {
            { 1, 0, 0, 0, 1 },
            { 0, 1, 0, 1, 0 },
            { 1, 0, 1, 0, 1 },
            { 0, 1, 0, 1 ,0 },
    };
    public static final int[][] formation_2 = {
            { 1, 1, 1, 1, 1, 1, 1, 1 },
            { 1, 1, 1, 0, 0, 1, 1, 1 },
            { 1, 0, 1, 1, 1, 1, 0, 1 },
            { 1, 1, 1, 0, 0, 1, 1, 1 },
            { 1, 1, 1, 1, 1, 1, 1, 1 },
    };
    public static final int[][] formation_3 = {
            { 1, 1, 0, 1, 1, 0, 1, 1 },
            { 0, 0, 1, 0, 0, 1, 0, 0 },
            { 0, 0, 0, 1, 1, 0, 0, 0 },
            { 0, 0, 1, 0, 0, 1, 0, 0 },
            { 1, 1, 0, 1, 1, 0, 1, 1 },
    };
    public static final int[][] formation_4 = {
            { 1, 1, 1, 1, 1, 1, 1, 1 },
            { 1, 0, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 1, 1, 1, 1, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 0, 1 },
            { 1, 1, 1, 1, 1, 1, 1, 1 },

    };

    public static final int[][][] formations = { formation_1,formation_2,formation_3,formation_4};
}
