import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

public class Playground {
    public static void main(String[] args) {

        SomeClass some = SomeClass.builder()
                .name("some1")
                .number(10)
                .list(new ArrayList<>(List.of("1", "2", "3")))
                .build();

        SomeClass some2 = some.toBuilder()
                .name("some2")
                .number(20)
                .build();

        some2.getList().add("4");

        System.out.println(some);
        System.out.println(some2);
    }
}

@SuperBuilder(toBuilder=true)
@ToString
@Getter
class SomeClass {
    private String name;
    private int number;
    private List<String> list;
}