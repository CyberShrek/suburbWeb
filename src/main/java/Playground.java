import org.vniizht.suburbsweb.util.Util;

public class Playground {
    public static void main(String[] args) {

        String someText = "This is some text.\nIt is longer than 1 line.\nThe end.";
        String substring = someText.substring(0, 4);
        System.out.println(
            Util.measureTime(() -> {
                for (int i = 0; i < 10000000; i++) {
                    boolean startsWith = substring.equals("This");
                }
            })
        );
    }
}