package root.functions;

public class Sleep {

    public static void sleep(int min, int max) {

        try {
            Thread.sleep(DefineCoordinate.rnd(min, max));
        } catch (Exception e) {
            System.out.println("Произошла ошибка во время ожидания выполнения действия");
        }

    }

}
