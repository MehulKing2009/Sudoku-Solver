
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class Solution {
    public static void main(String[] args) {


        Solution s = new Solution();
        for(String z : s.testcase) {
            char[][] x = s.String_to_char(z);
            if (s.solveSudoku(x)){

            System.out.println("+---------------------------+");
            for (char[] y : x) {
                System.out.println(Arrays.toString(y));
            }
            System.out.println("+---------------------------+" + "\n");
        }
        else{
            System.out.println("Invalid Input");
        }
    }
        }
    public char[][] String_to_char(String x){
        if(x.length() != 81)return null;
        char[][] matrix = new char[9][9];
        for(int i = 0 ; i< 9 ; i++){
            for(int j = 0 ; j< 9 ; j++){

             switch (x.charAt(i*9 + j)){
                 case '.','0'->{
                     matrix[i][j]='.';
                 }
                 case '1','2','3','4','5','6','7','8','9'->{
                     matrix[i][j] = x.charAt(i*9 + j);
                 }
                 default -> {
                     throw new IllegalArgumentException("Invalid Input");
                 }
                }
            }
        }
        return matrix;
    }

    // Sudoku Solver
    public boolean solveSudoku(char[][] board) {

        if(!isValidSudoku(board))throw new IllegalArgumentException("Invalid Sudoku");
      // initialization using Set<char>[][] for each
        // '.' marks the guess here

        Set<Character>[][] set = new Set[9][9];
        for(int i = 0 ; i<9;i++){
            for(int j=0;j<9;j++){
                set[i][j]=new HashSet<>();
                if(board[i][j]!='.'){
                    set[i][j].add(board[i][j]);
                }
                else {
                    // if it's not a '.' it's a no. so we are assigning every no. [1,9] into it's SET
                    for(int k = 1 ; k <= 9 ; k++){
                        set[i][j].add((char)(k + '0')); // '0' + 1 = '1'
                    }

                }
            }
        }
        // Main Logic Body
        while(true){
            boolean changed = false; // prevents re-running on sudoku that cannot be solved without guessing
            for(int i = 0 ; i<9 ;i++){
                for(int j = 0 ; j<9 ;j++){

                    if(set[i][j].isEmpty())return false; // just in case

                    if(set[i][j].size()!= 1){ // for set with >1 possible cases

                        // row and col based elimination
                        for(int col = 0 ; col < 9 ; col++){
                            if(set[i][col].size()== 1 && col!=j){
                                if(set[i][j].removeAll(set[i][col])){
                                    changed=true;
                                }

                            }
                        }

                        for(int row = 0 ; row < 9 ; row++){
                            if(set[row][j].size()== 1 && row!=i){
                                if(set[i][j].removeAll(set[row][j])){
                                    changed=true;
                                }
                            }
                        }
                        // for 3*3 box elimination
                        int rs = i / 3 * 3;
                        int cs = j / 3 * 3;
                        for (int row = rs; row < rs + 3; row++) {
                            for (int col = cs; col < cs + 3; col++) {
                                if (!(i == row && j == col)
                                        && set[row][col].size() == 1) {

                                    if (set[i][j].removeAll(set[row][col])) {
                                        changed = true;
                                    }
                                }
                            }
                        }
                    }

                    if(set[i][j].isEmpty()){return false;} // just in case
                }
            }
            if(!changed){ // not change -> need a guess
                break;
            }
        }
        if(solved(set)){ // copy to original char[][]
            for(int i = 0 ; i<9;i++){
                for(int j =0;j<9;j++){
                    board[i][j]=set[i][j].iterator().next();
                }
            }
            return solved(board) && isValidSudoku(board);
        }

        int guess =10; // to select minimum no. of guess cases
        int guess_i =0;
        int guess_j =0;
        Set<Character> set1 = new HashSet<>();
        for(int i =0;i<9 ;i++){
            for(int j =0;j<9;j++){
                int m = set[i][j].size();

                if(m==0)return false ; // just in case

                if(m != 1 && m < guess){
                    guess=m;
                    guess_i=i;
                    guess_j=j;
                    set1 =new HashSet<>(set[i][j]);
                }
            }
        }

        if(guess != 10){ // if guess == 10 -> sudoku is solved -> error in isValidSudoku
            for(char c : set1){
                char[][] temp = new char[9][9];
                copy(temp,board);
                temp[guess_i][guess_j]=c;
                if(solveSudoku(temp)){
                    copy(board,temp);
                    return true;
                }
            }
        }
        return false;

    }

    public void copy(char[][] x , char[][] from){
        for(int i = 0 ; i<9 ;i++){
            System.arraycopy(from[i],0,x[i],0,9);
        }
    }
    public boolean solved(char[][] board){
        for(int i = 0 ; i<9 ;i++){
            for(int j = 0 ; j<9 ;j++){
                if(board[i][j] == '.'){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean solved(Set<Character>[][] set){
        for(int i = 0 ; i<9 ;i++){
            for(int j = 0 ; j<9 ;j++){
                if(set[i][j].size() != 1){
                    return false;
                }
            }
        }
        return true;
    }
    char[] checkingfor = {'1','2','3','4','5','6','7','8','9'};
    public boolean isValidSudoku(char[][] board) {
        for(char[] x : board){
            if(! counntisone(x,checkingfor)){return false;}
        }
        char[] tempcheckhorizomtal = new char[9];
        for(int z = 0 ; z<9 ; z++) {
            for (int i = 0; i < 9; i++) {
                tempcheckhorizomtal[i] = board[i][z];
            }
            if (!counntisone(tempcheckhorizomtal, checkingfor)) {return false;}
        }
        for(int x = 0 ; x<9;x+=3){
            for(int y = 0 ; y<9;y+=3){
                if (!counntisone(new char[] {board[0+x][0+y],board[0+x][1+y],board[0+x][2+y],board[1+x][0+y],board[1+x][1+y],board[1+x][2+y],board[2+x][0+y],board[2+x][1+y],board[2+x][2+y]}, checkingfor)) {return false;}

            }
        }
        return true;


    }
    //   if (!counntisone(new char[] {board[0][0],board[0][1],board[0][3],board[1][0],board[1][1],board[1][2],board[2][0],board[2][1],board[2][2]}, checkingfor)) {return false;}

    public static boolean counntisone(char[] board,char target) {
        int count = 0;
        for(char z : board){
            if(z==target){
                count++;
            }
        }

        return count<=1;
    }
    public static boolean counntisone(char[] board,char[] tar){
        for(char x : tar){
            if(!counntisone(board,x)){return false;}
        }
        return true;
    }




    String[] testcase = {"980700600705900400000085000600400390000090020000001000400009003060040900000600007",
            "000000001002003040050460000000010700008004090670500000004000009080040030300008200",
            "000000000000001023002340500000000406006420050070000000000008000004630005900070630",
            "980760500700500080000008009870006090004070300000200000460007050090610004000000000",
            "000000001000002030004010560001000000040060170600008003015900000070001005400050700",

            ".".repeat(81),
    };

}

