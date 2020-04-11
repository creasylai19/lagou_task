import java.util.Scanner;

/**
 * 字符消除游戏，输入全部大写的字符串
 * 判断相邻两字符是否相同，如果相同，则删除，如ABCCBCCCAA消除后，变成ABBC
 * 这样一轮一轮循环消除，直至相邻字符没有相同为止。如上方ABBC再次消除后，变成AC
 * 最后，如果字符能完全消除，则输出Yes，否则输出No
 *
 * 当前算法，循环遍历，如果当前字符和下一字符相同，则消除，直至字符串末尾则进入下一次循环。其中，如果某次消除字符个数为0，则跳出循环
 * 更好的算法，一次循环，如果当前字符和下一字符相同，则消除。此时，返回前一字符，继续比较
 */
public class Main {

    public static void main(String[] args) {
        //ABCCBCCCAA NO
        //ABCCBA YES
        //ABCDEFGGFEDCBA YES
        //ABCDEFGGFEDBCA NO
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        //System.out.println("Input String is :" + line);

        judgeStringCanEraseAll(line);
    }

    public static void judgeStringCanEraseAll(String s){
        if( null == s || s.length() <= 1 ) {//if String is null or only contains one char or zero
            System.out.println("No");
            return;
        }
        String afterRemove = s.toUpperCase();
        do{
            String temp = removeStr(afterRemove);
            if( afterRemove.equals(temp) ) {
                break;
            }
            afterRemove = temp;
        } while(null != afterRemove && afterRemove.length() > 0);

        if( null == afterRemove || afterRemove.length() == 0 ) {
            System.out.println("Yes");
        } else {
//            System.out.println(afterRemove);
            System.out.println("No");
        }
    }


    public static String removeStr(String s){
        StringBuilder sb = new StringBuilder();
        if( null == s || s.length() == 0 ) {
            return s;
        }
        int strLength = s.length();
        for (int i = 0; i < strLength; i++) {
            if(i < strLength - 1 && s.charAt(i) == s.charAt(i+1)){
                i++;
            } else {
                sb.append(s.charAt(i));
            }
        }
        return sb.toString();
    }

}