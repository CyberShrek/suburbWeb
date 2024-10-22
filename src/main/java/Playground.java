import java.util.Arrays;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Playground {
    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date);
        run(100000000);
        System.out.println(new Date().getTime() - date.getTime());
    }

    private static void run(int times){
        for (int i = 0; i < times; i++) {
            measure(() -> {
//                List<String> list = new ArrayList<>();
//                list.add("4");
            });
        }
    }

    private static void measure(Runnable task) {
        Date date = new Date();
        task.run();
//        return (new Date().getTime() - date.getTime()) / 1000f;
    }
}